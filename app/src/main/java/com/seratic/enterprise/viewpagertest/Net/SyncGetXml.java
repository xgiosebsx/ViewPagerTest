package com.seratic.enterprise.viewpagertest.Net;

import android.os.AsyncTask;
import android.provider.DocumentsContract;


import com.seratic.enterprise.viewpagertest.Models.Cities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class SyncGetXml {
    String url;
    URL urlQuery;
    Cities cities;
    AsyncTask<Void, Void, Void> asyncTask;
    OnSyncListener onSyncListener;
    List<Cities> data;

    ArrayList<String> headlines = new ArrayList();
    ArrayList<String> links = new ArrayList();

    public static final int SYNC_CORRECT = 0;
    public static final int SYNC_FAILED = 1;


    public interface OnSyncListener {
        void OnPrepareConection(int state);

        void OnFinishedConection(int state, List<Cities> cities, String e);
    }

    public SyncGetXml(String url, Cities cities, OnSyncListener onSyncListener) {
        this.url = url;
        this.cities = cities;
        this.onSyncListener = onSyncListener;
    }

    public void conectinoWithServer() {
        asyncTask = new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                onSyncListener.OnPrepareConection(SYNC_CORRECT);
                data = new ArrayList<>();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    urlQuery = new URL(url);
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser pullParser = factory.newPullParser();
                    pullParser.setInput(getInputStream(urlQuery), "UTF_8");
                    boolean insideItem = false;

                    // Returns the type of current event: START_TAG, END_TAG, etc..
                    int eventType = pullParser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                         if (pullParser.getText() != null) {
                            Document doc = null;
                            try {
                                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                                        .parse(new InputSource(new StringReader(pullParser.getText())));
                            } catch (SAXException e) {
                                e.printStackTrace();
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            }
                            Cities city = new Cities();
                            NodeList errNodes = doc.getElementsByTagName("City");
                            NodeList errNodes2 = doc.getElementsByTagName("Country");
                            if (errNodes.getLength() == errNodes2.getLength())
                                if (errNodes2.getLength() > 0) {
                                    for (int i = 0; i < errNodes2.getLength(); i++) {
                                        city = new Cities();
                                        Element err = (Element) errNodes2.item(i);
                                        System.out.println(err.getChildNodes().item(0).getTextContent());
                                        city.setCountry(err.getChildNodes().item(0).getTextContent());

                                        Element err2 = (Element) errNodes.item(i);
                                        System.out.println(err2.getChildNodes().item(0).getTextContent());
                                        city.setCity(err2.getChildNodes().item(0).getTextContent());
                                        data.add(city);
                                    }

                            }

                        }

                        eventType = pullParser.next(); //move to next element
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                onSyncListener.OnFinishedConection(SYNC_CORRECT, data, null);
            }
        }.execute();

    }

    public InputStream getInputStream(URL url2) {
        try {
            return url2.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public ArrayList<String> heads() {
        return headlines;
    }
}

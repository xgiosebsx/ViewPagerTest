package com.seratic.enterprise.viewpagertest.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.seratic.enterprise.viewpagertest.Models.Cities;
import com.seratic.enterprise.viewpagertest.R;

import java.util.List;

public class ListCitiesAdapter extends BaseAdapter {
    List<Cities> data;
    Context context;

    public ListCitiesAdapter(List<Cities> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null){
            v = View.inflate(context, R.layout.template_list,null);
        }
        TextView txt = (TextView) v.findViewById(R.id.ciudad);
        txt.setText(data.get(i).getCity());
        return v;
    }
}

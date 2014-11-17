package com.thoughtworks;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Map;

public class TimeEstimationAdapter extends BaseAdapter {

    private final Context context;
    private final Map<String, String> data;
    private final String[] stations;

    public TimeEstimationAdapter(Context context, Map<String, String> data) {
        this.context = context;
        this.data = data;
        stations = data.keySet().toArray(new String[data.size()]);


    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(stations[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(android.R.layout.simple_list_item_2, viewGroup, false);
        }

        TextView nameTextView = (TextView) view.findViewById(android.R.id.text1);
        nameTextView.setText(stations[position]);

        TextView abbrTextView= (TextView) view.findViewById(android.R.id.text2);
        abbrTextView.setText(getItem(position).toString() + " minutes");

        return view;
    }
}

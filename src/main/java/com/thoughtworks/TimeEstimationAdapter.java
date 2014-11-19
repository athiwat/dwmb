package com.thoughtworks;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class TimeEstimationAdapter extends BaseAdapter {

    private final Context context;
    private List<Map<String, String>> estimates;

    public TimeEstimationAdapter(Context context, List<Map<String, String>> estimates) {
        this.context = context;
        this.estimates = estimates;
    }

    @Override
    public int getCount() {
        return estimates.size();
    }

    @Override
    public Map<String,String> getItem(int position) {
        return estimates.get(position);
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
        nameTextView.setText(getItem(position).keySet().toArray()[0].toString());

        TextView abbrTextView= (TextView) view.findViewById(android.R.id.text2);
        abbrTextView.setText(getItem(position).values().toArray()[0] + " minutes");

        return view;
    }
}

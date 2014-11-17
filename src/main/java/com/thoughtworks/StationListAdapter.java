package com.thoughtworks;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Map;

public class StationListAdapter extends BaseAdapter {
    private final String[] abbrs;
    private Context context;
    private Map<String, String> data;

    public StationListAdapter(Context context, Map<String, String> data) {
        this.context = context;
        this.data = data;
        abbrs = data.keySet().toArray(new String[data.size()]);


    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(abbrs[position]);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(android.R.layout.simple_list_item_2, viewGroup, false);
        }

        TextView nameTextView = (TextView) view.findViewById(android.R.id.text1);
        nameTextView.setText((String) getItem(position));

        String abbr = abbrs[position];
        TextView abbrTextView= (TextView) view.findViewById(android.R.id.text2);
        abbrTextView.setText(abbr);

        view.setTag(abbr);
        return view;
    }
}

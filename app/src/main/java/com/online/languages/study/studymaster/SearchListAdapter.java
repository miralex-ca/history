package com.online.languages.study.studymaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.online.languages.study.studymaster.data.DataItem;

import java.util.ArrayList;

public class SearchListAdapter extends BaseAdapter {

    Context context;
    ArrayList<DataItem> data;




    public SearchListAdapter(Context _context, ArrayList<DataItem> _data) {
        context = _context;
        data = _data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.search_item, parent, false);
        }

        DataItem currentItem = (DataItem) getItem(position);

        TextView textViewItemName = convertView.findViewById(R.id.title);


        textViewItemName.setText(currentItem.item);


        return convertView;
    }



}

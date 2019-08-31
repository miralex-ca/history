package com.online.languages.study.studymaster.adapters;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.online.languages.study.studymaster.R;
import com.online.languages.study.studymaster.data.DataItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SearchDataAdapter extends RecyclerView.Adapter<SearchDataAdapter.MyViewHolder> {


    ArrayList<DataItem> data;
    Context context;
    private String theme;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc;
        ImageView image, star;

        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            desc = view.findViewById(R.id.desc);
            image = view.findViewById(R.id.image);
            star = view.findViewById(R.id.listStarIcon);
        }
    }


    public SearchDataAdapter(Context _context, ArrayList<DataItem> _data, String _theme) {
        data = _data;
        context  = _context;
        theme = _theme;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DataItem dataItem = data.get(position);

        holder.title.setText(dataItem.item);
        holder.desc.setText(dataItem.info);


        String pic = dataItem.image;


        Picasso.with(context )
                .load("file:///android_asset/pics/"+ pic )
                //.transform(new RoundedTransformation(0,0))
                .fit()
                .centerCrop()
                .into(holder.image);


        if (dataItem.starred == 1) {
            holder.star.setVisibility(View.VISIBLE);
        } else {
            holder.star.setVisibility(View.INVISIBLE);
        }


        if (theme.equals("westworld")) {
            holder.image.setColorFilter(Color.argb(255, 50, 250, 240), PorterDuff.Mode.MULTIPLY);
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }



}

package com.online.languages.study.studymaster.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.online.languages.study.studymaster.R;
import com.online.languages.study.studymaster.data.NavSection;
import com.online.languages.study.studymaster.data.Section;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeCardRecycleAdapter extends RecyclerView.Adapter<HomeCardRecycleAdapter.MyViewHolder> {

    private ArrayList<NavSection> sections;
    Context context;
    private Boolean showWorld;
    private String theme;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc;
        public ImageView icon;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            desc = view.findViewById(R.id.desc);
            icon = view.findViewById(R.id.grid_image);
        }
    }

    public HomeCardRecycleAdapter(Context _context, ArrayList<NavSection> _sections, String _theme) {
        context  = _context;

        SharedPreferences appSettings = PreferenceManager.getDefaultSharedPreferences(context);
        showWorld = appSettings.getBoolean("world_section", false);

        sections = checkSections(_sections);
        theme = _theme;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_grid_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        NavSection section = sections.get(position);



        holder.title.setText(section.title);
        holder.desc.setText(section.desc);

        Picasso.with( context )

                //.load(R.drawable.f)
                //.load(R.raw.e)
                .load("file:///android_asset/pics/"+ section.image )
                .fit()
                .centerCrop()
                .into(holder.icon);


        if (theme.equals("westworld")) {
            holder.icon.setColorFilter(Color.argb(255, 50, 240, 240), PorterDuff.Mode.MULTIPLY);
        }

    }

    @Override
    public int getItemCount() {
        return sections.size();
    }


    private ArrayList<NavSection> checkSections(ArrayList<NavSection> sections) {

        ArrayList<NavSection> list = new ArrayList<>();

        for (NavSection section: sections) {
            if (section.spec.equals("world")) {
                if (showWorld) list.add(section);
            } else {
                list.add(section);
            }
        }

        return list;

    }

}

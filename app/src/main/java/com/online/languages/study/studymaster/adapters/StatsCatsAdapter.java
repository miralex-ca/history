package com.online.languages.study.studymaster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.online.languages.study.studymaster.Constants;
import com.online.languages.study.studymaster.R;
import com.online.languages.study.studymaster.data.Section;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.online.languages.study.studymaster.Constants.APP_SIMPLIFIED;


public class StatsCatsAdapter extends RecyclerView.Adapter<StatsCatsAdapter.MyViewHolder> {

    ArrayList<Section> sections = new ArrayList<>();
    Context context;

    ColorProgress colorProgress;

    Boolean simplified = false;


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, progress, desc;

        ImageView image;


        MyViewHolder(View view) {
            super(view);

            title = (TextView) itemView.findViewById(R.id.titleText);
            progress = (TextView) itemView.findViewById(R.id.progressText);
            desc = (TextView) itemView.findViewById(R.id.descText);
            image = itemView.findViewById(R.id.itemImage);
        }
    }


    public StatsCatsAdapter(Context _context, ArrayList<Section> _sections) {
        sections = _sections;
        context = _context;
        colorProgress = new ColorProgress(context);
        simplified = APP_SIMPLIFIED;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_cat_list_item, parent, false);

        if (sections.size() < 4) itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_cat_list_item_big, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Section section = sections.get(position);

        holder.title.setText(section.title_short);



        int catsNum = section.stadiedCatsCount;
        int errors = section.errorsCount;




       // holder.progress.setText(catData.progress+ "%");


        int progress = section.progress;

        if (Constants.SCREEN_SHOW) {

            if (position == 0) {progress = 100; errors = 0; catsNum = 3;}
            if (position == 1) {progress = 92; errors = 1; catsNum = 3;}
            if (position == 2) {progress = 96; errors = 0; catsNum = 4;}
            if (position == 3) {progress = 84; errors = 2; catsNum = 3;}
            if (position == 4) {progress = 69; errors = 0; catsNum = 3;}
            if (position == 5) {progress = 100; errors = 0; catsNum = 3;}
            if (position == 6) {progress = 69; errors = 0; catsNum = 3;}
            if (position == 7) {progress = 0; errors = 0; catsNum = 0;}
            if (position == 8) {progress = 0; errors = 0; catsNum = 0;}
        }


        String desc = context.getString(R.string.cats_studied)+ catsNum+" &nbsp;&nbsp;&nbsp;";

        if (errors > 0) {
            desc = desc + "<font color=\"red\">Ошибок: "+errors+"</font>";
        } else {

            desc = desc + "Ошибок: "+ errors;
        }

        holder.desc.setText(Html.fromHtml(desc));
        if (simplified) {
            holder.desc.setText(section.desc_short);
        }

        // */

        holder.progress.setText(progress +"%");


        holder.progress.setTextColor( colorProgress.getColorFromAttr(progress) );

        Picasso.with(context )
                //.load(R.drawable.f)
                //.load(R.raw.e)
                .load("file:///android_asset/pics/"+section.image)
                .transform(new RoundedTransformation(0,0))
                .fit()
                .centerCrop()
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return sections.size();
    }


}


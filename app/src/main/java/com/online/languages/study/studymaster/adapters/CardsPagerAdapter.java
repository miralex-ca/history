package com.online.languages.study.studymaster.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.online.languages.study.studymaster.CardsActivity;
import com.online.languages.study.studymaster.R;
import com.online.languages.study.studymaster.data.DataItem;
import com.online.languages.study.studymaster.data.DetailItem;

import java.util.ArrayList;


public class CardsPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<DataItem> wordList = new ArrayList<>();

    Boolean showTranslate;
    Boolean mixWords;
    Boolean showTranscript;
    Boolean reverseData;


    int count = 4;


    public CardsPagerAdapter(Context _context, ArrayList<DataItem> words) {
        this.context = _context;
        wordList = words;

    }

    @Override
    public int getCount() {
        return wordList.size();
    }

    public void setCount(int count) {
        if(count < 10){
            this.count = count;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.flashcard_item, container, false);

        DataItem wordData = wordList.get(position);
        showTranslate = CardsActivity.fShowTranslate;
        mixWords = CardsActivity.fMixWords;
        showTranscript = CardsActivity.fShowTranscript;
        reverseData = CardsActivity.fRevertData;


        LinearLayout content = (LinearLayout) itemView.findViewById(R.id.fCardContent);
        LinearLayout contentMirror = (LinearLayout) itemView.findViewById(R.id.fCardContentMirror);

        if (reverseData) {
            content.setVisibility(View.GONE);
            contentMirror.setVisibility(View.VISIBLE);
        } else {
            content.setVisibility(View.VISIBLE);
            contentMirror.setVisibility(View.GONE);
        }


        TextView text = (TextView) itemView.findViewById(R.id.fCardText);
        RelativeLayout answerBox = (RelativeLayout) itemView.findViewById(R.id.fAnswerBox);
        final TextView showMsg = (TextView) itemView.findViewById(R.id.showMsg);
        final TextView answer = (TextView) itemView.findViewById(R.id.fCardAnswer);

        TextView textMirror = (TextView) itemView.findViewById(R.id.fCardTextMirror);
        final LinearLayout fTextMirrorBox  = (LinearLayout) itemView.findViewById(R.id.fTextMirrorBox);

        RelativeLayout answerBoxMirror = (RelativeLayout) itemView.findViewById(R.id.fAnswerBoxMirror);
        final TextView showMsgMirror = (TextView) itemView.findViewById(R.id.showMsgMirror);
        final TextView answerMirror = (TextView) itemView.findViewById(R.id.fCardAnswerMirror);


        if (showTranslate){
            showMsg.setVisibility(View.GONE);
            answer.setVisibility(View.VISIBLE);

            showMsgMirror.setVisibility(View.GONE);
            fTextMirrorBox.setVisibility(View.VISIBLE);

        }

        text.setText( wordData.item );
        int infoSize = textSize( wordData.info );
        answer.setText(wordData.info);
        answer.setTextSize( infoSize );



        answerBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMsg.setVisibility(View.GONE);
                answer.setVisibility(View.VISIBLE);
            }
        }

        );


        textMirror.setText( wordData.item );

        int infoSizeMirror = textSizeMirror( wordData.info );
        answerMirror.setText(wordData.info);
        answerMirror.setTextSize( infoSizeMirror );

        answerBoxMirror.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 showMsgMirror.setVisibility(View.GONE);
                  fTextMirrorBox.setVisibility(View.VISIBLE);
              }
        }

        );


        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }



    private int textSize(String text) {
        int textLength = text.length();
        int tSize = context.getResources().getInteger(R.integer.f_info_txt_size_norm);

        if ( textLength > 150) tSize = context.getResources().getInteger(R.integer.f_info_txt_size_mid);
        if ( textLength > 200) tSize = context.getResources().getInteger(R.integer.f_info_txt_size_small);
        if ( textLength > 240 ) tSize = context.getResources().getInteger(R.integer.f_info_txt_size_smallest);
        return tSize;
    }

    private int textSizeMirror(String text) {
        int textLength = text.length();
        int tSize = context.getResources().getInteger(R.integer.f_info_txt_size_norm_opt);;

        if ( textLength > 150) tSize = context.getResources().getInteger(R.integer.f_info_txt_size_mid_opt);;
        if ( textLength > 200) tSize = context.getResources().getInteger(R.integer.f_info_txt_size_small);
        if ( textLength > 240 ) tSize = context.getResources().getInteger(R.integer.f_info_txt_size_smallest);

        return tSize;
    }



}

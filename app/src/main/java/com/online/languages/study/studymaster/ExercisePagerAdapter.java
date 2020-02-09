package com.online.languages.study.studymaster;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.online.languages.study.studymaster.adapters.RoundedCornersTransformation;
import com.online.languages.study.studymaster.adapters.RoundedTransformation;
import com.online.languages.study.studymaster.adapters.ThemeAdapter;
import com.online.languages.study.studymaster.data.ExerciseTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.online.languages.study.studymaster.Constants.EX_IMG_TYPE;
import static com.online.languages.study.studymaster.Constants.GALLERY_TAG;

class ExercisePagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ExerciseTask> tasks;


    private int type = 1;

    private int optionColor;
    private int disableColor;
    private int activeColor;

    private DBHelper dbHelper;


    private Boolean lessOptions = false;

    private ExerciseTask exerciseTask;

    private String exOptTitleLong;
    private String exOptTitleShort;
    private String exOptTitleLongLess;


    private int textLongNum;


    ExercisePagerAdapter(Context _context, ArrayList<ExerciseTask> _tasks) {
        context = _context;
        tasks = _tasks;
        dbHelper = new DBHelper(context);


        SharedPreferences appSettings = PreferenceManager.getDefaultSharedPreferences(context);
        ThemeAdapter themeAdapter = new ThemeAdapter(context, appSettings.getString(Constants.SET_THEME_TXT, Constants.SET_THEME_DEFAULT), false );
        int styleTheme = themeAdapter.styleTheme;

        TypedArray o = context.getTheme().obtainStyledAttributes(styleTheme, new int[] {R.attr.colorExOptionTxt});
        optionColor = o.getResourceId(0, 0);
        o.recycle();

        TypedArray d = context.getTheme().obtainStyledAttributes(styleTheme, new int[] {R.attr.colorExDisabledTxt});
        disableColor = d.getResourceId(0, 0);
        d.recycle();

        TypedArray a = context.getTheme().obtainStyledAttributes(styleTheme, new int[] {R.attr.colorExActiveTxt});
        activeColor = a.getResourceId(0, 0);
        a.recycle();


        exOptTitleShort = context.getResources().getString(R.string.ex_opt_short);
        exOptTitleLong = context.getResources().getString(R.string.ex_opt_long);

        exOptTitleLongLess = context.getResources().getString(R.string.ex_opt_long_less);

        textLongNum = context.getResources().getInteger(R.integer.ex_text_long_num);



    }

    @Override
    public int getCount() {
        return tasks.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.exercise_item, container, false);


        if (ExerciseActivity.exType == 2) {
          //  itemView = inflater.inflate(R.layout.exercise_item_tr, container, false);

          //  type = 2;
        }

        type = ExerciseActivity.exType; // TODO remove

        exerciseTask = new ExerciseTask(tasks.get(position));

        TextView text = itemView.findViewById(R.id.fCardText);
        TextView transcript = itemView.findViewById(R.id.fTranscriptBox);

        final RadioGroup radioGroup = itemView.findViewById(R.id.radioGroup1);

        radioGroup.removeAllViews();

        int paddingDp = 10;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);


        if (type == 1 ) radioGroup.setPadding(paddingPixel,0,0,0);

        lessOptions = false;
        int longCount = 0;
        for (String optionTxt: exerciseTask.options) {
            if (optionTxt.length() > textLongNum) longCount++;
        }
        if (longCount > 1) lessOptions = true;


        if (lessOptions) {
            exerciseTask.options = new ArrayList<>(exerciseTask.options.subList(0, Constants.TEST_LONG_OPTIONS_NUM));
        }

        int optionLen = exerciseTask.options.size();


        Random rand = new Random();
        int correctOptionIndex = rand.nextInt(optionLen);
        Collections.rotate(exerciseTask.options.subList(0, correctOptionIndex+1), -1);

        exerciseTask.correct = correctOptionIndex;


        for (int i = 0; i < optionLen; i++) {
            buildRadio(inflater, radioGroup);
        }


        LinearLayout quest = itemView.findViewById(R.id.exQuest);

        if (!ExerciseActivity.exShowTranscript || ExerciseActivity.exType == 2) {
            transcript.setVisibility(View.GONE);
        }

        if (ExerciseActivity.exButtonShow) {
            changeHeight(quest, ExerciseActivity.exTxtHeight);
        } else {
            changeHeight(quest, ExerciseActivity.exTxtMoreHeight);
        }
        

        text.setText( exerciseTask.quest);
        setTextStyle(text, exerciseTask.quest);

        transcript.setText( exerciseTask.questInfo );

        if (type == EX_IMG_TYPE ) {
            insertImage(exerciseTask, itemView, position);
        }

        setExOptions(radioGroup, exerciseTask);


        for (int i = 0; i < radioGroup.getChildCount(); i++) {

            RadioButton radio = (RadioButton) radioGroup.getChildAt(i);

            radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ExerciseActivity.exCheckedStatus) {
                        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(checkedRadioButtonId);
                        if (checkedRadioButton != null) {

                                setDefaultRadio(radioGroup);
                                checkedRadioButton.setTextColor( ContextCompat.getColor(  context , activeColor) );
                            if (!ExerciseActivity.exButtonShow) {
                                ExerciseActivity.exCheckedStatus = true;
                                checkItem(radioGroup, position);
                            }
                        }
                    }
                }
            });

        }

        itemView.setTag("myview" + position);
        container.addView(itemView);
        return itemView;
    }




    private void changeHeight(LinearLayout quest, int height) {
        quest.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
        quest.setLayoutParams(quest.getLayoutParams());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private void setDefaultRadio(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radio = (RadioButton) radioGroup.getChildAt(i);
            radio.setTextColor(ContextCompat.getColor(context, optionColor));
        }
    }

    private void buildRadio(LayoutInflater inflater, RadioGroup radioGroup) {

        String exOpt;

        RadioButton radio;

        if (type == 1 || type == EX_IMG_TYPE) {
            exOpt = exOptTitleShort;
        } else {
            exOpt = exOptTitleLong;
        }

        if (lessOptions) {
            exOpt = exOptTitleLongLess;
        }


        switch (exOpt) {
            case "long":
                radio = (RadioButton) inflater.inflate(R.layout.exercise_option_long, null);
                break;
            case "long_less":
                radio = (RadioButton) inflater.inflate(R.layout.exercise_option_long_less, null);
                break;
            default:
                radio = (RadioButton) inflater.inflate(R.layout.exercise_option, null);
                break;
        }



        radio.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
        radioGroup.addView(radio);

    }


    private void setTextStyle(TextView textView, String text) {

        if (type == 1) {
            textView.setTypeface(null, Typeface.NORMAL);
        } else {
            textView.setTypeface(null, Typeface.BOLD);
        }


        if (type == 2) {

            int questShortTextSize = context.getResources().getInteger(R.integer.ex_quest_short_txt_size_small);

            if (text.length() > 18) {
                questShortTextSize = context.getResources().getInteger(R.integer.ex_quest_short_txt_size_norm);
            }

            textView.setTextSize(questShortTextSize);

        } else {

            int questLongTextSize = textLongSize(text);
            textView.setTextSize(questLongTextSize);

        }

    }

    private int textLongSize(String text) {
        int textLength = text.length();

        int tSize = context.getResources().getInteger(R.integer.ex_quest_txt_size_norm);
        if ( textLength > 60) tSize = context.getResources().getInteger(R.integer.ex_quest_txt_size_medium);
        if ( textLength > 150 ) tSize = context.getResources().getInteger(R.integer.ex_quest_txt_size_small);
        if ( textLength > 200 ) tSize = context.getResources().getInteger(R.integer.ex_quest_txt_size_smallest);

        return tSize;
    }

    private void setExOptions(RadioGroup radiogroup, ExerciseTask task) {

        int[] correctTag = new int[]{ task.correct };
        radiogroup.setTag(correctTag);
        for (int i = 0; i < radiogroup.getChildCount(); i++) {

            String optionTxt = task.options.get(i);

            RadioButton radio = (RadioButton) radiogroup.getChildAt(i);

            if ( type == 1 || type == EX_IMG_TYPE ) {
                radio.setTypeface(null, Typeface.BOLD);
                if (optionTxt.length() > 20)
                    radio.setTextSize(context.getResources().getInteger(R.integer.ex_opt_short_txt_size_small));
            } else {
                radio.setTypeface(null, Typeface.NORMAL);
                radio.setTextSize(optionTextSize(optionTxt));
            }

            radio.setText(optionTxt);

        }

        setDefaultRadio(radiogroup);


    }



    private int optionTextSize(String text) {
        int textLength = text.length();


        int tSize = context.getResources().getInteger(R.integer.ex_opt_txt_size_norm);

        if (textLength > context.getResources().getInteger(R.integer.ex_opt_txt_length_long) ) {
            tSize = context.getResources().getInteger(R.integer.ex_opt_txt_size_small);
        }

        if ( textLength > context.getResources().getInteger(R.integer.ex_opt_txt_length_longest) ) {
            if (!lessOptions) tSize = context.getResources().getInteger(R.integer.ex_opt_txt_size_smallest);
        }

        return tSize;
    }



    private void checkItem(RadioGroup _radioGroup, int _position) {
        final RadioGroup radioGroup = _radioGroup;
        final int position = _position;
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                exCheckItem(radioGroup, position);
            }
        }, 300);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                ExerciseActivity.goToNextTask();
            }
        }, 1700);

    }

    void exCheckItem(RadioGroup radioGroup, int position)  {
        Boolean addToCorrect = !ExerciseActivity.exCheckedStatus;
        ExerciseActivity.exCheckedStatus = true;

        Boolean saveStats = ExerciseActivity.saveStats;

        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedRadioButtonId);
        int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);

        int[] data = (int[]) radioGroup.getTag() ;
        int correctTag = data[0];

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radio = (RadioButton) radioGroup.getChildAt(i);
            radio.setEnabled(false);
            radio.setTextColor(ContextCompat.getColor(context, disableColor));

            if (i == correctTag) {
                radio.setTextColor(ContextCompat.getColor(context, R.color.radio_correct));
            }
        }



        String savedInfo = tasks.get(position).savedInfo;  ///// must get position


        if (checkedIndex == correctTag) {
            if (ExerciseActivity.exButtonShow) {
                if (addToCorrect) {
                    ExerciseActivity.correctAnswers++;
                    if ( saveStats && !savedInfo.equals("") ) dbHelper.setWordResult(savedInfo);
                    saveCompleted(savedInfo, 0);
                }
            } else {
                ExerciseActivity.correctAnswers++;
                if ( saveStats && !savedInfo.equals("") )  dbHelper.setWordResult(savedInfo);
                saveCompleted(savedInfo, 0);
            }
            showCorrect(radioGroup);

        }else{

            if (checkedRadioButton != null) {
                checkedRadioButton.setTextColor(ContextCompat.getColor(context, R.color.red_snack));
                checkedRadioButton.setPaintFlags(checkedRadioButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            showWrong(radioGroup);


            if (ExerciseActivity.exButtonShow) {
                if (addToCorrect) {
                    if ( saveStats && !savedInfo.equals("") )  dbHelper.setError(savedInfo);
                    saveCompleted(savedInfo, 1);
                }
            } else {
                if ( saveStats && !savedInfo.equals("") )  dbHelper.setError(savedInfo);
                saveCompleted(savedInfo, 1);
            }


        }
    }



    private void insertImage (ExerciseTask task, View itemView, int position) {

        ImageView image = itemView.findViewById(R.id.exImage);


        ExerciseActivity.fCounterInfoBox.setVisibility(View.GONE);

        TextView exImgCounter = itemView.findViewById(R.id.exImgCounter);

        View textWrapper = itemView.findViewById(R.id.exTextWrapper);
        View imageWrapper = itemView.findViewById(R.id.exImageWrapper);

        textWrapper.setVisibility(View.GONE);
        imageWrapper.setVisibility(View.VISIBLE);


        String counter = (position+1) + "/" +tasks.size();

        exImgCounter.setText(counter);

        Picasso.with(context )
                .load("file:///android_asset/pics/"+ task.quest )
                .transform(new RoundedCornersTransformation(20,0))
                .fit()
                .centerCrop()
                .into(image);

    }

    private void saveCompleted(String id, int result) {
        ExerciseActivity.saveCompleted(id, result);
    }

    private void showCorrect (View view) {
        String message = context.getResources().getString(R.string.ex_txt_correct);
        int background = ContextCompat.getColor(context, R.color.green_snack);
        int textColor = ContextCompat.getColor(context, R.color.green_snack_txt);
        ExerciseActivity.showCheckResult (view, message, background, textColor);
    }

    private void showWrong (View view) {
        String message = context.getResources().getString(R.string.ex_txt_incorrect);
        int background = ContextCompat.getColor(context, R.color.red_snack);
        int textColor = ContextCompat.getColor(context, R.color.red_snack_txt);
        ExerciseActivity.showCheckResult(view, message, background, textColor);
    }


}

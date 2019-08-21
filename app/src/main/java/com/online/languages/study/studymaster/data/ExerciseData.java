package com.online.languages.study.studymaster.data;

import android.content.Context;

import com.online.languages.study.studymaster.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class ExerciseData {

    ArrayList<ExDataItem> data = new ArrayList<>();
    public ArrayList<ExerciseTask> tasks = new ArrayList<>();

    private int dataLength;

    private ArrayList<String> answersList = new ArrayList<>();

    Context context;

    int exType = 1;



    public ExerciseData(ArrayList<ExDataItem> _data, Context _context) {
        data = _data;
        dataLength = data.size();

        context = _context;

        for (int i = 0; i<dataLength; i++) {
            answersList.add(data.get(i).answer);
        }


        //prepareData();
    }



    public ExerciseData(Context _context, ArrayList<DataItem> _data, int _exType) {

        dataLength = _data.size();
        context = _context;
        exType = _exType;

        ExerciseDataCollect exerciseDataCollect = new ExerciseDataCollect(_context, _data, exType);

        tasks = exerciseDataCollect.tasks;

    }



    private void prepareData() {
        for (int i=0; i < dataLength; i++) {
          //  tasks.add( createTask(i) );
        }
    }

    private ExerciseTask setTask(int position) {
        int optionsLength = 4;

        int correctOptionIndex;
        String quest = data.get(position).quest;
        String questInfo = data.get(position).questInfo;
        String savedInfo = data.get(position).savedInfo;

        ArrayList<String> optionsText = new ArrayList<>();

        List<Integer> optionsData = new ArrayList<>();  // indexes for options from data array
        optionsData.add(position);

        List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < dataLength; i++) {dataList.add(i);}
        dataList.remove(position);
        Collections.shuffle(dataList);

        for (int i = 0; i < (optionsLength-1); i++) { optionsData.add(dataList.get(i)); }
        Collections.shuffle(optionsData);
        correctOptionIndex = optionsData.indexOf(position);  // index for correct from options array

        for (int i = 0; i < optionsLength; i++) {
            int optionIndexForOriginal = optionsData.get(i);
            String optionTxt = data.get(optionIndexForOriginal).answer;
            optionsText.add( optionTxt );
        }
        return new ExerciseTask(quest, questInfo, optionsText, correctOptionIndex, savedInfo);
    }


    private ExerciseTask createTask(int position) {

        int correctOptionIndex=0;

        String quest = data.get(position).quest;
        String questInfo = data.get(position).questInfo;
        String savedInfo = data.get(position).savedInfo;



        ArrayList<String> allAnswers = new ArrayList<>(answersList);


        ArrayList<String> options = new ArrayList<>();
        int optionsLength = Constants.TEST_OPTIONS_NUM;


        String tAnswer = data.get(position).answer;

        allAnswers.remove(position);

        options.add(tAnswer);

        Collections.shuffle(allAnswers);

        for (int i = 0; i < optionsLength-1; i++) {
            String option = getOption(options, allAnswers, tAnswer);
            options.add(option);
        }

        // options.set(0, options.get(0)+"*");

        Random rand = new Random();
        correctOptionIndex = rand.nextInt(optionsLength);
        Collections.rotate(options.subList(0, correctOptionIndex+1), -1);

        return new ExerciseTask(quest, questInfo, options, correctOptionIndex, savedInfo);

    }

    private String getOption(ArrayList<String> options, ArrayList<String> answers, String answer) {

        Collections.shuffle(answers);

        String option = answers.get(0);

        if (answer.equals(option)) option = answers.get(1);


        for (int i = 0; i<answers.size(); i++) {
            if ( ! checkOptions(answers.get(i), options ) ) {
                option = answers.get(i);
                break;
            }
        }

        return option;
    }

    private Boolean checkOptions(String option, ArrayList<String> optionsList) {
        Boolean foundSame = false;
        for (int i=0; i<optionsList.size(); i++) {
            if (option.equals(optionsList.get(i))) {
                foundSame = true;
                break;
            }
        }

        return foundSame;
    }


}

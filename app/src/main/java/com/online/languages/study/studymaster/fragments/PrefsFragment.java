package com.online.languages.study.studymaster.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.online.languages.study.studymaster.CatActivity;
import com.online.languages.study.studymaster.Constants;
import com.online.languages.study.studymaster.MainActivity;
import com.online.languages.study.studymaster.R;

import static com.online.languages.study.studymaster.Constants.APP_SIMPLIFIED;


public class PrefsFragment extends PreferenceFragmentCompat {

    PreferenceScreen screen;
    PreferenceGroup preferenceParent;


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //add xml

        addPreferencesFromResource(R.xml.settings);

        screen = getPreferenceScreen();
        preferenceParent = (PreferenceGroup) findPreference("interface");

        Preference hidden = getPreferenceManager().findPreference("hidden");
        screen.removePreference(hidden);

        Preference controlTests = getPreferenceManager().findPreference("control_tests");

        if (APP_SIMPLIFIED) {
            Preference data = getPreferenceManager().findPreference("data");
            screen.removePreference(data);

            controlTests.setVisible(false);
        }


        SharedPreferences appSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean full_version = appSettings.getBoolean(Constants.SET_VERSION_TXT, false);

        Preference versionItem = getPreferenceManager().findPreference("version");

        if (full_version)  versionItem.setVisible(false);



        final ListPreference btm = (ListPreference) getPreferenceManager().findPreference("btm_nav");
        if (Build.VERSION.SDK_INT < 21) btm.setVisible(false);

        btm.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                new android.os.Handler().postDelayed(new Runnable() {
                    public void run() {
                        ((MainActivity)getActivity()).bottomNavDisplay();
                    }
                }, 200);
                return true;
            }
        });



        final ListPreference list = (ListPreference) getPreferenceManager().findPreference("theme");

        //Toast.makeText(getActivity(), "Pref: "+ list, Toast.LENGTH_SHORT).show();

        list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Intent intent = getActivity().getIntent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);

                return true;
            }
        });



        final SwitchPreferenceCompat version = (SwitchPreferenceCompat) getPreferenceManager().findPreference(Constants.SET_VERSION_TXT);

        /*
                version.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        new android.os.Handler().postDelayed(new Runnable() {
                            public void run() {
                                Intent intent = getActivity().getIntent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                getActivity().startActivity(intent);
                            }
                        }, 600);
                        return true;
                    }
                });

*/
    }


    @Override
    public RecyclerView onCreateRecyclerView(LayoutInflater inflater,
                                             ViewGroup parent, Bundle savedInstanceState) {
        RecyclerView list = super.onCreateRecyclerView(inflater, parent,
                savedInstanceState);
        if (list != null) {
            ViewCompat.setNestedScrollingEnabled(list, false);
        }
        return list;
    }




}

package com.online.languages.study.studymaster;

import android.content.Context;
import android.os.Build;

import com.online.languages.study.studymaster.data.DataManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@Config(sdk = {Build.VERSION_CODES.O_MR1})
@RunWith(RobolectricTestRunner.class)
public class PreReleaseTest {

    private Context mockContext;

    @Before
    public void setUp() {
        mockContext = RuntimeEnvironment.application.getApplicationContext();
    }


    @Test
    public void pro_isCorrect() throws Exception {
        assertFalse(Constants.PRO);
    }

    @Test
    public void debug_isCorrect() throws Exception {
        assertFalse(Constants.DEBUG);
    }

    @Test
    public void jsonParams_areCorrect() throws Exception {
        DataManager dataManager = new DataManager(mockContext);
        dataManager.getParamsFromJSON();
        assertFalse(dataManager.simplified);
        assertFalse(dataManager.homecards);
        assertTrue(dataManager.gallerySection);
    }



}
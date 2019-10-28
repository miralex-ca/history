package com.online.languages.study.studymaster;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
public class PreReleaseAncientTest {

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
    public void gallery_isCorrect() throws Exception {
        assertFalse(Constants.GALLERY_SECTION);
    }

    @Test
    public void homeCards_isCorrect() throws Exception {
        assertFalse(Constants.HOME_CARDS);
    }

    @Test
    public void simplified_isCorrect() throws Exception {
        assertTrue(Constants.APP_SIMPLIFIED);
    }


    @Test
    public void sku_isCorrect() throws Exception {
        assertEquals(MainActivity.SKU_PREMIUM, "premium_upgrade");
    }

    @Test
    public void dbName_isCorrect() throws Exception {
        assertEquals(DBHelper.DATABASE_NAME, "userProgress");
    }

    @Test
    public void appId_isCorrect() throws Exception {
        assertEquals(BuildConfig.APPLICATION_ID, "com.online.languages.study.ancienthistory");
    }

    @Test
    public void appName_isCorrect() throws Exception {
        assertEquals("История Древнего Мира", mockContext.getString(R.string.app_name));
    }


    @Test
    public void exportFileName_isCorrect() throws Exception {
        assertEquals("Backup_stats.ahdb", mockContext.getString(R.string.backup_file_name));
    }

    @Test
    public void sectionIdLength_isCorrect() throws Exception {
        assertEquals(5, mockContext.getResources().getInteger(R.integer.section_id_length));
    }








}
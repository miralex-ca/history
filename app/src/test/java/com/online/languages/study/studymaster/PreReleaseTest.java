package com.online.languages.study.studymaster;

import org.junit.Test;

import static org.junit.Assert.*;

public class PreReleaseTest {

    @Test
    public void pro_isCorrect() throws Exception {
        assertFalse(Constants.PRO);
    }

    @Test
    public void debug_isCorrect() throws Exception {
        assertFalse(Constants.DEBUG);
    }

    @Test
    public void homeCards_isCorrect() throws Exception {
        assertFalse(Constants.HOME_CARDS);
    }

    @Test
    public void simplified_isCorrect() throws Exception {
        assertFalse(Constants.APP_SIMPLIFIED);
    }

    @Test
    public void gallery_isCorrect() throws Exception {
        assertTrue(Constants.GALLERY_SECTION);
    }


}
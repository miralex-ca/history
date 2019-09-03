package com.online.languages.study.studymaster;


import android.support.v4.content.ContextCompat;

public class Constants {

    public static final boolean PRO = true;  // TODO change in PRO
    public static final boolean DEBUG = true;
    public static final boolean SCREEN_SHOW = false;

    public static final String SET_VERSION_TXT = "full_version"; /// check text in settings
    public static final String SET_SHOW_AD= "show_ad"; /// check text in settings

    public static final String SET_THEME_TXT = "theme";
    public static final String SET_THEME_DEFAULT = "default";
    public static final String SET_THEME_DARK = "dark";

    public static final String APP_STARRED = "starred";
    public static final String APP_HISTORY = "history"; // настройка для истории

    public static final String LIST_ALL = "all_kanji";
    public static final String LIST_STUDIED = "studied";
    public static final String LIST_KNOWN = "known";
    public static final String LIST_FAMILIAR = "familiar";
    public static final String LIST_SEEN = "seen";

    public static final int STARRED_LIMIT = 30;
    public static final int LIMIT_STARRED_EX = 1;

    public static final int RATE_INCLUDE = 4; // used for priority of unfamiliar dataItems in tests

    public static final int QUEST_NUM = 60;  // limit for tests
    public static final int ALL_TEST_NUM = 100;  //  100 for prod

    public static final int REVISE_NUM = 30;

    static final String EXTRA_KEY_WORDS = "dataItems";
    public static final String EXTRA_CAT_TAG = "cat_tag";

    public static final String EXTRA_SECTION_NUM = "section_num";

    public static final String EXTRA_SECTION_ID = "section_id";
    public static final String EXTRA_CAT_ID = "cat_id";
    public static final String EXTRA_CAT_SPEC = "cat_spec";

    public static final String CAT_SPEC_DEFAULT = "norm";
    public static final String CAT_SPEC_PERS = "pers";
    public static final String CAT_SPEC_TERM = "term";
    public static final String CAT_SPEC_MISC = "misc";

    public static final String EXTRA_NAV_STRUCTURE = "nav_structure";

    public static final String EXTRA_FORCE_STATUS = "force_status";


    public static final String EXTRA_SECTION_PARENT = "parent";


    public static final int CAT_TESTS_NUM = 2;
    public static final int SECTION_TESTS_NUM = 2;
    public static final int SECTION_TESTS_NUM_ALL = 3;


    public static final int TEST_OPTIONS_NUM = 4;
    public static final int TEST_LONG_OPTIONS_NUM = 3;
    public static final int TEST_LONG_OPTION_LEN = 40;


    public static final int DATA_MODE = 1;


    public static final String EXTRA_DATA_TYPE = "data_type";

    public static final String CAT_TYPE_EXTRA = "extra";

    public static final String SET_DATA_MODE = "data_mode";

    public static final String STARRED_CAT_TAG = "starred";
    public static final String ERRORS_CAT_TAG = "errors";

    public static final String ALL_CAT_TAG = "all";
    public static final String SECTION_TEST_PREFIX = "all_";
    public static final String SECTION_TEST_EXTRA_POSTFIX = "_gen";

    public static final String STATUS_SHOW_DEFAULT = "1";

    public static final Boolean ONE_CAT = BuildConfig.SIMPLIFIED;


    public static final String REVISE_CAT_TAG = "revise";




    public enum Status {

        STUDIED,
        FAMILIAR,
        UNKNOWN

    }

}

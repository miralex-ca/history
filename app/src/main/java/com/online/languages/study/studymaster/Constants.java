package com.online.languages.study.studymaster;


public class Constants {

    public static final boolean PRO = false;  // TODO change in PRO
    public static final boolean DEBUG = false;  /// should be true to see ads in debug
    public static final boolean SCREEN_SHOW = false;

    public static final String SET_VERSION_TXT = "full_version"; /// check text in settings
    public static final String SET_SHOW_AD= "show_ad"; /// check text in settings

    public static final String SET_THEME_TXT = "theme";
    public static final String SET_THEME_DEFAULT = "default";
    public static final String SET_THEME_DARK = "dark";

    public static final String SET_GALLERY_LAYOUT = "gallery_layout";
    public static final int SET_GALLERY_LAYOUT_DEFAULT = 1;

    public static final String LIST_STUDIED = "studied";

    public static final int STARRED_LIMIT = 30;

    public static final int LIMIT_STARRED_EX = 1;

    public static final int RATE_INCLUDE = 4; // used for priority of unfamiliar dataItems in tests

    public static final int QUEST_NUM = 60;  // limit for tests
    public static final int SECTION_TEST_LIMIT = 100;  //  100 for prod
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
    public static final String CAT_SPEC_MAPS = "maps_list";
    public static final String CAT_SPEC_ITEMS_LIST = "items_list";
    public static final String CAT_SPEC_TEXT = "text";

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


    public static final int EX_IMG_TYPE = 3;

    public static final String CAT_TYPE_EXTRA = "extra";

    public static final String SET_DATA_MODE = "data_mode";

    public static final String STARRED_CAT_TAG = "starred";
    public static final String ERRORS_CAT_TAG = "errors";

    public static final String ALL_CAT_TAG = "all";
    public static final String SECTION_TEST_PREFIX = "all_";
    public static final String SECTION_TEST_EXTRA_POSTFIX = "_gen";

    public static final String STATUS_SHOW_DEFAULT = "1";

    public static final String IMG_LIST_LAYOUT = "img_list_layout";

    public static final String REVISE_CAT_TAG = "revise";
    public static final int EXPAND_TIME = 920;

    public static final String INFO_TAG = "#info";
    public static final String GALLERY_TAG = "#gallery";
    public static final String NAV_GALLERY_SPEC = "nav_gallery";

    public static final String MAPS_FOLDER= "pics/maps";

    public static final String STARRED_TAB_ACTIVE = "starred_active_tab";

    public static final int GALLERY_REQUESTCODE = 100;

    public static final int TEST_OPTIONS_RANGE = 10;

    public static final String FILTER_CHRONO = "chrono";

    public enum Status {
        STUDIED,
        FAMILIAR,
        UNKNOWN
    }

    public static final String TAB_ITEMS = "normal";
    public static final String TAB_GALLERY = "gallery";
    public static final String TABS_NORMAL = "normal";



    public static final String SET_SIMPLIFIED = "param_simplified";
    public static final String SET_HOMECARDS = "param_homecards";
    public static final String SET_GALLERY = "param_gallery";
    public static final String SET_STATS = "param_stats";

    public static final String STARRED_TABS = "gallery";


    public static final int TASK_DELAY_CORRECT = 900;
    public static final int TASK_DELAY_INCORRECT = 1800;


    public static final String FOLDER_PICS = "file:///android_asset/pics/";

    public static final String ACTION_NONE = "none";
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_VIEW = "view";
    public static final String ACTION_BOOKMARK = "bookmark";
    public static final String ACTION_CHANGE_ORDER = "change_order";
    public static final String ACTION_ARCHIVE = "archive";
    public static final String ACTION_MOVE = "move";
    public static final String ACTION_EDIT_GROUP = "edit_group";


    public static final String STATUS_NEW = "new";
    public static final String STATUS_NORM = "norm";
    public static final String STATUS_DELETED = "deleted";
    public static final String STATUS_UPDATED = "updated";
    public static final String STATUS_UPDATED_SORT = "updated_sort";

    public static final String EXTRA_NOTE_ID = "note_id";
    public static final String EXTRA_NOTE_ACTION = "note_action";

    public static final int NOTE_PIC_DEFAULT_INDEX = 1;

    public static final int NOTES_LIST_LIMIT = 30;  // normal 30
    public static final boolean NOTES_LIST_ANIMATION = true;

    public static final String NOTE_ARCHIVE = "note_archive";


}

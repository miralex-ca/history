package com.online.languages.study.studymaster.data;


public class ImageData {

    public String id = "";
    public String title = "";
    public String desc = "";
    public String weblink = "";
    public String author = "";
    public String image = "";


    public int prescale = 0;


    public ImageData() {}

    public ImageData(String _title, String _desc) {
        title = _title;
        desc = _desc;
    }

    public ImageData(String _title, String _link, String _id, String _image) {
        title = _title;
        weblink = _link;
        id = _id;
        image = _image;
    }

    public ImageData(String _title, String _link, String _id, String _image, String _desc) {
        title = _title;
        weblink = _link;
        id = _id;
        image = _image;
        desc = _desc;
    }

    public ImageData(String _title, String _link, String _id, String _image, String _desc, int _prescale) {
        title = _title;
        weblink = _link;
        id = _id;
        image = _image;
        desc = _desc;
        prescale = _prescale;
    }


}

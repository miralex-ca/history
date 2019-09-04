package com.online.languages.study.studymaster.data;

import android.os.Parcel;
import android.os.Parcelable;

public class DataItem implements Parcelable {

    public String item = "";
    public String info ="";
    public String id = "";
    public String image = "";

    public String divider = "";
    public int mode = 0;


    public int starred = 0;
    public long starred_time = 0;
    public int rate = 0;
    public int errors = 0;
    public int testError = 0;

    public long time = 0;
    public long time_errors = 0;



    public String type = "";

    public String cat = "";

    public DataItem() {
    }

    public DataItem(String _item) {
        item = _item;
    }

    public DataItem(String _item, String _info) {
        item = _item;
        info = _info;
    }

    public DataItem(String _item, String _info, String _id) {
        item = _item;
        info = _info;
        id = _id;
    }

    public DataItem(String _item, String _info, String _id, String _image) {
        item = _item;
        info = _info;
        id = _id;
        image = _image;
    }

    public DataItem(Parcel parcel){

        this.item = parcel.readString();
        this.info = parcel.readString();
        this.id = parcel.readString();
        this.image = parcel.readString();
        this.starred = parcel.readInt();
        this.testError = parcel.readInt();
    }


    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(item);
        dest.writeString(info);
        dest.writeString(id);
        dest.writeString(image);
        dest.writeInt(starred);
        dest.writeInt(testError);

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DataItem createFromParcel(Parcel source) {
            return new DataItem(source);
        }

        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };
}

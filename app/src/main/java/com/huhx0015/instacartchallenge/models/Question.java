package com.huhx0015.instacartchallenge.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class Question implements Parcelable {

    private String mItem;
    private List<String> mUrlList;

    public Question(String item, List<String> urlList) {
        this.mItem = item;
        this.mUrlList = urlList;
    }

    protected Question(Parcel in) {
        mItem = in.readString();
        mUrlList = in.createStringArrayList();
    }

    public String getItem() {
        return mItem;
    }

    public List<String> getUrlList() {
        return mUrlList;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mItem);
        dest.writeStringList(mUrlList);
    }
}

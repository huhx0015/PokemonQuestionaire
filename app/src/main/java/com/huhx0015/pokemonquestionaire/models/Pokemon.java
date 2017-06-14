package com.huhx0015.pokemonquestionaire.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class Pokemon implements Parcelable {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private String mItem;
    private List<String> mUrlList;

    public Pokemon(String item, List<String> urlList) {
        this.mItem = item;
        this.mUrlList = urlList;
    }

    protected Pokemon(Parcel in) {
        mItem = in.readString();
        mUrlList = in.createStringArrayList();
    }

    public String getItem() {
        return mItem;
    }

    public List<String> getUrlList() {
        return mUrlList;
    }

    public static final Creator<Pokemon> CREATOR = new Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
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

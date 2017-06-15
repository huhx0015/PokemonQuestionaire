package com.huhx0015.pokemonquestionaire.models.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

@Entity(tableName = "pokemon")
public class Pokemon implements Parcelable {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    @PrimaryKey
    private int mId;

    private String mItem;
    private List<String> mUrlList;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public Pokemon(int id, String item, List<String> urlList) {
        this.mId = id;
        this.mItem = item;
        this.mUrlList = urlList;
    }

    /** GET METHODS ____________________________________________________________________________ **/

    public int getId() {
        return mId;
    }

    public String getItem() {
        return mItem;
    }

    public List<String> getUrlList() {
        return mUrlList;
    }

    /** PARCELABLE METHODS _____________________________________________________________________ **/

    protected Pokemon(Parcel in) {
        mItem = in.readString();
        mUrlList = in.createStringArrayList();
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

package com.huhx0015.instacartchallenge.models;

import java.util.List;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class GroceryQuestion {

    private String mGroceryName;
    private List<String> mGroceryImageList;

    public String getGroceryName() {
        return mGroceryName;
    }

    public List<String> getGroceryImageList() {
        return mGroceryImageList;
    }

    public void setGroceryName(String mGroceryName) {
        this.mGroceryName = mGroceryName;
    }

    public void setGroceryImageList(List<String> mGroceryImageList) {
        this.mGroceryImageList = mGroceryImageList;
    }
}

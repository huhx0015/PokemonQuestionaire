package com.huhx0015.instacartchallenge.models;

import java.util.List;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class Question {

    private String mItem;
    private List<String> mUrlList;

    public Question(String item, List<String> urlList) {
        this.mItem = item;
        this.mUrlList = urlList;
    }

    public String getItem() {
        return mItem;
    }

    public List<String> getUrlList() {
        return mUrlList;
    }
}

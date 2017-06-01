package com.huhx0015.instacartchallenge.models;

import java.util.List;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class QuestionsResponse {

    private List<Question> mQuestionList;

    public QuestionsResponse(List<Question> questionList) {
        this.mQuestionList = questionList;
    }

    public List<Question> getQuestionList() {
        return mQuestionList;
    }
}

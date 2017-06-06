package com.huhx0015.pokemonquestionaire.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class QuestionsResponse implements Parcelable {

    private List<Question> mQuestionList;

    public QuestionsResponse(List<Question> questionList) {
        this.mQuestionList = questionList;
    }

    protected QuestionsResponse(Parcel in) {
        mQuestionList = in.createTypedArrayList(Question.CREATOR);
    }

    public List<Question> getQuestionList() {
        return mQuestionList;
    }

    public static final Creator<QuestionsResponse> CREATOR = new Creator<QuestionsResponse>() {
        @Override
        public QuestionsResponse createFromParcel(Parcel in) {
            return new QuestionsResponse(in);
        }

        @Override
        public QuestionsResponse[] newArray(int size) {
            return new QuestionsResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mQuestionList);
    }
}

package com.huhx0015.pokemonquestionaire.viewmodels.fragments;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;
import com.huhx0015.pokemonquestionaire.R;

/**
 * Created by Michael Yoon Huh on 6/14/2017.
 */

public class QuestionViewModel extends ViewModel {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // OBSERVABLE VARIABLES:
    public ObservableField<Integer> submitButtonVisibility = new ObservableField<>();
    public ObservableField<String> questionText = new ObservableField<>();
    public ObservableField<String> instructionsText = new ObservableField<>();
    public ObservableField<String> timeRemainingText = new ObservableField<>();
    public ObservableField<String> submitButtonText = new ObservableField<>();
    public ObservableField<String> firstQuestionImage = new ObservableField<>();
    public ObservableField<String> secondQuestionImage = new ObservableField<>();
    public ObservableField<String> thirdQuestionImage = new ObservableField<>();
    public ObservableField<String> fourthQuestionImage = new ObservableField<>();

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public QuestionViewModel(Context context) {
        setSubmitButtonVisible(false);
        setSubmitButtonText(context.getString(R.string.question_submit));
    }

    /** GET METHODS ____________________________________________________________________________ **/

    public boolean getSubmitButtonVisible() {
        switch (submitButtonVisibility.get()) {
            case View.VISIBLE:
                return true;
            case View.GONE:
                return false;
            default:
                return false;
        }
    }

    /** SET METHODS ____________________________________________________________________________ **/

    public void setQuestionImage(int position, String imageUrl) {
        switch(position) {
            case 0:
                setFirstQuestionImage(imageUrl);
                break;
            case 1:
                setSecondQuestionImage(imageUrl);
                break;
            case 2:
                setThirdQuestionImage(imageUrl);
                break;
            case 3:
                setFourthQuestionImage(imageUrl);
                break;
        }
    }

    /** OBSERVABLE METHODS _____________________________________________________________________ **/

    public void setSubmitButtonVisible(boolean isVisible) {
        if (isVisible) {
            submitButtonVisibility.set(View.VISIBLE);
        } else {
            submitButtonVisibility.set(View.GONE);
        }
    }

    public void setInstructionsText(String text) {
        instructionsText.set(text);
    }

    public void setQuestionText(String text) {
        questionText.set(text);
    }

    public void setSubmitButtonText(String text) {
        submitButtonText.set(text);
    }

    public void setTimeRemainingText(String text) {
        timeRemainingText.set(text);
    }

    public void setFirstQuestionImage(String url) {
        firstQuestionImage.set(url);
    }

    public void setSecondQuestionImage(String url) {
        secondQuestionImage.set(url);
    }

    public void setThirdQuestionImage(String url) {
        thirdQuestionImage.set(url);
    }

    public void setFourthQuestionImage(String url) {
        fourthQuestionImage.set(url);
    }
}

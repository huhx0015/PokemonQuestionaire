package com.huhx0015.pokemonquestionaire.viewmodels.fragments;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.view.View;

/**
 * Created by Michael Yoon Huh on 6/14/2017.
 */

public class QuestionViewModel extends ViewModel {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // OBSERVABLE VARIABLES:
    public ObservableField<Integer> submitButtonVisiblity = new ObservableField<>();
    public ObservableField<String> questionText = new ObservableField<>();
    public ObservableField<String> instructionsText = new ObservableField<>();
    public ObservableField<String> timeRemainingText = new ObservableField<>();
    public ObservableField<String> firstQuestionImage = new ObservableField<>();
    public ObservableField<String> secondQuestionImage = new ObservableField<>();
    public ObservableField<String> thirdQuestionImage = new ObservableField<>();
    public ObservableField<String> fourthQuestionImage = new ObservableField<>();

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public QuestionViewModel() {
        setSubmitButtonVisible(false);
    }

    /** GET METHODS ____________________________________________________________________________ **/

    public boolean getSubmitButtonVisible() {
        switch (submitButtonVisiblity.get()) {
            case View.VISIBLE:
                return true;
            case View.GONE:
                return false;
            default:
                return false;
        }
    }

    /** OBSERVABLE METHODS _____________________________________________________________________ **/

    public void setSubmitButtonVisible(boolean isVisible) {
        if (isVisible) {
            submitButtonVisiblity.set(View.VISIBLE);
        } else {
            submitButtonVisiblity.set(View.GONE);
        }
    }

    public void setInstructionsText(String text) {
        instructionsText.set(text);
    }

    public void setQuestionText(String text) {
        questionText.set(text);
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

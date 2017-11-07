package com.huhx0015.pokemonquestionaire.ui.fragments.question;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.view.View;
import com.huhx0015.pokemonquestionaire.constants.PokemonConstants;
import com.huhx0015.pokemonquestionaire.models.entities.Pokemon;

/**
 * Created by Michael Yoon Huh on 6/14/2017.
 */

public class QuestionViewModel extends ViewModel {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // CONSTANT VARIABLES:
    private static final int FIRST_QUESTION_ID = 0;
    private static final int SECOND_QUESTION_ID = 1;
    private static final int THIRD_QUESTION_ID = 2;
    private static final int FOURTH_QUESTION_ID = 3;

    // DATA VARIABLES:
    private boolean mIsTimeUp = false;
    private int mCorrectPosition;
    private int mSelectedPosition = PokemonConstants.STATE_CORRECT_POSITION_UNSET;
    private Pokemon mPokemon;

    // LISTENER VARIABLES:
    private QuestionViewModelListener mListener;

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

    /** CLICK LISTENER METHODS _________________________________________________________________ **/

    public void onClickSubmitButton(View view) {
        if (mListener != null) {
            mListener.onSubmitButtonClicked();
        }
    }

    public void onClickFirstQuestionImage(View view) {
        if (mListener != null) {
            mListener.onQuestionImageClicked(FIRST_QUESTION_ID);
        }
    }

    public void onClickSecondQuestionImage(View view) {
        if (mListener != null) {
            mListener.onQuestionImageClicked(SECOND_QUESTION_ID);
        }
    }

    public void onClickThirdQuestionImage(View view) {
        if (mListener != null) {
            mListener.onQuestionImageClicked(THIRD_QUESTION_ID);
        }
    }

    public void onClickFourthQuestionImage(View view) {
        if (mListener != null) {
            mListener.onQuestionImageClicked(FOURTH_QUESTION_ID);
        }
    }

    /** GET METHODS ____________________________________________________________________________ **/

    int getCorrectPosition() {
        return mCorrectPosition;
    }

    public Pokemon getPokemon() {
        return mPokemon;
    }

    int getSelectedPosition() {
        return mSelectedPosition;
    }

    boolean isTimeUp() {
        return mIsTimeUp;
    }

    /** SET METHODS ____________________________________________________________________________ **/

    void setCorrectPosition(int correctPosition) {
        this.mCorrectPosition = correctPosition;
    }

    void setIsTimeUp(boolean isTimeUp) {
        this.mIsTimeUp = isTimeUp;
    }

    public void setPokemon(Pokemon pokemon) {
        this.mPokemon = pokemon;
    }

    void setQuestionImage(int position, String imageUrl) {
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

    void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }

    public void setListener(QuestionViewModelListener listener) {
        this.mListener = listener;
    }

    /** OBSERVABLE METHODS _____________________________________________________________________ **/

    void setSubmitButtonVisible(boolean isVisible) {
        if (isVisible) {
            submitButtonVisibility.set(View.VISIBLE);
        } else {
            submitButtonVisibility.set(View.GONE);
        }
    }

    void setInstructionsText(String text) {
        instructionsText.set(text);
    }

    void setQuestionText(String text) {
        questionText.set(text);
    }

    void setSubmitButtonText(String text) {
        submitButtonText.set(text);
    }

    void setTimeRemainingText(String text) {
        timeRemainingText.set(text);
    }

    private void setFirstQuestionImage(String url) {
        firstQuestionImage.set(url);
    }

    private void setSecondQuestionImage(String url) {
        secondQuestionImage.set(url);
    }

    private void setThirdQuestionImage(String url) {
        thirdQuestionImage.set(url);
    }

    private void setFourthQuestionImage(String url) {
        fourthQuestionImage.set(url);
    }

    /** INTERFACE METHODS ______________________________________________________________________ **/

    public interface QuestionViewModelListener {
        void onSubmitButtonClicked();
        void onQuestionImageClicked(int position);
    }
}

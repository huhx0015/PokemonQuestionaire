package com.huhx0015.pokemonquestionaire.ui.fragments.result;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.view.View;

/**
 * Created by Michael Yoon Huh on 6/14/2017.
 */

public class ResultViewModel extends ViewModel {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LISTENER VARIABLES:
    private ResultViewModelListener mListener;

    // OBSERVABLE VARIABLES:
    public ObservableField<String> resultText = new ObservableField<>();

    /** CLICK LISTENER METHODS _________________________________________________________________ **/

    public void onClickTryAgainButton(View view) {
        if (mListener != null) {
            mListener.onTryAgainButtonClicked();
        }
    }

    /** SET METHODS ____________________________________________________________________________ **/

    public void setResultText(String text) {
        resultText.set(text);
    }

    public void setListener(ResultViewModelListener listener) {
        this.mListener = listener;
    }

    /** INTERFACE METHODS ______________________________________________________________________ **/

    public interface ResultViewModelListener {
        void onTryAgainButtonClicked();
    }
}

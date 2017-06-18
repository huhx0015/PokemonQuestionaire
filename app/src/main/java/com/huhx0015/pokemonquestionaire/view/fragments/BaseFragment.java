package com.huhx0015.pokemonquestionaire.view.fragments;

import android.arch.lifecycle.LifecycleFragment;
import com.huhx0015.pokemonquestionaire.view.interfaces.MainActivityListener;

/**
 * Created by Michael Yoon Huh on 6/18/2017.
 */

public class BaseFragment extends LifecycleFragment {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // FRAGMENT VARIABLES:
    protected MainActivityListener mListener;

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    /** SET METHODS ____________________________________________________________________________ **/

    public void setListener(MainActivityListener listener) {
        this.mListener = listener;
    }
}

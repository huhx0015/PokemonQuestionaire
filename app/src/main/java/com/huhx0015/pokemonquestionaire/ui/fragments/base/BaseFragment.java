package com.huhx0015.pokemonquestionaire.ui.fragments.base;

import android.support.v4.app.Fragment;
import com.huhx0015.pokemonquestionaire.ui.interfaces.MainActivityListener;

/**
 * Created by Michael Yoon Huh on 6/18/2017.
 */

public class BaseFragment extends Fragment {

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

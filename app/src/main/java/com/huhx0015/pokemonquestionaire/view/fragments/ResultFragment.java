package com.huhx0015.pokemonquestionaire.view.fragments;

import android.arch.lifecycle.LifecycleFragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.huhx0015.pokemonquestionaire.R;
import com.huhx0015.pokemonquestionaire.databinding.FragmentResultBinding;
import com.huhx0015.pokemonquestionaire.view.interfaces.MainActivityListener;
import com.huhx0015.pokemonquestionaire.viewmodels.fragments.QuestionResultViewModel;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class ResultFragment extends LifecycleFragment {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // DATA VARIABLES:
    private boolean mIsCorrect;

    // DATABINDING VARIABLES:
    private FragmentResultBinding mBinding;
    private QuestionResultViewModel mViewModel;

    // FRAGMENT VARIABLES:
    private MainActivityListener mListener;

    // LOGGING VARIABLES:
    private static final String LOG_TAG = ResultFragment.class.getSimpleName();

    // INSTANCE VARIABLES:
    private static final String INSTANCE_IS_CORRECT = LOG_TAG + "_INSTANCE_IS_CORRECT";

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public static ResultFragment newInstance(boolean isCorrect, MainActivityListener listener) {
        ResultFragment fragment = new ResultFragment();
        fragment.mIsCorrect = isCorrect;
        fragment.mListener = listener;
        return fragment;
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (savedInstanceState != null) {
            mIsCorrect = savedInstanceState.getBoolean(INSTANCE_IS_CORRECT);
        }

        Log.d(LOG_TAG, "onCreate(): Answer Result is: " + mIsCorrect);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.unbind();
    }

    /** FRAGMENT EXTENSION METHODS _____________________________________________________________ **/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(INSTANCE_IS_CORRECT, mIsCorrect);
    }

    /** VIEW METHODS ___________________________________________________________________________ **/

    private void initView() {
        initBinding();
        initText();
    }

    private void initBinding() {
        mBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.fragment_result, null, false);
        mViewModel = new QuestionResultViewModel();
        mBinding.setViewModel(mViewModel);
    }

    private void initText() {
        mBinding.fragmentResultText.setText(mIsCorrect ? getString(R.string.result_correct) : getString(R.string.result_wrong));
    }

//    @OnClick(R.id.fragment_result_try_again_button)
//    public void onTryAgainClicked() {
//        mListener.onTryAgainSelected(mIsCorrect);
//    }
}

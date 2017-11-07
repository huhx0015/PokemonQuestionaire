package com.huhx0015.pokemonquestionaire.ui.fragments.result;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.huhx0015.pokemonquestionaire.R;
import com.huhx0015.pokemonquestionaire.databinding.FragmentResultBinding;
import com.huhx0015.pokemonquestionaire.ui.fragments.base.BaseFragment;
import com.huhx0015.pokemonquestionaire.ui.interfaces.MainActivityListener;
import nl.dionsegijn.konfetti.models.Shape;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class ResultFragment extends BaseFragment implements ResultViewModel.ResultViewModelListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // DATA VARIABLES:
    //private boolean mIsCorrect = false;

    // DATABINDING VARIABLES:
    private FragmentResultBinding mBinding;
    private ResultViewModel mViewModel;

    // LOGGING VARIABLES:
    private static final String LOG_TAG = ResultFragment.class.getSimpleName();

    // INSTANCE VARIABLES:
    private static final String INSTANCE_IS_CORRECT = LOG_TAG + "_INSTANCE_IS_CORRECT";

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public static ResultFragment newInstance(boolean isCorrect, MainActivityListener listener) {
        ResultFragment fragment = new ResultFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean(INSTANCE_IS_CORRECT, isCorrect);
        fragment.setArguments(arguments);
        fragment.mListener = listener;
        return fragment;
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();

        if (getArguments() != null) {
            mViewModel.setIsCorrect(getArguments().getBoolean(INSTANCE_IS_CORRECT, false));
            Log.d(LOG_TAG, "onCreate(): Answer Result is: " + mViewModel.isCorrect());
        }

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.unbind();
    }

    /** VIEW METHODS ___________________________________________________________________________ **/

    private void initView() {
        initBinding();
        initGraphics();
        initText();
    }

    private void initBinding() {
        mBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.fragment_result, null, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(ResultViewModel.class);
        mViewModel.setListener(this);
        mBinding.setViewModel(mViewModel);
    }

    private void initGraphics() {
        if (mViewModel.isCorrect()) {
            mBinding.fragmentKonfettiView.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .setPosition(-50f, mBinding.fragmentKonfettiView.getWidth() + 50f, -50f, -50f)
                    .stream(300, 5000L);
        }
    }

    private void initText() {
        mViewModel.setResultText(mViewModel.isCorrect() ? getString(R.string.result_correct) : getString(R.string.result_wrong));
    }

    /** LISTENER METHODS _______________________________________________________________________ **/

    @Override
    public void onTryAgainButtonClicked() {
        mListener.onTryAgainSelected(mViewModel.isCorrect());
    }
}

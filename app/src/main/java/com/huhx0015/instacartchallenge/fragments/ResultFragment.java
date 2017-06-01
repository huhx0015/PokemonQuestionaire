package com.huhx0015.instacartchallenge.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.huhx0015.instacartchallenge.R;
import com.huhx0015.instacartchallenge.interfaces.MainActivityListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class ResultFragment extends Fragment {

    private boolean mIsCorrect;
    private MainActivityListener mListener;
    private Unbinder mUnbinder;

    @BindView(R.id.fragment_result_text) AppCompatTextView mResultText;

    public static ResultFragment newInstance(boolean isCorrect, MainActivityListener listener) {
        ResultFragment fragment = new ResultFragment();
        fragment.mIsCorrect = isCorrect;
        fragment.mListener = listener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void initView() {
        mResultText.setText(mIsCorrect ? getString(R.string.result_correct) : getString(R.string.result_wrong));
    }

    @OnClick(R.id.fragment_result_try_again_button)
    public void onTryAgainClicked() {
        mListener.onTryAgainSelected(mIsCorrect);
    }
}

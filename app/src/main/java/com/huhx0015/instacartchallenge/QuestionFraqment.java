package com.huhx0015.instacartchallenge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class QuestionFraqment extends Fragment {

    private Unbinder mUnbinder;

    @BindView(R.id.fragment_question_image_1) AppCompatImageView mQuestionImage1;
    @BindView(R.id.fragment_question_image_2) AppCompatImageView mQuestionImage2;
    @BindView(R.id.fragment_question_image_3) AppCompatImageView mQuestionImage3;
    @BindView(R.id.fragment_question_image_4) AppCompatImageView mQuestionImage4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void initView() {


    }

    private void initImages() {


    }

}

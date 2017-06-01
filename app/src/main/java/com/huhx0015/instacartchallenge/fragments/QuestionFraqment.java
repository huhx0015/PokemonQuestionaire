package com.huhx0015.instacartchallenge.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huhx0015.instacartchallenge.R;
import com.huhx0015.instacartchallenge.models.Question;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class QuestionFraqment extends Fragment {

    private static final String LOG_CAT = QuestionFraqment.class.getSimpleName();
    private static final String INSTANCE_QUESTION = LOG_CAT + "_INSTANCE_QUESTION";

    private Question mQuestion;
    private Unbinder mUnbinder;

    @BindView(R.id.fragment_question_image_1) AppCompatImageView mQuestionImage1;
    @BindView(R.id.fragment_question_image_2) AppCompatImageView mQuestionImage2;
    @BindView(R.id.fragment_question_image_3) AppCompatImageView mQuestionImage3;
    @BindView(R.id.fragment_question_image_4) AppCompatImageView mQuestionImage4;
    @BindView(R.id.fragment_question_text) AppCompatTextView mQuestionText;
    @BindView(R.id.fragment_question_instruction_text) AppCompatTextView mInstructionText;

    public static QuestionFraqment newInstance(Question question) {
        QuestionFraqment fraqment = new QuestionFraqment();
        fraqment.mQuestion = question;
        return fraqment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (savedInstanceState != null) {
            mQuestion = savedInstanceState.getParcelable(INSTANCE_QUESTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.clear(mQuestionImage1);
        Glide.clear(mQuestionImage2);
        Glide.clear(mQuestionImage3);
        Glide.clear(mQuestionImage4);
        mUnbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(INSTANCE_QUESTION, mQuestion);
    }

    private void initView() {
        initText();
        initImages();
    }

    private void initText() {
        mQuestionText.setText(mQuestion.getItem());
        mInstructionText.setText(String.format(getString(R.string.questions_instructions), mQuestion.getItem()));
    }

    private void initImages() {
        int imageCount = 0;
        for (String imageUrl : mQuestion.getUrlList()) {
            switch (imageCount) {
                case 0:
                    loadImage(imageUrl, mQuestionImage1);
                    break;
                case 1:
                    loadImage(imageUrl, mQuestionImage2);
                    break;
                case 2:
                    loadImage(imageUrl, mQuestionImage3);
                    break;
                case 3:
                    loadImage(imageUrl, mQuestionImage4);
                    break;
            }
            imageCount++;
        }
    }

    private void loadImage(String imageUrl, AppCompatImageView imageView) {
        Glide.with(getContext())
                .load(imageUrl)
                .into(imageView);
    }
}
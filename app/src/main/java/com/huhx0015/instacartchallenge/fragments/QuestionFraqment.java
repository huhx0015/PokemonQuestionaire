package com.huhx0015.instacartchallenge.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.huhx0015.instacartchallenge.R;
import com.huhx0015.instacartchallenge.interfaces.MainActivityListener;
import com.huhx0015.instacartchallenge.models.Question;
import com.huhx0015.instacartchallenge.utils.QuestionUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class QuestionFraqment extends Fragment {

    private static final String LOG_TAG = QuestionFraqment.class.getSimpleName();
    private static final String INSTANCE_QUESTION = LOG_TAG + "_INSTANCE_QUESTION";
    private static final String INSTANCE_CORRECT_POSITION = LOG_TAG + "_INSTANCE_CORRECT_POSITION";

    private static final int POSITION_1 = 1;
    private static final int POSITION_2 = 2;
    private static final int POSITION_3 = 3;
    private static final int POSITION_4 = 4;

    private int mCorrectPosition = -1;
    private MainActivityListener mListener;
    private Question mQuestion;
    private Unbinder mUnbinder;

    @BindView(R.id.fragment_question_image_1) AppCompatImageView mQuestionImage1;
    @BindView(R.id.fragment_question_image_2) AppCompatImageView mQuestionImage2;
    @BindView(R.id.fragment_question_image_3) AppCompatImageView mQuestionImage3;
    @BindView(R.id.fragment_question_image_4) AppCompatImageView mQuestionImage4;
    @BindView(R.id.fragment_question_text) AppCompatTextView mQuestionText;
    @BindView(R.id.fragment_question_instruction_text) AppCompatTextView mInstructionText;

    public static QuestionFraqment newInstance(Question question, MainActivityListener listener) {
        QuestionFraqment fraqment = new QuestionFraqment();
        fraqment.mQuestion = question;
        fraqment.mListener = listener;
        return fraqment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (savedInstanceState != null) {
            mQuestion = savedInstanceState.getParcelable(INSTANCE_QUESTION);
            mCorrectPosition = savedInstanceState.getInt(INSTANCE_CORRECT_POSITION);
        }

        if (mCorrectPosition == -1) {
            setCorrectAnswerPosition();
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
        outState.putInt(INSTANCE_CORRECT_POSITION, mCorrectPosition);
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

        // Loads the correct image.
        selectImage(mQuestion.getUrlList().get(mCorrectPosition), mCorrectPosition);

        // Loads the rest of the images.
        int imageCount = 0;
        for (String imageUrl : mQuestion.getUrlList()) {
            if (imageCount != mCorrectPosition) {
                selectImage(imageUrl, imageCount);
            }
            imageCount++;
        }
    }

    private void selectImage(String imageUrl, int position) {
        switch (position) {
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
    }

    private void loadImage(String imageUrl, AppCompatImageView imageView) {
        Glide.with(getContext())
                .load(imageUrl)
                .into(imageView);
    }

    private void setCorrectAnswerPosition() {
        this.mCorrectPosition = QuestionUtils.getRandomPosition();
        Log.d(LOG_TAG, "setCorrectAnswerPosition(): Correct image position set at: " + mCorrectPosition);
    }

    private void checkAnswer(int position) {
        if (position == mCorrectPosition) {
            mListener.onAnswerSelected(true);
            // TODO: Go to next fragment.
        } else {
            mListener.onAnswerSelected(false);
            // TODO: Show incorrect.
        }
    }

    @OnClick(R.id.fragment_question_image_1)
    public void onQuestionImage1Clicked() {
        checkAnswer(POSITION_1);
    }

    @OnClick(R.id.fragment_question_image_2)
    public void onQuestionImage2Clicked() {
        checkAnswer(POSITION_2);
    }

    @OnClick(R.id.fragment_question_image_3)
    public void onQuestionImage3Clicked() {
        checkAnswer(POSITION_3);
    }

    @OnClick(R.id.fragment_question_image_4)
    public void onQuestionImage4Clicked() {
        checkAnswer(POSITION_4);
    }
}
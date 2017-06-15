package com.huhx0015.pokemonquestionaire.view.fragments;

import android.arch.lifecycle.LifecycleFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.huhx0015.pokemonquestionaire.R;
import com.huhx0015.pokemonquestionaire.constants.PokemonConstants;
import com.huhx0015.pokemonquestionaire.view.interfaces.MainActivityListener;
import com.huhx0015.pokemonquestionaire.models.Pokemon;
import com.huhx0015.pokemonquestionaire.utils.QuestionUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class PokemonFraqment extends LifecycleFragment {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // CONSTANT VARIABLES:
    private static final int CORRECT_ANSWER_IMAGE_POSITION = 0;
    private static final int POSITION_1 = 0;
    private static final int POSITION_2 = 1;
    private static final int POSITION_3 = 2;
    private static final int POSITION_4 = 3;

    // DATA VARIABLES:
    private boolean mIsTimeUp = false;
    private int mCorrectPosition;
    private int mSelectedPosition;
    private Pokemon mPokemon;

    // FRAGMENT VARIABLES:
    private MainActivityListener mListener;
    private Unbinder mUnbinder;

    // LOGGING VARIABLES:
    private static final String LOG_TAG = PokemonFraqment.class.getSimpleName();

    // INSTANCE VARIABLES:
    private static final String INSTANCE_POKEMON = LOG_TAG + "_INSTANCE_POKEMON";
    private static final String INSTANCE_CORRECT_POSITION = LOG_TAG + "_INSTANCE_CORRECT_POSITION";
    private static final String INSTANCE_SELECTED_POSITION = LOG_TAG + "_INSTANCE_SELECTED_POSITION";
    private static final String INSTANCE_TIME_UP = LOG_TAG + "_INSTANCE_TIME_UP";

    // VIEW INJECTION VARIABLES:
    @BindView(R.id.fragment_question_image_1) AppCompatImageView mQuestionImage1;
    @BindView(R.id.fragment_question_image_2) AppCompatImageView mQuestionImage2;
    @BindView(R.id.fragment_question_image_3) AppCompatImageView mQuestionImage3;
    @BindView(R.id.fragment_question_image_4) AppCompatImageView mQuestionImage4;
    @BindView(R.id.fragment_question_text) AppCompatTextView mQuestionText;
    @BindView(R.id.fragment_question_instruction_text) AppCompatTextView mInstructionText;
    @BindView(R.id.fragment_question_time_remaining) AppCompatTextView mTimeRemainingText;
    @BindView(R.id.fragment_question_submit_button) AppCompatButton mSubmitButton;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public static PokemonFraqment newInstance(Pokemon pokemon, int correctPosition,
                                              MainActivityListener listener) {
        PokemonFraqment fraqment = new PokemonFraqment();
        fraqment.mPokemon = pokemon;
        fraqment.mCorrectPosition = correctPosition;
        fraqment.mListener = listener;
        return fraqment;
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (savedInstanceState != null) {
            mPokemon = savedInstanceState.getParcelable(INSTANCE_POKEMON);
            mCorrectPosition = savedInstanceState.getInt(INSTANCE_CORRECT_POSITION, PokemonConstants.STATE_CORRECT_POSITION_UNSET);
            mSelectedPosition = savedInstanceState.getInt(INSTANCE_SELECTED_POSITION, PokemonConstants.STATE_CORRECT_POSITION_UNSET);
            mIsTimeUp = savedInstanceState.getBoolean(INSTANCE_TIME_UP, false);
        }

        if (mCorrectPosition == PokemonConstants.STATE_CORRECT_POSITION_UNSET) {
            setCorrectAnswerPosition();
        }

        Log.d(LOG_TAG, "onCreate(): Correct image position at: " + mCorrectPosition);
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
    public void onResume() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mTimerReceiver,
                new IntentFilter(PokemonConstants.BROADCAST_TIMER));
        super.onResume();
        checkTimeUp();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTimerReceiver);
        super.onPause();
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

    /** FRAGMENT EXTENSION METHODS _____________________________________________________________ **/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(INSTANCE_POKEMON, mPokemon);
        outState.putInt(INSTANCE_CORRECT_POSITION, mCorrectPosition);
        outState.putInt(INSTANCE_SELECTED_POSITION, mSelectedPosition);
        outState.putBoolean(INSTANCE_TIME_UP, mIsTimeUp);
    }

    /** VIEW METHODS ___________________________________________________________________________ **/

    private void initView() {
        initText();
        initImages();
    }

    private void initText() {
        mQuestionText.setText(mPokemon.getItem());
        mInstructionText.setText(String.format(getString(R.string.questions_instructions), mPokemon.getItem()));

        if (mIsTimeUp) {
            mTimeRemainingText.setText(getString(R.string.questions_time_run_out));
        }
    }

    private void initImages() {

        // Sets the correct answer image.
        setImage(mPokemon.getUrlList().get(CORRECT_ANSWER_IMAGE_POSITION), mCorrectPosition);

        // Sets the rest of the images.
        int imageCount = 1;
        int position = 0;
        int numberOfImages = mPokemon.getUrlList().size();
        while (imageCount < numberOfImages) {
            String url = mPokemon.getUrlList().get(imageCount);
            if (position != mCorrectPosition) {
                setImage(url, position);
                imageCount++;
            }
            position++;
        }
    }

    private void setImage(String imageUrl, int position) {
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
        Log.d(LOG_TAG, "setImage(): Image set at position: " + position);
    }

    private void loadImage(String imageUrl, AppCompatImageView imageView) {
        Glide.with(getContext())
                .load(imageUrl)
                .into(imageView);
    }

    private void setCorrectAnswerPosition() {
        this.mCorrectPosition = QuestionUtils.getRandomPosition();
    }

    private void setSubmitButtonVisible() {
        if (mSubmitButton.getVisibility() == View.GONE) {
            mSubmitButton.setVisibility(View.VISIBLE);
        }
    }

    private void checkAnswer(int position) {
        if (position == mCorrectPosition) {
            mListener.onAnswerSelected(true);
        } else {
            mListener.onAnswerSelected(false);
        }
    }

    private void checkTimeUp() {
        if (mIsTimeUp) {
            mTimeRemainingText.setText(getString(R.string.questions_time_run_out));

            if (mSubmitButton.getVisibility() == View.GONE) {
                mSubmitButton.setVisibility(View.VISIBLE);
            }
            mSubmitButton.setText(getString(R.string.result_try_again));
        }
    }

    @OnClick(R.id.fragment_question_image_1)
    public void onQuestionImage1Clicked() {
        mSelectedPosition = POSITION_1;
        setSubmitButtonVisible();
    }

    @OnClick(R.id.fragment_question_image_2)
    public void onQuestionImage2Clicked() {
        mSelectedPosition = POSITION_2;
        setSubmitButtonVisible();
    }

    @OnClick(R.id.fragment_question_image_3)
    public void onQuestionImage3Clicked() {
        mSelectedPosition = POSITION_3;
        setSubmitButtonVisible();
    }

    @OnClick(R.id.fragment_question_image_4)
    public void onQuestionImage4Clicked() {
        mSelectedPosition = POSITION_4;
        setSubmitButtonVisible();
    }

    @OnClick(R.id.fragment_question_submit_button)
    public void onSubmitClicked() {
        if (!mIsTimeUp) {
            checkAnswer(mSelectedPosition);
        } else {
            mListener.onTryAgainSelected(true);
        }
    }

    /** BROADCAST METHODS ______________________________________________________________________ **/

    private BroadcastReceiver mTimerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "mTimerReceiver: Received update from TimerService.");

            long timeRemaining = intent.getLongExtra(PokemonConstants.EVENT_TIMER_REMAINING, 0);
            if (timeRemaining != 0) {
                mTimeRemainingText.setText(String.format(getString(R.string.questions_seconds), timeRemaining));
            }

            mIsTimeUp = intent.getBooleanExtra(PokemonConstants.EVENT_TIMER_FINISHED, false);
            checkTimeUp();
        }
    };
}
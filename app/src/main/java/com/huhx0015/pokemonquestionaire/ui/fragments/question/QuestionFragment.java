package com.huhx0015.pokemonquestionaire.ui.fragments.question;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.huhx0015.pokemonquestionaire.R;
import com.huhx0015.pokemonquestionaire.constants.PokemonConstants;
import com.huhx0015.pokemonquestionaire.data.PokemonPreferences;
import com.huhx0015.pokemonquestionaire.databinding.FragmentQuestionBinding;
import com.huhx0015.pokemonquestionaire.ui.fragments.base.BaseFragment;
import com.huhx0015.pokemonquestionaire.ui.interfaces.MainActivityListener;
import com.huhx0015.pokemonquestionaire.models.entities.Pokemon;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class QuestionFragment extends BaseFragment implements QuestionViewModel.QuestionViewModelListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // CONSTANT VARIABLES:
    private static final int CORRECT_ANSWER_IMAGE_POSITION = 0;

    // DATA VARIABLES:
    private boolean mIsTimeUp = false;
    private int mCorrectPosition;
    private int mSelectedPosition = PokemonConstants.STATE_CORRECT_POSITION_UNSET;
    private Pokemon mPokemon;

    // DATABINDING VARIABLES:
    private FragmentQuestionBinding mBinding;
    private QuestionViewModel mViewModel;

    // LOGGING VARIABLES:
    private static final String LOG_TAG = QuestionFragment.class.getSimpleName();

    // INSTANCE VARIABLES:
    private static final String INSTANCE_POKEMON = LOG_TAG + "_INSTANCE_POKEMON";
    private static final String INSTANCE_CORRECT_POSITION = LOG_TAG + "_INSTANCE_CORRECT_POSITION";
    private static final String INSTANCE_SELECTED_POSITION = LOG_TAG + "_INSTANCE_SELECTED_POSITION";
    private static final String INSTANCE_TIME_UP = LOG_TAG + "_INSTANCE_TIME_UP";

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public static QuestionFragment newInstance(Pokemon pokemon, int correctPosition,
                                               MainActivityListener listener) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(INSTANCE_POKEMON, pokemon);
        arguments.putInt(INSTANCE_CORRECT_POSITION, correctPosition);
        fragment.setArguments(arguments);
        fragment.mListener = listener;
        return fragment;
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mPokemon = savedInstanceState.getParcelable(INSTANCE_POKEMON);
            mCorrectPosition = savedInstanceState.getInt(INSTANCE_CORRECT_POSITION, PokemonConstants.STATE_CORRECT_POSITION_UNSET);
            mSelectedPosition = savedInstanceState.getInt(INSTANCE_SELECTED_POSITION, PokemonConstants.STATE_CORRECT_POSITION_UNSET);
            mIsTimeUp = savedInstanceState.getBoolean(INSTANCE_TIME_UP);
        } else if (getArguments() != null) {
            mPokemon = getArguments().getParcelable(INSTANCE_POKEMON);
            mCorrectPosition = getArguments().getInt(INSTANCE_CORRECT_POSITION);
        }

        Log.d(LOG_TAG, "onCreate(): Correct image position at: " + mCorrectPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mTimerReceiver,
                new IntentFilter(PokemonConstants.BROADCAST_TIMER));
        super.onResume();
        checkQuizState();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTimerReceiver);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.clear(mBinding.fragmentQuestionImage1);
        Glide.clear(mBinding.fragmentQuestionImage2);
        Glide.clear(mBinding.fragmentQuestionImage3);
        Glide.clear(mBinding.fragmentQuestionImage4);
        mBinding.unbind();
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
        initBinding();
        initText();
        initImages();
    }

    private void initBinding() {
        mBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.fragment_question, null, false);
        mViewModel = new QuestionViewModel(getContext());
        mViewModel.setListener(this);
        mBinding.setViewModel(mViewModel);
    }

    private void initText() {
        mViewModel.setQuestionText(mPokemon.getItem());
        mViewModel.setInstructionsText(String.format(getString(R.string.questions_instructions), mPokemon.getItem()));

        if (mIsTimeUp) {
            mViewModel.setTimeRemainingText(getString(R.string.questions_time_run_out));
        }
    }

    private void initImages() {

        // Sets the correct answer image.
        mViewModel.setQuestionImage(mCorrectPosition, mPokemon.getUrlList().get(CORRECT_ANSWER_IMAGE_POSITION));

        // Sets the rest of the images.
        int imageCount = 1;
        int position = 0;
        int numberOfImages = mPokemon.getUrlList().size();
        while (imageCount < numberOfImages) {
            String url = mPokemon.getUrlList().get(imageCount);

            if (position != mCorrectPosition) {
                mViewModel.setQuestionImage(position, url);
                imageCount++;
            }
            position++;
        }
    }

    /** QUESTION METHODS _______________________________________________________________________ **/

    private void checkAnswer(int position) {
        if (position == mCorrectPosition) {
            mListener.onAnswerSelected(true);
        } else {
            mListener.onAnswerSelected(false);
        }
    }

    private void checkQuizState() {

        // Used for determining if the timer ran out while the app was inactive.
        boolean isTimeUp = PokemonPreferences.getTimeUp(getContext());
        if (isTimeUp) {
            Log.d(LOG_TAG, "checkQuizState(): Time up was set in preferences.");
            mIsTimeUp = true;
            PokemonPreferences.clearPreferences(getContext());
        }

        Log.d(LOG_TAG, "checkQuizState(): mIsTimeUp: " + mIsTimeUp);

        if (mIsTimeUp) {
            mViewModel.setTimeRemainingText(getString(R.string.questions_time_run_out));
            mViewModel.setSubmitButtonVisible(true);
            mViewModel.setSubmitButtonText(getString(R.string.result_try_again));
        } else if (mSelectedPosition != PokemonConstants.STATE_CORRECT_POSITION_UNSET){
            mViewModel.setSubmitButtonVisible(true);
            mViewModel.setSubmitButtonText(getString(R.string.question_submit));
        }
    }

    /** BROADCAST METHODS ______________________________________________________________________ **/

    private BroadcastReceiver mTimerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "mTimerReceiver: Received update from TimerService.");

            long timeRemaining = intent.getLongExtra(PokemonConstants.EVENT_TIMER_REMAINING, 0);
            if (isVisible()) {
                if (timeRemaining != 0) {
                    mViewModel.setTimeRemainingText(String.format(getString(R.string.questions_seconds), timeRemaining));
                }
            }

            mIsTimeUp = intent.getBooleanExtra(PokemonConstants.EVENT_TIMER_FINISHED, false);
            Log.d(LOG_TAG, "mTimerReceiver: mIsTimeUp: " + mIsTimeUp);

            checkQuizState();
        }
    };

    /** LISTENER METHODS _______________________________________________________________________ **/

    @Override
    public void onSubmitButtonClicked() {
        if (!mIsTimeUp) {
            checkAnswer(mSelectedPosition);
        } else {
            mListener.onTryAgainSelected(true);
        }
    }

    @Override
    public void onQuestionImageClicked(int position) {
        mSelectedPosition = position;
        mViewModel.setSubmitButtonVisible(true);
    }
}
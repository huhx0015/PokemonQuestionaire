package com.huhx0015.pokemonquestionaire.ui.fragments.question;

import android.arch.lifecycle.ViewModelProviders;
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

    // DATABINDING VARIABLES:
    private FragmentQuestionBinding mBinding;
    private QuestionViewModel mViewModel;

    // LOGGING VARIABLES:
    private static final String LOG_TAG = QuestionFragment.class.getSimpleName();

    // INSTANCE VARIABLES:
    private static final String INSTANCE_POKEMON = LOG_TAG + "_INSTANCE_POKEMON";
    private static final String INSTANCE_CORRECT_POSITION = LOG_TAG + "_INSTANCE_CORRECT_POSITION";

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initBinding();

        if (getArguments() != null) {
            Pokemon pokemon = getArguments().getParcelable(INSTANCE_POKEMON);
            mViewModel.setPokemon(pokemon);
            mViewModel.setCorrectPosition(getArguments().getInt(INSTANCE_CORRECT_POSITION));

            Log.d(LOG_TAG, "onCreate(): Correct image position at: " + mViewModel.getCorrectPosition());
        }

        initText();
        initImages();

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

    /** VIEW METHODS ___________________________________________________________________________ **/

    private void initBinding() {
        mBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.fragment_question, null, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(QuestionViewModel.class);
        mViewModel.setSubmitButtonVisible(false);
        mViewModel.setSubmitButtonText(getString(R.string.question_submit));
        mViewModel.setListener(this);
        mBinding.setViewModel(mViewModel);
    }

    private void initText() {
        mViewModel.setQuestionText(mViewModel.getPokemon().getItem());
        mViewModel.setInstructionsText(String.format(getString(R.string.questions_instructions), mViewModel.getPokemon().getItem()));

        if (mViewModel.isTimeUp()) {
            mViewModel.setTimeRemainingText(getString(R.string.questions_time_run_out));
        }
    }

    private void initImages() {

        // Sets the correct answer image.
        mViewModel.setQuestionImage(mViewModel.getCorrectPosition(),
                mViewModel.getPokemon().getUrlList().get(CORRECT_ANSWER_IMAGE_POSITION));

        // Sets the rest of the images.
        int imageCount = 1;
        int position = 0;
        int numberOfImages = mViewModel.getPokemon().getUrlList().size();
        while (imageCount < numberOfImages) {
            String url = mViewModel.getPokemon().getUrlList().get(imageCount);

            if (position != mViewModel.getCorrectPosition()) {
                mViewModel.setQuestionImage(position, url);
                imageCount++;
            }
            position++;
        }
    }

    /** QUESTION METHODS _______________________________________________________________________ **/

    private void checkAnswer(int position) {
        if (position == mViewModel.getCorrectPosition()) {
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
            mViewModel.setIsTimeUp(true);
            PokemonPreferences.clearPreferences(getContext());
        }

        Log.d(LOG_TAG, "checkQuizState(): mIsTimeUp: " + mViewModel.isTimeUp());

        if (mViewModel.isTimeUp()) {
            mViewModel.setTimeRemainingText(getString(R.string.questions_time_run_out));
            mViewModel.setSubmitButtonVisible(true);
            mViewModel.setSubmitButtonText(getString(R.string.result_try_again));
        } else if (mViewModel.getSelectedPosition() != PokemonConstants.STATE_CORRECT_POSITION_UNSET){
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

            mViewModel.setIsTimeUp(intent.getBooleanExtra(PokemonConstants.EVENT_TIMER_FINISHED, false));
            Log.d(LOG_TAG, "mTimerReceiver: mIsTimeUp: " + mViewModel.isTimeUp());

            checkQuizState();
        }
    };

    /** LISTENER METHODS _______________________________________________________________________ **/

    @Override
    public void onSubmitButtonClicked() {
        if (!mViewModel.isTimeUp()) {
            checkAnswer(mViewModel.getSelectedPosition());
        } else {
            mListener.onTryAgainSelected(true);
        }
    }

    @Override
    public void onQuestionImageClicked(int position) {
        mViewModel.setSelectedPosition(position);
        mViewModel.setSubmitButtonVisible(true);
    }
}
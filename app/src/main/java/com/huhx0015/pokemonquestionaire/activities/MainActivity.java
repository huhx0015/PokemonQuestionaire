package com.huhx0015.pokemonquestionaire.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huhx0015.pokemonquestionaire.models.Pokemon;
import com.huhx0015.pokemonquestionaire.services.TimerService;
import com.huhx0015.pokemonquestionaire.interfaces.MainActivityListener;
import com.huhx0015.pokemonquestionaire.constants.PokemonConstants;
import com.huhx0015.pokemonquestionaire.fragments.PokemonFraqment;
import com.huhx0015.pokemonquestionaire.R;
import com.huhx0015.pokemonquestionaire.fragments.ResultFragment;
import com.huhx0015.pokemonquestionaire.models.PokemonResponse;
import com.huhx0015.pokemonquestionaire.utils.JsonUtils;
import com.huhx0015.pokemonquestionaire.utils.QuestionUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String INSTANCE_FRAGMENT_TAG = LOG_TAG + "_FRAGMENT_TAG";
    private static final String INSTANCE_POKEMONS = LOG_TAG + "_POKEMONS";
    private static final String INSTANCE_SELECTED_POKEMON = LOG_TAG + "_SELECTED_POKEMON";
    private static final String INSTANCE_CORRECT_POSITION = LOG_TAG + "_CORRECT_POSITION";

    private int mCorrectPosition = PokemonConstants.STATE_CORRECT_POSITION_UNSET;
    private String mFragmentTag;
    private Pokemon mSelectedPokemon;
    private PokemonResponse mPokemonResponse;

    @BindView(R.id.main_fragment_container) RelativeLayout mFragmentContainer;
    @BindView(R.id.main_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.main_toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();

        if (savedInstanceState != null) {
            mPokemonResponse = savedInstanceState.getParcelable(INSTANCE_POKEMONS);
            mSelectedPokemon = savedInstanceState.getParcelable(INSTANCE_SELECTED_POKEMON);
            mFragmentTag = savedInstanceState.getString(INSTANCE_FRAGMENT_TAG);
            mCorrectPosition = savedInstanceState.getInt(INSTANCE_CORRECT_POSITION);

            if (mSelectedPokemon != null) {
                loadFragment(PokemonFraqment.newInstance(mSelectedPokemon, mCorrectPosition, this),
                        PokemonFraqment.class.getSimpleName());
                return;
            }
        }

        loadJson();
    }

    @Override
    public void onBackPressed() {
        startTimer(false);
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mPokemonResponse != null) {
            outState.putParcelable(INSTANCE_POKEMONS, mPokemonResponse);
        }
        if (mSelectedPokemon != null) {
            outState.putParcelable(INSTANCE_SELECTED_POKEMON, mSelectedPokemon);
        }
        outState.putString(INSTANCE_FRAGMENT_TAG, mFragmentTag);
        outState.putInt(INSTANCE_CORRECT_POSITION, mCorrectPosition);
    }

    private void initView() {
        setSupportActionBar(mToolbar);
    }

    private void loadJson() {
        JsonAsyncTask task = new JsonAsyncTask();
        task.execute(PokemonConstants.POKEMON_ASSET_NAME);
    }

    private void loadFragment(Fragment fragment, String tag) {
        this.mFragmentTag = tag;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(mFragmentContainer.getId(), fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void startTimer(boolean isStart) {
        if (isStart) {
            startService(new Intent(this, TimerService.class));
        } else {
            stopService(new Intent(this, TimerService.class));
        }
    }

    @Override
    public void onAnswerSelected(boolean isCorrect) {
        loadFragment(ResultFragment.newInstance(isCorrect, this), ResultFragment.class.getSimpleName());
        startTimer(false);
    }

    @Override
    public void onTryAgainSelected(boolean isNewQuestion) {
        if (isNewQuestion) {
            mSelectedPokemon = QuestionUtils.getRandomQuestion(mPokemonResponse.getQuestionList());
            mCorrectPosition = QuestionUtils.getRandomPosition();
        }

        loadFragment(PokemonFraqment.newInstance(mSelectedPokemon, mCorrectPosition, this),
                PokemonFraqment.class.getSimpleName());
        startTimer(true);
    }

    private class JsonAsyncTask extends AsyncTask<String, Void, Pokemon> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Pokemon doInBackground(String... params) {
            String responseJson = JsonUtils.loadJsonFromAsset(PokemonConstants.POKEMON_ASSET_NAME, MainActivity.this);
            PokemonResponse questionResponse = JsonUtils.getGroceryQuestionsFromJson(responseJson);

            if (questionResponse != null && questionResponse.getQuestionList() != null &&
                    questionResponse.getQuestionList().size() > 0) {
                mPokemonResponse = questionResponse;
                return QuestionUtils.getRandomQuestion(questionResponse.getQuestionList());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Pokemon pokemon) {
            super.onPostExecute(pokemon);
            mProgressBar.setVisibility(View.GONE);

            if (pokemon == null) {
                Toast.makeText(MainActivity.this, "An error occurred while attempting to load a pokemon.",
                        Toast.LENGTH_LONG).show();
            } else {
                mSelectedPokemon = pokemon;
                mCorrectPosition = QuestionUtils.getRandomPosition();
                loadFragment(PokemonFraqment.newInstance(mSelectedPokemon, mCorrectPosition,
                        MainActivity.this), PokemonFraqment.class.getSimpleName());
                startTimer(true);
            }
        }
    }
}
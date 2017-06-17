package com.huhx0015.pokemonquestionaire.view.activities;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.huhx0015.pokemonquestionaire.databinding.ActivityMainBinding;
import com.huhx0015.pokemonquestionaire.databinding.ContentMainBinding;
import com.huhx0015.pokemonquestionaire.models.entities.Pokemon;
import com.huhx0015.pokemonquestionaire.services.TimerService;
import com.huhx0015.pokemonquestionaire.utils.SnackbarUtils;
import com.huhx0015.pokemonquestionaire.view.interfaces.MainActivityListener;
import com.huhx0015.pokemonquestionaire.constants.PokemonConstants;
import com.huhx0015.pokemonquestionaire.view.fragments.QuestionFraqment;
import com.huhx0015.pokemonquestionaire.R;
import com.huhx0015.pokemonquestionaire.view.fragments.ResultFragment;
import com.huhx0015.pokemonquestionaire.utils.QuestionUtils;
import com.huhx0015.pokemonquestionaire.viewmodels.activities.MainViewModel;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LifecycleRegistryOwner, MainActivityListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // DATA VARIABLES:
    private int mCorrectPosition = PokemonConstants.STATE_CORRECT_POSITION_UNSET;
    private Pokemon mSelectedPokemon;

    // DATABINDING VARIABLES:
    private ActivityMainBinding mActivityMainBinding;
    private ContentMainBinding mContentMainBinding;
    private MainViewModel mViewModel;

    // FRAGMENT VARIABLES:
    private String mFragmentTag;

    // LOGGING VARIABLES:
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // INSTANCE VARIABLES:
    private static final String INSTANCE_FRAGMENT_TAG = LOG_TAG + "_FRAGMENT_TAG";
    private static final String INSTANCE_SELECTED_POKEMON = LOG_TAG + "_SELECTED_POKEMON";
    private static final String INSTANCE_CORRECT_POSITION = LOG_TAG + "_CORRECT_POSITION";

    /** ACTIVITY LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

//        if (savedInstanceState != null) {
//            mPokemonResponse = savedInstanceState.getParcelable(INSTANCE_POKEMONS);
//            mSelectedPokemon = savedInstanceState.getParcelable(INSTANCE_SELECTED_POKEMON);
//            mFragmentTag = savedInstanceState.getString(INSTANCE_FRAGMENT_TAG);
//            mCorrectPosition = savedInstanceState.getInt(INSTANCE_CORRECT_POSITION);
//
//            if (mSelectedPokemon != null) {
//                loadFragment(QuestionFraqment.newInstance(mSelectedPokemon, mCorrectPosition, this),
//                        QuestionFraqment.class.getSimpleName());
//                return;
//            }
//        }

        subscribe(); // Subscribes an observer on the mPokemonListData object in mViewModel.

        mViewModel.loadData(this); // Loads the Pokemon question list data from a local JSON file.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContentMainBinding.unbind();
        mActivityMainBinding.unbind();
    }

    /** ACTIVITY EXTENSION METHODS _____________________________________________________________ **/

    @Override
    public void onBackPressed() {
        startTimer(false);
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mSelectedPokemon != null) {
            outState.putParcelable(INSTANCE_SELECTED_POKEMON, mSelectedPokemon);
        }
        outState.putString(INSTANCE_FRAGMENT_TAG, mFragmentTag);
        outState.putInt(INSTANCE_CORRECT_POSITION, mCorrectPosition);
    }

    /** LIFECYCLE OWNER METHODS ________________________________________________________________ **/

    @Override
    public LifecycleRegistry getLifecycle() {
        return null;
    }

    /** LISTENER METHODS _______________________________________________________________________ **/

    @Override
    public void onAnswerSelected(boolean isCorrect) {
        loadFragment(ResultFragment.newInstance(isCorrect, this), ResultFragment.class.getSimpleName());
        startTimer(false);
    }

    @Override
    public void onTryAgainSelected(boolean isNewQuestion) {
        if (isNewQuestion) {
            mSelectedPokemon = QuestionUtils.getRandomQuestion(mViewModel.getPokemonListData().getValue());
            mCorrectPosition = QuestionUtils.getRandomPosition();
        }

        loadFragment(QuestionFraqment.newInstance(mSelectedPokemon, mCorrectPosition, this),
                QuestionFraqment.class.getSimpleName());
        startTimer(true);
    }

    /** INIT METHODS ___________________________________________________________________________ **/

    private void initView() {
        initBinding();
        initToolbar();
    }

    private void initBinding() {
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mContentMainBinding = mActivityMainBinding.contentMain;

        mViewModel = new MainViewModel();
        mActivityMainBinding.setViewModel(mViewModel);
        mContentMainBinding.setViewModel(mViewModel);
    }

    private void initToolbar() {
        setSupportActionBar(mActivityMainBinding.mainToolbar);
    }

    /** FRAGMENT METHODS _______________________________________________________________________ **/

    private void loadFragment(Fragment fragment, String tag) {
        this.mFragmentTag = tag;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(mContentMainBinding.mainFragmentContainer.getId(), fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /** SERVICE METHODS ________________________________________________________________________ **/

    private void startTimer(boolean isStart) {
        if (isStart) {
            startService(new Intent(this, TimerService.class));
        } else {
            stopService(new Intent(this, TimerService.class));
        }
    }

    /** SUBSCRIBER METHODS _____________________________________________________________________ **/

    // TODO: New LiveData subscriber method.
    private void subscribe() {

        // POKEMON LIST OBSERVER:
        final Observer<List<Pokemon>> pokemonListObserver = new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(@Nullable List<Pokemon> pokemon) {
                Log.d(LOG_TAG, "subscribe(): mPokemonList data has changed.");

                if (pokemon != null) {

                    // TODO: Select a random pokemon question
                    mSelectedPokemon = QuestionUtils.getRandomQuestion(pokemon);
                    mCorrectPosition = QuestionUtils.getRandomPosition();
                    loadFragment(QuestionFraqment.newInstance(mSelectedPokemon, mCorrectPosition,
                            MainActivity.this), QuestionFraqment.class.getSimpleName());
                    startTimer(true);

                } else {
                    SnackbarUtils.displaySnackbar(mActivityMainBinding.getRoot(),
                            "An error occurred while attempting to load a pokemon.",
                            Snackbar.LENGTH_LONG, ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                }
            }
        };

        // Observes changes on mPokemonListData in mViewModel.
        mViewModel.getPokemonListData().observe(this, pokemonListObserver);
    }
}
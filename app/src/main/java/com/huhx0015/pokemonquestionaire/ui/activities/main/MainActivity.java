package com.huhx0015.pokemonquestionaire.ui.activities.main;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.huhx0015.pokemonquestionaire.ui.fragments.base.BaseFragment;
import com.huhx0015.pokemonquestionaire.ui.interfaces.MainActivityListener;
import com.huhx0015.pokemonquestionaire.ui.fragments.question.QuestionFragment;
import com.huhx0015.pokemonquestionaire.R;
import com.huhx0015.pokemonquestionaire.ui.fragments.result.ResultFragment;
import com.huhx0015.pokemonquestionaire.utils.QuestionUtils;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // DATABINDING VARIABLES:
    private ActivityMainBinding mActivityMainBinding;
    private ContentMainBinding mContentMainBinding;
    private MainViewModel mViewModel;

    // LIFECYCLE VARIABLES:
    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    // LOGGING VARIABLES:
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /** ACTIVITY LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        if (savedInstanceState != null && mViewModel.getSelectedPokemon() != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            BaseFragment fragment = (BaseFragment) fragmentManager.findFragmentByTag(mViewModel.getFragmentTag());

            if (fragment != null) {
                Log.d(LOG_TAG, "onCreate(): Loading previous fragment: " + mViewModel.getFragmentTag());

                fragment.setListener(this);
                loadFragment(fragment, mViewModel.getFragmentTag());
            }
            return;
        }

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

    /** LIFECYCLE OWNER METHODS ________________________________________________________________ **/

    @NonNull
    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

    /** LISTENER METHODS _______________________________________________________________________ **/

    @Override
    public void onAnswerSelected(boolean isCorrect) {
        mViewModel.setIsCorrect(isCorrect);
        loadFragment(ResultFragment.newInstance(isCorrect, this), ResultFragment.class.getSimpleName());
        startTimer(false);
    }

    @Override
    public void onTryAgainSelected(boolean isNewQuestion) {
        if (isNewQuestion) {
            mViewModel.setSelectedPokemon(QuestionUtils.getRandomQuestion(mViewModel.getPokemonListData().getValue()));
            mViewModel.setCorrectPosition(QuestionUtils.getRandomPosition());
        }

        loadFragment(QuestionFragment.newInstance(mViewModel.getSelectedPokemon(), mViewModel.getCorrectPosition(), this),
                QuestionFragment.class.getSimpleName());
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

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mActivityMainBinding.setViewModel(mViewModel);
        mContentMainBinding.setViewModel(mViewModel);
    }

    private void initToolbar() {
        setSupportActionBar(mActivityMainBinding.mainToolbar);
    }

    /** FRAGMENT METHODS _______________________________________________________________________ **/

    private void loadFragment(Fragment fragment, String tag) {
        mViewModel.setFragmentTag(tag);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(mContentMainBinding.mainFragmentContainer.getId(), fragment, mViewModel.getFragmentTag());
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

    private void subscribe() {

        // POKEMON LIST OBSERVER:
        final Observer<List<Pokemon>> pokemonListObserver = new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(@Nullable List<Pokemon> pokemon) {
                Log.d(LOG_TAG, "subscribe(): mPokemonList data has changed.");

                if (pokemon != null) {
                    mViewModel.setSelectedPokemon(QuestionUtils.getRandomQuestion(pokemon));
                    mViewModel.setCorrectPosition(QuestionUtils.getRandomPosition());

                    loadFragment(QuestionFragment.newInstance(mViewModel.getSelectedPokemon(), mViewModel.getCorrectPosition(),
                            MainActivity.this), QuestionFragment.class.getSimpleName());
                    startTimer(true);

                } else {
                    SnackbarUtils.displaySnackbar(mActivityMainBinding.getRoot(),
                            getString(R.string.data_load_error),
                            Snackbar.LENGTH_LONG, ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                }
            }
        };

        // Observes changes on mPokemonListData in mViewModel.
        mViewModel.getPokemonListData().observe(this, pokemonListObserver);
    }
}
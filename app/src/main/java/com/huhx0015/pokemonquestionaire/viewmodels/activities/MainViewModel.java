package com.huhx0015.pokemonquestionaire.viewmodels.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableInt;
import android.os.AsyncTask;
import android.view.View;
import com.huhx0015.pokemonquestionaire.constants.PokemonConstants;
import com.huhx0015.pokemonquestionaire.models.entities.Pokemon;
import com.huhx0015.pokemonquestionaire.models.responses.PokemonResponse;
import com.huhx0015.pokemonquestionaire.utils.JsonUtils;
import java.util.List;

/**
 * Created by Michael Yoon Huh on 6/14/2017.
 *
 * Reference: http://bytes.schibsted.com/data-binding-android-architecture-components-preview/
 */

public class MainViewModel extends ViewModel {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // LIVE DATA VARIABLES:
    private MutableLiveData<List<Pokemon>> mPokemonListData;

    // OBSERVABLE VARIABLES:
    public ObservableInt progressBarVisibility = new ObservableInt();
    public ObservableInt errorTextVisiblity = new ObservableInt();

    // VIEWMODEL VARIABLES:
    private boolean mProgressBarVisible = false;
    private boolean mErrorTextVisible = false;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public MainViewModel() {
        setProgressBarVisible(false);
        setErrorTextVisible(false);
    }

    /** GET METHODS ____________________________________________________________________________ **/

    public boolean getProgressBarVisible() {
        return mProgressBarVisible;
    }

    /** OBSERVABLE METHODS _____________________________________________________________________ **/

    public void setProgressBarVisible(boolean isVisible) {
        if (isVisible) {
            progressBarVisibility.set(View.VISIBLE);
            mProgressBarVisible = true;
        } else {
            progressBarVisibility.set(View.GONE);
            mProgressBarVisible = false;
        }
    }

    public void setErrorTextVisible(boolean isVisible) {
        if (isVisible) {
            errorTextVisiblity.set(View.VISIBLE);
            mErrorTextVisible = true;
        } else {
            errorTextVisiblity.set(View.GONE);
            mErrorTextVisible = false;
        }
    }

    /** LIVE DATA METHODS ______________________________________________________________________ **/

    public LiveData<List<Pokemon>> getPokemonListData() {
        return mPokemonListData;
    }

    /** DATA METHODS ___________________________________________________________________________ **/

    public void loadData(Context context) {
        JsonAsyncTask task = new JsonAsyncTask(context);
        task.execute(PokemonConstants.POKEMON_ASSET_NAME);
    }

    /** SUBCLASSES _____________________________________________________________________________ **/

    private class JsonAsyncTask extends AsyncTask<String, Void, List<Pokemon>> {

        private Context context;

        JsonAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setErrorTextVisible(false);
            setProgressBarVisible(true);
        }

        @Override
        protected List<Pokemon> doInBackground(String... params) {
            String responseJson = JsonUtils.loadJsonFromAsset(PokemonConstants.POKEMON_ASSET_NAME, context);
            PokemonResponse questionResponse = JsonUtils.getGroceryQuestionsFromJson(responseJson);

            if (questionResponse != null && questionResponse.getPokemonList() != null &&
                    questionResponse.getPokemonList().size() > 0) {
                return questionResponse.getPokemonList();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Pokemon> pokemonList) {
            super.onPostExecute(pokemonList);
            setProgressBarVisible(false);

            if (pokemonList == null || pokemonList.size() < 1) {
                setErrorTextVisible(true);
            }

            // Observable onChanged() method in MainActivity will be invoked once the data is set.
            mPokemonListData.setValue(pokemonList);
        }
    }
}

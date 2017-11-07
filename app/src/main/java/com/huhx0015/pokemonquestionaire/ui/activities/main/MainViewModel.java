package com.huhx0015.pokemonquestionaire.ui.activities.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableField;
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
    private MutableLiveData<List<Pokemon>> mPokemonListData = new MutableLiveData<>();;

    // OBSERVABLE VARIABLES:
    public ObservableField<Integer> progressBarVisibility = new ObservableField<>();
    public ObservableField<Integer> errorTextVisibility = new ObservableField<>();

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public MainViewModel() {
        setProgressBarVisible(false);
        setErrorTextVisible(false);
    }

    /** GET METHODS ____________________________________________________________________________ **/

    public boolean getProgressBarVisible() {
        switch (progressBarVisibility.get()) {
            case View.VISIBLE:
                return true;
            case View.GONE:
                return false;
            default:
                return false;
        }
    }

    public boolean getErrorTextVisible() {
        switch(errorTextVisibility.get()) {
            case View.VISIBLE:
                return true;
            case View.GONE:
                return false;
            default:
                return false;
        }
    }

    /** SET METHODS ____________________________________________________________________________ **/

    public void setPokemonList(List<Pokemon> list) {
        mPokemonListData.setValue(list);
    }

    /** OBSERVABLE METHODS _____________________________________________________________________ **/

    private void setProgressBarVisible(boolean isVisible) {
        if (isVisible) {
            progressBarVisibility.set(View.VISIBLE);
        } else {
            progressBarVisibility.set(View.GONE);
        }
    }

    private void setErrorTextVisible(boolean isVisible) {
        if (isVisible) {
            errorTextVisibility.set(View.VISIBLE);
        } else {
            errorTextVisibility.set(View.GONE);
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

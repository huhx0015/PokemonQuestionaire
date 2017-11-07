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

    // DATA VARIABLES:
    private boolean mIsCorrect = false;
    private int mCorrectPosition = PokemonConstants.STATE_CORRECT_POSITION_UNSET;
    private Pokemon mSelectedPokemon;

    // FRAGMENT VARIABLES:
    private String mFragmentTag;

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

    int getCorrectPosition() {
        return mCorrectPosition;
    }

    String getFragmentTag() {
        return mFragmentTag;
    }

    Pokemon getSelectedPokemon() {
        return mSelectedPokemon;
    }

    /** SET METHODS ____________________________________________________________________________ **/

    void setCorrectPosition(int correctPosition) {
        this.mCorrectPosition = correctPosition;
    }

    void setFragmentTag(String tag) {
        this.mFragmentTag = tag;
    }

    void setIsCorrect(boolean isCorrect) {
        this.mIsCorrect = isCorrect;
    }

    void setSelectedPokemon(Pokemon selectedPokemon) {
        this.mSelectedPokemon = selectedPokemon;
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

    LiveData<List<Pokemon>> getPokemonListData() {
        return mPokemonListData;
    }

    /** DATA METHODS ___________________________________________________________________________ **/

    void loadData(Context context) {
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

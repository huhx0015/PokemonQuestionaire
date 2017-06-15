package com.huhx0015.pokemonquestionaire.contracts.activities;

import android.arch.lifecycle.LiveData;
import com.huhx0015.pokemonquestionaire.models.Pokemon;
import com.huhx0015.pokemonquestionaire.models.PokemonResponse;

/**
 * Created by Michael Yoon Huh on 6/14/2017.
 */

public interface MainActivityContract {

    interface View {

        void showFragment(String tag);

        void showProgressBar(boolean isDisplay);
    }

    interface Data {

        LiveData<Pokemon> getPokemon();

        LiveData<PokemonResponse> getPokemonResponse();
    }
}
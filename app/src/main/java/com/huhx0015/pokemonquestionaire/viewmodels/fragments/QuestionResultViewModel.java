package com.huhx0015.pokemonquestionaire.viewmodels.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import com.huhx0015.pokemonquestionaire.models.entities.Pokemon;

/**
 * Created by Michael Yoon Huh on 6/14/2017.
 */

public class QuestionResultViewModel extends ViewModel {

    private Pokemon mSelectedPokemon = null;
    private int mCorrectPosition = 0;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public QuestionResultViewModel(@NonNull Pokemon selectedPokemon, int correctPosition) {
        this.mSelectedPokemon = selectedPokemon;
        this.mCorrectPosition = correctPosition;
    }

    public QuestionResultViewModel() {}







    /** SUBCLASSES _____________________________________________________________________________ **/

    /** --------------------------------------------------------------------------------------------
     *  [Factory] CLASS
     *  DESCRIPTION: A creator is used to inject the selected Pokemon and correct position values.
     *  REFERENCE: https://github.com/googlesamples/android-architecture-components/blob/master/BasicSample/app/src/main/java/com/example/android/persistence/viewmodel/ProductViewModel.java
     *  --------------------------------------------------------------------------------------------
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final Pokemon mSelectedPokemon;
        private final int mCorrectPosition;

        public Factory(@NonNull Pokemon selectedPokemon, int correctPosition) {
            this.mSelectedPokemon = selectedPokemon;
            this.mCorrectPosition = correctPosition;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new QuestionResultViewModel(mSelectedPokemon, mCorrectPosition);
        }
    }
}

package com.huhx0015.pokemonquestionaire.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class PokemonResponse implements Parcelable {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private List<Pokemon> mPokemonList;

    public PokemonResponse(List<Pokemon> pokemonList) {
        this.mPokemonList = pokemonList;
    }

    protected PokemonResponse(Parcel in) {
        mPokemonList = in.createTypedArrayList(Pokemon.CREATOR);
    }

    public List<Pokemon> getQuestionList() {
        return mPokemonList;
    }

    public static final Creator<PokemonResponse> CREATOR = new Creator<PokemonResponse>() {
        @Override
        public PokemonResponse createFromParcel(Parcel in) {
            return new PokemonResponse(in);
        }

        @Override
        public PokemonResponse[] newArray(int size) {
            return new PokemonResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mPokemonList);
    }
}

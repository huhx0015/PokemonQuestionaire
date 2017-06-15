package com.huhx0015.pokemonquestionaire.utils;

import com.huhx0015.pokemonquestionaire.models.Pokemon;

import java.util.List;
import java.util.Random;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class QuestionUtils {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private static final int NUMBER_OF_IMAGES = 4;

    /** UTILITY METHODS ________________________________________________________________________ **/

    public static Pokemon getRandomQuestion(List<Pokemon> pokemonList) {
        Random random = new Random();
        int randomPosition = random.nextInt(pokemonList.size());
        return pokemonList.get(randomPosition);
    }

    public static int getRandomPosition() {
        Random random = new Random();
        return random.nextInt(NUMBER_OF_IMAGES);
    }
}

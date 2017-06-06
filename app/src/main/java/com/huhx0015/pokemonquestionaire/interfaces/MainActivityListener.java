package com.huhx0015.pokemonquestionaire.interfaces;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public interface MainActivityListener {
    void onAnswerSelected(boolean isCorrect);
    void onTryAgainSelected(boolean isNewQuestion);
}

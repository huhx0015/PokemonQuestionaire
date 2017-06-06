package com.huhx0015.pokemonquestionaire.utils;

import com.huhx0015.pokemonquestionaire.models.Question;
import java.util.List;
import java.util.Random;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class QuestionUtils {

    private static final int NUMBER_OF_IMAGES = 4;

    public static Question getRandomQuestion(List<Question> questionList) {
        Random random = new Random();
        int randomPosition = random.nextInt(questionList.size());
        return questionList.get(randomPosition);
    }

    public static int getRandomPosition() {
        Random random = new Random();
        return random.nextInt(NUMBER_OF_IMAGES);
    }
}

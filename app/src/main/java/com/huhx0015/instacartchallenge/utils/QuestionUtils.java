package com.huhx0015.instacartchallenge.utils;

import com.huhx0015.instacartchallenge.models.Question;
import java.util.List;
import java.util.Random;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class QuestionUtils {

    public static Question getRandomQuestion(List<Question> questionList) {
        Random random = new Random();
        int randomPosition = random.nextInt(questionList.size());
        return questionList.get(randomPosition);
    }
}

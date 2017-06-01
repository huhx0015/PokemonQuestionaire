package com.huhx0015.instacartchallenge.models;

import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class QuestionsJsonDeserializer implements JsonDeserializer<QuestionsResponse> {

    private static final String LOG_TAG = QuestionsJsonDeserializer.class.getSimpleName();

    @Override
    public QuestionsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        List<Question> questionList = null;

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {

            if (entry.getValue() instanceof JsonArray) {
                String itemName = entry.getKey();
                Log.d(LOG_TAG, "Item Name: " + itemName);

                JsonArray array = (JsonArray) entry.getValue();

                List<String> urlList = new ArrayList<>();

                for (JsonElement object : array) {
                    String url = context.deserialize(object, String.class);
                    Log.d(LOG_TAG, "URL: " + url);
                    urlList.add(url);
                }

                if (questionList == null) {
                    questionList = new ArrayList<>();
                }
                questionList.add(new Question(itemName, urlList));
            }
        }

        return new QuestionsResponse(questionList);
    }
}
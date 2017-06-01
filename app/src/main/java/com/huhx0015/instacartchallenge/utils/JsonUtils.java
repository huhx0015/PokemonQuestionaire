package com.huhx0015.instacartchallenge.utils;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huhx0015.instacartchallenge.constants.GroceryConstants;
import com.huhx0015.instacartchallenge.models.Question;
import com.huhx0015.instacartchallenge.models.QuestionsJsonDeserializer;
import com.huhx0015.instacartchallenge.models.QuestionsResponse;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    public static String loadJsonFromAsset(String fileName, Context context) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();

            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            return new String(buffer, GroceryConstants.TYPE_ENCODING_UTF8);
        } catch (IOException e) {
            Log.e(LOG_TAG, "ERROR: Failed to open " + fileName + " json file.");
        }
        return null;
    }

    public static QuestionsResponse getGroceryQuestionsFromJson(String response) throws JSONException {

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(QuestionsResponse.class, new QuestionsJsonDeserializer());
        Gson gson = builder.create();
        QuestionsResponse question = gson.fromJson(response, QuestionsResponse.class);

        Log.d(LOG_TAG, "Question SIze: " + question.getQuestionList().size());

        for (Question questiond : question.getQuestionList()) {
            Log.d(LOG_TAG, "URL Size: " + questiond.getUrlList().size());
        }

//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson gson = gsonBuilder.create();
//        Question[] founderArray = gson.fromJson(response, Question[].class);
//
//
//        for (Question question : founderArray) {
//            Log.d(LOG_TAG, "question: " + question.getGroceryImageList().size());
//
//        }
//        return null;

        //List<Question> list = new ArrayList<>();

//        Gson gson = new Gson();
//        Type listType = new TypeToken<QuestionsResponse>(){}.getType();
//        QuestionsResponse questionList = (QuestionsResponse) gson.fromJson(response, listType);
//
//        for (Question question : questionList.getGroceryQuestionList()) {
//            Log.d(LOG_TAG, question.toString());
//        }

//        for (Question question : list) {
//
//            Log.d(LOG_TAG, question.toString());
//
//        }

//        JSONObject json = new JSONObject(response);
//
//        Iterator<String> iter = json.keys();
//        while (iter.hasNext()) {
//            String key = iter.next();
//            Log.d(LOG_TAG, key);
//            try {
//                Object object = json.get(key);
//
//                if (object instanceof JSONArray) {
//                    JSONArray array = (JSONArray) object;
//
//
//                    for (int i = 0; i < array.length(); i++){
//
//                        String url = array.getString(i);
//
//                        Log.d(LOG_TAG, url);
//
//                    }
//
//                }
//
//
//                //JSONArray array = (JSONArray) json.get(key);
//
//
//
//            } catch (JSONException e) {
//                Log.d(LOG_TAG, "ERROR " + e.getLocalizedMessage());
//                // Something went wrong!
//            }
//        }


//        JSONArray array;
//        try {
//            array = new JSONArray(response);
//
//            for (int i = 0; i < array.length(); i++) {
//                JSONObject obj = array.getJSONObject(i);
//
//
//
//
//            }
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

//        JSONObject jsonObject = new JSONObject(response);
//
//        Gson gson = new Gson();
//        GsonBuilder builder = new GsonBuilder();
//
//        builder.

        return null;
    }
}

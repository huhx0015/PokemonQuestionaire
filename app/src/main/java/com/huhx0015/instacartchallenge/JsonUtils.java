package com.huhx0015.instacartchallenge;

import android.content.Context;
import android.util.Log;
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
}

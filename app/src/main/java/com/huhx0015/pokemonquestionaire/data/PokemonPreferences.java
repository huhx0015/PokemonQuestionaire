package com.huhx0015.pokemonquestionaire.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Michael Yoon Huh on 6/18/2017.
 */

public class PokemonPreferences {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private static final String PREFERENCE_TIME_UP = "PREFERENCE_TIME_UP";

    /** SHARED PREFERENCES METHODS _____________________________________________________________ **/

    public static void clearPreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }

    public static void setTimeUp(boolean isTimeUp, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putBoolean(PREFERENCE_TIME_UP, isTimeUp);
        preferencesEditor.apply();
    }

    public static boolean getTimeUp(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PREFERENCE_TIME_UP, false);
    }
}
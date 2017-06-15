package com.huhx0015.pokemonquestionaire.services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import com.huhx0015.pokemonquestionaire.constants.PokemonConstants;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class TimerService extends Service {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private CountDownTimer mTimer;

    /** SERVICE METHODS ________________________________________________________________________ **/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mTimer = new CountDownTimer(PokemonConstants.QUIZ_COUNTDOWN_TIME_LIMIT, PokemonConstants.QUIZ_COUNTDOWN_TIME_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                long timeRemaining = millisUntilFinished / PokemonConstants.QUIZ_COUNTDOWN_TIME_INTERVAL;

                Intent intent = new Intent(PokemonConstants.BROADCAST_TIMER);
                intent.putExtra(PokemonConstants.EVENT_TIMER_REMAINING, timeRemaining);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }

            public void onFinish() {
                Intent intent = new Intent(PokemonConstants.BROADCAST_TIMER);
                intent.putExtra(PokemonConstants.EVENT_TIMER_FINISHED, true);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }

        }.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mTimer != null) {
            mTimer.cancel();
        }
    }
}
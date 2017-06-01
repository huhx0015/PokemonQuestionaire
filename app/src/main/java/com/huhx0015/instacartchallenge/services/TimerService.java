package com.huhx0015.instacartchallenge.services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import com.huhx0015.instacartchallenge.constants.GroceryConstants;

/**
 * Created by Michael Yoon Huh on 5/31/2017.
 */

public class TimerService extends Service {

    private CountDownTimer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mTimer = new CountDownTimer(GroceryConstants.QUIZ_COUNTDOWN_TIME_LIMIT, GroceryConstants.QUIZ_COUNTDOWN_TIME_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                long timeRemaining = millisUntilFinished / GroceryConstants.QUIZ_COUNTDOWN_TIME_INTERVAL;

                Intent intent = new Intent(GroceryConstants.BROADCAST_TIMER);
                intent.putExtra(GroceryConstants.EVENT_TIMER_REMAINING, timeRemaining);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }

            public void onFinish() {
                Intent intent = new Intent(GroceryConstants.BROADCAST_TIMER);
                intent.putExtra(GroceryConstants.EVENT_TIMER_FINISHED, true);
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
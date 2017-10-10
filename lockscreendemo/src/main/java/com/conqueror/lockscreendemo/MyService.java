package com.conqueror.lockscreendemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private Timer timer;
    private PowerManager.WakeLock wl;
    private PowerManager pm;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (pm.isScreenOn()) {
                    Log.i("john", "屏幕已经亮了");
                } else {
                    Log.i("john", "-----开始亮屏---");
                    wl.acquire();
                }
            }
        }, 1000 * 30, 1000 * 30);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
//        wl.release();
    }
}

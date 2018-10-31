package com.lipnus.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Sunpil on 2016-04-07.
 */
public class TestService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getApplicationContext(), "서비스실행", Toast.LENGTH_LONG).show();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

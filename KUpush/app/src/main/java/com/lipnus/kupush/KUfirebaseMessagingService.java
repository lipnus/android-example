package com.lipnus.kupush;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class KUfirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("Firebase", "onMessageReceived() 호출됨");
        super.onMessageReceived(remoteMessage);
    }
}

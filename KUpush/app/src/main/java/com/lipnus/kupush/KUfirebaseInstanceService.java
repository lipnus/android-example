package com.lipnus.kupush;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class KUfirebaseInstanceService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        Log.d("FirebaseIDDService", "onTockenRefresh() 호출됨");
        super.onTokenRefresh();
    }
}

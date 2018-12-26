package com.example.manish.wpaper.pushNotif;

import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshTok = FirebaseInstanceId.getInstance().getToken();
        storerefreshTokInPref(refreshTok);
    }

    private void storerefreshTokInPref(String refreshTok) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("shared_pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("regID",refreshTok);
        editor.commit();
    }
}

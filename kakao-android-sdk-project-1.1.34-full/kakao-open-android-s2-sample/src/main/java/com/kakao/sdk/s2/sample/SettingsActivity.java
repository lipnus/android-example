package com.kakao.sdk.s2.sample;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.kakao.s2.Event;
import com.kakao.s2.EventsLogResponse;
import com.kakao.s2.S2Service;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.Utility;
import com.kakao.util.helper.log.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CheckedTextView view = (CheckedTextView)findViewById(R.id.activity1_event_button);

        initializeViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity1_event_button:
            case R.id.activity2_event_button:
            case R.id.activity3_event_button:
                processCheckedTextView((CheckedTextView)v);
                break;
            case R.id.publish_batched_events_button:
                publishBatchedEvents();
                break;
        }
    }

    private void initializeViews() {
        int[] buttonIds = {R.id.activity1_event_button, R.id.activity2_event_button, R.id.activity3_event_button};
        int[] stringIds = {R.string.text_activity1, R.string.text_activity2, R.string.text_activity3};
        SharedPreferences preferences = getSharedPreferences(Utility.getAppPackageName(getApplicationContext()), MODE_PRIVATE);
        for (int i = 0; i < buttonIds.length; i++) {
            ((CheckedTextView)findViewById(buttonIds[i])).setChecked(preferences.getBoolean(getResources().getString(stringIds[i]), false));
        }
    }

    public void processCheckedTextView(CheckedTextView view) {
        view.setChecked(!view.isChecked());
        boolean isChecked = view.isChecked();

        String key = null;
        switch (view.getId()) {
            case R.id.activity1_event_button:
                key = getResources().getString(R.string.text_activity1);
                break;
            case R.id.activity2_event_button:
                key = getResources().getString(R.string.text_activity2);
                break;
            case R.id.activity3_event_button:
                key = getResources().getString(R.string.text_activity3);
                break;
            default:
                return;
        }

        SharedPreferences preferences = getSharedPreferences(Utility.getAppPackageName(getApplicationContext()), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, isChecked);
        editor.apply();
    }

    private void publishBatchedEvents() {
        S2Service.getInstance().publishEvents();
    }
}
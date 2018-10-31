package com.lipnus.brochure;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;

import java.util.List;

/**
 * Created by Sunpil on 2016-06-05.
 */
public class VoiceRecognition {

    private PackageManager pm;


    public final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    public VoiceRecognition(Context ctx) {
        this.pm = ctx.getPackageManager();
    }

    // 음성 인식을 지원하는지 확인
    public boolean recognitionAvailable() {
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        if (activities.size() != 0) {
            return true;
            // 지원할 경우 true 반환
        } else {
            return false;
            // 지원하지 않을 경우 false 반환
        }
    }

    // 구글 음성 인식 intent 생성
    public Intent getVoiceRecognitionIntent(String message) {
        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, message);

        return intent;
    }
}
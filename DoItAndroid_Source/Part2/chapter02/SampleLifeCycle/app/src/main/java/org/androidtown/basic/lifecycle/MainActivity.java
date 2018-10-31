package org.androidtown.basic.lifecycle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * 액티비티의 수명주기에 대해 알 수 있습니다.
 *
 * @author Mike
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 요청 코드 정의
     */
    public static final int REQUEST_CODE_ANOTHER = 1001;

    public static final String PREF_ID = "Pref01";
    public static final int actMode = Activity.MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "onCreate() 호출됨.", Toast.LENGTH_LONG).show();
    }

    public void onButton1Clicked(View v) {
        // 인텐트 객체를 만듭니다.
        Intent intent = new Intent(this, AnotherActivity.class);

        // 액티비티를 띄워주도록 startActivityForResult() 메소드를 호출합니다.
        startActivityForResult(intent, REQUEST_CODE_ANOTHER);
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "onDestroy() 호출됨.", Toast.LENGTH_LONG).show();

        super.onDestroy();
    }


    @Override
    protected void onPause() {
        Toast.makeText(this, "onPause() 호출됨.", Toast.LENGTH_LONG).show();

        saveCurrentState();

        super.onPause();
    }


    @Override
    protected void onRestart() {
        Toast.makeText(this, "onRestart() 호출됨.", Toast.LENGTH_LONG).show();

        super.onRestart();
    }


    @Override
    protected void onResume() {
        Toast.makeText(this, "onResume() 호출됨.", Toast.LENGTH_LONG).show();

        restoreFromSavedState();

        super.onResume();
    }


    @Override
    protected void onStart() {
        Toast.makeText(this, "onStart() 호출됨.", Toast.LENGTH_LONG).show();

        super.onStart();
    }


    @Override
    protected void onStop() {
        Toast.makeText(this, "onStop() 호출됨.", Toast.LENGTH_LONG).show();

        super.onStop();
    }



    /**
     * 새로운 액티비티에서 돌아올 때 자동 호출되는 메소드
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ANOTHER) {
            Toast toast = Toast.makeText(this, "onActivityResult() 메소드가 호출됨. 요청코드 : " + requestCode + ", 결과코드 : " + resultCode, Toast.LENGTH_LONG);
            toast.show();
        }

    }

    protected void restoreFromSavedState() {
        SharedPreferences myPrefs = getSharedPreferences(PREF_ID, actMode);
        if ((myPrefs != null) && (myPrefs.contains("txtMsg")) ) {
            String myData = myPrefs.getString("txtMsg", "");
            Toast.makeText(this, "Restored : " + myData, Toast.LENGTH_SHORT).show();
        }
    }

    protected void saveCurrentState() {
        SharedPreferences myPrefs = getSharedPreferences(PREF_ID, actMode);
        SharedPreferences.Editor myEditor = myPrefs.edit();
        myEditor.putString( "txtMsg", "My name is mike." );
        myEditor.commit();
    }

    protected void clearMyPrefs() {
        SharedPreferences myPrefs = getSharedPreferences(PREF_ID, actMode);
        SharedPreferences.Editor myEditor = myPrefs.edit();
        myEditor.clear();
        myEditor.commit();
    }

}

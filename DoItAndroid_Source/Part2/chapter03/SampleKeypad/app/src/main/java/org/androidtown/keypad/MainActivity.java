package org.androidtown.keypad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 키패드 설정에 따라 숫자 키패드, 패스워드 키패드 등이 다르게 보이는 것을 알 수 있습니다.
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

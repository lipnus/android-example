package org.androidtown.focus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 레이아웃에서 입력상자에 button_selector를 배경으로 설정하면 포커스를 받을 때 배경을 바꿀 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

package org.androidtown.ui.linearlayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * 리니어 레이아웃 사용하기
 *
 * - 앱을 실행한 후 원하는 버튼을 하나 선택하면 리니어 레이아웃의 속성을 이용해 만든 화면이 보입니다.
 * - 화면이 어떻게 만들어졌는지는 res/layout 폴더 밑의 해당 XML 레이아웃 파일을 보시면 됩니다.
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

    public void onButton1Clicked(View v) {
        setContentView(R.layout.normal);
    }

    public void onButton2Clicked(View v) {
        setContentView(R.layout.padding);
    }

    public void onButton3Clicked(View v) {
        setContentView(R.layout.gravity);
    }

    public void onButton4Clicked(View v) {
        setContentView(R.layout.weight);
    }

    public void onButton5Clicked(View v) {
        setContentView(R.layout.baseline);
    }

    public void onButton6Clicked(View v) {
        setContentView(R.layout.gravitytext01);
    }

    public void onButton7Clicked(View v) {
        setContentView(R.layout.gravitytext02);
    }

    public void onButton8Clicked(View v) {
        setContentView(R.layout.gravitytext03);
    }

    public void onButton9Clicked(View v) {
        Intent intent = new Intent(getApplicationContext(), SampleLayoutCodeActivity.class);
        startActivity(intent);
    }

}

package org.androidtown.ui.relativelayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 상대 레이아웃 구성하기
 *
 * - 앱을 실행한 후 원하는 버튼을 하나 선택하면 상대 레이아웃의 속성을 사용한 화면이 보입니다.
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

    /**
     * 첫번째 버튼을 눌렀을 때 normal.xml 에 정의된 화면 레이아웃을 보여줍니다.
     * @param v
     */
    public void onButton1Clicked(View v) {
        setContentView(R.layout.normal);
    }

    /**
     * 두번째 버튼을 눌렀을 때 overlay.xml 에 정의된 화면 레이아웃을 보여줍니다.
     * @param v
     */
    public void onButton2Clicked(View v) {
        setContentView(R.layout.overlay);
    }

    /**
     * 세번째 버튼을 눌렀을 때 centerfill.xml 에 정의된 화면 레이아웃을 보여줍니다.
     * @param v
     */
    public void onButton3Clicked(View v) {
        setContentView(R.layout.centerfill);
    }

}

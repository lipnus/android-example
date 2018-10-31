package org.androidtown.ui.bitmap.selector;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 비트맵 Selector를 이용해 비트맵 버튼을 만드는 방법을 알 수 있습니다.
 *
 * @author Mike
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액션바 감추기
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide();
        }

        // 버튼 이벤트 처리
        Button arrowLeftBtn = (Button)findViewById(R.id.arrowLeftBtn);
        arrowLeftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "버튼이 눌렸어요.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}

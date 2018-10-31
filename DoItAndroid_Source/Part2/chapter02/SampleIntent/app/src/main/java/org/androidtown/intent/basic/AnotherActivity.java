package org.androidtown.intent.basic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 새로운 액티비티
 * 
 * @author Mike
 */
public class AnotherActivity extends Activity {
	Button backButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.another);

        backButton = (Button) findViewById(R.id.backButton);
		
		// 버튼을 눌렀을 때 메인 액티비티로 돌아갑니다.
        backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
        		// 객체를 만듭니다.
        		Intent resultIntent = new Intent();
        		resultIntent.putExtra("name", "mike");

                // 응답을 전달하고 이 액티비티를 종료합니다.
        		setResult(RESULT_OK, resultIntent);
                finish();
			}
		});

    }
 
}

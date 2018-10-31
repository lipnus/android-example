package org.androidtown.intent.call;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 인텐트로 전화걸기 화면을 보여주는 방법을 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {
    TextView textView1;
    EditText editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.textView1);
        editText1 = (EditText) findViewById(R.id.editText1);

    }

    public void onButton1Clicked(View v) {
        // 입력상자에 입력한 전화번호를 가져옴
        String data = editText1.getText().toString();

        // 인텐트를 만들고 이것을 이용해 액티비티를 띄워줌
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(data));
        startActivity(intent);
    }

}

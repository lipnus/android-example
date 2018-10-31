package com.lipnus.parcel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {

    public static final String KEY_SIMPLE_DATA = "data";
    TextView tV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        tV = (TextView) findViewById(R.id.fuckingtext);
        Button backBtn = (Button) findViewById(R.id.end_btn);

        processIntent();

        // 버튼을 눌렀을 때 메인 액티비티로 돌아갑니다.
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
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

    private void processIntent() {

        Log.d("SSIBAL", "여기작동하냐?");

        // 인텐트 안의 번들 객체를 참조합니다.
        Bundle bundle = getIntent().getExtras();

        // 번들 객체 안의 SimpleData 객체를 참조합니다.
        SimpleData data = (SimpleData)bundle.getParcelable(KEY_SIMPLE_DATA);

        // 텍스트뷰에 값을 보여줍니다.
        //tV.setText("Parcelable 객체로 전달된 값\nNumber : " + data.getNumber() + "\nMessage : " + data.getMessage());
    }
}




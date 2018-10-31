package com.lipnus.intent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AnotherActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ANOTHER = 1001;

    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        btn = (Button) findViewById(R.id.open_btn);

        //이벤트 리스너로 한번 해보자
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultiT = new Intent();
                resultiT.putExtra("name", "LIPNUS");
                setResult(RESULT_OK, resultiT);
                finish();
            }
        });

    }


}

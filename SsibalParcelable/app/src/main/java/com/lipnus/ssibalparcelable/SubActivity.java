package com.lipnus.ssibalparcelable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SubActivity extends AppCompatActivity {

    TextView tV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        tV = (TextView) findViewById( R.id.showing_text );

        //자신을 호출한 액티비티가 보낸 인텐트에서 직렬화 객체를 추출한다.
        //==========================================================================================
        Intent iT = getIntent();
        FuckingData fuck = (FuckingData) iT.getParcelableExtra("LIP");

        if(fuck == null){
            Toast.makeText(getApplicationContext(), "FuckingData가 빈채로 왔다", Toast.LENGTH_LONG).show();
            return;
        }
        //==========================================================================================


        //전달받은 직렬화객체의 내용을 출력한다.
        //==========================================================================================
        tV.setText(fuck.getInt() + ", " + fuck.getString() );
        //==========================================================================================
    }

    public void onClick(View v){
        finish();
    }
}

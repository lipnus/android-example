package com.lipnus.parcel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_SIMPLE_DATA = "DATA";
    public static final int REQUEST_CODE_ANOTHER = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("SSIBAL", "로그작동하냐?");
    }

    public void onClick(View v){
        Intent iT = new Intent(getApplicationContext(), SubActivity.class);
        SimpleData data = new SimpleData(100, "Hello Android!");
        iT.putExtra(KEY_SIMPLE_DATA, data);

        // 액티비티를 띄워주도록 startActivityForResult() 메소드를 호출합니다.
        startActivityForResult(iT, REQUEST_CODE_ANOTHER);
    }


    //인텐트의 결과값을 받음
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ANOTHER) {
            Toast toast = Toast.makeText(this, "onActivityResult() 메소드가 호출됨. 요청코드 : " + requestCode + ", 결과코드 : " + resultCode, Toast.LENGTH_LONG);
            toast.show();
        }

    }
}

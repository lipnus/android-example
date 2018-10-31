package com.lipnus.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editID = null;
    EditText editPW = null;
    Button btnLogin = null;
    TextView tVAlarm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        editID = (EditText)findViewById(R.id.edit_id);
        editPW = (EditText)findViewById(R.id.edit_pw);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tVAlarm = (TextView) findViewById(R.id.tv_alarm);
    }

    public void onClick_Login(View v){

        String strID = editID.getText().toString();
        String strPW = editPW.getText().toString();


        //아이디와 비밀번호가 제대로 입력되었는지 체크
        //==========================================================================================
        if (editID.getText().toString().equals("") || editPW.getText().toString().equals("")){
            tVAlarm.setText("아이디와 비밀번호를 입력해주세요.");
        }
        else{
            //인텐트를 이용해서 메뉴호출
            //--------------------------------------------------------------------------------------
            Intent iT = new Intent(getApplicationContext(), MenuActivity.class);

            //Bundle객체를 이용하여 아이디와 비밀번호를 전송
            Bundle bundleData = new Bundle();
            bundleData.putString("STR_ID", strID);
            bundleData.putString("STR_PW", strPW);
            iT.putExtra("LOGIN_DATA", bundleData);

            startActivityForResult(iT, 1818);
            //--------------------------------------------------------------------------------------
        }
        //==========================================================================================

    }


    //보낸 인텐트에서 답장을 받은 걸 처리하는 매소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        //1818로 보낸 것에 대한 답장
        if(requestCode == 1818){

            //requestCode가 1818이고, 그걸로 보낸 것들 중에서 RESULT_OK로 보낸 답장
            if(resultCode == RESULT_OK){
                tVAlarm.setText("선택한 메뉴: " + intent.getExtras().getString("RETURN"));
            }

        }
    }
}

package org.androidtown.basic.receiver.sms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


/**
 * SMS를 수신하면 화면에 그 내용을 보여주는 방법을 알 수 있습니다.
 * 브로드캐스트 수신자를 이용하면서 수신 SMS의 내용을 화면에 보여주는 방법을 알 수 있습니다.
 *
 * 이 메인 액티비티는 화면에 보여주기 위한 것이 아닙니다.
 *
 * API 23 : 실행 시간에 사용자에게 위험 권한(RECEIVE_SMS)을 요청하는 것이 필요합니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "SMS 수신 권한 있음.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "SMS 수신 권한 없음.", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
                Toast.makeText(this, "SMS 권한 설명 필요함.", Toast.LENGTH_LONG).show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.RECEIVE_SMS},
                        1);

            }
        }


    }


    public void onClick(View v){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "SMS 수신 권한 있음.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "SMS 수신 권한 없음.", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
                Toast.makeText(this, "SMS 권한 설명 필요함.", Toast.LENGTH_LONG).show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.RECEIVE_SMS},
                        1);

            }
        }
    }

    public void onButton1Clicked(View v) {

        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "SMS 권한을 사용자가 승인함.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this, "SMS 권한 거부됨.", Toast.LENGTH_LONG).show();

                }

                return;
            }

        }
    }

}

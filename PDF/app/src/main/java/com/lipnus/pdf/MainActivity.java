package com.lipnus.pdf;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    EditText eT = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //에디트텍스트 객체 참조
        eT = (EditText) findViewById(R.id.edit_text);

        checkDangerousPermissions();
    }

    public void onClick(View v){
        String fileName = eT.getText().toString();
        if(fileName.length() > 0){
            openPDF( fileName.trim() );
        }
        else{
            Toast.makeText(getApplicationContext(), "PDF파일명을 입력하세요.", Toast.LENGTH_LONG).show();
        }
    }

    public void openPDF(String contentsPath){
        File file = new File(contentsPath);

        if(file.exists()){
            Uri path = Uri.fromFile(file);
            Intent iT = new Intent(Intent.ACTION_VIEW);
            iT.setDataAndType(path, "application/pdf");
            iT.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try{startActivity(iT);}
            catch(ActivityNotFoundException e){
                Toast.makeText(getApplicationContext(), "뷰어가 없어", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "파일이 없습니다.", Toast.LENGTH_LONG).show();
        }
    }


    //뭔진 모르겠으나 걍 그대로 빼낌
    //========================================================================================================================
    //========================================================================================================================
    private void checkDangerousPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                String[] permissions = {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //========================================================================================================================
    //========================================================================================================================
}

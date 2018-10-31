package org.androidtown.pdf;

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

/**
 * 인텐트를 이용해 PDF 문서를 보기 위한 뷰어를 띄우는 방법에 대해 알 수 있습니다.
 *
 *  이 프로젝트 안의 data 폴더에 있는 sample.pdf 파일을 단말의 SD 카드에 넣었다면,
 *  /sdcard/sample.pdf 라고 입력상자에 입력하면 됩니다.
 *
 * @author Mike
 */
public class MainActivity extends AppCompatActivity {
    EditText editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = (EditText) findViewById(R.id.editText1);

        checkDangerousPermissions();
    }

    public void onButton1Clicked(View v) {
        // 입력한 파일명을 가져옵니다.
        String filename = editText1.getText().toString();
        if (filename.length() > 0) {
            openPDF(filename.trim());
        } else {
            Toast.makeText(getApplicationContext(), "PDF 파일명을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * PDF 파일을 열기 위해 정의한 메소드
     *
     * @param contentsPath
     */
    public void openPDF(String contentsPath) {
        File file = new File(contentsPath);

        if (file.exists()) {
            // 입력한 파일 정보로 Uri 객체 생성
            Uri path = Uri.fromFile(file);

            // 인텐트 객체를 만들고 setDataAndType() 메소드로 Uri 객체 설정
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                // 액티비티 띄우기
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "PDF 파일을 보기 위한 뷰어 앱이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "PDF 파일이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

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

}

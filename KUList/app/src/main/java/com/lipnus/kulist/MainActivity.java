package com.lipnus.kulist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    ListView listview;
    ListViewAdapter adapter;
    File sdcard, file;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SD카드
        sdcard = Environment.getExternalStorageDirectory();
        file = new File(sdcard, "capture.jpg"); //sd카드 폴더 안에 파일객체를 만듦

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);


        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });




    }

    public void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)); //파일을 Uri라는 포맷으로 맞춤

        //응답을 받아야 하니까 forRusult
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(requestCode== 101 && resultCode == Activity.RESULT_OK){

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 32; //(1/32크기로 리샘플링해서 로딩)

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

            //리스트뷰에 추가
            adapter.addItem("Name", "Comment", bitmap);
            adapter.notifyDataSetChanged();
        }

    }


}

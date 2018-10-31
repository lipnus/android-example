package com.lipnus.kum_gson_structure;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.lipnus.kum_gson_structure.com.lipnus.volley.IVolleyResult;
import com.lipnus.kum_gson_structure.com.lipnus.volley.VolleyConnect;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WriteActivity extends AppCompatActivity {

    //manifests에 android:windowSoftInputMode="adjustResize" 이걸 추가해줘야 키보드에 의해서 커서가 가려지지 않음

    IVolleyResult mResultCallback = null;
    VolleyConnect volley;

    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;

    EditText reviewEt;
    ImageView iV;

    private String UPLOAD_URL ="http://kumchurk.ivyro.net/app/upload_menureview.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_menu);

        reviewEt = (EditText) findViewById(R.id.review_eT);
        iV = (ImageView) findViewById(R.id.imageview);

        //Volley 콜백함수
        initVolleyCallback();
    }


    //버튼클릭
    public void onClick_reviewUpload(View v){
        Connect();
    }
    public void onClick_selectImage(View v){
        showFileChooser();
    }

    //오늘이 몇일인가
    public String getTodayDate(){

        //오늘
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyyMMddHHmm", Locale.KOREA );
        java.util.Date currentTime = new java.util.Date ( );
        String todayDate = mSimpleDateFormat.format(currentTime);

        return todayDate;
    }

    //volley 접속
    public void Connect(){

        String url = UPLOAD_URL;

        //비트맵을 String으로 바꿈
        String image = getStringImage(bitmap);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", "808");
        params.put("memo", reviewEt.getText().toString() );
        params.put("res_name", "매스플레이트");
        params.put("menu_name", "목살스테이크샐러드");
        params.put("menu_image", image);
        params.put("created_at", getTodayDate());
        params.put("updated_at", getTodayDate());

        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    }


    //Volley 콜백
    void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {

            @Override
            public void notifySuccess(String response) {
                //전송의 결과를 받는 부분
                Log.d("VOVO", response);
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분
                Log.d("VOVO", "에러: "+ error);
            }
        };
    }


    //이미지파일을 선택(사진첩을 호출하고 onActivityResult로 받음)
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    //사진첩 호출의 결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            try {

                //리사이징
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2; //(1/2 크기로 리샘플링해서 로딩)

                // 데이타를 받아와서 bitmap에 저장(위에서 정한 option적용)
                Uri currImageURI = data.getData();
                bitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(currImageURI), null, options);

                //반띵했는데 가로가 650이상인 경우
                if ( bitmap.getWidth() > 650 ){
                    bitmap = resizeBitmap(bitmap);
                }


                //Setting the Bitmap to ImageView
                iV.setImageBitmap(bitmap);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //이미지를 Base64(64비트?) 의 String으로 변환해줌
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    //비트맵크기조절(비율유지)
    public Bitmap resizeBitmap(Bitmap original) {

        //가로를 650으로 줄인다. 세로는 비율에 맞게 줄어듦
        int resizeWidth = 650;

        double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
        if (result != original) {
            original.recycle();
        }
        return result;
    }
}

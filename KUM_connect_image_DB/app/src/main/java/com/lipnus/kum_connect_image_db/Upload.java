package com.lipnus.kum_connect_image_db;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class Upload extends AppCompatActivity {
    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;
    private EditText editTextName;
    TextView tV;

    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL ="http://kumchurk.ivyro.net/test2/upload.php";

    private String KEY_ID = "user_id";
    private String KEY_HEART = "heart";
    private String KEY_FUCK = "fuck";
    private String KEY_MEMO = "memo";
    private String KEY_COMMENT_NUM = "comment_num";
    private String KEY_RES_NAME = "res_name";
    private String KEY_MENU_NAME = "menu_name";
    private String KEY_MENU_IMAGE = "menu_image";
    private String KEY_CREATED_AT = "created_at";
    private String KEY_UPDATED_AT = "updated_at";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        editTextName = (EditText) findViewById(R.id.editText);
        imageView  = (ImageView) findViewById(R.id.imageView);
        tV = (TextView) findViewById(R.id.tV);

    }


    public void onClick_upload(View v){ //사진업로드
        uploadImage();
    }
    public void onClick_choose(View v){ //사진종류선택
        showFileChooser();
    }
    public void onClick_makeJson(View v){
        String jsonStr = makeJson();
    }
    public void onClick_check(View v){
        Intent i = new Intent(this, Download.class);
        startActivity(i);
    }
    public void onClick_volleysystem(View v){
        Intent i = new Intent(this, VolleyClassTest.class);
        startActivity(i);
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
                if ( bitmap.getWidth()>650 ){
                    bitmap = resizeBitmap(bitmap);
                }

                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    //이미지를 Base64(64비트?) 의 String으로 변환해줌
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //volley를 이용하여 이미지를 업로드
    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();

                        //Showing toast message of the response(서버로 부터 받은 결과값)
                        tV.setText(s);
//                        Toast.makeText(Upload.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(Upload.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Converting Bitmap to String(텍스트화시킨 이미지)
                String image = getStringImage(bitmap);

                //메뉴이름
                String name = editTextName.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_ID, "dd");
                params.put(KEY_HEART, "1");
                params.put(KEY_FUCK, "2");
                params.put(KEY_MEMO, "리뷰내용");
                params.put(KEY_COMMENT_NUM, "3");
                params.put(KEY_RES_NAME, "식당이름");
                params.put(KEY_MENU_NAME, name);
                params.put(KEY_MENU_IMAGE, image);
                params.put(KEY_CREATED_AT, "201702240000");
                params.put(KEY_UPDATED_AT, "201702240000");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    //json String을 만든다
    public String makeJson(){
        Gson gson = new Gson();
        ArrayList<MenuComment> CommentList = new ArrayList<>();

        //이미지뷰에 띄워져 있는 이미지뷰를 텍스트화하여 json에 넣음
        String image = getStringImage(bitmap);

        //메뉴이름
        String name = editTextName.getText().toString().trim();

        //dto
        CommentList.add(new MenuComment(1, 2, 3, "글내용", 4, "식당이름", name, image, "201702240000","201702240000" ));

        //gson을 이용하여 dto를 json으로 변환
        String result = gson.toJson(CommentList);
        Log.d("GSGS", ":"+result);

        return result;
    }
}
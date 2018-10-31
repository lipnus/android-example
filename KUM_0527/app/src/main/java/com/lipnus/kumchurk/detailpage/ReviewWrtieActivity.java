package com.lipnus.kumchurk.detailpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lipnus.kumchurk.GlobalApplication;
import com.lipnus.kumchurk.IVolleyResult;
import com.lipnus.kumchurk.R;
import com.lipnus.kumchurk.ScrollActivity;
import com.lipnus.kumchurk.VolleyConnect;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReviewWrtieActivity extends AppCompatActivity {

    //manifests에 android:windowSoftInputMode="adjustResize" 이걸 추가해줘야 키보드에 의해서 커서가 가려지지 않음

    IVolleyResult mResultCallback = null;
    IVolleyResult mResultCallback2 = null;
    VolleyConnect volley;

    private Bitmap bitmap = null;
    private int PICK_IMAGE_REQUEST = 1;

    EditText reviewEt;
    ImageView iV;

    TextView menuNameTv;
    TextView resNameTv;

    //이미지만 업로드 버튼
    Button imgUploadBt;

    //지금 화면에서 표시할 메뉴에름과 식당이름(인텐트로 받아옴)
    private String nowResName;
    private String nowMenuName;

    //어디서 호출했는지
    private String callFrom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);


        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //페이지전환 애니매이션 설정
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);


        reviewEt = (EditText) findViewById(R.id.review_eT);
        iV = (ImageView) findViewById(R.id.imageview);
        menuNameTv = (TextView) findViewById(R.id.menuwrite_menuname_Tv);
        resNameTv = (TextView) findViewById(R.id.menuwrite_resName_Tv);
        imgUploadBt = (Button) findViewById(R.id.rw_img_uploaad_bt);

        //이미지업로드 버튼
        Glide.with(this)
                .load( R.drawable.imageupload )
                .into(iV);
        iV.setScaleType(ImageView.ScaleType.FIT_XY);

        //앞의 엑티비티로부터 식당이름과 메뉴이름을 받는다
        Intent iT = getIntent();
        nowResName = iT.getExtras().getString("res_name");
        nowMenuName = iT.getExtras().getString("menu_name");
        callFrom = iT.getExtras().getString("callFrom");

        //인텐트로부터 받은 메뉴이름과 식당이름표시
        menuNameTv.setText(nowMenuName);
        resNameTv.setText(nowResName);

        //Volley 콜백함수
        initVolleyCallback();
        initVolleyCallback2();

        //사용자의 권한을 확인
        connect_level();
    }

    @Override
    public void onBackPressed() {

        //이미지를 올렸거나 쓴 글이 있으면 한번 물어본다
//        if( bitmap != null || !(reviewEt.getText().toString().equals("")) ) {}

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("리뷰작성 페이지에서 나가기")
                .setCancelText("취소")
                .setConfirmText("나가기")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        finish();

                    }
                })
                .show();



    }


    //글꼴적용을 위해서 필요(참조 : http://gun0912.tistory.com/10 )
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    //버튼클릭
    public void onClick_reviewUpload(View v){

        if(bitmap == null){
            //이미지가 없으면 업로드 불가능
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Notice")
                    .setContentText("사진을 올려주세요")
                    .show();

        }
        else{
            //이미지는 있고, 글이 없는경우
            if(reviewEt.getText().toString().equals("")){
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Notice")
                        .setContentText("리뷰글을 작성해주세요")
                        .show();
            }else{
                //이미지와 글이 모두 있음
                connect();
            }

        }

    }
    public void onClick_selectImage(View v){
        showFileChooser();
    }
    public void onClick_reviewUpload_onlyImg(View v){
        if(bitmap == null){
            //이미지가 없으면 업로드 불가능
            Toast.makeText(getApplicationContext(), "이미지를 업로드 해주세요", Toast.LENGTH_LONG).show();
        }
        else{
            connect_imgOnly();
        }
    }





    //volley 접속(사용자 권한 확인)
    public void connect_level(){
        Log.d("VOVO", "접속...");
        String url = "http://kumchurk.ivyro.net/app/download_userinfo_vip.php";

        Map<String, String> params = new HashMap<>();
        params.put("user_id", GlobalApplication.getUser_id());


        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵, [다이얼로그 사용하지 않음(3)]
        volley = new VolleyConnect(mResultCallback2, this, url, params, 3);
    }

    //volley 콜백(권한확인)
    public void initVolleyCallback2(){
        mResultCallback2 = new IVolleyResult() {
            @Override
            public void notifySuccess(String response) {

                //전송의 결과를 받는 부분
                Log.d("SCLI", response);
                Log.d("ASAS", "???" + response);
                levelCheck(response);
            }

            @Override
            public void notifyError(VolleyError error) {
                //전송 시 에러가 생겼을 때 받는 부분

                Log.d("VOVO", "에러: "+ error);

            }
        };
    }

    //사용자의 레벨 체크
    public void levelCheck(String response){

        if(response.equals("10")){
            Toast.makeText(getApplicationContext(), "당신은 사진만 올릴 수도 있습니다!", Toast.LENGTH_LONG).show();
            imgUploadBt.setVisibility(View.VISIBLE);
        }
    }





    //오늘이 몇일인가
    public String getTodayDate(){

        //오늘
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyyMMddHHmm", Locale.KOREA );
        java.util.Date currentTime = new java.util.Date ( );
        String todayDate = mSimpleDateFormat.format(currentTime);

        return todayDate;
    }

    //volley 접속(리뷰쓰기)
    public void connect(){

        String url = "http://kumchurk.ivyro.net/app/upload_menureview.php";;

        //비트맵을 String으로 바꿈
        String image = getStringImage(bitmap);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", GlobalApplication.getUser_id() );
        params.put("memo", reviewEt.getText().toString() );
        params.put("res_name", nowResName);
        params.put("menu_name", nowMenuName);
        params.put("menu_image", image);
        params.put("created_at", getTodayDate());
        params.put("updated_at", getTodayDate());

        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    }

    //volley 접속(이미지만 올리기, 콜백이후 과정은 동일하므로 같이 쓴다)
    public void connect_imgOnly(){

        String url = "http://kumchurk.ivyro.net/app/upload_menureview_imgonly.php";

        //비트맵을 String으로 바꿈
        String image = getStringImage(bitmap);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", GlobalApplication.getUser_id() );
        params.put("res_name", nowResName);
        params.put("menu_name", nowMenuName);
        params.put("menu_image", image);

        //값을 받아올 리스너, Context, url, post로 보낼 것들의 key와 value들을 담은 해쉬맵
        volley = new VolleyConnect(mResultCallback, this, url, params);
    }



    //Volley 콜백
    void initVolleyCallback(){
        mResultCallback = new IVolleyResult() {

            @Override
            public void notifySuccess(String response) {

                //전송의 결과를 받는 부분
                uploadSuccess();
            }

            @Override
            public void notifyError(VolleyError error) {

                //전송 시 에러가 생겼을 때 받는 부분
                uploadFail();
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
//                //사진이 돌아갔으면 원래 방향대로 돌린다
//                int rotateAngle = GetExifOrientation( data.getData().getPath() );
//                Toast.makeText(getApplicationContext(), "" + rotateAngle, Toast.LENGTH_LONG).show();
//
//
//                //리사이징
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 2; //(1/2 크기로 리샘플링해서 로딩)
//
//                // 데이타를 받아와서 bitmap에 저장(위에서 정한 option적용)
//                Uri currImageURI = data.getData();
//                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(currImageURI), null, options);
//
//                //반띵했는데 가로가 650이상인 경우
//                if ( bitmap.getWidth() > 650 ){
//                    bitmap = resizeBitmap(bitmap);
//                }


//                if(rotateAngle != 0){
//                    bitmap = imgRotate(bitmap, rotateAngle);
//                }


                Glide.with(this)
                        .load( data.getData() )
                        .centerCrop()
                        .into(iV);



                Glide.with(this)
                        .load( data.getData() )
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                bitmap = resource;
                                Log.d("DDDD", "전" + bitmap.getWidth());
                                if ( bitmap.getWidth() > 650 ){
                                    bitmap = resizeBitmap(bitmap);
                                }
                                Log.d("DDDD", "후:" + bitmap.getWidth());
                            }
                        });

                Log.d("DDDD", "시발2");







            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //비트맵 이미지를 회전
    private Bitmap imgRotate(Bitmap bmp, int rotate){
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);

        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        bmp.recycle();

        return resizedBitmap;
    }


    //사진의 회전정보를 얻어옴(몇도 돌아갔는지)
    public synchronized static int GetExifOrientation(String filepath)
    {
        int degree = 0;
        ExifInterface exif = null;

        try
        {
            exif = new ExifInterface(filepath);
        }
        catch (IOException e)
        {
            Log.e("TATA", "cannot read exif");
            e.printStackTrace();
        }

        if (exif != null)
        {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

            if (orientation != -1)
            {
                // We only recognize a subset of orientation tag values.
                switch(orientation)
                {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }

        return degree;
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

    //리뷰성공 다이알로그
    public void uploadSuccess(){
        //삭제성공 메시지를 띄우고 액티비티 종료
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Complete")
                .setContentText("리뷰가 등록되었습니다")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();

                        //메뉴창 띄워줌
                        Intent iT = new Intent(getApplicationContext(), ScrollActivity.class);
                        iT.putExtra("res_name", nowResName);
                        iT.putExtra("menu_name", nowMenuName);
                        startActivity(iT);

                        if(callFrom.equals("ScrollAvtivity")){
                            //앞에 있떤 메뉴표시창은 닫음
                            ScrollActivity scActivity = (ScrollActivity)ScrollActivity.SCActivity;
                            scActivity.finish();

                        }

                        //현재 창 닫음
                        finish();
                    }
                })
                .show();
    }

    public void uploadFail(){


        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("sorry..")
                .setContentText("네트워크 접속 장애\n다시 시도할께요!")
                .setCustomImage(R.drawable.sorry2)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        connect();

                    }
                })
                .show();


    }

}


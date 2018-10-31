package com.lipnus.kum_connect_image_db;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.bumptech.glide.Glide;

import java.util.Hashtable;
import java.util.Map;

public class Download extends AppCompatActivity {
    //서버로부터 json형태로 그림경로를 다운받고, glide로 그 경로에 있는 그림 출력


    private String UPLOAD_URL ="http://kumchurk.ivyro.net/test2/download.php";
    ImageView iV;
    TextView tV;
    String downloadData=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        iV = (ImageView) findViewById(R.id.downIv);
        tV = (TextView) findViewById(R.id.downTv);

        uploadImage();
    }


    //서버호출
    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();

                        //Showing toast message of the response(s: 서버로 부터 받은 결과값)
                        downloadData = s;

                        //그림경로
                        tV.setText(s);

                        //경로를 이용하여 그림을 glide로 띄움
                        glideImage(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(Download.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {



                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("request", "need_data");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void glideImage(String url){
        Glide.with(this)
                .load(url)
                .thumbnail(0.3f)
                .into(iV);
    }
}

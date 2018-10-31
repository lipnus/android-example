package com.lipnus.kum_volleysystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Sunpil on 2017-02-26.
 */

public class VolleyConnect {

    //resonse 리스너(즉각 결과가 나오는게 아니고, 접속이 끝나야 결과가 나오므로 호출하는 곳에서는 리스너로 받아야 한다)
    IVolleyResult mResultCallback = null;

    Context context; //이걸 호출한 곳의컨텍스트
    private String UPLOAD_URL; //접속주소
    private Map<String,String> params = new Hashtable<String, String>(); //post할 데이터들을 모아놓은 MAP

    //생성자
    VolleyConnect(IVolleyResult resultCallback, Context context, String url, Map<String, String> parmas){
        mResultCallback = resultCallback;
        this.context = context;
        this.UPLOAD_URL = url;
        this.params = parmas;

        ConnectServer();
    }

    //volley를 이용하여 서버에 접속
    private void ConnectServer(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(context,"Uploading...","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();

                        //결과값은 s;

                        //결과값을 리스너를 통해 전달
                        if(mResultCallback != null){
                            mResultCallback.notifySuccess(s);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(context, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                        //결과값을 리스너를 통해 전달
                        if(mResultCallback != null){
                            mResultCallback.notifyError(volleyError);
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //보내는 부분

                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}

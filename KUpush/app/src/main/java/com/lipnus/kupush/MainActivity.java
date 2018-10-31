package com.lipnus.kupush;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView logText;
    TextView regIdText;

    String regId;

    RequestQueue queue;
    TextView receivedText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logText = (TextView) findViewById(R.id.log);
        regIdText = (TextView) findViewById(R.id.regTv);
        receivedText = (TextView) findViewById(R.id.receiveTv);
        getRegId();

        Button btn = (Button) findViewById(R.id.sendButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPush("");
            }
        });

        queue = Volley.newRequestQueue(getApplicationContext());
    }

    private void sendPush(String input){

        String url = "https://fcm.googleapis.com/fcm/send";

        JSONObject requestData = new JSONObject();
        try{
            requestData.put("priority", "high");

            JSONObject dataObj = new JSONObject();
            dataObj.put("sender", "010-1000-1000");
            dataObj.put("contents", input);
            requestData.put("data", dataObj);

            JSONArray idArray = new JSONArray();
            idArray.put(0, regId);
            requestData.put("registration_ids", idArray);

        }catch(Exception e){}




        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestData,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        println("요청에 대한 응답 : " + response.toString());
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("응답에러 :"+ error.getMessage());
                    }
                }
        ){
            @Override
            public String getPostBodyContentType() {
                return "application/json";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>(String, String);
                return params;
            }

            //======================================================================================
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Authorization", "AAAAC8CljTk:APA91bGcNMWKhEPOXhLn97z4k9LL2hT_ErP3byB1mShXKmi_mbNlMz109tMgcGQJOpntpcnUQtBmaq4GjNP8xaWoD7VdScdOzjyk9DuH2y5GKVQmHjwT5sbJaSWldI6UWMpw5wmHx252");
                return headers;
            }



        };
        request.setShouldCache(false);
        queue.add(request);
        println("전송요청함");

    }

    private void getRegId(){
        println("등록ID요청함");
        regId = FirebaseInstanceId.getInstance().getToken();

        if(regId != null){
            regIdText.setText(regId);
        }

    }

    public void println(String data){
        logText.append(data + "\n");
    }
}

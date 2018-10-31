package com.lipnus.kusocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    EditText editText2;


    //사용자가 입력하는 서버정보
    String host;
    int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });

    }
    public void request(){
        //volley사용
        String url = "http://10.16.33.250:7002/";

        StringRequest request = new StringRequest(
                Request.Method.GET, //겟인지 포스트인지
                url, //접속주소
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답받음" + response);

                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러발생" + error.getMessage());
                    }
                }
        ){
            //전송시에 구분자를 보내는 것
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("data_type", "coustomer");

                return params;
            }
        };

        request.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request); //요청이 날라감
        println("요청보냄");

    }

    public void processResponse(String data){
        //JSON 파싱(gson 이용)
        Gson gson = new Gson();
        CustomerList customerList = gson.fromJson(data, CustomerList.class);

        println("고객의 수: " + customerList.data.size());
        for(int i=0; i<customerList.data.size(); i++){
            println("고객#" + i);

            Customer customer = customerList.data.get(i);
            println("이름: " + customer.name);
            println("나이: " + customer.age);
            println("전화번호: " + customer.mobile);
        }
    }

    public void println( String data){ //data가 run안쪽에서 접근이 가능하지 않기때문에 final사용
        textView.append(data + "\n");
    }


}

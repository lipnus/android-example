package com.lipnus.lipnus_newwork_model;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        //요청버튼을 클릭
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
        String url = "http://kucomputer.ivyro.net/kumchurk/getdata.php";

        StringRequest request = new StringRequest(
                Request.Method.POST, //GET방식인지 POST방식인지
                url, //접속주소
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답내용");
                        println(response);
                        //processResponse(response);
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
        /*

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

        */
    }

    public void println( String data){ //data가 run안쪽에서 접근이 가능하지 않기때문에 final사용
        textView.append(data + "\n");
    }
}

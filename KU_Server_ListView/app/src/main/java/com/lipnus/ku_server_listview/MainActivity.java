package com.lipnus.ku_server_listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

import static java.sql.DriverManager.println;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    ListViewAdapter adapter;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadData();
            }
        });


    }

    public void downloadData(){
        //volley사용
        String url = "http://10.16.33.250:7002/";

        StringRequest request = new StringRequest(
                Request.Method.GET, //겟인지 포스트인지
                url, //접속주소
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //println("응답받음" + response);

                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //println("에러발생" + error.getMessage());
                        Toast.makeText(getApplicationContext(), "에러발생", Toast.LENGTH_LONG).show();
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
        JsonItemList customerList = gson.fromJson(data, JsonItemList.class);

        println("고객의 수: " + customerList.data.size());
        for(int i=0; i<customerList.data.size(); i++){
            JsonItem customer = customerList.data.get(i);
            //리스트뷰에 추가
            adapter.addItem(customer.name, customer.age, customer.mobile);
            adapter.notifyDataSetChanged();

        }
    }
}

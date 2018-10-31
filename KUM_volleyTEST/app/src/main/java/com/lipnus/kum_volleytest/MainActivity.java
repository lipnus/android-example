package com.lipnus.kum_volleytest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
    }



    //서버에 요청청
   public void request(){

       //volley사용
       String url = "http://kucomputer.ivyro.net/kumchurk/getdata.php"; //3개의 데이터를 input으로 받는 테스트페이지

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
               params.put("data1", "아영");
               params.put("data2", "조현");
               params.put("data3", "조이");

               return params;
           }
       };

       request.setShouldCache(false);

       RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
       queue.add(request); //요청이 날라감
       println("요청보냄");

    }



    public void println( String data){
        textView.append(data + "\n");
    }

    public void onClick(View v){
        Log.d("VOVO", "request()호출");
        request();
    }

    public void onClick2(View v){
        makeJson();
    }

    public void onClick3(View v){
        request2();
    }

    //서버에서 json받아서 표시하기
    public void request2(){

        //volley사용
        String url = "http://kucomputer.ivyro.net/kumchurk/jsontest.php"; //json을 출력해주는 페이지

        StringRequest request = new StringRequest(
                Request.Method.POST, //GET방식인지 POST방식인지
                url, //접속주소
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답내용");
                        println(response);
                        gsonData(response); //받은json을 java어?로 바꿈
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
                params.put("data1", "아영");
                params.put("data2", "조현");
                params.put("data3", "조이");

                return params;
            }
        };

        request.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request); //요청이 날라감
        println("요청보냄");

    }

    public void makeJson(){
        Gson gson = new Gson();

//        FoodInfo_list foodList = new FoodInfo_list();
        ArrayList<FoodInfo> foodList = new ArrayList<>();

        foodList.add(new FoodInfo("메뉴이름", "식당이름", "가격", "주소"));
        foodList.add(new FoodInfo("메뉴이름2", "식당이름2", "가격2", "주소2"));
        foodList.add(new FoodInfo("메뉴이름3", "식당이름3", "가격3", "주소3"));
        foodList.add(new FoodInfo("메뉴이름4", "식당이름4", "가격4", "주소4"));
        String result = gson.toJson(foodList);
        println(result);
        Log.d("GSGS", ":"+result);
        gsonData(result);
    }


    public void gsonData(String data){

        //JSON 파싱(gson 이용)
        Gson gson = new Gson();
        FoodInfo[] foodList = gson.fromJson(data, FoodInfo[].class);

        println("메뉴의 수: " + foodList.length );

        for(int i=0; i<foodList.length; i++){
            println("메뉴#" + i);

            FoodInfo foodinfo = foodList[i];
            println("메뉴이름: " + foodinfo.menuName);
            println("가게이름: " + foodinfo.resName);
            println("가격: "+ foodinfo.price);
            println("이미지경로: " + foodinfo.imgUrl);
        }

    }
}

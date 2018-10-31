package com.lipnus.kudatabase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    SQLiteDatabase database;

    EditText eTv, eTv2, eTv3;

    ListView listview;
    ListViewAdapter adapter;

    private static int sequence = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textview);

        eTv = (EditText) findViewById(R.id.edittext);
        eTv2 = (EditText) findViewById(R.id.edittext2);
        eTv3 = (EditText) findViewById(R.id.edittext3);

        //리스트뷰 초기화

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);



        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDataBase();
            }
        });

        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTable();
            }
        });

        Button btn3 = (Button)findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = eTv.getText().toString();
                int age = Integer.parseInt(eTv2.getText().toString());
                String mobile = eTv3.getText().toString();


                //DB에 추가
                insertData(name, age, mobile);

                //리스트뷰에 추가
                adapter.addItem(name, age, mobile);
                adapter.notifyDataSetChanged();
            }
        });

        Button btn4 = (Button)findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectData();
            }
        });

        Button btn5 = (Button)findViewById(R.id.button5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTime();
            }
        });
    }

    //현재시간으로 데이터를 처리
    public void checkTime() {
        /*
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date();
        String curDateStr = format.format(curDate);
        println("현재시간: " + curDateStr);

        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMDDHHmmss");
        String curDateStr2 = format2.format(curDate);

        String seq

        String curValue = curDateStr2 + sequence++;

        String filename = "capture_" + curValue + ".jpg";
        */
    }

    public void selectData(){
        String sql = "SELECT name, age, mobile FROM customer";

        try {
            Cursor cursor = database.rawQuery(sql, null);
            for(int i=0; i<cursor.getCount(); i++){
                cursor.moveToNext();

                String name = cursor.getString(0);
                int age = cursor.getInt(1);
                String mobile = cursor.getString(2);

                println("데이터 #"+i + " : " + name + ", "+ age + ", " + mobile);
            }

            println("데이터 조회완료");
        }catch (Exception e){
            e.printStackTrace();
            println("데이터 조회 시 에러발생");
        }

    }

    //데이터삽입
    public void insertData(String name, int age, String mobile){

        Object[] params = new Object[3];
        params[0] = name;
        params[1] = age;
        params[2] = mobile;

        String sql = "INSERT INTO customer(name, age, mobile) VALUES (?,?,?)";

        try {
            database.execSQL(sql, params);
            println("데이터를 추가했습니다");

        }catch (Exception e){
            e.printStackTrace();
            println("데이터 추가 시 에러발생");
        }
    }

    //테이블생성
    public void createTable(){
        String sql = "CREATE TABLE customer(name text, age integer, mobile text)";

        try {
            database.execSQL(sql);
            println("테이블생성");
        }catch (Exception e){
            e.printStackTrace();
            println("테이블 생성 시 에러발생");
        }


    }


    public void openDataBase(){
        String databaseName = "customer";

        try {
            database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
            println("데이터베이스 오픈됨 : " + databaseName);
        }catch (Exception e){
            e.printStackTrace();
            println("데이터베이스 오픈 시 에러발생");
        }
    }

    public void println(String data){
        textView.append(data + "\n");
    }
}

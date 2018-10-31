package com.lipnus.scheduleapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LogIn extends AppCompatActivity {

    //Preference선언 (한번 로그인이 성공하면 자동으로 처리하는데 이용)
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    EditText edit_id;
    EditText edit_pw;

    //뒤로버튼 3초안에 2번누르면 종료
    private final long	FINSH_INTERVAL_TIME    = 3000;
    private long		backPressedTime        = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Prefrence설정
        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

        edit_id = (EditText) findViewById(R.id.edit_id);
        edit_id.setNextFocusDownId(R.id.edit_pw); //'다음'을 누르면 비밀번호로 포커스 이동
        edit_pw = (EditText) findViewById(R.id.edit_pw);


        //자동로그인 체크 & 자동접속(false는 저장된 값이 없을경우 출력하는 초깃값을 의미)
        //==========================================================================================
        if(setting.getBoolean("Auto_Login_enabled", false)){

            //Toast.makeText(this, "저장된 ID: "+setting.getString("ID", "") +
            //        "\n저장된 PW: " +setting.getString("PW", ""), Toast.LENGTH_LONG).show();

            edit_id.setText( setting.getString("ID", "") );
            edit_id.setNextFocusDownId(R.id.edit_pw); //'다음'을 누르면 비밀번호로 포커스 이동

            edit_pw.setText( setting.getString("PW", "") );


            //자동접속시에도 커스텀어플리케이션에 아이디를 저장해 놓는다.(서버에 접속할때 씀)
            CustomApplication.userID = setting.getString("ID", "");

            //Today Activity를 호출한다
            Intent iT = new Intent(this, Today.class);
            //iT.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(iT);
        }
        //==========================================================================================



    }

    //로그인버튼 터치
    public void onClick_Login(View v){

        String ID = edit_id.getText().toString();
        String PW = edit_pw.getText().toString();

        //서버에 접속해서 확인
        connectServer(ID, PW);

    }

    //로그인결과(성공:login 아이디이상:id_error 비밀번호이상:password_error)
    public void loginResult(String loginResultStr){

        String ID = edit_id.getText().toString();
        String PW = edit_pw.getText().toString();

        //로그인성공
        if(loginResultStr.equals("login")){
            Toast.makeText(this, "로그인 성공", Toast.LENGTH_LONG).show();

            //Preference에 ID와 PW를 저장
            editor.putString("ID", ID);
            editor.putString("PW", PW);
            editor.putBoolean("Auto_Login_enabled", true);
            editor.commit();

            //커스텀어플리케이션에 아이디를 저장해 놓는다.(서버에 접속할때 씀)
            CustomApplication.userID = ID;

            //Today Activity를 호출한다
            Intent iT = new Intent(this, Today.class);
            //iT.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(iT);
        }

        //로그인실패
        else{

            if(loginResultStr.equals("id_error")){
                Toast.makeText(this, "ID를 확인해주세요", Toast.LENGTH_LONG).show();
            }
            else if(loginResultStr.equals("password_error")){
                Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_LONG).show();
            }

            //자동로그인 Preference 초기화
            editor.clear();
            editor.commit();
        }
    }



    /**=============================================================================================
     *
     * AyncTask를 이용하여 웹에 접속
     *
     * =============================================================================================
     */
    private void connectServer(String id, String password){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;



            @Override //시작
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(LogIn.this, "Loading..", null, true, true);
            }

            @Override //끝
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();
                loginResult(s);
            }

            @Override //메인루프
            protected String doInBackground(String... params) {

                try{
                    String id = (String)params[0];
                    String password = (String)params[1];

                    //접속할 주소
                    String link = CustomApplication.serverPath;
                    link = link + "app/login.php";


                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();



                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        //AyacTask객체를 만들고 실행시킨다
        InsertData task = new InsertData();
        task.execute(id,password);
    }//connecttoserver클래스


    //==============================================================================================
    //뒤로버튼 눌렀을때 앱종료
    //==============================================================================================
    @Override
    public void onBackPressed() {
        long tempTime        = System.currentTimeMillis();
        long intervalTime    = tempTime - backPressedTime;

        if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
            //3초안에 한번 더 누르면 종료
            ActivityCompat.finishAffinity(this);
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(),"'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }
}

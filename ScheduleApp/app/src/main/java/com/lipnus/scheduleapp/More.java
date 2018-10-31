package com.lipnus.scheduleapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class More extends AppCompatActivity {

    //하단메뉴의 버튼객체
    Button todayBtn;
    Button wbsBtn;
    Button iaBtn;
    Button testBtn;
    Button moreBtn;

    //팝업으로 뜨는 비밀번호 수정창
    LinearLayout changePassLinear;

    //팝업으로 뜨는 비밀번호 수정창의 EditText
    EditText oldpass;
    EditText newpass;
    EditText confirmpass;

    //뒤로버튼 3초안에 2번누르면 종료
    private final long	FINSH_INTERVAL_TIME    = 3000;
    private long		backPressedTime        = 0;

    //Preference선언 (접속되어있는 ID를 이용해서 회원정보를 수정)
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    //유저의 아이디,비밀번호
    String userID;
    String userPW;

    //아이디표시
    TextView idTv;

    //이름표시(수정가능)
    EditText nameEt;

    //팝업창의 OldPassword, ConfirmPassword (틀리면 빨간색으로 바뀜)
    TextView oldpassTv;
    TextView confirmTv;

    //서버로부터 받은 이름 데이터
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);


        //하단메뉴 버튼설정
        //===========================================================
        todayBtn = (Button) findViewById(R.id.todayBtn);
        wbsBtn = (Button)findViewById(R.id.wbsBtn);
        iaBtn = (Button)findViewById(R.id.iaBtn);
        testBtn = (Button)findViewById(R.id.testBtn);
        moreBtn = (Button)findViewById(R.id.moreBtn);

        todayBtn.setBackgroundResource(R.drawable.todaybutton);
        wbsBtn.setBackgroundResource(R.drawable.wbsbutton);
        iaBtn.setBackgroundResource(R.drawable.iabutton);
        testBtn.setBackgroundResource(R.drawable.testbutton);
        moreBtn.setBackgroundResource(R.drawable.moreclick);
        //===========================================================

        //아이디표시
        idTv = (TextView)findViewById(R.id.useridTv);

        //이름표시(수정가능)
        nameEt = (EditText)findViewById(R.id.nameEt);

        //Oldpassword, Confirm TextView
        oldpassTv = (TextView)findViewById(R.id.oldpassTv);
        confirmTv = (TextView)findViewById(R.id.conformPasswordTv);

        //비밀번호 수정팝업 레이아웃
        changePassLinear = (LinearLayout) findViewById( R.id.passwordChangeLinear);

        //비밀버호 수정창의 EditText
        oldpass = (EditText) findViewById(R.id.edit_oldPass);
        oldpass.setNextFocusDownId(R.id.edit_newPass);
        newpass = (EditText) findViewById(R.id.edit_newPass);
        newpass.setNextFocusDownId(R.id.edit_confirmPass);
        confirmpass = (EditText) findViewById(R.id.edit_confirmPass);

        //프레퍼런스에 저장되어 있는 ID값 불러오기
        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();
        userID = setting.getString("ID", "");
        userPW = setting.getString("PW", "");

        //유저의 아이디을 표시
        idTv.setText(userID);

        //유저의 이름을 표시(초깃값)
        connectServer(userID, "whatisyourname", userPW);



        //비밀번호 입력이 틀렸음을 실시간으로 알려줌
        oldpass.addTextChangedListener(new TextWatcher()
        {

            public void afterTextChanged(Editable s)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {
                // TODO Auto-generated method stub
                //비밀번호 맞음
                if(userPW.equals(oldpass.getText().toString()) ){
                    oldpassTv.setTextColor(Color.parseColor("#000000"));
                }else{ //틀림
                    oldpassTv.setTextColor(Color.parseColor("#c11111"));
                }

            }

        });

        //새 비밀번호 입력이 틀렸음을 실시간으로 알려줌1
        newpass.addTextChangedListener(new TextWatcher()
        {

            public void afterTextChanged(Editable s)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {
                // TODO Auto-generated method stub
                //비밀번호 맞음
                if(confirmpass.getText().toString().equals( newpass.getText().toString() ) ){
                    confirmTv.setTextColor(Color.parseColor("#000000"));
                }else{ //틀림
                    confirmTv.setTextColor(Color.parseColor("#c11111"));
                }

            }

        });

        //새 비밀번호 입력이 틀렸음을 실시간으로 알려줌2
        confirmpass.addTextChangedListener(new TextWatcher()
        {

            public void afterTextChanged(Editable s)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {
                // TODO Auto-generated method stub
                Log.d("AAAA", "바뀜");
                //비밀번호 맞음
                if(confirmpass.getText().toString().equals( newpass.getText().toString() ) ){
                    confirmTv.setTextColor(Color.parseColor("#000000"));
                }else{ //틀림
                    confirmTv.setTextColor(Color.parseColor("#c11111"));
                }

            }

        });

    }


    //LOGOUT버튼 터치
    public void onClick_logout(View v){

        //자동로그인 해제
        editor.putBoolean("Auto_Login_enabled", false);
        editor.commit();

        Intent iT = new Intent(this, LogIn.class);
        iT.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(iT);
    }

    //PasswordCange터치
    public void onClick_changePassword(View v){
        oldpass.setText("");
        newpass.setText("");
        confirmpass.setText("");

        changePassLinear.setVisibility( View.VISIBLE );
    }

    //비밀번호 변경 팝업창 취소(반투명한 검은부분 터치)
    public void onClick_pasChangeCancel(View v){
        //키보드 제거
        InputMethodManager im = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        changePassLinear.setVisibility( View.GONE );
    }

    //비밀번호 팝업창의 Done 버튼을 터치
    public void onClick_passChangeDone(View v){

        String oldStr, newStr, confirmStr;
        oldStr = oldpass.getText().toString();
        newStr = newpass.getText().toString();
        confirmStr = confirmpass.getText().toString();


        if( userPW.equals(oldStr) && newStr.equals( confirmStr ) ){

            //조건이 충족되어 새 비밀번호로 바꾸기(웹)
            connectServer(userID, userName, newStr);

            //프리퍼런스값 교체(다음번에도 걍 자동로그인)
            editor.putString("PW", newStr);
            editor.commit();

            userPW = newStr;

            Toast.makeText(getApplicationContext(), "비밀번호가 변경 되었습니다", Toast.LENGTH_LONG).show();
            changePassLinear.setVisibility( View.GONE );//창 닫기
        }
        else{
            //조건이 불만족(어디가 잘못되었는지 빨간색으로 표시되므로 따로 설명안해줘도 됨)
            Toast.makeText(getApplicationContext(), "입력한 값이 올바른지 확인해주세요" , Toast.LENGTH_LONG).show();
        }


        //키보드 제거
        InputMethodManager im = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

    }

    //save버튼
    public void onClick_save(View v){
        connectServer(userID, nameEt.getText().toString(), userPW);
        Toast.makeText(getApplicationContext(), "Name이 저장되었습니다", Toast.LENGTH_LONG).show();
    }








    /**=============================================================================================
     *
     * AyncTask를 이용하여 웹에 접속
     *
     * =============================================================================================
     */
    private void connectServer(String input_id, String input_name, String input_password){

        class InsertData extends AsyncTask<String, Void, String> {

            @Override //시작
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override //끝
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                takeResult(s);
            }

            @Override //메인루프
            protected String doInBackground(String... params) {

                try{
                    String id = (String)params[0];
                    String name = (String)params[1];
                    String password = (String)params[2];

                    //접속할 주소
                    String link = CustomApplication.serverPath;
                    link = link + "app/changeinfo.php";

                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
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
        InsertData task2 = new InsertData();
        task2.execute(input_id, input_name, input_password);

    }


    public void takeResult(String s){
        //Toast.makeText(getApplicationContext(), ""+s, Toast.LENGTH_LONG).show();
        Log.d("AAAA", s);

        userName = s;

        //이름을 표시해준다
        nameEt.setText(s);

    }




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





    //==============================================================================================
    //하단 메뉴
    //==============================================================================================
    public void onClick_menu(View v){

        CustomApplication CusApp = (CustomApplication) getApplication();
        CusApp.controlBottomMenu(v.getId());
        overridePendingTransition(R.anim.noting, R.anim.noting);
    }
}
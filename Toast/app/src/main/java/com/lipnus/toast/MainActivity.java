package com.lipnus.toast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tV = (TextView) findViewById(R.id.textview);
    }

    public void onClick(View v){

        //레이아웃 인플레이터 객체 참조
        LayoutInflater inflater = getLayoutInflater();

        //토스트를 위한 레이아웃 인플레이션
        View layout = inflater.inflate(R.layout.img_toast, (ViewGroup)findViewById(R.id.toast_layout_root));

        //토스트 객체 생성
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, 0, 600);
        toast.setDuration(Toast.LENGTH_LONG);

        toast.setView(layout);
        toast.show();

    }

    public void onClick_AlertDialog(View v){

        //createDialogBox()메소드를 호출하여 대화상자 객체 생성
        AlertDialog dialog = createDialogBox();

        //대화상자 보여주기
        dialog.show();
    }

    private AlertDialog createDialogBox(){

        //Alert Dialog에는 Builder라는게 있어서 쉽게 할 수 있다고 얼핏 본 기억이 나는군
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //대화상자 타이틀, 메시지, 아이콘 설정
        builder.setTitle("제목");
        builder.setMessage("종료하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("예!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tV.setText("예 누름    코드: " + Integer.toString(which));
            }
        });

        builder.setNeutralButton("취소!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tV.setText("취소누름    코드: " + Integer.toString(which));
            }
        });

        builder.setNegativeButton("아니오!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tV.setText("아니오    코드: " + Integer.toString(which));
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }
}

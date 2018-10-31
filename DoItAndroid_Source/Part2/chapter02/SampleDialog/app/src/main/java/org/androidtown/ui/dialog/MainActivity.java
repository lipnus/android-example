package org.androidtown.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * 간단한 알림 대화상자를 만들어 보여주는 방법을 볼 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {
    TextView textView1;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.textView1);

    }

    /**
     * 버튼을 눌렀을 때 대화상자 객체를 생성하는 메소드를 호출
     * @param v
     */
    public void onButton1Clicked(View v) {
        AlertDialog dialog = createDialogBox();
        dialog.show();
    }


    /**
     * 대화상자 객체 생성
     */
    private AlertDialog createDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("안내");
        builder.setMessage("종료하시겠습니까?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // 예 버튼 설정
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                msg = "예 버튼이 눌렀습니다. " + Integer.toString(whichButton);
                textView1.setText(msg);
            }
        });

        // 취소 버튼 설정
        builder.setNeutralButton("취소",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                msg = "취소 버튼이 눌렸습니다. " + Integer.toString(whichButton);
                textView1.setText(msg);
            }
        });

        // 아니오 버튼 설정
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                msg = "아니오 버튼이 눌렸습니다. " + Integer.toString(whichButton);
                textView1.setText(msg);
            }
        });

        // 빌더 객체의 create() 메소드 호출하면 대화상자 객체 생성
        AlertDialog dialog = builder.create();

        return dialog;
    }

}

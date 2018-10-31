package org.androidtown.toast.shape;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 토스트 메시지의 모양을 바꾸어 띄우는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButton1Clicked(View v) {
        // 토스트의 모양을정의하고 있는 레이아웃을 인플레이션합니다.
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(
                R.layout.toastborder,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        // 레이아웃의 텍스트뷰에 보여줄 문자열을 설정합니다.
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Hello My Android!");

        // 토스트 객체를 만듭니다.
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, 0, -100);
        toast.setDuration(Toast.LENGTH_SHORT);

        // 토스트에 뷰를 설정합니다. 이 뷰가 토스트로 보여집니다.
        toast.setView(layout);
        toast.show();
    }

}

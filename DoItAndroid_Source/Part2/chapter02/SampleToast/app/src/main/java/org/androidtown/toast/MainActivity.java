package org.androidtown.toast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 토스트의 위치를 원하는 곳에 두고 보여주는 방법을 알 수 있습니다.
 *
 * @author Mike
 */
public class MainActivity extends AppCompatActivity {

    EditText editText1;
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

    }

    public void onButton1Clicked(View v) {
        try {
            Toast toastView = Toast.makeText(this,
                    "Hello Android!",
                    Toast.LENGTH_LONG);

            int xOffset = Integer.valueOf(editText1.getText().toString());
            int yOffset = Integer.valueOf(editText2.getText().toString());

            // 입력된 x, y offset 값을 이용해 위치를 지정합니다.
            toastView.setGravity(Gravity.CENTER, xOffset, yOffset);

            toastView.show();

        } catch (NumberFormatException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}

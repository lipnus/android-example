package org.androidtown.ui.linearlayout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 자바 코드를 이용해 레이아웃을 만드는 방법을 알 수 있습니다.
 * 
 * @author Mike
 *
 */
public class SampleLayoutCodeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout mainLayout = new LinearLayout(this);
        
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	        LinearLayout.LayoutParams.MATCH_PARENT,
	        LinearLayout.LayoutParams.WRAP_CONTENT);
        
        Button button01 = new Button(this);
        button01.setText("Button 01");
        button01.setLayoutParams(params);
        mainLayout.addView(button01);
        
        setContentView(mainLayout);
    }
 
}

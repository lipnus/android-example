package org.androidtown.ui.anim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 뷰에 애니메이션을 적용하는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 */
public class MainActivity extends AppCompatActivity {

    Animation flowAnim;
    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // anim 폴더 안에 정의되어 있는 애니메이션 액션 정보를 로딩합니다.
        flowAnim = AnimationUtils.loadAnimation(this, R.anim.flow);

        textView1 = (TextView) findViewById(R.id.textView1);

    }

    public void onButton1Clicked(View v) {
        // 애니메이션 리스너 등록
        flowAnim.setAnimationListener(new FlowAnimationListener());
        // 애니메이션 시작
        textView1.startAnimation(flowAnim);
    }

    /**
     * 애니메이션이 끝나는 시점을 알기 위해 애니메이션 리스너를 정의합니다.
     */
    private final class FlowAnimationListener implements Animation.AnimationListener {

        /**
         * 애니메이션이 끝날 때 자동 호출됨
         */
        public void onAnimationEnd(Animation animation) {
            Toast.makeText(getApplicationContext(), "애니메이션 종료됨.", Toast.LENGTH_LONG).show();
        }

        /**
         * 애니메이션이 반복될 때 자동 호출됨
         */
        public void onAnimationRepeat(Animation animation) {
        }

        /**
         * 애니메이션이 시작할 때 자동 호출됨
         */
        public void onAnimationStart(Animation animation) {
        }

    }

}

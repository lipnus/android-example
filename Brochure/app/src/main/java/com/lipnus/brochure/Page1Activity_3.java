package com.lipnus.brochure;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Page1Activity_3 extends FragmentActivity {

    int count=0;

    //현재액티비티
    public static Activity page1Activity_3;

    //버튼객체를 배열로 선언가능하네
    Button[] btn = new Button[14];

    //애니매이션
    Animation comein;
    Animation fadeout;

    //클릭한 네모에 대한 설명을 띄워주는 레이아웃 박스
    LinearLayout LL;

    //그 박스 안의 객체들
    ImageView iV;
    TextView tV;

    //다음스테이지로
    Button nextBtn;

    //버튼터치해주세요
    TextView plzTouchtV;

    //몇개 둘러봐야 다음페이지로 간다
    int clickCount = 0;
    boolean chamOK = false; //참깨라면은 힌트이기 때문에 반드시 봐야함

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1_3);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //현재 액티비티를 변수에 넣음
        page1Activity_3 = Page1Activity_3.this;

        //애니매이션
        comein = AnimationUtils.loadAnimation(this, R.anim.comein);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        //버튼터치해주세요
        plzTouchtV = (TextView) findViewById(R.id.plzTouchBtn);

        //레이아웃 박스
        LL = (LinearLayout) findViewById(R.id.linearLayoutBox);

        //레이아웃 박스 안의 객체
        tV = (TextView) findViewById(R.id.boxTextView);
        iV = (ImageView) findViewById(R.id.boxImgView);

        //다음스테이지 버튼
        nextBtn = (Button) findViewById(R.id.nextBtn);

        //버튼
        btn[0] = (Button)findViewById(R.id.AA0);
        btn[1] = (Button)findViewById(R.id.AA1);
        btn[2] = (Button)findViewById(R.id.AA2);
        btn[3] = (Button)findViewById(R.id.AA3);

        btn[4] = (Button)findViewById(R.id.A0);
        btn[5] = (Button)findViewById(R.id.A1);
        btn[6] = (Button)findViewById(R.id.A2);
        btn[7] = (Button)findViewById(R.id.A3);

        btn[8] = (Button)findViewById(R.id.B0);
        btn[9] = (Button)findViewById(R.id.B1);
        btn[10] = (Button)findViewById(R.id.B2);

        btn[11] = (Button)findViewById(R.id.C0);
        btn[12] = (Button)findViewById(R.id.C1);

        btn[13] = (Button)findViewById(R.id.D0);

        //시작할때 전부 안보이게 해놓음
        for(int i=0; i<14; i++){
            btn[i].startAnimation(comein);
        }




    }

    //다음 페이지로
    public void onClick(View v){

        Toast.makeText(this, "페이지를 넘겨주세요!!", Toast.LENGTH_LONG).show();
        Intent iT = new Intent(this, Page2Activity.class);
        startActivity(iT);
    }


    public void onClick_beat(View v) {

        //가려진 박스 보이기
        LL.setVisibility(View.VISIBLE);

        //기본글씨는 검정
        tV.setTextColor(Color.BLACK);

        //몇변 구경했는지 센다
        clickCount++;



        //첫줄
        switch (v.getId()) {

            case R.id.A0: {

                break;
            }
            case R.id.A1: {
                //어쿠스틱 콜라보
                LL.setBackgroundColor(Color.rgb(139,6,6));
                iV.setImageResource(R.drawable.nero);
                tV.setTextColor(Color.WHITE);
                tV.setText("감명깊게 본 웹툰 \n네로의 실험실");
                LL.startAnimation(fadeout);
                break;
            }
            case R.id.A2: {
                LL.setBackgroundColor(Color.rgb(161,224,156));
                iV.setImageResource(R.drawable.mint);
                tV.setText("민트초코칩 \n좋아하는 맛. 치약맛!");
                LL.startAnimation(fadeout);
                break;
            }
            case R.id.A3: { //구림낀 날씨
                LL.setBackgroundColor(Color.rgb(0,0,0));
                iV.setImageResource(R.drawable.cloudy);
                tV.setTextColor(Color.WHITE);
                tV.setText("햇빛 쨍쨍한 날보다 구름끼고 바람부는 날씨");
                LL.startAnimation(fadeout);
                break;
            }


            //두번째줄
            case R.id.B0: {
                break;
            }
            case R.id.B1: {
                //어쿠스틱 콜라보
                LL.setBackgroundColor(Color.rgb(215,54,103));
                iV.setImageResource(R.drawable.acoustic);
                tV.setTextColor(Color.WHITE);
                tV.setText("어쿠스틱 콜라보 \n좋아하는 가수");
                LL.startAnimation(fadeout);
                break;
            }
            case R.id.B2: {
                LL.setBackgroundColor(Color.rgb(82,84,227));
                iV.setImageResource(R.drawable.ice);
                tV.setTextColor(Color.WHITE);
                tV.setText("얼음을 좋아해서 음료를 마신 뒤 남은 얼음을 아작아작");
                LL.startAnimation(fadeout);

            }


            //세번째줄
            case R.id.C0: {
                break;
            }
            case R.id.C1: { //참깨라면
                chamOK = true;
                LL.setBackgroundColor(Color.rgb(255,238,2));
                iV.setImageResource(R.drawable.chamggae);
                tV.setText("참깨라면의 맛에는 우주의 진리가 담겨있다");
                LL.startAnimation(fadeout);


                break;
            }


            //네번째줄줄
            case R.id.D0: {
                break;
            }
        }

        //네번 이상 클릭하면 다음으로 갈 수 있음
        if(clickCount > 3 && chamOK == true){
            plzTouchtV.setVisibility(View.INVISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }


}
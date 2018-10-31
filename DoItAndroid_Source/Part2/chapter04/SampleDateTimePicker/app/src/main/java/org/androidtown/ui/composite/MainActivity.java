package org.androidtown.ui.composite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 날짜와 시간을 한꺼번에 선택할 수 있는 복합위젯을 만드는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");

    //텍스트뷰와 DataeTimePicker객체 선언
    TextView textView1;
    DateTimePicker dateTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView)findViewById(R.id.textView1);
        dateTimePicker = (DateTimePicker)findViewById(R.id.dateTimePicker);



        // 현재 시간 텍스트뷰에 표시(일단 기본적으로 한번 표시해 주는듯)
        Log.i("Fuck", "날짜 초기화");
        Calendar calendar = Calendar.getInstance();
        calendar.set(dateTimePicker.getYear(), dateTimePicker.getMonth(), dateTimePicker.getDayOfMonth(), dateTimePicker.getCurrentHour(), dateTimePicker.getCurrentMinute());
        textView1.setText(dateFormat.format(calendar.getTime()));


        // DateimePicker 리스너이벤트 처리
        dateTimePicker.setOnDateTimeChangedListener(new DateTimePicker.OnDateTimeChangedListener() {

            //날짜가 바뀌었을때 다시 표시해 주는 듯
            public void onDateTimeChanged(DateTimePicker view, int year, int monthOfYear, int dayOfYear, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();//달력객체 설정? Calender.getInstance()가 현재 날짜 받아오는건가
                calendar.set(year, monthOfYear, dayOfYear, hourOfDay, minute); //

                // 바뀐 시간을 텍스트뷰에 표시(시간을 딱 형식에 맞게 표시해 주는듯?)
                textView1.setText(dateFormat.format(calendar.getTime()));

                Log.i("Fuck", "변경된 값을 화면에 표시");
            }
        });



    }

}

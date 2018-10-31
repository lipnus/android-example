package com.lipnus.challenge8;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 그리드뷰를 이용해 월별 캘린더를 만드는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 */
public class MainActivity extends AppCompatActivity {

    //액션바
    ActionBar actionBar;

    //해쉬맵
    HashMap hashMap;

    //해당 날짜의 스케줄이 있을때
    Boolean scheduledOK = false;








    /**
     * 월별 캘린더 뷰 객체
     */
    CalendarMonthView monthView;

    /**
     * 월별 캘린더 어댑터
     */
    CalendarMonthAdapter monthViewAdapter;

    /**
     * 월을 표시하는 텍스트뷰
     */
    TextView monthText;

    //현재 년월일
    int curYear;
    int curMonth;
    int curDay;

    //일정관리 레이아웃
    LinearLayout topLayout;
    TextView showDateTV;
    EditText getHourEdit;
    EditText getMinEdit;
    EditText getDataEdit;

    //리스트뷰와 어댑터
    ListView listView;
    IconTextListAdapter adapter;


    //정보표시(임시)
    TextView showInformationTV;

    //현재위치
    int CurPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topLayout = (LinearLayout)findViewById(R.id.topLayout);
        showDateTV = (TextView)findViewById(R.id.showDate);
        showInformationTV = (TextView)findViewById(R.id.showInformationTV);

        getDataEdit = (EditText)findViewById(R.id.editData);
        getHourEdit =(EditText)findViewById(R.id.editHour);
        getMinEdit=(EditText)findViewById(R.id.editMin);

        //해쉬맵 생성
        hashMap = new HashMap();



//리스트뷰
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
        // 리스트뷰 객체 참조
        listView = (ListView) findViewById(R.id.listView);

        // 어댑터 객체 생성
        adapter = new IconTextListAdapter(this);

        // 아이템 데이터 만들기(이러한 형식을 이용하면 됨)
        //adapter.addItem(new IconTextItem("18시 18분", "빵셔틀"));

        // 리스트뷰에 어댑터 설정
        listView.setAdapter(adapter);

        // 새로 정의한 리스너로 객체를 만들어 설정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IconTextItem curItem = (IconTextItem) adapter.getItem(position);
                String[] curData = curItem.getData();

                Toast.makeText(getApplicationContext(), "Selected : " + curData[0], Toast.LENGTH_LONG).show();

            }

        });
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
//■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■


        //==========================================================================================
        // 액션바 객체를 참조할 때는 getActionBar() 메소드를 사용합니다.
        actionBar = this.getSupportActionBar();

        //actionBar.show(); 액션바보임
        //actionBar.hide(); 액션바숨김

        // 타이틀의 부제목을 설정합니다.
        actionBar.setSubtitle("존나 복잡하네...");
        //==========================================================================================


        // 월별 캘린더 뷰 객체 참조
        monthView = (CalendarMonthView) findViewById(R.id.monthView);
        monthViewAdapter = new CalendarMonthAdapter(this);
        monthView.setAdapter(monthViewAdapter);

        // 리스너 설정
        monthView.setOnDataSelectionListener(new OnDataSelectionListener() {
            public void onDataSelected(AdapterView parent, View v, int position, long id) {
                // 현재 선택한 일자 정보 표시
                MonthItem curItem = (MonthItem) monthViewAdapter.getItem(position);
                curDay = curItem.getDay();
                CurPosition = position;

                //현재 선택된 곳의 일정들을 리스트뷰에 표시해준다.(일정이 있다면)
                //=====================================================================================================
                String TodayDate = Integer.toString(curYear) + Integer.toString(curMonth)+Integer.toString(curDay);

                showInformationTV.setText("위치:"+position + "  날짜:"+TodayDate);



                //리스트뷰에 표시해줄것들
                String ShowListHour = null;
                String ShowListMin= null;
                String ShowListData= null;



                //해쉬맵에서 출력해오는 부분
                Collection collection = hashMap.keySet();
                Iterator iterator = collection.iterator();

                while(iterator.hasNext()){ //하나하나 넘어가며 확인
                    String HashMapKey = (String)iterator.next(); //키값 저장

                    //오늘 날짜에 맞는게 있으면 리스트에 표시해준다.
                    if(TodayDate.equals(HashMapKey)){
                        showInformationTV.setText("일정이 있는 날입니다");
                        scheduledOK = true;
                    }
                }




                if(scheduledOK){
                    showInformationTV.setText("일정데이터검색");
                    Collection collection2 = hashMap.keySet();
                    Iterator iterator2 = collection2.iterator();

                    while (iterator2.hasNext()){
                        String SelectDayHashKey = (String)iterator2.next(); //키값 저장
                        String SelectDayHashData = (String)hashMap.get(SelectDayHashKey); //해당키값에 대한 데이터 저장

                        if(SelectDayHashKey.equals(TodayDate+"Hour")){
                            showInformationTV.append("시간Get");
                            ShowListHour = SelectDayHashData;
                        }
                        if(SelectDayHashKey.equals((TodayDate+"Min"))){
                            showInformationTV.append("분Get");
                            ShowListMin = SelectDayHashData;
                        }
                        if(SelectDayHashKey.equals((TodayDate+"Data"))){
                            showInformationTV.append("내용Get");
                            ShowListData = SelectDayHashData;
                        }
                    }

                    adapter.addItem(new IconTextItem(ShowListHour+":"+ShowListMin, ShowListData  ));
                    listView.setAdapter(adapter);
                    scheduledOK = false;
                }






                //=====================================================================================================


                Log.d("MainActivity", "Selected : " + curDay);

            }
        });

        monthText = (TextView) findViewById(R.id.monthText);
        setMonthText();

        // 이전 월로 넘어가는 이벤트 처리
        Button monthPrevious = (Button) findViewById(R.id.monthPrevious);
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setPreviousMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });

        // 다음 월로 넘어가는 이벤트 처리
        Button monthNext = (Button) findViewById(R.id.monthNext);
        monthNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });


    }

    /**
     * 월 표시 텍스트 설정
     */
    private void setMonthText() {
        curYear = monthViewAdapter.getCurYear();
        curMonth = monthViewAdapter.getCurMonth();

        monthText.setText(curYear + "년 " + (curMonth + 1) + "월");
    }



    //액션바설정
    //==================================================================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add_schedule:  // 일정추가
                //□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
                curYear = monthViewAdapter.getCurYear();
                curMonth = monthViewAdapter.getCurMonth();

                showDateTV.setText("(" + curYear + "." + (curMonth + 1) + "." + curDay + ")"); //다이얼로그에 날짜 띄워줌
                topLayout.setVisibility(View.VISIBLE);
                //□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //==================================================================================================

    public void onClick_save(View v){

    //일정관리 Layout에서 저장을 눌렀을때
    //==============================================================================================
        topLayout.setVisibility(View.INVISIBLE);

        //날짜
        String DateHashID = Integer.toString(curYear) + Integer.toString(curMonth)+Integer.toString(curDay);

        //해쉬맵에 데이터를 저장
        synchronized (hashMap){
            //일정이 있는 날
            hashMap.put(DateHashID, Integer.toString(curDay));

            //일정의 시간, 분, 내용
            hashMap.put(DateHashID + "Hour", getHourEdit.getText().toString());
            hashMap.put(DateHashID + "Min", getMinEdit.getText().toString());
            hashMap.put(DateHashID + "Data", getDataEdit.getText().toString());
        }

        Collection collection = hashMap.keySet();
        Iterator iterator = collection.iterator();

        //엔터두번
        showInformationTV.append("\n\n");

        //해쉬맵에서 출력해오는 부분
        while(iterator.hasNext()){ //하나하나 넘어가며 확인
            String HashMapKey = (String)iterator.next(); //키값 저장
            String HashMapData = (String)hashMap.get(HashMapKey); //해당키값에 대한 데이터 저장

            showInformationTV.append(HashMapKey + ": " + HashMapData + "\n");
        }

        showInformationTV.append("현재 해쉬맵의 크기: "+ hashMap.size());

        //해쉬맵의 업데이트 된 부분을 어뎁터뷰로 보내줌
        monthViewAdapter.setHashMapData(hashMap);


        getHourEdit.setText("00");
        getMinEdit.setText("00");
        getDataEdit.setText("일정내용");
        Toast.makeText(getApplicationContext(), "일정이 추가되었습니다", Toast.LENGTH_LONG).show();
    //==============================================================================================
    }

    public void onClick_close(View v){


        showInformationTV.setText(Integer.toString( monthViewAdapter.getSelectedPosition()));


        getHourEdit.setText("00");
        getMinEdit.setText("00");
        getDataEdit.setText("일정내용");
        topLayout.setVisibility(View.INVISIBLE);
    }
}

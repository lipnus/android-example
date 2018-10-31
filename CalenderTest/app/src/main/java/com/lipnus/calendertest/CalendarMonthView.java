package com.lipnus.calendertest;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * Created by Sunpil on 2016-03-16.
 */
/**
 * 그리드뷰를 상속하여 새로운 뷰를 정의한 클래스
 * 선택한 일자를 표시하는 기능등을 효율적으로 처리하기 위해 정의한 클래스
 */
public class CalendarMonthView extends GridView {

    // 자체제작 보세 리스너
    private OnDataSelectionListener selectionListener;

    // 어뎁터 객체 선언( 이 어뎁터도 보세인듯)
    CalendarMonthAdapter adapter;

    // 생성자(Context받고 init 실행)
    public CalendarMonthView(Context context) {
        super(context);
        init();
    }

    // 생성자 오버라이딩?
    public CalendarMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    //속성 초기화
    public void init(){

        //뷰를 초기화하는 메소드를 만들고, 그 안에서 칼럼의 개수 설정
        setBackgroundColor(Color.GRAY);
        setVerticalSpacing(1);
        setHorizontalSpacing(1);
        setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

        // 칼럼의 갯수 설정
        setNumColumns(7);

        // 그리드뷰의 원래 이벤트 처리 리스너 설정
        setOnItemClickListener(new OnItemClickAdapter());
    }


//어댑터에 관련된 부분분
//=======================================================================================================
    /**
     * 어댑터 설정
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        super.setAdapter(adapter);

        this.adapter = (CalendarMonthAdapter) adapter;
    }

    /**
     * 어댑터 객체 리턴
     *
     * @return
     */
    public BaseAdapter getAdapter() {
        return (BaseAdapter)super.getAdapter();
    }

    /**
     * 리스너 설정
     *
     * @param listener
     */
    public void setOnDataSelectionListener(OnDataSelectionListener listener) {
        this.selectionListener = listener;
    }

    /**
     * 리스너 객체 리턴
     *
     * @return
     */
    public OnDataSelectionListener getOnDataSelectionListener() {
        return selectionListener;
    }

    class OnItemClickAdapter implements OnItemClickListener {

        public OnItemClickAdapter() {

        }

        public void onItemClick(AdapterView parent, View v, int position, long id) {

            if (adapter != null) {
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetInvalidated();
            }

            if (selectionListener != null) {
                selectionListener.onDataSelected(parent, v, position, id);
            }


        }

    }
//========================================================================================================


}

package com.lipnus.challenge8;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 어댑터 객체 정의
 * 
 * @author Mike
 *
 */
public class CalendarMonthAdapter extends BaseAdapter {



	//해쉬맵
	HashMap hashMap;

	//최초의 HashMap데이터가 MainActivity로부터 여기로 넘어오면 켜지고, 그 이후부터만 여기서 헤쉬맵을 처리
	boolean hashOn=false;

	//현재날짜
	String Today;



	public static final String TAG = "CalendarMonthAdapter";
	
	Context mContext;
	
	public static int oddColor = Color.rgb(225, 225, 225);
	public static int headColor = Color.rgb(12, 32, 158);
	
	private int selectedPosition = -1;
	
	private MonthItem[] items;
	
	private int countColumn = 7;
	
	int mStartDay;
	int startDay;
	int curYear;
	int curMonth;
	
	int firstDay;
	int lastDay;
	
	Calendar mCalendar;
	boolean recreateItems = false;
	
	public CalendarMonthAdapter(Context context) {
		super();

		mContext = context;
		
		init();
	}
	
	public CalendarMonthAdapter(Context context, AttributeSet attrs) {
		super();

		mContext = context;
		
		init();
	}

	private void init() {
		items = new MonthItem[7 * 6];

		mCalendar = Calendar.getInstance();
		recalculate();
		resetDayNumbers();
		
	}
	
	public void recalculate() {

		// set to the first day of the month
		mCalendar.set(Calendar.DAY_OF_MONTH, 1);
		
		// get week day
		int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
		firstDay = getFirstDay(dayOfWeek);
		Log.d(TAG, "firstDay : " + firstDay);
		
		mStartDay = mCalendar.getFirstDayOfWeek();
		curYear = mCalendar.get(Calendar.YEAR);
		curMonth = mCalendar.get(Calendar.MONTH);
		lastDay = getMonthLastDay(curYear, curMonth);
		
		Log.d(TAG, "curYear : " + curYear + ", curMonth : " + curMonth + ", lastDay : " + lastDay);
		
		int diff = mStartDay - Calendar.SUNDAY - 1;
        startDay = getFirstDayOfWeek();
		Log.d(TAG, "mStartDay : " + mStartDay + ", startDay : " + startDay);
		
	}
	
	public void setPreviousMonth() {
		mCalendar.add(Calendar.MONTH, -1);
        recalculate();
        
        resetDayNumbers();
        selectedPosition = -1;
	}
	
	public void setNextMonth() {
		mCalendar.add(Calendar.MONTH, 1);
        recalculate();
        
        resetDayNumbers();
        selectedPosition = -1;
	}
	
	public void resetDayNumbers() {
		for (int i = 0; i < 42; i++) {
			// calculate day number
			int dayNumber = (i+1) - firstDay;
			if (dayNumber < 1 || dayNumber > lastDay) {
				dayNumber = 0;
			}
			
	        // save as a data item
	        items[i] = new MonthItem(dayNumber);
		}
	}
	
	private int getFirstDay(int dayOfWeek) {
		int result = 0;
		if (dayOfWeek == Calendar.SUNDAY) {
			result = 0;
		} else if (dayOfWeek == Calendar.MONDAY) {
			result = 1;
		} else if (dayOfWeek == Calendar.TUESDAY) {
			result = 2;
		} else if (dayOfWeek == Calendar.WEDNESDAY) {
			result = 3;
		} else if (dayOfWeek == Calendar.THURSDAY) {
			result = 4;
		} else if (dayOfWeek == Calendar.FRIDAY) {
			result = 5;
		} else if (dayOfWeek == Calendar.SATURDAY) {
			result = 6;
		}
		
		return result;
	}
	
	
	public int getCurYear() {
		return curYear;
	}
	
	public int getCurMonth() {
		return curMonth;
	}
	
	
	public int getNumColumns() {
		return 7;
	}

	public int getCount() {
		return 7 * 6;
	}

	public Object getItem(int position) {
		return items[position];
	}

	public long getItemId(int position) {
		return position;
	}




//position이 0~47까지 반복문 돌리듯 호출됨... position이 for문의 i라 생각하면 될듯
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d(TAG, "getView(" + position + ") called.");

		MonthItemView itemView;
		if (convertView == null) {
			itemView = new MonthItemView(mContext);
		} else {
			itemView = (MonthItemView) convertView;
		}	
		
		// create a params
		GridView.LayoutParams params = new GridView.LayoutParams(
				GridView.LayoutParams.MATCH_PARENT,
				120);
		
		// calculate row and column
		int rowIndex = position / countColumn;
		int columnIndex = position % countColumn;
		
		Log.d(TAG, "Index : " + rowIndex + ", " + columnIndex);

		// set item data and properties
		itemView.setItem(items[position]);
		itemView.setLayoutParams(params);
		itemView.setPadding(2, 2, 2, 2);
		
		// set properties
		itemView.setGravity(Gravity.LEFT);
		
		if (columnIndex == 0) {
			itemView.setTextColor(Color.RED);
		} else {
			itemView.setTextColor(Color.BLACK);
		}
		
		// set background color
		if (position == getSelectedPosition()) {
        	itemView.setBackgroundColor(Color.YELLOW);
        } else {
        	itemView.setBackgroundColor(Color.WHITE);
        }


		//관찰을 위해서 감시하는 부분. 아무 기능없음.
		//==============================================================================================
		if(position==5) {
			Log.d("SIBAL", "현재선택: " + getSelectedPosition());


			Log.d("SIBAL", "mStartDay: " + mStartDay + "  startDay: " + startDay + "  curYear: +" + curYear +
					"  curMonth: +" + curMonth + "  firstDay: +" + firstDay + "  lastDay: +" + lastDay);
		}

		Today = Integer.toString(curYear) + Integer.toString(curMonth)+Integer.toString( (position-firstDay+1) );
		//Log.d("SIBAL", "position:" + position + "지금: " + Today);


		if(hashOn == true){
			Collection collection = hashMap.keySet();
			Iterator iterator = collection.iterator();

			//해쉬맵에서 출력해오는 부분
			while(iterator.hasNext()){ //하나하나 넘어가며 확인
				String HashMapKey = (String)iterator.next(); //키값 저장
				String HashMapData = (String)hashMap.get(HashMapKey); //해당키값에 대한 데이터 저장

				//Log.d("SIBAL", "해쉬맵의 데이터: " + HashMapKey);

				if(Today.equals(HashMapKey)){
					itemView.setBackgroundColor(Color.RED);
				}
			}
		}else{
			//Log.d("SIBAL", "저장된 데이터없음");
		}

		//==============================================================================================

		return itemView;
	}

	
    /**
     * Get first day of week as android.text.format.Time constant.
     * @return the first day of week in android.text.format.Time
     */
    public static int getFirstDayOfWeek() {
        int startDay = Calendar.getInstance().getFirstDayOfWeek();
        if (startDay == Calendar.SATURDAY) {
            return Time.SATURDAY;
        } else if (startDay == Calendar.MONDAY) {
            return Time.MONDAY;
        } else {
            return Time.SUNDAY;
        }
    }
 
	
    /**
     * get day count for each month
     * 
     * @param year
     * @param month
     * @return
     */
    private int getMonthLastDay(int year, int month){
    	switch (month) {
 	   		case 0:
      		case 2:
      		case 4:
      		case 6:
      		case 7:
      		case 9:
      		case 11:
      			return (31);

      		case 3:
      		case 5:
      		case 8:
      		case 10:
      			return (30);

      		default:
      			if(((year%4==0)&&(year%100!=0)) || (year%400==0) ) {
      				return (29);   // 2월 윤년계산
      			} else { 
      				return (28);
      			}
 	   	}
 	}






	
	
	/**
	 * set selected row
	 * 
	 * @param selectedRow
	 */
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	//지금 선택되어 있는 칸을 반환한다.
	public int getSelectedPosition() {
		return selectedPosition;
	}





	//MainActivity로부터 해쉬맵 데이터 받아옴
	public void setHashMapData(HashMap hsm){
		if(hashOn==false){hashOn=true;}

		hashMap = hsm;
	}

}

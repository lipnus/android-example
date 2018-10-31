package com.lipnus.challenge8;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 아이템으로 보여줄 뷰 정의
 * 
 * @author Mike
 *
 */
public class IconTextView extends LinearLayout {

	private TextView showTimeTV;
	private TextView showDataTV;

	//생성자에서 바로 값들을 지정
	public IconTextView(Context context, IconTextItem aItem) {
		super(context);

		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.listitem, this, true);

		//각 텍스트뷰에 값들을 설정
		showTimeTV = (TextView) findViewById(R.id.showScheduleTimeTV);
		showTimeTV.setText(aItem.getData(0));

		showDataTV = (TextView) findViewById(R.id.showScheduleTitleTV);
		showDataTV.setText(aItem.getData(1));


	}

	/**
	 * set Text
	 *
	 * @param index
	 * @param data
	 */
	public void setText(int index, String data) {
		if (index == 0) {
			showTimeTV.setText(data);
		} else if (index == 1) {
			showDataTV.setText(data);
		} else {
			throw new IllegalArgumentException();
		}
	}


}

package com.lipnus.kumchurk.submenu;

import com.lipnus.kumchurk.data.Alarm;

/**
 * Created by Sunpil on 2016-07-13.
 *
 * 리스트뷰의 아이템데이터
 *
 */

public class AlarmListViewItem {

    private Alarm alarmData;

    public AlarmListViewItem(Alarm alarmData) {
        this.alarmData = alarmData;
    }

    public Alarm getAlarmData() {

        return alarmData;
    }
}
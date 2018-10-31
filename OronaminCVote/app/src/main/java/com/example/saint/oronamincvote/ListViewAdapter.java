package com.example.saint.oronamincvote;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by saint on 2016-08-06.
 */
public class ListViewAdapter extends BaseAdapter {
    protected ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    private final Handler mHandler;

    public ListViewAdapter(Handler handler){
        this.mHandler = handler;
    }
    @Override
    public int getCount(){
        return listViewItemList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item,parent, false);
        }

        TextView roomName = (TextView) convertView.findViewById(R.id.RoomName);
        Button enterButton = (Button) convertView.findViewById(R.id.enterButton);
        final ListViewItem listViewItem = listViewItemList.get(position);
        roomName.setText(listViewItem.getRoomName());


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomName = listViewItemList.get(position).getRoomName();
                mHandler.obtainMessage(MainActivity.CLICKITEM,roomName).sendToTarget();
            }
        });



        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemList.get(position);
    }

    public void addItem(String roomName){

        boolean existed = false;
        for(int i=0;i<listViewItemList.size();i++){
            if(listViewItemList.get(i).getRoomName().equals(roomName)){
                existed = true;
            }
        }

        if(existed==false){
            ListViewItem item = new ListViewItem();
            item.setRoomName(roomName);
            listViewItemList.add(item);
        }
    }
}

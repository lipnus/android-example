package com.lipnus.fragmenttest2;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Sunpil on 2016-03-03.
 */
public class ListFragment extends Fragment {
    String[] values = {"첫번째그림", "두번째그림", "세번째그림"};

    public static interface ImageSelectionCallback{
        public void onImageSelected(int position);
    }

    //뭐지? 왜 인터페이시를 클래스 안에서 정의하고 구현하는거지??
    public ImageSelectionCallback callback;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof ImageSelectionCallback){
            callback = (ImageSelectionCallback)context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_list, container, false);


        //리스트뷰인데... 몰라
        //=================================================================================================================
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                if(callback != null){
                    callback.onImageSelected(position);
                }
            }
        });
        //=================================================================================================================

        return rootView;
    }
}

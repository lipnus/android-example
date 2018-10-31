package com.lipnus.kumchurk.kum_class;

import android.util.Log;

import com.lipnus.kumchurk.GlobalApplication;
import com.lipnus.kumchurk.data.MenuRes_Info;
import com.lipnus.kumchurk.data.main.Main_Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunpil on 2017-04-22.
 * 여기에선 Globalapplication.menuData에 저장된 식당과 메뉴데이터들 중에서
 * 호출한 요구조건(거리, 가격 등등)
 * 에 맞게 데이터들을 적당히 뽑아서 반환해준다
 */

public class Select_menu {

    private Main_Data mainData; //rowData
    private List<Main_Data> mainData2; //랜덤으로 골라서 넣어서 포장


    //생성자
    public Select_menu(Main_Data mainData) {
        this.mainData = mainData;
        GlobalApplication.setMainData(mainData); //원본을 어플리케이션에 업로드
    }

//    public List<Main_Data> selectOption1() {
//
//
//        List<Main_Data> md_List = new ArrayList<>();
//        Main_Data temp_md = new Main_Data();
//        List<Menu_Info_Selected> temp_menuinfo;
//        List<Res_Info_Selected> temp_resinfo;
//
//        //0번 페이지는 추천메뉴
//        //==========================================================================================
//        temp_menuinfo = randomSelect(8); //8개짜리 메뉴 리스트를 얻어옴
//        temp_resinfo = findRes(temp_menuinfo); //얻어온 8개짜리 리스트에 해당하는 식당들 리스트
//
//        //temp_md에 메뉴8개, 식당8개가 들어있다(한 페이지 완성)
//        temp_md.setMenuInfoSelected(temp_menuinfo);
//        temp_md.setResInfoSelected(temp_resinfo);
//
//        //한 페이지가 완성되었으므로 전체 리스트에 추가
//        md_List.add(temp_md);
//        //==========================================================================================
//
//
//        //1~4개의 페이지를 만들고, 한 페이지마다 메뉴3개씩 넣음
//        //==========================================================================================
//        for (int i = 1; i < 5; i++) {
//            temp_md = new Main_Data();
//            temp_menuinfo = randomSelect(3); //3개짜리 메뉴 리스트를 얻어옴
//            temp_resinfo = findRes(temp_menuinfo); //얻어온 3개짜리 리스트에 해당하는 식당들 리스트
//
//            //temp_md에 메뉴3개, 식당3개가 들어있다(한 페이지 완성)
//            temp_md.setMenuInfoSelected(temp_menuinfo);
//            temp_md.setResInfoSelected(temp_resinfo);
//
//            //한 페이지가 완성되었으므로 전체 리스트에 추가
//            md_List.add(temp_md);
//        }
//        //==========================================================================================
//
//        return md_List;
//    }


    //생성자에서 받아놓은 mainData안의 menuresInfo를 3개씩 포장
    public void makeMainList() {

        mainData2 = new ArrayList<Main_Data>(); //반환데이어
        Main_Data temp_md; //임시데이터

        //첫번째 리스트는 8개짜리
        temp_md = new Main_Data();
        temp_md.setMenuresInfo( randomSelect(8) );
        mainData2.add(temp_md);



        //4개덩어리 포장(개당 3개)
        for(int i=0; i<5; i++){

            //3개를 리스트로 묶음
            temp_md = new Main_Data();
            temp_md.setMenuresInfo( randomSelect(3) );
            mainData2.add(temp_md);

        }

        //Application에 정렬된 메뉴리스트를 어플리케이션에 업로드
        GlobalApplication.setMainData2(mainData2);


        //확인
        for(int i=0; i<5; i++){
            Log.d("VZVZ", GlobalApplication.getMainData2().get(i).getMenuresInfo().get(0).getMenu_name());
            Log.d("VZVZ", GlobalApplication.getMainData2().get(i).getMenuresInfo().get(1).getMenu_name());
            Log.d("VZVZ", GlobalApplication.getMainData2().get(i).getMenuresInfo().get(2).getMenu_name());
            Log.d("VZVZ", "----------------------------------------------------------------------------");
        }

    }

    //앱력받은 개수만큼의 menuresInfo 리스트를 반환
    public List<MenuRes_Info> randomSelect(int count) {

        int menuSize = mainData.menuresInfo.size(); //정보의 개수
        int ranNum; //랜덤넘버
        List<MenuRes_Info> mrI = new ArrayList<>(); //반환할 형식

        for (int i = 0; i < count; i++) {
            ranNum = SimpleFunction.getRandom(0, menuSize - 1); //난수생성
            mrI.add(mainData.menuresInfo.get(ranNum)); //반환될 리스트에 넣음
            mainData.menuresInfo.remove(ranNum); //넣었던 것은 다른곳에 또 넣어지지 않게 삭제
            menuSize--;
        }

        return mrI;
    }

}



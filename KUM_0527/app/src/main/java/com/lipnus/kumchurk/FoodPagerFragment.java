package com.lipnus.kumchurk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.lipnus.kumchurk.data.Menu_Review;
import com.lipnus.kumchurk.detailpage.DetailReveiwActivity;

import java.util.List;

public class FoodPagerFragment {


    public static class PlaceholderFragment extends Fragment {

        //==========================================================================================

        //volly를 통해 json을 받으면 '메뉴리뷰'부분은 객체화되어 이곳에 저장되어 진다
        //이 정보를 토대로 뷰페이저에 메뉴사진을 띄워준다(사실 필요한 것은 사진뿐)
        private static List<Menu_Review> menu_review = null;

        //리뷰가 하나도 없는경우, 혹시 사진만 올라가 있는 파일이있는지 알아보기위해필요
        private static String menuName=null;
        private static String resName=null;

        //이 메소드를 통해 값을 입력
        public static void addMenu(List<Menu_Review> mr, String mN, String rN){
            menu_review = mr;
            menuName = mN;
            resName = rN;
        }
        //==========================================================================================

        //현재 어느페이지인지
        private static final String ARG_SECTION_NUMBER = "section_number";



        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            Log.d("VPVP", "PlaceholderFragment newInstance " + sectionNumber);
            return fragment;
        }

        public PlaceholderFragment() {
            Log.d("VPVP", "PlaceholderFragment()");
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.d("VPVP", "onCreateView()");

            int viewNum = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView;
            ImageView iV;
            final Context context;

            rootView = inflater.inflate(R.layout.fragment_image_inside, container, false);
            iV = (ImageView)rootView.findViewById(R.id.imageView);
            context = rootView.getContext();

            //올려진 리뷰가 1개 이상일때
            if(menu_review.size() >0){

                //제일 마지막으로 올린 사진이 제일 처음에 나와야 하므로 뒤집어준다
                final int index = menu_review.size() - viewNum;

                Log.d("DTTD", "인덱스: "+ index  + ", " + index);

                Glide.with(context)
                        .load( menu_review.get( index ).getMenu_image() )
                        .placeholder(R.drawable.res_loading)
                        .priority(Priority.HIGH)
                        .centerCrop()
                        .into(iV);
                iV.setScaleType(ImageView.ScaleType.FIT_XY);

                //사진을 터치하면 상세페이지로 이동
                iV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent iT = new Intent(context, DetailReveiwActivity.class);
                        iT.putExtra("review_num", Integer.toString(menu_review.get(index).getNo()));
                        startActivity(iT);

                    }
                });
            }

            //올라온 리뷰가 0개일때(기본사진을 표시)
            else{

                //이미지만 올라가 있는지 확인하고 띄워줌
                String onlyImgPath = "http://kumchurk.ivyro.net/app/image/menu_imgonly/" + menuName + "_"+ resName +".png";

                Glide.with(context)
                        .load(onlyImgPath)
                        .centerCrop()
                        .placeholder(R.drawable.empty_table2)
                        .thumbnail(0.3f)
                        .into(iV);
                iV.setScaleType(ImageView.ScaleType.FIT_XY);
            }



            Log.d("FFF", viewNum + "번째뷰");
            return rootView;
        }


    }
}
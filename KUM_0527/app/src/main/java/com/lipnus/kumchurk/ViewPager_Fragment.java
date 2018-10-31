package com.lipnus.kumchurk;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lipnus.kumchurk.data.MenuRes_Info;
import com.lipnus.kumchurk.kum_class.SimpleFunction;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

import static com.lipnus.kumchurk.kum_class.SimpleFunction.distanceMinute;

/**
 * Created by Sunpil on 2017-01-24.
 */
//프래그먼트
public class ViewPager_Fragment {

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            int viewNum = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView;
            Context context;


            switch(viewNum){
                case 1:
                    rootView = setFragment1(container, inflater);
                    break;
                case 2:
                    rootView = setFragment2(container, inflater);
                    break;
                case 3:
                    rootView = setFragment3(container, inflater);
                    break;

                default:
                    rootView = setFragment4(container, inflater);



                    break;
            }

            Log.d("FFF", viewNum + "번째뷰생성");
            return rootView;

        }//onCreateView


        //각 프래그먼트들의 소스코드가 들어있다
        public View setFragment1(ViewGroup container, LayoutInflater inflater){

            final List<MenuRes_Info> mrI =  GlobalApplication.getMainData2().get(1).getMenuresInfo();

            //이미지경로
            String imagepath = mrI.get(0).getMenu_image();

            //가격
            String price = SimpleFunction.displayPrice(mrI.get(0).getMenu_price(), mrI.get(0).getMenu_price3(), mrI.get(0).getMenu_price3());

            //이름과 가격표시
            String menuName = mrI.get(0).getMenu_name() + "\n\n" + price;

            //거리
            String disMin = distanceMinute(mrI.get(0).getRes_latitude(), mrI.get(0).getRes_longitude());
            String resName = "이곳으로부터 " + disMin + "의 " + mrI.get(0).getRes_name() ;

            //--------------------------------------------------------------------------------------
            View rootView;
            final Context context;

            rootView = inflater.inflate(R.layout.fragment_layout, container, false);
            context = rootView.getContext();

            ImageView menuIv = (ImageView)rootView.findViewById(R.id.circle_menu_img_iv);
            ImageView circleIv = (ImageView) rootView.findViewById(R.id.circle_center_iv);

            TextView menuNameTv = (TextView) rootView.findViewById(R.id.circle_menuname_tv);
            TextView resNameTv = (TextView)rootView.findViewById(R.id.circle_resname_tv);

            //--------------------------------------------------------------------------------------



            circleIv.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : click event
                    String res_name = mrI.get(0).getRes_name();
                    String menu_name = mrI.get(0).getMenu_name();

                    Intent iT = new Intent(context, ScrollActivity.class);
                    iT.putExtra("res_name", res_name);
                    iT.putExtra("menu_name", menu_name);
                    startActivity(iT);
                }
            });



            //제목, 가격
            menuNameTv.setText(menuName);

            //거리, 식당이름
            resNameTv.setText(resName);


            //가운데 동그라미
            Glide.with( context )
                    .load( imagepath )
                    .bitmapTransform(new CropCircleTransformation(context), new CenterCrop(context))
                    .thumbnail(0.1f)
                    .into( circleIv );
            circleIv.setScaleType(ImageView.ScaleType.FIT_XY);

            //전체이미지
            Glide.with( context )
                    .load( imagepath )
                    .placeholder(R.drawable.main_loading2)
                    .bitmapTransform(new BlurTransformation(context, 4),
                            new CenterCrop(context),new ColorFilterTransformation(context, Color.argb(150, 0, 0, 0)))
                    .thumbnail(0.1f)
                    .into( menuIv );
            menuIv.setScaleType(ImageView.ScaleType.FIT_XY);

            return rootView;

        }

        public View setFragment2(ViewGroup container, LayoutInflater inflater){

            final List<MenuRes_Info> mrI =  GlobalApplication.getMainData2().get(2).getMenuresInfo();

            //이미지경로
            String imagepath = mrI.get(0).getMenu_image();
            String imagepath2 = mrI.get(1).getMenu_image();
            String imagepath3 = mrI.get(2).getMenu_image();

            //가격
            String price = Double.toString((double)mrI.get(0).getMenu_price()/1000);
            String price2 = Double.toString((double)mrI.get(1).getMenu_price()/1000);
            String price3 = Double.toString((double)mrI.get(2).getMenu_price()/1000);

            //이름과 가격표시
            String menuName = mrI.get(0).getMenu_name() + " " + price;
            String menuName2 = mrI.get(1).getMenu_name() + " " + price2;
            String menuName3 = mrI.get(2).getMenu_name() + " " + price3;

            //거리
            String disMin = distanceMinute(mrI.get(0).getRes_latitude(), mrI.get(0).getRes_longitude());
            String disMin2 = distanceMinute(mrI.get(1).getRes_latitude(), mrI.get(1).getRes_longitude());
            String disMin3 = distanceMinute(mrI.get(2).getRes_latitude(), mrI.get(2).getRes_longitude());

            String resName = mrI.get(0).getRes_name() + " "+ disMin;
            String resName2 = mrI.get(1).getRes_name() + " "+ disMin2;
            String resName3 = mrI.get(2).getRes_name() + " "+ disMin3;

            //--------------------------------------------------------------------------------------
            View rootView;
            final Context context;

            rootView = inflater.inflate(R.layout.fragment_layout2, container, false);
            context = rootView.getContext();


            ImageView topIv = (ImageView)rootView.findViewById(R.id.diagonal_top_iv);
            ImageView middleIv = (ImageView)rootView.findViewById(R.id.diagonal_middle_iv);
            ImageView bottomIv = (ImageView)rootView.findViewById(R.id.diagonal_bottom_iv);

            TextView topTv = (TextView) rootView.findViewById(R.id.diagonal_top_tv);
            TextView middleTv = (TextView)rootView.findViewById(R.id.diagonal_middle_tv);
            TextView bottomTv = (TextView)rootView.findViewById(R.id.diagonal_bottom_tv);

            TextView topTv2 = (TextView) rootView.findViewById(R.id.diagonal_top2_tv);
            TextView middleTv2 = (TextView)rootView.findViewById(R.id.diagonal_middle2_tv);
            TextView bottomTv2 = (TextView)rootView.findViewById(R.id.diagonal_bottom2_tv);
            //--------------------------------------------------------------------------------------

            topIv.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : click event
                    String res_name = mrI.get(0).getRes_name();
                    String menu_name = mrI.get(0).getMenu_name();

                    Intent iT = new Intent(context, ScrollActivity.class);
                    iT.putExtra("res_name", res_name);
                    iT.putExtra("menu_name", menu_name);
                    startActivity(iT);
                }
            });

            middleIv.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : click event
                    String res_name = mrI.get(1).getRes_name();
                    String menu_name = mrI.get(1).getMenu_name();

                    Intent iT = new Intent(context, ScrollActivity.class);
                    iT.putExtra("res_name", res_name);
                    iT.putExtra("menu_name", menu_name);
                    startActivity(iT);
                }
            });

            bottomIv.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : click event
                    String res_name = mrI.get(2).getRes_name();
                    String menu_name = mrI.get(2).getMenu_name();

                    Intent iT = new Intent(context, ScrollActivity.class);
                    iT.putExtra("res_name", res_name);
                    iT.putExtra("menu_name", menu_name);
                    startActivity(iT);
                }
            });


            //제목, 가격
            topTv.setText(menuName);
            middleTv.setText(menuName2);
            bottomTv.setText(menuName3);


            //거리, 식당이름
            topTv2.setText(resName);
            middleTv2.setText(resName2);
            bottomTv2.setText(resName3);


            //이미지
            Glide.with( context )
                    .load( imagepath )
                    .placeholder(R.drawable.main_loading)
                    .bitmapTransform(
                            new CenterCrop(context),
                            new VignetteFilterTransformation(context, new PointF(0.5f, 0.7f),
                                    new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.9f),
                            new MaskTransformation(context, R.drawable.mask_top))
                    .into( topIv );
            topIv.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with( context )
                    .load( imagepath2 )
                    .placeholder(R.drawable.main_loading)
                    .centerCrop()
                    .thumbnail(0.3f)
                    .into( middleIv );
            middleIv.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with( context )
                    .load(imagepath3 )
                    .placeholder(R.drawable.main_loading)
                    .bitmapTransform(
                            new CenterCrop(context),
                            new MaskTransformation(context, R.drawable.mask_bottom))
                    .into( bottomIv );
            bottomIv.setScaleType(ImageView.ScaleType.FIT_XY);

            return rootView;
        }

        public View setFragment3(ViewGroup container, LayoutInflater inflater){

            //3페이지의 데이터로 할당된 것
            final List<MenuRes_Info> mrI =  GlobalApplication.getMainData2().get(3).getMenuresInfo();

            //이미지경로
            String imagepath = mrI.get(0).getMenu_image();
            String imagepath2 = mrI.get(1).getMenu_image();
            String imagepath3 = mrI.get(2).getMenu_image();

            //가격
            String price = Double.toString((double)mrI.get(0).getMenu_price()/1000);
            String price2 = Double.toString((double)mrI.get(1).getMenu_price()/1000);
            String price3 = Double.toString((double)mrI.get(2).getMenu_price()/1000);

            //이름과 가격표시
            String menuName = mrI.get(0).getMenu_name() + " " + price;
            String menuName2 = mrI.get(1).getMenu_name() + " " + price2;
            String menuName3 = mrI.get(2).getMenu_name() + " " + price3;

            //거리
            String disMin = distanceMinute(mrI.get(0).getRes_latitude(), mrI.get(0).getRes_longitude());
            String disMin2 = distanceMinute(mrI.get(1).getRes_latitude(), mrI.get(1).getRes_longitude());
            String disMin3 = distanceMinute(mrI.get(2).getRes_latitude(), mrI.get(2).getRes_longitude());

            String resName = mrI.get(0).getRes_name() + " "+ disMin;
            String resName2 = mrI.get(1).getRes_name() + " "+ disMin2;
            String resName3 = mrI.get(2).getRes_name() + " "+ disMin3;


            //--------------------------------------------------------------------------------------
            View rootView;
            final Context context;

            rootView = inflater.inflate(R.layout.fragment_layout3, container, false);
            context = rootView.getContext();

            ImageView topIv = (ImageView)rootView.findViewById(R.id.vertical_top_iv);
            ImageView middleIv = (ImageView)rootView.findViewById(R.id.vertical_middle_iv);
            ImageView bottomIv = (ImageView)rootView.findViewById(R.id.vertical_bottom_iv);
            TextView topTv = (TextView) rootView.findViewById(R.id.vertical_top_tv);
            TextView middleTv = (TextView)rootView.findViewById(R.id.vertical_middle_tv);
            TextView bottomTv = (TextView)rootView.findViewById(R.id.vertical_bottom_tv);
            TextView topTv2 = (TextView) rootView.findViewById(R.id.vertical_top2_tv);
            TextView middleTv2 = (TextView)rootView.findViewById(R.id.vertical_middle2_tv);
            TextView bottomTv2 = (TextView)rootView.findViewById(R.id.vertical_bottom2_tv);
            //--------------------------------------------------------------------------------------

            topIv.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : click event
                    String res_name = mrI.get(0).getRes_name();
                    String menu_name = mrI.get(0).getMenu_name();

                    Intent iT = new Intent(context, ScrollActivity.class);
                    iT.putExtra("res_name", res_name);
                    iT.putExtra("menu_name", menu_name);
                    startActivity(iT);
                }
            });
            middleIv.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : click event
                    String res_name = mrI.get(1).getRes_name();
                    String menu_name = mrI.get(1).getMenu_name();

                    Intent iT = new Intent(context, ScrollActivity.class);
                    iT.putExtra("res_name", res_name);
                    iT.putExtra("menu_name", menu_name);
                    startActivity(iT);
                }
            });
            bottomIv.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : click event
                    String res_name = mrI.get(2).getRes_name();
                    String menu_name = mrI.get(2).getMenu_name();

                    Intent iT = new Intent(context, ScrollActivity.class);
                    iT.putExtra("res_name", res_name);
                    iT.putExtra("menu_name", menu_name);
                    startActivity(iT);
                }
            });


            //제목, 가격
            topTv.setText(menuName);
            middleTv.setText(menuName2);
            bottomTv.setText(menuName3);

            //거리, 식당이름
            topTv2.setText(resName);
            middleTv2.setText(resName2);
            bottomTv2.setText(resName3);


            //이미지
            Glide.with( context )
                    .load( imagepath )
                    .placeholder(R.drawable.main_loading)
                    .bitmapTransform(new CenterCrop(context), new VignetteFilterTransformation(context, new PointF(0.5f, 0.7f),
                            new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.9f))
                    .into( topIv );
            topIv.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with( context )
                    .load( imagepath2 )
                    .placeholder(R.drawable.main_loading)
                    .centerCrop()
                    .into( middleIv );
            middleIv.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with( context )
                    .load(imagepath3 )
                    .placeholder(R.drawable.main_loading)
                    .bitmapTransform(new CenterCrop(context), new VignetteFilterTransformation(context, new PointF(0.5f, 0.3f),
                            new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.96f))
                    .into( bottomIv );
            bottomIv.setScaleType(ImageView.ScaleType.FIT_XY);

            return rootView;
        }

        public View setFragment4(ViewGroup container, LayoutInflater inflater){

            final List<MenuRes_Info> mrI =  GlobalApplication.getMainData2().get(4).getMenuresInfo();

            //이미지경로
            String imagepath = mrI.get(0).getMenu_image();
            String imagepath2 = mrI.get(1).getMenu_image();
            String imagepath3 = mrI.get(2).getMenu_image();

            //가격
            String price = Double.toString((double)mrI.get(0).getMenu_price()/1000);
            String price2 = Double.toString((double)mrI.get(1).getMenu_price()/1000);
            String price3 = Double.toString((double)mrI.get(2).getMenu_price()/1000);

            //이름과 가격표시
            String menuName = mrI.get(0).getMenu_name() + " " + price;
            String menuName2 = mrI.get(1).getMenu_name() + " " + price2;
            String menuName3 = mrI.get(2).getMenu_name() + " " + price3;

            //거리
            String disMin = distanceMinute(mrI.get(0).getRes_latitude(), mrI.get(0).getRes_longitude());
            String disMin2 = distanceMinute(mrI.get(1).getRes_latitude(), mrI.get(1).getRes_longitude());
            String disMin3 = distanceMinute(mrI.get(2).getRes_latitude(), mrI.get(2).getRes_longitude());

            String resName = mrI.get(0).getRes_name() + " "+ disMin;
            String resName2 = mrI.get(1).getRes_name() + " "+ disMin2;
            String resName3 = mrI.get(2).getRes_name() + " "+ disMin3;

            //--------------------------------------------------------------------------------------
            View rootView;
            final Context context;

            rootView = inflater.inflate(R.layout.fragment_layout2, container, false);
            context = rootView.getContext();


            ImageView topIv = (ImageView)rootView.findViewById(R.id.diagonal_top_iv);
            ImageView middleIv = (ImageView)rootView.findViewById(R.id.diagonal_middle_iv);
            ImageView bottomIv = (ImageView)rootView.findViewById(R.id.diagonal_bottom_iv);

            TextView topTv = (TextView) rootView.findViewById(R.id.diagonal_top_tv);
            TextView middleTv = (TextView)rootView.findViewById(R.id.diagonal_middle_tv);
            TextView bottomTv = (TextView)rootView.findViewById(R.id.diagonal_bottom_tv);

            TextView topTv2 = (TextView) rootView.findViewById(R.id.diagonal_top2_tv);
            TextView middleTv2 = (TextView)rootView.findViewById(R.id.diagonal_middle2_tv);
            TextView bottomTv2 = (TextView)rootView.findViewById(R.id.diagonal_bottom2_tv);
            //--------------------------------------------------------------------------------------

            topIv.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : click event
                    String res_name = mrI.get(0).getRes_name();
                    String menu_name = mrI.get(0).getMenu_name();

                    Intent iT = new Intent(context, ScrollActivity.class);
                    iT.putExtra("res_name", res_name);
                    iT.putExtra("menu_name", menu_name);
                    startActivity(iT);
                }
            });

            middleIv.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : click event
                    String res_name = mrI.get(1).getRes_name();
                    String menu_name = mrI.get(1).getMenu_name();

                    Intent iT = new Intent(context, ScrollActivity.class);
                    iT.putExtra("res_name", res_name);
                    iT.putExtra("menu_name", menu_name);
                    startActivity(iT);
                }
            });

            bottomIv.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : click event
                    String res_name = mrI.get(2).getRes_name();
                    String menu_name = mrI.get(2).getMenu_name();

                    Intent iT = new Intent(context, ScrollActivity.class);
                    iT.putExtra("res_name", res_name);
                    iT.putExtra("menu_name", menu_name);
                    startActivity(iT);
                }
            });


            //제목, 가격
            topTv.setText(menuName);
            middleTv.setText(menuName2);
            bottomTv.setText(menuName3);


            //거리, 식당이름
            topTv2.setText(resName);
            middleTv2.setText(resName2);
            bottomTv2.setText(resName3);


            //이미지
            Glide.with( context )
                    .load( imagepath )
                    .placeholder(R.drawable.main_loading)
                    .bitmapTransform(
                            new CenterCrop(context),
                            new VignetteFilterTransformation(context, new PointF(0.5f, 0.7f),
                                    new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.9f),
                            new MaskTransformation(context, R.drawable.mask_top_r))
                    .into( topIv );
            topIv.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with( context )
                    .load( imagepath2 )
                    .placeholder(R.drawable.main_loading)
                    .centerCrop()
                    .thumbnail(0.3f)
                    .into( middleIv );
            middleIv.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with( context )
                    .load(imagepath3 )
                    .placeholder(R.drawable.main_loading)
                    .bitmapTransform(
                            new CenterCrop(context),
                            new MaskTransformation(context, R.drawable.mask_bottom_r))
                    .into( bottomIv );
            bottomIv.setScaleType(ImageView.ScaleType.FIT_XY);

            return rootView;

        }


    }
}

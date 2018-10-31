package com.lipnus.kum_imageload_library;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.MaskTransformation;

public class MainActivity extends AppCompatActivity {

    ImageView iV, iV2, iV3, iV4;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iV = (ImageView) findViewById(R.id.iV);
        iV2 = (ImageView) findViewById(R.id.iV2);
        iV3 = (ImageView) findViewById(R.id.iV3);
        iV4 = (ImageView) findViewById(R.id.iV4);

        mContext = this;

        //이미지삽입
        Glide.with(this)
                .load("http://3.bp.blogspot.com/-aeCkNXBMo4Y/VrMZaWLjeaI/AAAAAAAABX8/VT_8ADpZs88/s1600/150710_%25EB%25AE%25A4%25EB%25B1%2585%25ED%2587%25B4%25EA%25B7%25BC_%25282%2529.jpg")
                .placeholder(R.drawable.loading) //로딩
                .thumbnail(0.2f)
                .into(iV);
        iV.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(this)
                .load("http://fimg3.pann.com/new/download.jsp?FileID=35727381")
                .placeholder(R.drawable.loading) //로딩
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(iV2);

        Glide.with(this)
                .load("http://fimg3.pann.com/new/download.jsp?FileID=35727381")
                .placeholder(R.drawable.loading) //로딩
                .bitmapTransform(new GrayscaleTransformation(this), new BlurTransformation(this))
                .into(iV3);

        Glide.with(this)
                .load("http://fimg3.pann.com/new/download.jsp?FileID=35727381")
                .placeholder(R.drawable.loading) //로딩
                .bitmapTransform(new MaskTransformation(this, R.drawable.mask_bottom))
                .into(iV4);


    }
}

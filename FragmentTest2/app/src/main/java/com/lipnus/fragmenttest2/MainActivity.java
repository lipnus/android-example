package com.lipnus.fragmenttest2;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements ListFragment.ImageSelectionCallback {

    //프래그먼트객체 선언
    ListFragment listFragment = null;
    ViewerFragment viewerFragment = null;

    //이미지파일
    int[] images = {R.drawable.dream01, R.drawable.dream02, R.drawable.dream03};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        listFragment = (ListFragment) manager.findFragmentById(R.id.listFragment);
        viewerFragment = (ViewerFragment) manager.findFragmentById(R.id.viewerFragment);
    }

    @Override
    public void onImageSelected(int position){
        viewerFragment.setImage(images[position]);
    }
}

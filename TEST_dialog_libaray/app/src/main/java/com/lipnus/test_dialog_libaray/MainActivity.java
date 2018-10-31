package com.lipnus.test_dialog_libaray;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.kcode.bottomlib.BottomDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaa);
    }

    public void onClick(View v){

        new SweetAlertDialog(this)
                .setTitleText("Here's a message!")
                .setContentText("It's pretty, isn't it?")
                .setContentText("두개 되냐?")
                .show();
    }

    public void onClick2(View v){
        BottomDialog dialog = BottomDialog.newInstance("title",new String[]{"item1","item2"});
/**
 *
 * BottomDialog dialog = BottomDialog.newInstance("titleText","cancelText",new String[]{"item1","item2"});
 *
 * use public static BottomDialog newInstance(String titleText,String cancelText, String[] items)
 * set cancel text
 */
        dialog.show(getSupportFragmentManager(),"dialog");
        //add item click listener
        dialog.setListener(new BottomDialog.OnClickListener() {
            @Override
            public void click(int position) {
                Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_LONG).show();
            }
        });
    }
}

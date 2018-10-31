package org.androidtown.menu;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * 메뉴를 사용하는 가장 기초적인 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 */
public class MainActivity extends AppCompatActivity {

    //액션바
    ActionBar actionBar;
    boolean showingActionBar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar  = this.getSupportActionBar();
    }


    public void onClick(View v){
        actionBar.hide();
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 메뉴를 위한 XML 파일 설정
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch(curId) {
            case R.id.menu_refresh:
                Toast.makeText(this, "새로고침 메뉴가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_search:
                Toast.makeText(this, "검색 메뉴가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_settings:
                Toast.makeText(this, "설정 메뉴가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }



}

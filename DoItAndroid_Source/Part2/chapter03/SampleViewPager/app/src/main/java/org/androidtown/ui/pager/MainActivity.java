package org.androidtown.ui.pager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 뷰페이저를 사용하는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뷰페이저 객체를 참조하고 어댑터를 설정합니다.
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        pager.setAdapter(adapter);

    }


    /**
     * 뷰페이저를 위한 어댑터 정의
     */
    public class ViewPagerAdapter extends PagerAdapter {
        // sample names
        private String[] names = { "John", "Mike", "Sean", "Kim"};
        // sample image resource ids
        private int[] resIds = {R.drawable.dream01, R.drawable.dream01, R.drawable.dream03, R.drawable.dream03  };
        // sample call numbers
        private String[] callNumbers = {"010-7777-1234", "010-7788-1234", "010-7799-1234", "010-1234-1234"  };

        /**
         * Context 객체
         */
        private Context mContext;

        /**
         * 초기화
         *
         * @param context
         */
        public ViewPagerAdapter( Context context ) {
            mContext = context;
        }

        /**
         * 페이지 갯수
         */
        public int getCount() {
            return 4;
        }





        /**
         * 뷰페이저가 만들어졌을 때 호출됨
         */
        public Object instantiateItem(ViewGroup container, int position) { //position은 현재 페이지의 위치인듯
            // create a instance of the page and set data

            Toast.makeText(getApplicationContext(), "position:"+position, Toast.LENGTH_LONG).show();

            PersonPage page = new PersonPage(mContext);
            page.setNameText(names[position]);
            page.setImage(resIds[position]);
            page.setCallNumber(callNumbers[position]);

            // 컨테이너에 추가 (컨테이너가 뭐지? 무역상인가? 시발)
            container.addView(page, position);

            return page;
        }

        /**
         * Called to remove the page
         */
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View)view);
        }

        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

    }


}

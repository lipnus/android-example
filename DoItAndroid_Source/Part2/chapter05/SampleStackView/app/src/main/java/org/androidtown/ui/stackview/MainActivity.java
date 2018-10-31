package org.androidtown.ui.stackview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.StackView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 스택뷰를 사용하는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StackView stk = (StackView)this.findViewById(R.id.stackView1);
        stk.setCameraDistance(10.0f);

        ArrayList<StackItem> items = new ArrayList<StackItem>();
        items.add(new StackItem("(1) 김민영", this.getResources().getDrawable(R.drawable.contact)));
        items.add(new StackItem("(2) 홍수영", this.getResources().getDrawable(R.drawable.contact)));
        items.add(new StackItem("(3) 진가람", this.getResources().getDrawable(R.drawable.contact)));
        items.add(new StackItem("(4) 김진수", this.getResources().getDrawable(R.drawable.contact)));
        items.add(new StackItem("(5) 박찬형", this.getResources().getDrawable(R.drawable.contact)));

        StackAdapter adapt = new StackAdapter(this, R.layout.stack_item, items);

        stk.setAdapter(adapt);

    }

    /**
     * 어댑터 클래스 정의
     */
    public class StackAdapter extends ArrayAdapter<StackItem> {

        private ArrayList<StackItem> items;
        private Context ctx;

        public StackAdapter(Context context, int resId, ArrayList<StackItem> objects) {
            super(context, resId, objects);

            this.items = objects;
            this.ctx = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.stack_item, null);
            }

            StackItem m = items.get(position);

            if (m != null) {
                TextView text = (TextView) v.findViewById(R.id.textView1);
                ImageView img = (ImageView) v.findViewById(R.id.imageView1);

                if (text != null) {
                    text.setText(m.itemText);
                    img.setImageDrawable(m.itemPhoto);
                }
            }

            return v;
        }
    }

}

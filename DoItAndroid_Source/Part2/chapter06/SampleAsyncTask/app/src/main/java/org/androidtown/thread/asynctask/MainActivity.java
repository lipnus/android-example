package org.androidtown.thread.asynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * AsyncTask 를 이용하여 백그라운드 작업을 실행하는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    TextView textView01;
    ProgressBar progress;
    BackgroundTask task;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView01 = (TextView) findViewById(R.id.textView01);
        progress = (ProgressBar) findViewById(R.id.progress);

        // 실행 버튼 이벤트 처리
        Button executeBtn = (Button) findViewById(R.id.executeBtn);
        executeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 새로운 Task 객체를 만들고 실행
                task = new BackgroundTask();
                task.execute(100);
            }
        });

        // 취소 버튼 이벤트 처리
        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                task.cancel(true);
            }
        });
    }

    /**
     * 새로운 Task 객체를 정의
     */
    class BackgroundTask extends AsyncTask<Integer , Integer , Integer> {
        protected void onPreExecute() {
            value = 0;
            progress.setProgress(value);
        }

        protected Integer doInBackground(Integer ... values) {
            while (isCancelled() == false) {
                value++;
                if (value >= 100) {
                    break;
                } else {
                    publishProgress(value);
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {}
            }

            return value;
        }

        protected void onProgressUpdate(Integer ... values) {
            progress.setProgress(values[0].intValue());
            textView01.setText("Current Value : " + values[0].toString());
        }

        protected void onPostExecute(Integer result) {
            progress.setProgress(0);
            textView01.setText("Finished.");
        }

        protected void onCancelled() {
            progress.setProgress(0);
            textView01.setText("Cancelled.");
        }
    }

}

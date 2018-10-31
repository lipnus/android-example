package org.androidtown.gcm.push;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

/**
 * 푸시 메시지를 받는 쪽에서 단말 등록하고, 푸시 메시지를 보내는 쪽에서 메시지를 보내면, 푸시 메시지를 받는 쪽에서 메시지를 받아 화면에 보여주는 과정을 알 수 있습니다.
 * 이 예제는 푸시 서비스를 받는 쪽과 보내는 쪽이 하나로 합쳐진 것으로,
 * 실제 앱에서는 받는 쪽의 기능을 하나의 앱으로, 보내는 쪽의 기능을 하나의 앱으로 분리하여야 합니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends ActionBarActivity {
    public static final String TAG = "MainActivity";

    EditText messageInput;
    TextView messageOutput;

    /**
     * 서버 : Sender 객체 선언
     */
    Sender sender;

    Handler handler = new Handler();

    /**
     * collapseKey 설정을 위한 Random 객체
     */
    private Random random ;

    /**
     * 구글 서버에 메시지 보관하는 기간(초단위로 4주까지 가능)
     */
    private int TTLTime = 60;

    /**
     * 단말기에 메시지 전송 재시도 횟수
     */
    private	int RETRY = 3;

    /*
     * 등록된 ID 저장
     */
    ArrayList<String> idList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 서버 : GOOGLE_API_KEY를 이용해 Sender 초기화
        sender = new Sender(GCMInfo.GOOGLE_API_KEY);

        // 서버 : 전송할 메시지 입력 박스
        messageInput = (EditText) findViewById(R.id.messageInput);

        // 수신할 메시지 출력 박스
        messageOutput = (TextView) findViewById(R.id.messageOutput);


        // 단말 등록하기 버튼
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    // 단말 등록하고 등록 ID 받기
                    registerDevice();

                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 서버 : 전송하기 버튼
        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String data = messageInput.getText().toString();

                sendToDevice(data);
            }
        });

        // 인텐트를 전달받는 경우
        Intent intent = getIntent();
        if (intent != null) {
            processIntent(intent);
        }
    }

    /**
     * 단말 등록
     */
    private void registerDevice() {

        RegisterThread registerObj = new RegisterThread();
        registerObj.start();

    }

    /**
     * 푸시 메시지 전송
     */
    private void sendToDevice(String data) {

        SendThread thread = new SendThread(data);
        thread.start();

    }

    class RegisterThread extends Thread {
        public void run() {

            try {
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                String regId = gcm.register(GCMInfo.PROJECT_ID);
                println("푸시 서비스를 위해 단말을 등록했습니다.");
                println("등록 ID : " + regId);

                // 등록 ID 리스트에 추가 (현재는 1개만)
                idList.clear();
                idList.add(regId);

            } catch(Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    private void println(String msg) {
        final String output = msg;
        handler.post(new Runnable() {
            public void run() {
                Log.d(TAG, output);
                Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * GCM 메시지 전송을 위한 스레드
     */
    class SendThread extends Thread {
        String data;

        public SendThread(String inData) {
            data = inData;
        }

        public void run() {

            try {
                sendText(data);
            } catch(Exception ex) {
                ex.printStackTrace();
            }

        }

        public void sendText(String msg)
                throws Exception
        {

            if( random == null){
                random = new Random(System.currentTimeMillis());
            }

            String messageCollapseKey = String.valueOf(Math.abs(random.nextInt()));

            try {
                // 푸시 메시지 전송을 위한 메시지 객체 생성 및 환경 설정
                Message.Builder gcmMessageBuilder = new Message.Builder();
                gcmMessageBuilder.collapseKey(messageCollapseKey).delayWhileIdle(true).timeToLive(TTLTime);
                gcmMessageBuilder.addData("type","text");
                gcmMessageBuilder.addData("command", "show");
                gcmMessageBuilder.addData("data", URLEncoder.encode(data, "UTF-8"));

                Message gcmMessage = gcmMessageBuilder.build();

                // 여러 단말에 메시지 전송 후 결과 확인
                MulticastResult resultMessage = sender.send(gcmMessage, idList, RETRY);
                String output = "GCM 전송 메시지 결과 => " + resultMessage.getMulticastId()
                        + "," + resultMessage.getRetryMulticastIds() + "," + resultMessage.getSuccess();

                println(output);

            } catch(Exception ex) {
                ex.printStackTrace();

                String output = "GCM 메시지 전송 과정에서 에러 발생 : " + ex.toString();
                println(output);

            }

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent() called.");

        processIntent(intent);

        super.onNewIntent(intent);
    }

    /**
     * 수신자로부터 전달받은 Intent 처리
     *
     * @param intent
     */
    private void processIntent(Intent intent) {
        String from = intent.getStringExtra("from");
        if (from == null) {
            Log.d(TAG, "from is null.");
            return;
        }

        String command = intent.getStringExtra("command");
        String type = intent.getStringExtra("type");
        String data = intent.getStringExtra("data");

        println("DATA : " + command + ", " + type + ", " + data);
        messageOutput.setText("[" + from + "]로부터 수신한 데이터 : " + data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

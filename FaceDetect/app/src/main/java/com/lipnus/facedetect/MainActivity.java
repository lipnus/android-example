package com.lipnus.facedetect;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // 화면 크기. 카메라 마다 지원하는 사이즈는 다양하다.
    // 이건 사진을 찍는 사이즈가 아니다. 프리뷰의 사이즈다. 프리뷰 사이즈도 카메라마다 몇개가 정해져있다.
    // getSupportedPreviewSizes 를 이용해서 받아 올 수도 있지만.. 이게 안드로 2.1 이상 API 인가 그렇다.
    // 하위 버전이거나.. 이게 또 안되는 기계도 있다. 그럴땐 그냥 속편하게 사이즈를 지정하도록 하자.
    // 참고로 (넥서스원)에서 지원하는 프리뷰 사이즈는 아래와 같다.
    // 800x480,720x480,640x480,576x432,480x320,384x288,352x288,320x240,240x160,176x144

    public final static int SCREEN_WIDTH = 320;
    public final static int SCREEN_HEIGHT = 240;

    // myData 가 전달받은 이미지 데이터이다. 그외에는 화면출력뿐...
    public static PreviewData myData = new PreviewData();



    //블루투스
    //==============================================================================================

    // 사용자 정의 함수로 블루투스 활성 상태의 변경 결과를 App으로 알려줄때 식별자로 사용됨(0보다 커야함)
    static final int REQUEST_ENABLE_BT = 10;
    int mPariedDeviceCount = 0;
    Set<BluetoothDevice> mDevices;

    // 폰의 블루투스 모듈을 사용하기 위한 오브젝트.
    BluetoothAdapter mBluetoothAdapter;

    /**
     BluetoothDevice 로 기기의 장치정보를 알아낼 수 있는 자세한 메소드 및 상태값을 알아낼 수 있다.
     연결하고자 하는 다른 블루투스 기기의 이름, 주소, 연결 상태 등의 정보를 조회할 수 있는 클래스.
     현재 기기가 아닌 다른 블루투스 기기와의 연결 및 정보를 알아낼 때 사용.
     */
    BluetoothDevice mRemoteDevie;

    // 스마트폰과 페어링 된 디바이스간 통신 채널에 대응 하는 BluetoothSocket
    BluetoothSocket mSocket = null;
    OutputStream mOutputStream = null;
    InputStream mInputStream = null;
    String mStrDelimiter = "\n";
    char mCharDelimiter =  '\n';


    Thread mWorkerThread = null;
    byte[] readBuffer;
    int readBufferPosition;

    EditText mEditSend;
    TextView mEditReceive;
    Button mButtonSend;
    //==============================================================================================
    //음악
    MediaPlayer mp = new MediaPlayer();

    //프로그래스바
    ProgressDialog loading;

    //asynctask에 쓰이는거
    BackgroundTask task;
    int value;
    boolean st0 = false;
    boolean st1 = false;
    boolean st2 = false;
    boolean st3 = false;

    //블루투스와의 연결상태
    boolean connectBlue = false;

    //상태를 표시해주는 텍스트뷰
    TextView mainTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkDangerousPermissions();

        setContentView(R.layout.activity_main);


        mainTv = (TextView) findViewById(R.id.mainTv);

        // 새로운 Task 객체를 만들고 실행
        task = new BackgroundTask();
        task.execute(100);



    }

    public void onClick_connectBlue(View v){
        // 블루투스 활성화
        checkBluetooth();
        connectBlue = true;
    }

    public void musicControl(int type){

        //재생중인 상태이면 정지
        if (mp != null) {mp.pause();}

        //메모리를 릴리즈해준다(이거 안하면 일정 횟수 이상 실행하면 재생안됨)
        mp.release();

        switch (type) {
            case 1:
                mp = MediaPlayer.create(this, R.raw.robotsound);
                mp.start();
                break;

            case 2:
                mp = MediaPlayer.create(this, R.raw.welcome);
                mp.start();
                break;
        }
    }

    /**=============================================================================================
     *
     * AyncTask를 이용하여 값을 확인하고 블루투스로 전송
     *
     * =============================================================================================
     */


    class BackgroundTask extends AsyncTask<Integer , Integer , Integer> {
        protected void onPreExecute() {
            value = 0;
        }

        protected Integer doInBackground(Integer ... values) {
            while (isCancelled() == false) {

                    //0.1초마다 체크
                    publishProgress(value);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {}
            }

            return value;
        }

        // faceState : 0:대기모드(정지)  1:얼굴최초발견(인사?하기)  2:얼굴재발견(일시정지)  3:움직
        protected void onProgressUpdate(Integer ... values) {

            if(CustomApplication.faceState == 0 && st0 == false){
                Log.d("AAAA", "대기모드(정지)");
                mainTv.setText("대기모드(정지)");



                if(connectBlue==true) {sendData("0");}
                st0 = true;
                st1 = false; st2 = false; st3 = false;

            }else if(CustomApplication.faceState == 1 && st1 == false){
                Log.d("AAAA", "얼굴최초발견(발견을 표시)");
                mainTv.setText("얼굴최초발견(발견을 표시)");

                //음악(웰컴)
                musicControl(2);

                if(connectBlue==true) {sendData("1");}
                st1 = true;
                st0 = false; st2 = false; st3 = false;

            }else if(CustomApplication.faceState == 2 && st2 == false){
                Log.d("AAAA", "얼굴재발견(일시정지)");
                mainTv.setText("얼굴재발견(일시정지)");

                //음악끄기
                if (mp != null) {mp.pause();}

                if(connectBlue==true) {sendData("2");}
                st2 = true;
                st0 = false; st1 = false; st3 = false;

            }else if(CustomApplication.faceState == 3 && st3 == false){
                Log.d("AAAA", "움직");
                mainTv.setText("움직");

                //음악 띠리띠리
                musicControl(1);

                if(connectBlue==true) {sendData("3");}
                st3 = true;
                st0 = false; st1 = false; st2 = false;
            }

            //mainTv.setText("Current Value : " + CustomApplication.faceState);
        }

        protected void onPostExecute(Integer result) {
            mainTv.setText("Finished.");
        }

        protected void onCancelled() {
            mainTv.setText("Cancelled.");
        }
    }


    //앱의 카메라 권한을 얻어오는 메소드
    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }


    //권한이 잘 되었는지 결과를 확인받는 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    // 블루투스 장치의 이름이 주어졌을때 해당 블루투스 장치 객체를 페어링 된 장치 목록에서 찾아내는 코드.
    BluetoothDevice getDeviceFromBondedList(String name) {
        // BluetoothDevice : 페어링 된 기기 목록을 얻어옴.
        BluetoothDevice selectedDevice = null;
        // getBondedDevices 함수가 반환하는 페어링 된 기기 목록은 Set 형식이며,
        // Set 형식에서는 n 번째 원소를 얻어오는 방법이 없으므로 주어진 이름과 비교해서 찾는다.
        for(BluetoothDevice deivce : mDevices) {
            // getName() : 단말기의 Bluetooth Adapter 이름을 반환
            if(name.equals(deivce.getName())) {
                selectedDevice = deivce;
                break;
            }
        }
        return selectedDevice;
    }

    // 문자열 전송하는 함수(쓰레드 사용 x)
    void sendData(String msg) {
        //msg += mStrDelimiter;  // 문자열 종료표시 (\n)
        try{
            // getBytes() : String을 byte로 변환
            // OutputStream.write : 데이터를 쓸때는 write(byte[]) 메소드를 사용함.
            // byte[] 안에 있는 데이터를 한번에 기록해 준다.
            mOutputStream.write(msg.getBytes());  // 문자열 전송.
        }catch(Exception e) {  // 문자열 전송 도중 오류가 발생한 경우
            Toast.makeText(getApplicationContext(), "데이터 전송중 오류가 발생",
                    Toast.LENGTH_LONG).show();
            finish();  // App 종료
        }
    }

    //  connectToSelectedDevice() : 원격 장치와 연결하는 과정을 나타냄.
    //   실제 데이터 송수신을 위해서는 소켓으로부터 입출력 스트림을 얻고 입출력 스트림을 이용하여 이루어 진다.
    void connectToSelectedDevice(String selectedDeviceName) {
        // BluetoothDevice 원격 블루투스 기기를 나타냄.
        mRemoteDevie = getDeviceFromBondedList(selectedDeviceName);
        // java.util.UUID.fromString : 자바에서 중복되지 않는 Unique 키 생성.
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            // 소켓 생성, RFCOMM 채널을 통한 연결.
            // createRfcommSocketToServiceRecord(uuid) : 이 함수를 사용하여 원격 블루투스 장치와
            //                                           통신할 수 있는 소켓을 생성함.
            // 이 메소드가 성공하면 스마트폰과 페어링 된 디바이스간 통신 채널에 대응하는
            //  BluetoothSocket 오브젝트를 리턴함.
            mSocket = mRemoteDevie.createRfcommSocketToServiceRecord(uuid);
            mSocket.connect(); // 소켓이 생성 되면 connect() 함수를 호출함으로써 두기기의 연결은 완료된다.

            // 데이터 송수신을 위한 스트림 얻기.
            // BluetoothSocket 오브젝트는 두개의 Stream을 제공한다.
            // 1. 데이터를 보내기 위한 OutputStrem
            // 2. 데이터를 받기 위한 InputStream
            mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream();

            // 데이터 수신 준비.
            beginListenForData();

        }catch(Exception e) { // 블루투스 연결 중 오류 발생
            Toast.makeText(getApplicationContext(),
                    "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            finish();  // App 종료
        }
    }

    // 데이터 수신(쓰레드 사용 수신된 메시지를 계속 검사함)
    void beginListenForData() {
        final Handler handler = new Handler();

        readBufferPosition = 0;                 // 버퍼 내 수신 문자 저장 위치.
        readBuffer = new byte[1024];            // 수신 버퍼.

        // 문자열 수신 쓰레드.
        mWorkerThread = new Thread(new Runnable()
        {
            @Override
            public void run() {
                // interrupt() 메소드를 이용 스레드를 종료시키는 예제이다.
                // interrupt() 메소드는 하던 일을 멈추는 메소드이다.
                // isInterrupted() 메소드를 사용하여 멈추었을 경우 반복문을 나가서 스레드가 종료하게 된다.
                while(!Thread.currentThread().isInterrupted()) {
                    try {
                        // InputStream.available() : 다른 스레드에서 blocking 하기 전까지 읽은 수 있는 문자열 개수를 반환함.
                        int byteAvailable = mInputStream.available();   // 수신 데이터 확인
                        if(byteAvailable > 0) {                        // 데이터가 수신된 경우.
                            byte[] packetBytes = new byte[byteAvailable];
                            // read(buf[]) : 입력스트림에서 buf[] 크기만큼 읽어서 저장 없을 경우에 -1 리턴.
                            mInputStream.read(packetBytes);
                            for(int i=0; i<byteAvailable; i++) {
                                byte b = packetBytes[i];
                                if(b == mCharDelimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    //  System.arraycopy(복사할 배열, 복사시작점, 복사된 배열, 붙이기 시작점, 복사할 개수)
                                    //  readBuffer 배열을 처음 부터 끝까지 encodedBytes 배열로 복사.
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);

                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable(){
                                        // 수신된 문자열 데이터에 대한 처리.
                                        @Override
                                        public void run() {
                                            //mStrDelimiter = "\n";
                                            mEditReceive.setText(mEditReceive.getText().toString() + data+ mStrDelimiter);
                                        }

                                    });
                                }
                                else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }

                    } catch (Exception e) {    // 데이터 수신 중 오류 발생.
                        Toast.makeText(getApplicationContext(), "데이터 수신 중 오류가 발생 했습니다.", Toast.LENGTH_LONG).show();
                        finish();            // App 종료.
                    }
                }
            }

        });

    }

    // 블루투스 지원하며 활성 상태인 경우.
    void selectDevice() {
        // 블루투스 디바이스는 연결해서 사용하기 전에 먼저 페어링 되어야만 한다
        // getBondedDevices() : 페어링된 장치 목록 얻어오는 함수.
        mDevices = mBluetoothAdapter.getBondedDevices();
        mPariedDeviceCount = mDevices.size();

        if(mPariedDeviceCount == 0 ) { // 페어링된 장치가 없는 경우.
            Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            finish(); // App 종료.
        }
        // 페어링된 장치가 있는 경우.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 장치 선택");

        // 각 디바이스는 이름과(서로 다른) 주소를 가진다. 페어링 된 디바이스들을 표시한다.
        List<String> listItems = new ArrayList<String>();
        for(BluetoothDevice device : mDevices) {
            // device.getName() : 단말기의 Bluetooth Adapter 이름을 반환.
            listItems.add(device.getName());
        }
        listItems.add("취소");  // 취소 항목 추가.


        // CharSequence : 변경 가능한 문자열.
        // toArray : List형태로 넘어온것 배열로 바꿔서 처리하기 위한 toArray() 함수.
        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
        // toArray 함수를 이용해서 size만큼 배열이 생성 되었다.
        listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                // TODO Auto-generated method stub
                if(item == mPariedDeviceCount) { // 연결할 장치를 선택하지 않고 '취소' 를 누른 경우.
                    Toast.makeText(getApplicationContext(), "연결할 장치를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else { // 연결할 장치를 선택한 경우, 선택한 장치와 연결을 시도함.
                    connectToSelectedDevice(items[item].toString());
                }
            }

        });

        builder.setCancelable(false);  // 뒤로 가기 버튼 사용 금지.
        AlertDialog alert = builder.create();
        alert.show();
    }


    void checkBluetooth() {
        /**
         * getDefaultAdapter() : 만일 폰에 블루투스 모듈이 없으면 null 을 리턴한다.
         이경우 Toast를 사용해 에러메시지를 표시하고 앱을 종료한다.
         */
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null ) {  // 블루투스 미지원
            Toast.makeText(getApplicationContext(), "기기가 블루투스를 지원하지 않습니다.", Toast.LENGTH_LONG).show();
            finish();  // 앱종료
        }
        else { // 블루투스 지원
            /** isEnable() : 블루투스 모듈이 활성화 되었는지 확인.
             *               true : 지원 ,  false : 미지원
             */
            if(!mBluetoothAdapter.isEnabled()) { // 블루투스 지원하며 비활성 상태인 경우.
                Toast.makeText(getApplicationContext(), "현재 블루투스가 비활성 상태입니다.", Toast.LENGTH_LONG).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // REQUEST_ENABLE_BT : 블루투스 활성 상태의 변경 결과를 App 으로 알려줄 때 식별자로 사용(0이상)
                /**
                 startActivityForResult 함수 호출후 다이얼로그가 나타남
                 "예" 를 선택하면 시스템의 블루투스 장치를 활성화 시키고
                 "아니오" 를 선택하면 비활성화 상태를 유지 한다.
                 선택 결과는 onActivityResult 콜백 함수에서 확인할 수 있다.
                 */
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else // 블루투스 지원하며 활성 상태인 경우.
                selectDevice();
        }
    }




    // onDestroy() : 어플이 종료될때 호출 되는 함수.
    //               블루투스 연결이 필요하지 않는 경우 입출력 스트림 소켓을 닫아줌.
    @Override
    protected void onDestroy() {
        try{
            mWorkerThread.interrupt(); // 데이터 수신 쓰레드 종료
            mInputStream.close();
            mSocket.close();
        }catch(Exception e){}
        super.onDestroy();
    }


    // onActivityResult : 사용자의 선택결과 확인 (아니오, 예)
    // RESULT_OK: 블루투스가 활성화 상태로 변경된 경우. "예"
    // RESULT_CANCELED : 오류나 사용자의 "아니오" 선택으로 비활성 상태로 남아 있는 경우  RESULT_CANCELED
    /**
     사용자가 request를 허가(또는 거부)하면 안드로이드 앱의 onActivityResult
     메소도를 호출해서 request의 허가/거부를 확인할수 있다.
     첫번째 requestCode: startActivityForResult 에서 사용했던 요청 코드. REQUEST_ENABLE_BT 값
     두번째 resultCode : 종료된 액티비티가 setReuslt로 지정한 결과 코드.
     RESULT_OK, RESULT_CANCELED 값중 하나가 들어감.
     세번째 data   : 종료된 액티비티가 인테트를 첨부했을 경우, 그 인텐트가 들어있고 첨부하지 않으면 null
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // startActivityForResult 를 여러번 사용할 땐 이런 식으로
        // switch 문을 사용하여 어떤 요청인지 구분하여 사용함.
        switch(requestCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK) { // 블루투스 활성화 상태
                    selectDevice();
                }
                else if(resultCode == RESULT_CANCELED) { // 블루투스 비활성화 상태 (종료)
                    Toast.makeText(getApplicationContext(), "블루투수를 사용할 수 없어 프로그램을 종료합니다",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}


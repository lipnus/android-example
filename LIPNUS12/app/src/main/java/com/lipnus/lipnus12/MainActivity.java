package com.lipnus.lipnus12;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    TextView tV;
    TextView howManyPhoneNumberTv;

    AsynThread task;
    int value;




    ArrayList<Integer> phoneIndexList = new ArrayList<Integer>();
    int phoneCount;
    int recordIndex = 0;











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        howManyPhoneNumberTv = (TextView) findViewById(R.id.howManyPhoneNumberTextView);
        tV = (TextView) findViewById(R.id.textView);
    }

    public void onClick_Aync(View v){
        task = new AsynThread();
        task.execute(100);
    }

    class AsynThread extends AsyncTask<Integer , Integer , Integer> {
        protected void onPreExecute() {
            //시작
            value = 0;
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
            //중간중간에 출력
            tV.setText(Integer.toString(value));
        }

        protected void onPostExecute(Integer result) {
            //종료
            //progress.setProgress(0);
            //textView01.setText("Finished.");
        }

        protected void onCancelled() {
            //정지
            //progress.setProgress(0);
            //textView01.setText("Cancelled.");
        }
    }


    public void onClick(View v) {
        getContact();
    }

    public void getContact() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(Phone.CONTENT_URI, null, null, null, null);

        //개수
        int count = cursor.getCount();
        howManyPhoneNumberTv.setText(Integer.toString(count));
        Toast.makeText(this, "전화번호의 개수: " + count, Toast.LENGTH_LONG).show();






        // find only contacts with phone numbers
        phoneCount = 0;
        phoneIndexList.clear();
        while(cursor.moveToNext()) {
            String hasPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (hasPhoneNumber.equalsIgnoreCase("1")) {
                int curPosition = cursor.getPosition();
                phoneIndexList.add(curPosition);
                phoneCount++;
            }
        }
        tV.append("Count of contacts with phone number : " + phoneCount);

        // display the first contact
        int index = phoneIndexList.get(recordIndex);

        //if (cursor == null) {
        //    queryContacts();
        //}

        cursor.moveToPosition(index);



        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

        if (name != null) {
            tV.append("이름:"+name+ "\n");
        } else {

        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String hasPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        if (hasPhoneNumber.equalsIgnoreCase("1")) {
            hasPhoneNumber = "true";
        } else {
            hasPhoneNumber = "false" ;
        }

        String phoneNumber = null;
        if (Boolean.parseBoolean(hasPhoneNumber)) {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
            while (phones.moveToNext()) {
                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phones.close();
        }

        if (phoneNumber != null) {
            tV.append("번호:" + phoneNumber + "\n");
        } else {
            tV.append("번호없냐?");
        }


        //int nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        //String name = cursor.getString(nameIdx);
        //

        /*
        // 번호 표시
        int phoneIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        String phone = cursor.getString(phoneIdx);
        phoneView.setText(phone);

        // 사진 표시
        ContentResolver cr = getContentResolver();
        int contactId_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        Long contactId = cursor.getLong(contactId_idx);
        Log.e("###", contactId_idx + " | " + contactId + " | " + name);

        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);

        // 사진없으면 기본 사진 보여주기
        if (input != null)
        {
            Bitmap contactPhoto = BitmapFactory.decodeStream(input);
            photoView.setImageBitmap(contactPhoto);
        }
        else
        {
            photoView.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo));
        }
        */
    }



    //이 단추를 눌러야 권한을 받음
    public void onClick_getAllow(View v){

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            tV.append("권한 이미 있음\n");
        } else {
            tV.append("권한 없음\n");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                tV.append("권한 설명 필요\n");

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.READ_CONTACTS},
                        1);
            }
        }
    }

    //허가받은거 확인하는 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "SMS 권한을 사용자가 승인함.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "SMS 권한 거부됨.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

}

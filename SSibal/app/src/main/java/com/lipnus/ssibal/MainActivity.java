package com.lipnus.ssibal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Context ct;
    TextView tV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tV = (TextView) findViewById(R.id.textview);
        setContext(this);
    }

    public void setContext(Context context) {
        ct = context;
    }

    public void onClick(View v) {

        Intent iT = new Intent(Intent.ACTION_PICK);
        iT.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(iT, 0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Cursor cursor = getContentResolver().query(data.getData(),
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);

        cursor.moveToFirst();
        String sName = cursor.getString(0);
        String sNumber = cursor.getString(1);
        cursor.close();

    }

    public void show(String sName, String sNumber) {
        tV.setText("이름: " + sName + "   번호: " + sNumber);
    }


    public void onClick2(View v) {

        getContact(ct);

    }

    public void getContact(Context context) {

        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);



        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

        Cursor phones = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
        while (phones.moveToNext()) {
            String number = phones.getString(phones
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            switch (type) {
                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                    Log.i("TYPE_HOME", "" + number);
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                    Log.i("TYPE_MOBILE", "" + number);
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                    Log.i("TYPE_WORK", "" + number);
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                    Log.i("TYPE_FAX_WORK", "" + number);
                    break;
                case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                    Log.i("TYPE_FAX_HOME", "" + number);
                    break;

                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                    Log.i("TYPE_OTHER", "" + number);
                    break;
            }
        }
        phones.close();
        cursor.close();

    }
}


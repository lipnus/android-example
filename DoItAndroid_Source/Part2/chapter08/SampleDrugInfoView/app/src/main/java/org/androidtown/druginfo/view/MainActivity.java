package org.androidtown.druginfo.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 약품정보 리스트를 샘플로 만들어볼 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    EditText editSearch;
    DataListView listView;
    IconTextListAdapter adapter;
    Button btnSearch;
    InputMethodManager imm;

    String strSearch;
    String strSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        // adapter
        adapter = new IconTextListAdapter(this);

        // ListView
        listView = new DataListView(this);

        LinearLayout linLayout = (LinearLayout)findViewById(R.id.linLayoutDrugList );
        linLayout.addView(listView);

        editSearch = (EditText) findViewById(R.id.editSearch);


        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {

                strSearch = editSearch.getText().toString();
                strSearchQuery = strSearch.concat("%");

                // open database
                DatabaseHelper.mContext = getApplicationContext();
                DatabaseHelper.openDatabase( DatabaseHelper.drugDatabaseFile);
                Cursor cursor = DatabaseHelper.queryMasterTable(strSearchQuery);

                AddCursorData(cursor);

                // bind Adapter
                listView.setAdapter(adapter);

                // hide soft keypad
                imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

            }
        });



        listView.setOnDataSelectionListener( new OnDataSelectionListener () {

            public void onDataSelected(AdapterView parent, View v, int position, long id) {
                // make intent
                IconTextItem selectItem = (IconTextItem)adapter.getItem(position);

                //String title = selectItem.getData(0);
                Bundle bundle = new Bundle();
                bundle.putString("data0", selectItem.getData(0));
                bundle.putString("data1", selectItem.getData(1));
                bundle.putString("data2", selectItem.getData(2));
                bundle.putString("data3", selectItem.getData(3));

                Intent intent = new Intent( getApplicationContext(), DrugDetailActivity.class );
                intent.putExtras(bundle);
                startActivity ( intent );
            }
        });

        checkDangerousPermissions();
    }

    private void checkDangerousPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                String[] permissions = {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

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


    protected void onDestroy() {
        super.onDestroy();

        DatabaseHelper.closeDatabase();
    }

    public void AddCursorData ( Cursor outCursor ) {

        int recordCount = outCursor.getCount();
        println("cursor count : " + recordCount + "\n");

        adapter.clear();

        // get column index
        int drugCodeCol = outCursor.getColumnIndex("DRUGCODE");
        int drugNameCol = outCursor.getColumnIndex("DRUGNAME");
        int prodKNameCol = outCursor.getColumnIndex("PRODKRNM");
        int distrNameCol = outCursor.getColumnIndex("DISTRNAME");

        // get reaources
        Resources res = getResources();

        for (int i = 0; i < recordCount; i++) {
            outCursor.moveToNext();
            String drugCode = outCursor.getString(drugCodeCol);
            String drugName = outCursor.getString(drugNameCol);
            String prodKName = outCursor.getString(prodKNameCol);
            String distrName = outCursor.getString(distrNameCol);

            adapter.addItem( new IconTextItem(res.getDrawable(R.drawable.capsule1),prodKName,drugCode ,drugName,distrName));
        }

        outCursor.close();
    }

    public void println(String msg) {
        Log.d(TAG, msg);
    }

}

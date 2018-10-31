package com.lipnus.webconnecttest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class PosttestActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextAdd;
    TextView tV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posttest);

        editTextName = (EditText) findViewById(R.id.name);
        editTextAdd = (EditText) findViewById(R.id.address);

        tV = (TextView) findViewById(R.id.result);

    }

    public void insert(View view){
        String name = editTextName.getText().toString();
        String address = editTextAdd.getText().toString();

        insertToDatabase(name, address);


    }

    private void insertToDatabase(String name, String address){

        class InsertData extends AsyncTask<String, Void, String>{
            ProgressDialog loading;



            @Override //시작
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PosttestActivity.this, "Please Wait", null, true, true);
            }

            @Override //끝
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                tV.setText(s);

            }

            @Override //메인루프
            protected String doInBackground(String... params) {

                try{
                    String name = (String)params[0];
                    String address = (String)params[1];

                    String link="http://lipnus.ivyro.net/logintest.php";
                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();



                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(name,address);
    }
}

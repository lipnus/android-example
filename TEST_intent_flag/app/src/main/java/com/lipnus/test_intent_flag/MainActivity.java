package com.lipnus.test_intent_flag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    String textData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.inputEt);

        Fruit f1 = new Fruit(10, 20);
        int b = f1.fucking(10, 20);

    }



    public void onClick_1 (View v){

        textData = editText.getText().toString();

        Intent intent = new Intent( getApplicationContext(), Main2Activity.class );
        intent.putExtra("nickname", textData);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("number", 11);

        startActivity(intent);

    }

//    public void onClick_1(View v){
//        Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
//        startActivity(intent);
//        finish();
//    }
}

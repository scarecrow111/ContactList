package com.example.nubia.contacttest.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nubia.contacttest.R;
import com.example.nubia.contacttest.bean.DatabaseConstant;
import com.example.nubia.contacttest.util.RegExNumber;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    EditText addName;
    EditText addNumber;
    Uri uri1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        addName = (EditText) findViewById(R.id.add_name);
        addNumber = (EditText)findViewById(R.id.add_number);
        Button cancelAddition = (Button)findViewById(R.id.cancel_action);
        Button submitAddition = (Button)findViewById(R.id.submit_action);
        cancelAddition.setOnClickListener(this);
        submitAddition.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_action:
                finish();
                break;
            case R.id.submit_action:
                String savedName = addName.getText().toString();
                String savedNumber = addNumber.getText().toString();
                boolean judge = RegExNumber.isMobile(savedNumber);
                if (judge == true && savedName != null) {
                    addContactToDB(savedName, savedNumber);
                    String newIdStr = uri1.getPathSegments().get(1);
                    Intent intentSubmit = new Intent();
                    intentSubmit.putExtra(DatabaseConstant.DB_NAME, savedName);
                    intentSubmit.putExtra(DatabaseConstant.DB_NUMBER, savedNumber);
                    intentSubmit.putExtra(DatabaseConstant.DB_ID, newIdStr);
                    setResult(RESULT_OK, intentSubmit);
                    finish();
                } else {
                    Toast.makeText(this, "输入不合法", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    private void addContactToDB(String name, String number){
        Uri uri = Uri.parse(MainActivity.URI);
        ContentValues values = new ContentValues();
        values.put(DatabaseConstant.DB_NAME, name);
        values.put(DatabaseConstant.DB_ID, number);
        uri1 = getContentResolver().insert(uri, values);
    }
}

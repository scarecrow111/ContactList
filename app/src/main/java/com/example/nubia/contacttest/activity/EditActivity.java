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
import com.example.nubia.contacttest.util.RegExNumber;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{
    EditText editName;
    EditText editNumber;
    private long idL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editName = (EditText) findViewById(R.id.edit_name);
        editNumber = (EditText)findViewById(R.id.edit_number);
        Button cancelEdition = (Button)findViewById(R.id.cancel_edit_action);
        Button submitEdition = (Button)findViewById(R.id.submit_edit_action);
        cancelEdition.setOnClickListener(this);
        submitEdition.setOnClickListener(this);
        Intent intent = getIntent();
        idL = intent.getLongExtra("id", 1);
        String tempName = intent.getStringExtra("name");
        String tempNumber = intent.getStringExtra("number");
        editName.setText(tempName);
        editNumber.setText(tempNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_edit_action:
                finish();
                break;
            case R.id.submit_edit_action:
                String savedName = editName.getText().toString();
                String savedNumber = editNumber.getText().toString();
                boolean judge = RegExNumber.isMobile(savedNumber);
                if (judge == true && savedName != null){
                    modifyContactToDB(idL, savedName, savedNumber);
                    Intent intentEditSubmit = new Intent();
                    intentEditSubmit.putExtra("name", savedName);
                    intentEditSubmit.putExtra("number", savedNumber);
                    setResult(RESULT_OK, intentEditSubmit);
                    finish();
                } else {
                    Toast.makeText(this, "输入不合法", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    private void modifyContactToDB(long id, String name, String number){
        Uri uri = Uri.parse(MainActivity.URI +"/"+ id);
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("number", number);
        getContentResolver().update(uri, values, null, null);
    }
}

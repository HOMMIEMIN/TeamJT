package com.example.homin.test1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.sql.ResultSet;

public class WriteActivity extends AppCompatActivity {
    private EditText et1,et2;
    public static final String TITLE_KEY = "subject";
    public static final String BODY_KEY = "body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        et1 = findViewById(R.id.editText3);
        et2 = findViewById(R.id.editText4);
    }

    public void clickSave(View view) {
        String title = et1.getText().toString();
        String body = et2.getText().toString();
        Intent intent = new Intent();
        intent.putExtra(TITLE_KEY,title);
        intent.putExtra(BODY_KEY,body);
        setResult(RESULT_OK,intent);
        finish();
    }
}

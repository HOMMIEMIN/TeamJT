package com.example.homin.test1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ReadMemoPlusUrlActivity extends AppCompatActivity {

    public static final String TAG = "IMG";

    private ImageView imageView;
    public static final String MEMO_URL = "memo_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_memo_plus_url);

        // 어플 위에 뜨는 액션바 숨기는 코드!
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        imageView = findViewById(R.id.ReadMemoplusurl_view);

        Intent intent = getIntent();
        final String url = intent.getStringExtra(MEMO_URL);
        Log.i(TAG,"url" + url);
        Glide.with(this).load(url).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void EndFinish(View view) {
        finish();
    }
}

package com.example.homin.test1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ReadMemoActivity extends Activity {

    public static final String MEMO_NAME = "memo_name";
    public static final String MEMO_ID = "memo_id";
    public static final String MEMO_URL = "memo_url";
    public static final String MEMO_TITLE = "memo_title";
    public static final String MEMO_CONTENT = "memo_content";
    public static final String MEMO_TIME = "memo_time";
    private ImageView imageView;
    private TextView tv_name, tv_title, tv_content, tv_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_read_memo);

        imageView = findViewById(R.id.imageView_Memo);
        tv_name = findViewById(R.id.textViewName_Memo);
        tv_title = findViewById(R.id.textViewTitle_Memo);
        tv_content = findViewById(R.id.textViewContent_Memo);
        tv_time = findViewById(R.id.textViewTime_Memo);

        Intent intent = getIntent();
        String name = intent.getStringExtra(MEMO_NAME);
        String id = intent.getStringExtra(MEMO_ID);
        String title = intent.getStringExtra(MEMO_TITLE);
        String content = intent.getStringExtra(MEMO_CONTENT);
        String url = intent.getStringExtra(MEMO_URL);
        String time = intent.getStringExtra(MEMO_TIME);

        Glide.with(this).load(url).centerCrop().override(300,300).bitmapTransform(new CropCircleTransformation(this))
                .into(imageView);

        tv_name.setText(name);
        tv_title.setText(title);
        tv_content.setText(content);
        tv_time.setText(time);
        Log.i("fff2",name);
        Log.i("fff2",title);
        Log.i("fff2",content);
        Log.i("fff2",time);

    }
}

package com.example.homin.test1;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ReadMemoActivity extends Activity {

    public static final String TAG = "wnalstjrdl";

    public static final String MEMO_NAME = "memo_name";
    public static final String MEMO_ID = "memo_id";
    public static final String MEMO_URL = "memo_url";
    public static final String MEMO_TITLE = "memo_title";
    public static final String MEMO_CONTENT = "memo_content";
    public static final String MEMO_TIME = "memo_time";

    // 이미지 확대 필요 변수들
    private static String key;
    private UserDataTable userDataTable;
    private PopupWindow popupWindow;

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
        final String content = intent.getStringExtra(MEMO_CONTENT);
        final String url = intent.getStringExtra(MEMO_URL);
        String time = intent.getStringExtra(MEMO_TIME);

        Glide.with(this).load(url).override(400,350).into(imageView);

        tv_name.setText(name);
        tv_title.setText(title);
        tv_content.setText(content);
        tv_time.setText(time);
        Log.i("fff2",name);
        Log.i("fff2",title);
        Log.i("fff2",content);
        Log.i("fff2",time);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 이미지 확대
                Intent intent = new Intent(ReadMemoActivity.this, ReadMemoPlusUrlActivity.class);
                intent.putExtra(MEMO_URL, url);
                startActivity(intent);
//
            }
        });


        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 이미지 확대
                Intent intent = new Intent(ReadMemoActivity.this, ReadMemoPlusUrlActivity.class);
                intent.putExtra(MEMO_URL, url);
                startActivity(intent);
            }
        });


        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 이미지 확대
                Intent intent = new Intent(ReadMemoActivity.this, ReadMemoPlusUrlActivity.class);
                intent.putExtra(MEMO_URL, url);
                startActivity(intent);
            }
        });
    }


    private void pulsSajin() {
        //uri 받기
        Intent intent = getIntent();
        final String url = intent.getStringExtra(MEMO_URL);
        //TODO: 이미지 확대
        if (url == null) {
            popupWindow.dismiss();
            Toast.makeText(ReadMemoActivity.this, "저장된 이미지가 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG,"글쓴이 클릭시"+url);
            View popupView = getLayoutInflater().inflate(R.layout.popup_plusdetailimage, null);
            //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정
            popupWindow= new PopupWindow(
                    popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            }); // click

            ImageView plusDetailImage = popupView.findViewById(R.id.popup_PlusDetailImageVeiw);
            popupWindow.setFocusable(true); // 외부영역 선택시 종료
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            Log.i(TAG,"보여주기 직전 값값"+url);
            Glide.with(getApplicationContext()).load(url).into(plusDetailImage);

        }

    }
}

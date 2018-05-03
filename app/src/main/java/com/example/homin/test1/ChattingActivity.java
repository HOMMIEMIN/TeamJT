package com.example.homin.test1;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.example.homin.test1.FriendFragment.*;

public class ChattingActivity extends AppCompatActivity {

    private static final String TAG = "action";

    public void chattingbackkey(View view) {
        finish();
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder>{


        @Override
        public int getItemViewType(int position) {
            if(cList.get(position).getName().equals(DaoImple.getInstance().getLoginId())) {
                return 0;
            }else{
                return 1;
            }
        }

        @Override
        public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ChatHolder holder = null;


            switch (viewType) {
                case 0:
                    LayoutInflater inflater = LayoutInflater.from(ChattingActivity.this);
                    View itemView = inflater.inflate(R.layout.chat_layout2, parent, false);
                    holder = new ChatHolder(itemView);
                break;
                case 1:
                    LayoutInflater inflater2 = LayoutInflater.from(ChattingActivity.this);
                    View itemView2 = inflater2.inflate(R.layout.chat_layout, parent, false);
                    holder = new ChatHolder(itemView2);
                break;
            }


            return holder;
        }

        @Override
        public void onBindViewHolder(ChatAdapter.ChatHolder holder, int position) {
            FragmentActivity activity = DaoImple.getActivity(ChattingActivity.this);

                holder.tv1.setText(cList.get(position).getName());
                holder.tv2.setText(cList.get(position).getChat());
                holder.tv3.setText(cList.get(position).getTime());
                String id = holder.tv1.getText().toString();

            if(holder.getItemViewType() == 0) {
                if (activity != null) {
                    if(DaoImple.getInstance().getContact().getResizePictureUrl() != null) {
//                        대화창 네모난 이미지
//                        Glide.with(activity).load(DaoImple.getInstance().getContact().getResizePictureUrl())
//                                .bitmapTransform(new CropCircleTransformation(ChattingActivity.this)).centerCrop()
//                                .into(holder.iv);
//                      대화창 동그란 이미지
                        holder.iv.setBackground(new ShapeDrawable(new OvalShape()));
                        holder.iv.setClipToOutline(true);
                        Glide.with(activity).load(DaoImple.getInstance().getContact().getResizePictureUrl()).into(holder.iv);

                    }else{
                        holder.iv.setImageResource(R.drawable.p1);
                    }
                    holder.tv2.setBackground(getDrawable(R.drawable.ddd1));
                }
            }else {
                if (activity != null) {
                    if (yourImage != null) {
//                      대화창 네모난 이미지
//                        Glide.with(activity).load(yourImage)
//                                .bitmapTransform(new CropCircleTransformation(ChattingActivity.this)).centerCrop()
//                                .into(holder.iv);

//                      대화창 동그란 이미지
                        holder.iv.setBackground(new ShapeDrawable(new OvalShape()));
                        holder.iv.setClipToOutline(true);
                        Glide.with(activity).load(yourImage).into(holder.iv);


                        Log.i(TAG,"리싸이클러뷰"+ yourImage);
                    } else {
                        holder.iv.setImageResource(R.drawable.p1);
                    }
                }
                holder.tv2.setBackground(getDrawable(R.drawable.ddd));
            }


        }

        @Override
        public int getItemCount() {
            return cList.size();
        }

        class ChatHolder extends RecyclerView.ViewHolder{
            ImageView iv;
            TextView tv1;
            TextView tv2;
            TextView tv3;

            public ChatHolder(View itemView) {
                super(itemView);
                iv = itemView.findViewById(R.id.imageView_chatLaout);
                tv1 = itemView.findViewById(R.id.textView_chatLayout1);
                tv2 = itemView.findViewById(R.id.textView_chatLaout2);
                tv3 = itemView.findViewById(R.id.textView_Time);
            }
        }
    }

    private RecyclerView recyclerView;
    private EditText et;
    private Button btn;
    private List<Chat> cList;
    private DatabaseReference reference;
    private int position1;
    private ChatAdapter adapter;
    private String youId;
    private String yourName;
    private boolean check;
    private String putKey;
    private String yourImage;
    private TextView textView;
    private Chat chat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

//        DaoImple.getInstance().getcList().get(cList.size()-1).getChat();

        et = findViewById(R.id.editText_sends);
        btn = findViewById(R.id.button_sends);
        recyclerView = findViewById(R.id.recyclerView_chatting);
        DaoImple.getInstance().setChattingActivity(this);
        if (cList != null) {
            Log.i(TAG, "(onCreate)cList" + cList);
        } else {
            Log.i(TAG, "cList is null");
        }

        cList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();

        youId = intent.getStringExtra(CHAT_YOURID);
        Log.i(TAG,"gg1_youId"+youId);
        yourName = intent.getStringExtra(CHAT_YOURNAME);
        check = intent.getBooleanExtra("check",false);
        yourImage = intent.getStringExtra(CHAT_YOURIMAGE);
        recyclerView.hasFixedSize();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter();
        recyclerView.setAdapter(adapter);


        if(check){
            putKey = getPutKey(DaoImple.getInstance().getKey(),getKey(youId));
            check = false;
        }else {
            putKey = getPutKey(DaoImple.getInstance().getKey(),getKey(youId));
        }

        Log.i(TAG,"kaka"+youId);

        // 액션바
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle(getkey2(youId)+" 님과 대화창") ;

        cList.clear();


        reference.child("Chat").child(putKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                cList.add(chat);

               int a = cList.size();
               recyclerView.scrollToPosition(adapter.getItemCount()-1);
                adapter.notifyDataSetChanged();
                Log.i("ghals1","리스트 사이즈 : " + cList.size());


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
                SimpleDateFormat time = new SimpleDateFormat("a hh시mm분");
                time.setTimeZone(timeZone);

                Chat c = new Chat(DaoImple.getInstance().getLoginId(),DaoImple.getInstance().getLoginEmail(),
                        et.getText().toString(),time.format(date).toString());

                et.setText("");
                reference.child("Chat").child(putKey).push().setValue(c);


            }
        });


    }

    public String getKey(String id){
        int b = id.indexOf("@");
        String key1 = id.substring(0,b);
        int d = id.indexOf(".");
        String key2 = id.substring(b + 1,d);
        String key3 = id.substring(d + 1,id.length());
        String key = key1+key2+key3;

        return key;
    }

    public String getkey2 (String id){
        int b = id.indexOf("@");
        String key = id.substring(0,b);

        return key;
    }

    public String getPutKey(String myId, String yourId){
        String result = null;
        int my = 0;
        int you = 0;
        char[] arrayMyId = new char[myId.length()];
        char[] arrayYourId = new char[yourId.length()];
        arrayMyId = myId.toCharArray();
        arrayYourId = yourId.toCharArray();

        for(int a = 0 ; a < arrayMyId.length ; a++){
            my += arrayMyId[a];
        }

        for(int a = 0 ; a < arrayYourId.length ; a++){
            you += arrayYourId[a];
        }

        if(my < you){
            result = myId + yourId;
        }else{
            result = yourId + myId;
        }

        return result;
    }

    @Override
    protected void onPause() {
        Log.i("ddd66", "onDestroy");
        super.onPause();
    }

    @Override
    protected void onStop() {
        DaoImple.getInstance().setChattingActivity(null);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("ddd66", "onDestroy");
        DaoImple.getInstance().setChattingActivity(null);
        super.onDestroy();
    }

    // 액션바 적용1
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);

        return true;
    }

    // 액션바 적용2(닫기아이콘 리스너)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_close:
                Toast.makeText(this, "닫기", Toast.LENGTH_SHORT).show();
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);

    }

}

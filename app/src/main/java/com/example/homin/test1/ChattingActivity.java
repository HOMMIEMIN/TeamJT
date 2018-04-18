package com.example.homin.test1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import static com.example.homin.test1.FriendFragment.*;

public class ChattingActivity extends AppCompatActivity {

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
                    View itemView = inflater.inflate(R.layout.chat_layout, parent, false);
                    holder = new ChatHolder(itemView);
                break;
                case 1:
                    LayoutInflater inflater2 = LayoutInflater.from(ChattingActivity.this);
                    View itemView2 = inflater2.inflate(R.layout.chat_layout2, parent, false);
                    holder = new ChatHolder(itemView2);
                break;
            }


            return holder;
        }

        @Override
        public void onBindViewHolder(ChatAdapter.ChatHolder holder, int position) {

                holder.iv.setImageResource(R.drawable.p1);
                holder.tv1.setText(cList.get(position).getName());
                holder.tv2.setText(cList.get(position).getChat());
                holder.tv3.setText(cList.get(position).getTime());
                String id = holder.tv1.getText().toString();
            if(holder.getItemViewType() == 0) {
                holder.tv2.setBackground(getDrawable(R.drawable.ddd));
            }else{
                holder.tv2.setBackground(getDrawable(R.drawable.ddd1));
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        et = findViewById(R.id.editText_sends);
        btn = findViewById(R.id.button_sends);
        recyclerView = findViewById(R.id.recyclerView_chatting);

        if (cList != null) {
            Log.i("kaka", "cList: " + cList);
        } else {
            Log.i("kaka", "cList is null");
        }

        cList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();

        youId = intent.getStringExtra(CHAT_YOURID);
        Log.i("gg1",youId);
        yourName = intent.getStringExtra(CHAT_YOURNAME);
        check = intent.getBooleanExtra("check",false);

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

        Log.i("kaka",youId);



        cList.clear();

        reference.child("Chat").child(putKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                cList.add(chat);
                adapter.notifyDataSetChanged();

               int a = cList.size();
               recyclerView.scrollToPosition(cList.size()-1);

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

}

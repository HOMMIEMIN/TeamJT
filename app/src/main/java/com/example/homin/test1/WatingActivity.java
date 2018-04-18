package com.example.homin.test1;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class WatingActivity extends AppCompatActivity {

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomHolder>{
        private Contact yourContact;
        private List<String> myfList;
        private List<String> yourfList;
        private List<String> myWattingList;
        private List<Contact> contactList;
        private String yourKey;


        @Override
        public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View itemView = inflater.inflate(R.layout.wating_layout,parent,false);

            CustomHolder holder = new CustomHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(CustomHolder holder, final int position) {
            if(contactList == null){
                contactList = new ArrayList<>();
            }

            yourKey = getKey(list.get(position));
            Log.i("ggg1", "리스트 : "+yourKey);

            reference.child("Contact").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Contact contact = dataSnapshot.getValue(Contact.class);
                    contactList.add(contact);
                    if(dataSnapshot.getKey().equals(yourKey))
                        Log.i("ggg1", "파이어베이스 getKey" + dataSnapshot.getKey());
                    yourContact = dataSnapshot.getValue(Contact.class);
                    Log.i("ggg1", "유저 네임 : "+ yourContact.getUserName());
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


            holder.textView.setText(list.get(position));
//            holder.imageView.setImageResource(list.get(position).getPicture());

            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(int a = 0; a < contactList.size() ; a++){
                        if(contactList.get(a).getUserId().equals(list.get(position))){
                            yourContact = contactList.get(a);
                        }
                    }

                    if(DaoImple.getInstance().getContact().getWattingList() == null){
                        myWattingList = new ArrayList<>();
                    }else{
                        myWattingList = DaoImple.getInstance().getContact().getWattingList();
                    }

                    myContact = DaoImple.getInstance().getContact();


                    if(myContact.getFriendList() == null) {
                        myfList = new ArrayList<>();
                    }else{
                        myfList = myContact.getFriendList();
                    }

                    if(yourContact.getFriendList() == null){
                        yourfList = new ArrayList<>();
                    }else{
                        yourfList = yourContact.getFriendList();
                    }


                    for(int a = 0; a < myWattingList.size() ; a++){
                        if(myWattingList.get(a).equals(list.get(position))){
                            myWattingList.remove(a);
                        }
                    }
                    yourfList.add(myContact.getUserId());
                    myfList.add(yourContact.getUserId());

                    myContact.setFriendList(myfList);
                    myContact.setWattingList(myWattingList);
                    yourContact.setFriendList(yourfList);

                    reference.child("Contact").child(DaoImple.getInstance().getKey()).setValue(myContact);

                    String yourKey = getKey(yourContact.getUserId());
                    reference.child("Contact").child(yourKey).setValue(yourContact);

                    list.remove(position);
                    adapter.notifyDataSetChanged();

                    Toast.makeText(WatingActivity.this, "친구 등록이 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });

            holder.btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String myKey = DaoImple.getInstance().getKey();
                    String deleteKey = keyList.get(position);
                    list.remove(position);
//                    keyList.remove(position);
//                    reference.child(myKey).child("RequestList/"+deleteKey).removeValue();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(WatingActivity.this, "친구 요청을 취소 하였습니다", Toast.LENGTH_SHORT).show();

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class CustomHolder extends RecyclerView.ViewHolder{

            TextView textView;
            ImageView imageView;
            Button btnAdd;
            Button btnCancle;


            public CustomHolder(View itemView) {
                super(itemView);
                btnAdd = itemView.findViewById(R.id.btncancleWating);
                btnCancle = itemView.findViewById(R.id.btnAddWating);
                imageView = itemView.findViewById(R.id.imageView_WaittingLayout);
                textView = itemView.findViewById(R.id.textView_WaittingLayout1);
            }
        }
    }

    private RecyclerView recyclerView;
    private List<String> list;
    private DatabaseReference reference;
    private List<String> keyList;
    private CustomAdapter adapter;
    private int savePosition;
    private Contact myContact;
    private Contact youContact;
    private List<Contact> youList;
    private List<Contact> myList;
    private String myKey;
    private String youKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wating);
        list = new ArrayList<>();
        keyList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyler);
        reference = FirebaseDatabase.getInstance().getReference();

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

        String key = DaoImple.getInstance().getKey();

        reference.child("Contact").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(DaoImple.getInstance().getKey())) {
                    Log.i("vv", "에드 들어옴");
                    Contact c = dataSnapshot.getValue(Contact.class);
                    Log.i("vv", "이름이당이름 : " + c.getUserName());
                    list = c.getWattingList();
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    adapter.notifyDataSetChanged();
                }

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
}

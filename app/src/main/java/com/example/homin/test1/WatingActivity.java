package com.example.homin.test1;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
        private int listPosition;
        private boolean add;
        private boolean cancle;



        @Override
        public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View itemView = inflater.inflate(R.layout.wating_layout,parent,false);

            CustomHolder holder = new CustomHolder(itemView);

            return holder;
        }

        @Override
        public void onBindViewHolder(final CustomHolder holder, final int position) {
            listPosition = position;
            final List<String> listReset = list;
            Log.i("ggg34","position : " + position);
            Log.i("ggg34","listPosition : " + listPosition);
            Log.i("ggg34","listSize : " + list.size());
            Log.i("ggg34","listResetSize : " + listReset.size());
            Log.i("ggg34","listSize : " + list.size());
            if(contactList == null){
                contactList = new ArrayList<>();
            }



            yourKey = getKey(list.get(position));
            Log.i("ggg1", "리스트 : " + yourKey);

            reference.child("Contact").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Contact contact = dataSnapshot.getValue(Contact.class);
                    contactList.add(contact);
                    Log.i("ghals33",contactList.size() +"개");
                    if(list.get(position).equals(contact.getUserId())) {
                        holder.textView.setText(contact.getUserName());
                        holder.imageView.setBackground(new ShapeDrawable(new OvalShape()));
                        holder.imageView.setClipToOutline(true);
                        if(contact.getResizePictureUrl() != null) {
                            Glide.with(getApplicationContext()).load(contact.getResizePictureUrl()).into(holder.imageView);
                        }else{
                            holder.imageView.setImageResource(R.drawable.p1);
                        }
                        holder.textName.setText(contact.getUserId());
                    }

                    if(contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())){
                        myContact = dataSnapshot.getValue(Contact.class);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    Contact contact = dataSnapshot.getValue(Contact.class);
                    contactList.clear();
                    contactList.add(contact);
                    if(list.size() == 0) {

                        if (contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                            myContact = dataSnapshot.getValue(Contact.class);
                            Log.i("ggg34", "myContact 생성 : " + myContact.getUserName());
                        }
                    }

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
            Log.i("ghals33","콘텍트 사이즈 : "+contactList.size());

//            holder.imageView.setImageResource(list.get(position).getPicture());

            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add = true;
                    reference.child("Contact").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Contact con = dataSnapshot.getValue(Contact.class);
                            if(add){
                            if(con.getUserId().equals(list.get(position))) {
                                yourContact = con;
                                Log.i("ggg34", "yourContact 생성 : " + yourContact.getUserName());
                                if (DaoImple.getInstance().getContact().getWattingList() == null) {
                                    myWattingList = new ArrayList<>();
                                } else {
                                    myWattingList = myContact.getWattingList();
                                }


                                if (myContact.getFriendList() == null) {
                                    myfList = new ArrayList<>();
                                } else {
                                    myfList = myContact.getFriendList();
                                }

                                if (yourContact.getFriendList() == null) {
                                    yourfList = new ArrayList<>();
                                } else {
                                    yourfList = yourContact.getFriendList();
                                }

                                Log.i("ggg34", "삭제 전 사이즈 : " + myWattingList.size());
                                for (int a = 0; a < myWattingList.size(); a++) {
                                    if (myWattingList.get(a).equals(list.get(position))) {
                                        Log.i("ggg34", "삭제 : " + myWattingList.get(a));
                                        myWattingList.remove(a);

                                    }
                                }
                                yourfList.add(myContact.getUserId());
                                myfList.add(yourContact.getUserId());

                                myContact.setFriendList(myfList);
                                myContact.setWattingList(myWattingList);
                                yourContact.setFriendList(yourfList);
                                DaoImple.getInstance().setContact(myContact);

                                reference.child("Contact").child(DaoImple.getInstance().getKey()).setValue(myContact);

                                String yourKey = getKey(yourContact.getUserId());
                                reference.child("Contact").child(yourKey).setValue(yourContact);

                                list.remove(position);
                                adapter.notifyDataSetChanged();

                                Toast.makeText(WatingActivity.this, "친구 등록이 되었습니다.", Toast.LENGTH_SHORT).show();
                                add = false;
                            }
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
            });

            holder.btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancle = true;

                    reference.child("Contact").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Contact c = dataSnapshot.getValue(Contact.class);
                            if(cancle) {
                                if (c.getUserId().equals(DaoImple.getInstance().getLoginEmail())) {
                                    myContact = c;

                                    String myKey = DaoImple.getInstance().getKey();

                                    Contact contact = DaoImple.getInstance().getContact();
                                    List<String> wattingList = myContact.getWattingList();
                                    for (int a = 0; a < wattingList.size(); a++) {
                                        if (list.get(position).equals(wattingList.get(a))) {
                                            wattingList.remove(a);
                                            Log.i("ggg34", wattingList.size() + "");

                                        }
                                    }

                                    list.remove(position);
                                    myContact.setWattingList(wattingList);
                                    DaoImple.getInstance().setContact(myContact);
                                    reference.child("Contact").child(myKey).setValue(myContact);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(WatingActivity.this, "친구 요청을 취소 하였습니다", Toast.LENGTH_SHORT).show();
                                    cancle = false;
                                }
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
            TextView textName;


            public CustomHolder(View itemView) {
                super(itemView);
                btnAdd = itemView.findViewById(R.id.btncancleWating);
                btnCancle = itemView.findViewById(R.id.btnAddWating);
                imageView = itemView.findViewById(R.id.imageView_WaittingLayout);
                textView = itemView.findViewById(R.id.textView_WaittingLayout1);
                textName = itemView.findViewById(R.id.textView_WattingName);
            }
        }
    }

    private RecyclerView recyclerView;
    private List<String> list;
    private List<Contact> conList;
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
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wating);

        // 액션바
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("친구요청 알림") ;

        list = new ArrayList<>();
        keyList = new ArrayList<>();
        conList = new ArrayList<>();
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

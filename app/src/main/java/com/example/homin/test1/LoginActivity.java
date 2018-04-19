package com.example.homin.test1;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPwd;
    private Button btnLogin;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private int num;
    private ProgressDialog progressDialog;
    private Thread loginThread;
    private DatabaseReference reference;
    private String key;
    private Map<String,Bitmap> pictureList;//친구 아이디를 key값으로 받고 그의따른 bitMap을 저장하는 Map
    private int count;
    private List<String> stringkey;//map의 key값들인 친구 아이디를 넣음
    private Priority priority; // 이미지 다운로드 우선 순위 설정


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        pictureList = new HashMap();
        setContentView(R.layout.activity_login);
        stringkey = new ArrayList<>();

        etEmail = findViewById(R.id.editText);
        etPwd = findViewById(R.id.editText2);
        btnLogin = findViewById(R.id.button_login);
        btnSignUp = findViewById(R.id.button_signUp);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET};
            requestPermissions(permissions,21);
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginActivity.this);

                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("정보 불러오는 중...");
                progressDialog.show();

                clickLogin();
            }
        });
    }

    private void clickLogin() {
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPwd.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인 하세요.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else{
                            DaoImple.getInstance().setLoginEmail(etEmail.getText().toString());
                            int a = etEmail.getText().toString().indexOf("@");
                            String name = etEmail.getText().toString().substring(0,a);
                            DaoImple.getInstance().setLoginId(name);
                            DaoImple.getInstance().setLoginEmail(etEmail.getText().toString());


                            int c = etEmail.getText().toString().indexOf("@");
                            String key1 = etEmail.getText().toString().substring(0,c);

                            int b = etEmail.getText().toString().indexOf(".");
                            String key2 = etEmail.getText().toString().substring(c + 1,b);


                            String key3 = etEmail.getText().toString().substring(b + 1,etEmail.getText().toString().length());
                            key = key1+key2+key3;
                            DaoImple.getInstance().setKey(key);





                            reference.child("Contact").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    Contact contactInOrder = dataSnapshot.getValue(Contact.class); //차례대로 들어오는 Contact들

                                    if(contactInOrder.getUserId().equals(etEmail.getText().toString())) {// 내 컨텍트를 찾는 if문
                                        // 내 사진 다운로드
                                            if(contactInOrder.getPictureUrl()!= null) // 내 Contact에 url 에 들어가있는지 체크
                                            Glide.with(getApplicationContext()).load(contactInOrder.getPictureUrl())
                                                    .asBitmap().priority(Priority.IMMEDIATE).override(100, 100).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().into(new SimpleTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                    pictureList.put(etEmail.getText().toString(), resource);
                                                }
                                            });

                                            final Contact myContact = dataSnapshot.getValue(Contact.class); //내 컨텍트 설정

                                            DaoImple.getInstance().setLoginEmail(myContact.getUserId());
                                            DaoImple.getInstance().setLoginId(myContact.getUserName());
                                            DaoImple.getInstance().setContact(myContact);




                                        reference.child("Contact").addChildEventListener(new ChildEventListener() {
                                            boolean intentNotsent = true; //인텐트가 전달되었는지 안되었는지 체크하기 위한 boolean
                                            //친구목록이 없는 경우 , 친구목록이 있는데 친구가 url을 가지고 있는 경우 아니면 가지고 있지 않은 경우를 나누기 위해 쓰임
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                final Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                                Contact friendsContact = dataSnapshot.getValue(Contact.class);// 친구목록에 있는 list 비교를 위한 Contact설정
                                                Log.i("hi1", "내 친구들 목록 사이즈: " + myContact.getFriendList().size());

                                                for (int a = 0; a < myContact.getFriendList().size(); a++) {
                                                    final int index = a;
                                                    Log.i("hi1", friendsContact.getUserId());
                                                    Log.i("hi1", "MyContact.list:" + myContact.getFriendList().get(a));

                                                    if (friendsContact.getUserId().equals(myContact.getFriendList().get(a))) { //친구목록에 있는 친구들의 bitmap들을 다 다운로드
                                                        if (friendsContact.getPictureUrl() != null) {
                                                            stringkey.add(friendsContact.getUserId());//친구 아이디 목록 ( HashMap의 Key값들을 List에 넣음)
                                                            Glide.with(LoginActivity.this).load(friendsContact.getPictureUrl())
                                                                    .asBitmap().override(100, 100).fitCenter().into(new SimpleTarget<Bitmap>() {
                                                                @Override
                                                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                                    pictureList.put(stringkey.get(index), resource); //HashMap인 PictureList에 Key 값인 친구 아이디와 그에따른 BitMap을 넣음
                                                                    count++;
                                                                    Log.i("hi1", "Count: " + count);
                                                                    if (myContact.getFriendList().size() == count) {
                                                                        DaoImple.getInstance().setPictureList(pictureList);
                                                                        count = 0;
                                                                        Log.i("qq1","사진리스트 가져옴");
                                                                        intentNotsent = false;
                                                                        Log.i("qq23q","startActivity1");
                                                                        startActivity(intent); // Bitmap 다운로드 완료후 맵으로 넘어가는 intent 설정


                                                                    }

                                                                }
                                                            });
                                                        } else {
                                                            stringkey.add(friendsContact.getUserId());
                                                            pictureList.put(stringkey.get(index), null);
                                                            count++;
                                                        }

                                                        Log.i("hi1", "Count: " + count);
                                                        Log.i("hi1", "getFriendsList.size: " + myContact.getFriendList().size());

                                                        if (myContact.getFriendList().size() == count) {
                                                            DaoImple.getInstance().setPictureList(pictureList);
                                                            count = 0;
                                                            Log.i("qq23q","startActivity2");
                                                            startActivity(intent); //Bitmap 다운로드 완료후 맵으로 넘어가는 intent 설정
                                                            finish();
                                                            intentNotsent = false;

                                                        }
                                                    }
                                                }
//                                                if (intentNotsent) {
//                                                    DaoImple.getInstance().setPictureList(pictureList);
//                                                    intentNotsent = false;
//                                                    Log.i("qq23q","startActivity3");
//                                                    startActivity(intent);//포문까지도 안 들어오는 경우 (친구가 없는 경우)
//                                                }
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
                    }
                });


    }

}

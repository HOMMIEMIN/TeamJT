package com.example.homin.test1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private CheckBox checkBox;
    private EditText etEmail;
    private EditText etPwd;
    private Button btnLogin;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private int num;
    private ProgressDialog progressDialog;
    private Thread loginThread;
    private DatabaseReference reference;
    private String id;
    private String pw;
    private String key;
    private Map<String, Bitmap> pictureList;//친구 아이디를 key값으로 받고 그의따른 bitMap을 저장하는 Map
    private int count;
    private int index;
    private List<String> stringkey;//map의 key값들인 친구 아이디를 넣음
    private Priority priority; // 이미지 다운로드 우선 순위 설정
    private boolean intentNotsent = true; //인텐트가 전달되었는지 안되었는지 체크하기 위한 boolean
    //친구목록이 없는 경우 , 친구목록이 있는데 친구가 url을 가지고 있는 경우 아니면 가지고 있지 않은 경우를 나누기 위해 쓰임
    private Intent intent = null;


    @Override
    protected void onResume() {
        Log.i("ggqs","리즘");
        if(progressDialog != null){
            progressDialog.dismiss();
        }
        intentNotsent = true;
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = new Intent(this,NotificationService.class);
//        stopService(intent);

        intent = new Intent(LoginActivity.this, MapsActivity.class);
        pictureList = new HashMap();
        setContentView(R.layout.activity_login);
        stringkey = new ArrayList<>();

        etEmail = findViewById(R.id.editText);
        etPwd = findViewById(R.id.editText2);
        btnLogin = findViewById(R.id.button_login);
        btnSignUp = findViewById(R.id.button_signUp);
        checkBox = findViewById(R.id.checkBox);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        // 저장한 로그인정보 가져오기
        loginIdLoad();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            ,Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions, 21);
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
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

    // 저장한 로그인 정보 가져오기기
   private void loginIdLoad() {
        SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
        if(preferences.getString("id","") != null) {
            etEmail.setText(preferences.getString("id", ""));
            etPwd.setText(preferences.getString("pw", ""));
            checkBox.setChecked(true);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            checkGps();
        }
    }

    @SuppressLint("MissingPermission")
    private void checkGps() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS 서비스가 꺼져 있습니다. 설정 화면으로 이동 합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        }
    }

    private void clickLogin() {
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPwd.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인 하세요.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            DaoImple.getInstance().setLoginEmail(etEmail.getText().toString());
                            int a = etEmail.getText().toString().indexOf("@");
                            id = etEmail.getText().toString();
                            pw = etPwd.getText().toString();

                            DaoImple.getInstance().setLoginEmail(etEmail.getText().toString());

                            key = DaoImple.getInstance().getFirebaseKey(etEmail.getText().toString());
                            DaoImple.getInstance().setKey(key);
                            Log.i("ggqs",key);


                            reference.child("Contact").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    Contact contactInOrder = dataSnapshot.getValue(Contact.class); //차례대로 들어오는 Contact들

                                    if (contactInOrder.getUserId().equals(etEmail.getText().toString())) {// 내 컨텍트를 찾는 if문

                                        final Contact myContact = dataSnapshot.getValue(Contact.class); //내 컨텍트 설정
                                        DaoImple.getInstance().setLoginEmail(myContact.getUserId());
                                        DaoImple.getInstance().setLoginId(myContact.getUserName());
                                        DaoImple.getInstance().setContact(myContact);

                                        intentNotsent = false;
                                        Log.i("qq23q", "startActivity1");

                                        Log.i("qq23q", DaoImple.getInstance().getKey());
                                        boolean loginCheck = myContact.isLoginCheck();

                                        // 이미 로그인 되어 있는 대상이라면 로그인 할 수 없음
                                        if(loginCheck){
                                            Toast.makeText(LoginActivity.this, "이미 로그인 되어 있습니다.", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }else {
                                            idSaveCheck(id,pw); // 아이디와 패스워드 저장
                                            progressDialog.dismiss();
                                            myContact.setLoginCheck(true);
                                            Contact contact = missLocation(myContact);
                                            reference.child("Contact").child(DaoImple.getInstance().getKey()).setValue(contact);
                                            startActivity(intent); // Bitmap 다운로드 완료후 맵으로 넘어가는 intent 설정
                                            finish();
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
                    }
                });

    }

    private Contact missLocation(Contact myContact) {
        List<Double> myLocation = myContact.getUserLocation();
        double lat = myLocation.get(0);
        double lon = myLocation.get(1);
        lat+=0.0001;
        lon+=0.0001;
        myLocation.clear();
        myLocation.add(lat);
        myLocation.add(lon);
        return myContact;
    }

    private void idSaveCheck(String email, String pw){
        if(checkBox.isChecked()) {
            SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("id", email);
            editor.putString("pw", pw);
            editor.commit();
        }else{
            SharedPreferences preferences = getSharedPreferences("save", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("id", null);
            editor.putString("pw", null);
            editor.commit();
        }
    }

}

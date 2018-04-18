package com.example.homin.test1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private EditText etSignUpName;
    private EditText etSignUpEmail;
    private EditText etSignUpPwd;
    private Button btnSignUpLogin;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etSignUpEmail = findViewById(R.id.editText_signEmail);
        etSignUpName = findViewById(R.id.editText_signName);
        etSignUpPwd = findViewById(R.id.editText_signPwd);
        btnSignUpLogin = findViewById(R.id.btn_signUp);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("가입중...");
        btnSignUpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                clickSignUp();
            }
        });

    }

    private void clickSignUp() {
        mAuth.createUserWithEmailAndPassword(etSignUpEmail.getText().toString(),etSignUpPwd.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "가입 실패", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else {
                            Toast.makeText(SignUpActivity.this, "가입 성공", Toast.LENGTH_SHORT).show();

                            int a = etSignUpEmail.getText().toString().indexOf("@");
                            String key1 = etSignUpEmail.getText().toString().substring(0,a);
                            int b = etSignUpEmail.getText().toString().indexOf(".");
                            String key2 = etSignUpEmail.getText().toString().substring(a,b);
                            String key3 = etSignUpEmail.getText().toString().substring(b,etSignUpEmail.getText().toString().length());

                            int a1 = etSignUpEmail.getText().toString().indexOf("@");
                            String d1 = etSignUpEmail.getText().toString().substring(0,a1);

                            int b1 = etSignUpEmail.getText().toString().indexOf(".");
                            String d2 = etSignUpEmail.getText().toString().substring(a1 + 1,b);

                            String d3 = etSignUpEmail.getText().toString().substring(b1 + 1,etSignUpEmail.getText().toString().length());
                            String key = d1+d2+d3;


                            List<String> list = new ArrayList<>();
                            List<String> list2 = new ArrayList<>();
                            List<Double> location = new ArrayList<>();
                            location.add(111.111);
                            location.add(222.222);

                            Contact c = new Contact(etSignUpEmail.getText().toString(),etSignUpName.getText().toString(),"dddd",location,list,list2,true);

                            reference.child("Contact").child(key).setValue(c);


                            progressDialog.dismiss();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}

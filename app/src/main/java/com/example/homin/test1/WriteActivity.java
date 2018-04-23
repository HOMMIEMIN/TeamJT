package com.example.homin.test1;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class WriteActivity extends Activity {
    private EditText et1,et2;
    public static final String TITLE_KEY = "subject";
    public static final String BODY_KEY = "body";
    public static final int GALLERY_KEY = 321;
    public static final String IMAGE_KEY = "image";
    public static final String TIME_KEY = "time";
    public static final String IMAGEURL_KEY = "imageUrl";
    private ImageView imageView;
    private Bitmap image;
    private Uri uri;
    private StorageReference storageReference;
    private Uri url;
    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        et1 = findViewById(R.id.editText3);
        et2 = findViewById(R.id.editText4);
        imageView = findViewById(R.id.open_imageView);
    }

    public void clickSave(View view) {

        if(et1.getText().toString() == null || et2.getText().toString() == null || uri == null){
            Toast.makeText(this, "항목을 모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }else{
            dialog = new ProgressDialog(this);
            dialog.setTitle("사진 업로드 중....");
            dialog.show();

            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://test33-32739.appspot.com");
            storageReference.child(DaoImple.getInstance().getKey()+"/"+uri.getLastPathSegment()).
                    putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    List<String> list = new ArrayList<>();
                    url = taskSnapshot.getDownloadUrl();
                    String imageUrl = url.toString();
                    Log.i("vvv66",imageUrl);
                    String time = createTime();
                    String title = et1.getText().toString();
                    String body = et2.getText().toString();
//
                    Intent intent = new Intent();
                    intent.putExtra(TIME_KEY,time);
                    intent.putExtra(TITLE_KEY,title);
                    intent.putExtra(BODY_KEY,body);
                    intent.putExtra(IMAGEURL_KEY,imageUrl);
                    setResult(RESULT_OK,intent);
                    dialog.dismiss();
                    Log.i("ggv","writeActivity finish()");
                    finish();

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(WriteActivity.this, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        }

    }

    private String createTime(){
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat time = new SimpleDateFormat("yy/MM/dd, HH시mm분");
        time.setTimeZone(timeZone);

        return time.format(date).toString();
    }


    public void clickOpenGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,GALLERY_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("kkk7","갤러리 결과888");
        Log.i("kkk7",requestCode+"   " + resultCode);
        if(requestCode == GALLERY_KEY && resultCode == RESULT_OK){
            try {
                Log.i("kkk7","갤러리 결과");
                uri = data.getData();
                image = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                imageView.setImageBitmap(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

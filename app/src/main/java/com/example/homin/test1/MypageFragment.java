package com.example.homin.test1;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import static com.example.homin.test1.MapsActivity.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MypageFragment extends Fragment {

    private static Context context;
    private static String key;
    private static DatabaseReference reference;
    private static ImageView imageView;
    private TextView textView;


    public MypageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        String downUriToStr = getArguments().getString("downUriToStr");
//        downUri = Uri.parse(downUriToStr);

        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        textView = view.findViewById(R.id.textView);
        textView.setText(DaoImple.getInstance().getLoginId());

        // 프로필 이미지 설정
        imageView = view.findViewById(R.id.imageView);
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        imageView.setClipToOutline(true);
        getProImg();

        // 프로필 이미지 버튼화 - 프로필 이미지 변경
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MapsActivity)getActivity()).clickedProImgBotton();
            }
        });

        return view;
    }

    // 프로필 이미지 가져오는 메소드
    private void getProImg() {
        key = DaoImple.getInstance().getKey();
        Log.i(TAG, "key: " + key);
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReferenceFromUrl("gs://test33-32739.appspot.com/");
//        StorageReference pathRef = storageRef.child(key + "/").child("profileImage/curProImg.jpg");

        String curProImgUrl = DaoImple.getInstance().getContact().getPictureUrl();
        Log.i(TAG, "curProImgUrl: " + curProImgUrl);
        Log.i(TAG, "imageView.getDrawable(): " + imageView.getDrawable());

        //TODO: 프로필 이미지 이슈 해결되면 아래 주석 풀기
        if (curProImgUrl != null) { // Firebase에 저장된 파일이 있을 때
            Glide.with(this).load(curProImgUrl).into(imageView);

        } else { // 없을 때
            //TODO: 아이콘 뭘로..

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //TODO: 리사이클러 뷰

    }

    // Firebase에 사진 업로드하는 메소드
    public static void uploadFile(Uri filePath) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("프로필 사진 업데이트 중...");
            progressDialog.show();

            // storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference dataRef = database.getReference();

                    // 파일명 지정
            String filename = "curProImg.jpg";
            key = DaoImple.getInstance().getKey();

            StorageReference storageRef = storage.getReferenceFromUrl("gs://test33-32739.appspot.com/").child(key + "/").child("profileImage/" + filename);
            storageRef.putFile(filePath)
                    // 성공했을 때
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "업데이트 완료!", Toast.LENGTH_SHORT).show();

                            Uri downUri = taskSnapshot.getDownloadUrl();
                            Log.i(TAG, "다운로드uri: " + downUri);
                            String downUriToStr = downUri.toString();

                            DaoImple.getInstance().getContact().setPictureUrl(downUriToStr);

                            Log.i(TAG,key);

                            Contact contact = DaoImple.getInstance().getContact();
                            Log.i(TAG, "contact: " + contact.getPictureUrl());

                            dataRef.child("Contact").child(key).setValue(contact);

                            // 변경된 프로필 이미지 ImageView에 바로 적용
                            Glide.with(context).load(downUri).into(imageView);

                            

                        }
                    })
                    // 실패했을 때
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "업데이트 실패..", Toast.LENGTH_SHORT).show();
                        }
                    })
                    // 진행중..
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                            double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            Toast.makeText(context, "사진을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        } // end if

    } // end uploadFile()
}

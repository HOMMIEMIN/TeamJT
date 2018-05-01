package com.example.homin.test1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class WriteActivity extends Activity {
    private EditText et1,et2;
    public static final String TITLE_KEY = "subject";
    public static final String BODY_KEY = "body";
//    public static final int GALLERY_KEY = 321;
    public static final String IMAGE_KEY = "image";
    public static final String TIME_KEY = "time";
    public static final String IMAGEURL_KEY = "imageUrl";
    private ImageView preview, closeImg, insertPic, imgFrame;
    private Bitmap image;
//    private Uri uri;
    private StorageReference storageReference;
    private Uri url;
    private ProgressDialog dialog;
    private static final int CAMERA_CODE = 1000;
    private static final int GALLERY_CODE = 1001;
    private static final int CROP_IMAGE_CODE = 1002;
    private Uri photoUri, albumUri;
    private Uri selectedUri;
    Boolean albumPick = false;
    private BottomSheetDialog bottomSheetDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        et1 = findViewById(R.id.editTitle);
        et2 = findViewById(R.id.editContent);
        imgFrame = findViewById(R.id.imageFrame);
        preview = findViewById(R.id.realImageView);
        closeImg = findViewById(R.id.closeImgBtn);
        insertPic = findViewById(R.id.imageInsertPic);
//        cameraBtn = findViewById(R.id.imageInsertPic);
//        cameraBtn.setBackground(new ShapeDrawable(new OvalShape()));
//        cameraBtn.setClipToOutline(true);

//        imageView = findViewById(R.id.open_imageView);
    }

    // 글 등록
    public void clickSave(View view) {

        if(et1.getText().toString() == null || et2.getText().toString() == null || selectedUri == null){
            Toast.makeText(this, "항목을 모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }else{
            dialog = new ProgressDialog(this);
            dialog.setTitle("사진 업로드 중....");
            dialog.show();

            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://test33-32739.appspot.com");
            storageReference.child(DaoImple.getInstance().getKey()+"/"+selectedUri.getLastPathSegment()).
                    putFile(selectedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    List<String> list = new ArrayList<>();
                    url = taskSnapshot.getDownloadUrl();
                    String imageUrl = selectedUri.toString();
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

    // 현재 시간 타임 스탬프
    private String createTime(){
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat time = new SimpleDateFormat("yy/MM/dd, HH시mm분");
        time.setTimeZone(timeZone);

        return time.format(date).toString();
    }


//    public void clickOpenGallery(View view) {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent,GALLERY_KEY);
//    }

    // 글쓰기 액티비티 닫기
    public void onClickClose(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {

            switch (requestCode) {
                case GALLERY_CODE:
                    albumPick = true;
                    File albumFile = null;
                    try {
                        albumFile = MypageFragment.createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (albumFile != null) {
                        albumUri = FileProvider.getUriForFile(this, getPackageName(), albumFile);
                    }

                    photoUri = data.getData(); // 선택한 사진 Uri 정보

                case CAMERA_CODE:
                    cropImage();
                    break;

                case CROP_IMAGE_CODE:
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); // 동기화?
                    if (albumPick == false) {
                        mediaScanIntent.setData(photoUri);

                    } else if (albumPick == true) {
                        albumPick = false;
                        mediaScanIntent.setData(albumUri);

                    }
                    this.sendBroadcast(mediaScanIntent);

                    imgFrame.setVisibility(View.VISIBLE);
                    preview.setVisibility(View.VISIBLE);
                    try {
                        image = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    preview.setImageBitmap(image);
//                    preview.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);
                    preview.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    preview.setScaleType(ImageView.ScaleType.FIT_XY);
                    closeImg.setVisibility(View.VISIBLE);

                    break;

            }
        }
    }


    // 선택했던 이미지 지우기
    public void onClickImgClose(View view) {
        selectedUri = null;
        preview.setVisibility(View.INVISIBLE);
        closeImg.setVisibility(View.INVISIBLE);
        imgFrame.setVisibility(View.INVISIBLE);
    }

    // 사진 버튼 누르면 불러오는 하단 메뉴
    /**
     * showing bottom sheet dialog
     */
    public void showBottomSheetDialog(View view) {
        view = getLayoutInflater().inflate(R.layout.fragment_write_dialog, null);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

//
//    /**
//     * showing bottom sheet dialog fragment
//     * same layout is used in both dialog and dialog fragment
//     */
//    @OnClick(R.id.btn_bottom_sheet_dialog_fragment)
//    public void showBottomSheetDialogFragment() {
//        WriteDialogFragment bottomSheetFragment = new WriteDialogFragment();
//        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
//    }

    // 갤러리에서 사진 선택하는 메소드
    public void onClickSelectImgByAlbum(View view) {
        bottomSheetDialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK);
        //TODO: ACTION_PICK(이미지가 저장되어있는 폴더를 선택) ACTION_GET_CONTENT(전체 이미지를 폴더 구분없이 최신 이미지 순)랑 둘 비교
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    } // end onClickSelectImgByAlbum()


    // 프로필 사진 직접 촬영하는 메소드
    public void onClickTakePicture(View view) {
        bottomSheetDialog.dismiss();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = MypageFragment.createImageFile(); // 원본 이미지 파일 저장 폴더 생성
                } catch (IOException ex) {
                    Toast.makeText(this, "createImageFile 실패", Toast.LENGTH_LONG).show();
                }

                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); // 경로에 저장

                    startActivityForResult(takePictureIntent, CAMERA_CODE);
                }

            }
        }
    } // end onClickTakePicture()

    // 원본 이미지 crop하는 메소드
    private void cropImage() {

        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        cropIntent.setDataAndType(photoUri, "image/*");
//        cropIntent.putExtra("outputX", 200); // crop한 이미지의 x축 크기
//        cropIntent.putExtra("outputY", 200); // crop한 이미지의 y축 크기
//        cropIntent.putExtra("aspectX", 1); // crop 박스의 x축 비율
//        cropIntent.putExtra("aspectY", 1); // crop 박스의 y축 비율
        cropIntent.putExtra("scale", true);

        if (albumPick == false) {
            selectedUri = photoUri;
            cropIntent.putExtra("output", selectedUri);

        } else if (albumPick == true) {
            selectedUri = albumUri;
            cropIntent.putExtra("output", selectedUri);

        }

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(cropIntent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, selectedUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent in = new Intent(cropIntent);
        ResolveInfo res = list.get(0);
        in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        in.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        grantUriPermission(res.activityInfo.packageName, selectedUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        in.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

        startActivityForResult(in, CROP_IMAGE_CODE);

    } // end cropImage()


}

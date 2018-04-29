package com.example.homin.test1;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by stu on 2018-03-26.
 */

public class DaoImple {

    private static DaoImple instance = null;
    private String LoginEmail;
    private List<Chat> cList;
    private String LoginId;
    private String youEmail;
    private String key;
    private Contact contact;
    private Map<String,Bitmap> pictureList;
    private LatLng WriteLocation;
    private List<ItemMemo> itemMemoList;
    private List<ItemPerson> itemPersonList;
    private UserDataTable myPageUserData;
    private FragmentActivity fragmentActivity;
    private String newDate;
    private Context ChattingActivity;

    public UserDataTable getMyPageUserData() {
        return myPageUserData;
    }

    public void setMyPageUserData(UserDataTable myPageUserData) {
        this.myPageUserData = myPageUserData;
    }



    public List<ItemMemo> getItemMemoList() {
        return itemMemoList;
    }

    public List<ItemPerson> getItemPersonList() {
        return itemPersonList;
    }

    public void setItemPersonList(List<ItemPerson> itemPersonList) {
        this.itemPersonList = itemPersonList;
    }

    public void setItemMemoList(List<ItemMemo> itemMemoList) {
        this.itemMemoList = itemMemoList;
    }

    public static void setInstance(DaoImple instance) {
        DaoImple.instance = instance;
    }

    public LatLng getWriteLocation() {
        return WriteLocation;
    }

    public void setWriteLocation(LatLng writeLocation) {
        WriteLocation = writeLocation;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static DaoImple getInstance(){
        if(instance == null){
            instance = new DaoImple();
        }
        return instance;
    }

    public String getYouEmail() {
        return youEmail;
    }

    public void setYouEmail(String youEmail) {
        this.youEmail = youEmail;
    }

    private DaoImple(){
        cList = new ArrayList<>();
    }



    public String getLoginEmail() {
        return LoginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        LoginEmail = loginEmail;
    }


    public List<Chat> getcList() {
        return cList;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public void setcList(List<Chat> cList) {
        this.cList = cList;
    }

    public Map<String, Bitmap> getPictureList() {
        return pictureList;
    }

    public void setPictureList(Map<String, Bitmap> pictureList) {
        this.pictureList = pictureList;
    }


    // 이메일에서 특수문자 뺀 key값 구하기
    public String getFirebaseKey(String id){
        int b = id.indexOf("@");
        String key1 = id.substring(0,b);
        int d = id.indexOf(".");
        String key2 = id.substring(b + 1,d);
        String key3 = id.substring(d + 1,id.length());
        String key = key1+key2+key3;

        return key;
    }

    public String getDateFormat(String getDate) {

//        getDate = "2012년 7월 11일 (수)"; // 18/04/23, 15시10분

        // SimpleDateFormat의 형식을 선언한다.
        SimpleDateFormat originalFormat = new SimpleDateFormat("yy/MM/dd, HH시mm분");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy년MM월dd일 a hh시mm분");

        // 날짜 형식 변환시 파싱 오류를 try.. catch..로 체크한다.
        try {
            // 문자열 타입을 날짜 타입으로 변환한다.
            Date originalDate = originalFormat.parse(getDate);

            // 날짜 형식을 원하는 타입으로 변경한다.
            newDate = newFormat.format(originalDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public Context getChattingActivity() {
        return ChattingActivity;
    }

    public void setChattingActivity(Context chattingActivity) {
        ChattingActivity = chattingActivity;
    }

    public static FragmentActivity getActivity(Context context) {
        if (context instanceof FragmentActivity) {
            return (FragmentActivity) context;
        }

        while (context instanceof ContextWrapper) {
            if (context instanceof FragmentActivity) {
                if(!((FragmentActivity) context).isFinishing()) {
                    return (FragmentActivity) context;
                }else{
                    return null;
                }
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private static Activity getChatActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            if (!((Activity) context).isFinishing())
                return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }



}

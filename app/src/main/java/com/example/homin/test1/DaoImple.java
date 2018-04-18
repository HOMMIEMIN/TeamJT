package com.example.homin.test1;

import android.graphics.Bitmap;

import java.util.ArrayList;
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


}

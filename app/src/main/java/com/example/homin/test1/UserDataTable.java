package com.example.homin.test1;

import java.util.List;

public class UserDataTable {
    private String userId;
    private String name;
    private String imageUrl;
    private List<Double> location;
    private String Title;
    private String content;
    private String data;

    public UserDataTable(){}

    public UserDataTable(String userId, String name, String imageUrl, List<Double> location, String title, String content, String data) {
        this.userId = userId; // 유저 아이디
        this.name = name; // 이름
        this.imageUrl = imageUrl; // 글에 올리는 사진
        this.location = location; // 글쓰는 지역 위치
        Title = title; // 제목
        this.content = content; // 글내용
        this.data = data; //
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

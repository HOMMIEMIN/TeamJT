package com.example.homin.test1;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contact implements Serializable{

    private String UserId;
    private String UserName;
    private String pictureUrl;
    private List<Double> userLocation;
    private List<String> friendList;
    private List<String> wattingList;
    private boolean isPublic;


    public Contact(){}

    public Contact(String userId, String userName, String pictureUrl, List<Double> userLocation, List<String> friendList, List<String> wattingList, boolean isPublic) {
        UserId = userId;
        UserName = userName;
        this.pictureUrl = pictureUrl;
        this.userLocation = userLocation;
        this.friendList = friendList;
        this.wattingList = wattingList;
        this.isPublic = isPublic;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public List<Double> getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(List<Double> userLocation) {
        this.userLocation = userLocation;
    }

    public List<String> getFriendList() {
        if(friendList == null){
            friendList = new ArrayList<>();
        }
            return friendList;


    }

    public void setFriendList(List<String> friendList) {

        this.friendList = friendList;
    }

    public List<String> getWattingList() {

            return wattingList;

    }

    public void setWattingList(List<String> wattingList) {
        this.wattingList = wattingList;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}

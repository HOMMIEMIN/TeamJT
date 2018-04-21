package com.example.homin.test1;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ItemMemo implements ClusterItem {
    private final LatLng mPosition;
    private String userId;
    private String userName;
    private String title;
    private String content;
    private String time;
    private String imageUrl;
    private Bitmap icon;

    public ItemMemo(double lat, double lng, String userId, String userName, String title, String content, String time, String imageUrl, Bitmap bitmap) {
        mPosition = new LatLng(lat, lng);
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.time = time;
        this.imageUrl = imageUrl;
        icon = bitmap;
    }

    public LatLng getPosition() {
        return mPosition;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Bitmap getIcon() {
        return icon;
    }
}

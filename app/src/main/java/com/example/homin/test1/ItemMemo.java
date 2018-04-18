package com.example.homin.test1;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ItemMemo implements ClusterItem {
    private final LatLng mPosition;
    private String title;

    public ItemMemo(double lat, double lng, String title) {
        mPosition = new LatLng(lat, lng);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}

package com.example.homin.test1;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ItemDestination implements ClusterItem {



    private LatLng mPosition;
    private String title;
    private String snippet;


    public ItemDestination(){}
    public ItemDestination(LatLng mPosition,String title, String snippet) {
        this.mPosition = mPosition;
        this.title = title;
        this.snippet = snippet;

    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}

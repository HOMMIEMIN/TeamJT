package com.example.homin.test1;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


    //사람의 대한 마커 클러스터아이템
    public class ItemPerson implements ClusterItem {


        private final LatLng mPosition;
        private String userId;
        private Bitmap image;


        public ItemPerson(double lat, double lng, String id, Bitmap image) {
            mPosition = new LatLng(lat, lng);
            this.userId = id;
            this.image = image;

        }

        public String getTitle() {
            return userId;
        }

        @Override
        public String getSnippet() {
            return null;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        public Bitmap getImage() {
            return image;
        }
    }


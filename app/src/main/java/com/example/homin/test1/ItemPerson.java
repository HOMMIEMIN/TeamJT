package com.example.homin.test1;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


    //사람의 대한 마커 클러스터아이템
    public class ItemPerson implements ClusterItem {


        private final LatLng mPosition;
        private String userId;
        private String userName;
        private String image;


        public ItemPerson(double lat, double lng, String id,String name,String image) {
            mPosition = new LatLng(lat, lng);
            this.userId = id;
            this.image = image;
            userName = name;

        }

        public String getTitle() {
            return userId;
        }

        @Override
        public String getSnippet() {
            return null;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        public String getImage() {
            return image;
        }
    }


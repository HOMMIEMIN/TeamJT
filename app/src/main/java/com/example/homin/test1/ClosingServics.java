package com.example.homin.test1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ClosingServics extends Service {

    private Contact closeContact;
    private DatabaseReference reference;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    // Recent App 상태에서 앱 종료시, 로그인 상태 변경
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        reference = FirebaseDatabase.getInstance().getReference();
        Log.i("ghals",DaoImple.getInstance().getKey());
        reference.child("Contact").child(DaoImple.getInstance().getKey()).child("loginCheck").setValue(false);
        stopSelf();
    }


    private Contact missLocation(Contact myContact) {
        List<Double> myLocation = myContact.getUserLocation();
        double lat = myLocation.get(0);
        double lon = myLocation.get(1);
        lat+=0.01;
        lon+=0.01;
        myLocation.clear();
        myLocation.add(lat);
        myLocation.add(lon);
        return myContact;
    }
}

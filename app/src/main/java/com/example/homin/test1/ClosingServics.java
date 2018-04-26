package com.example.homin.test1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClosingServics extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    // Recent App 상태에서 앱 종료시, 로그인 상태 변경
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Contact myContact = DaoImple.getInstance().getContact();
        myContact.setLoginCheck(false);
        reference.child("Contact").child(DaoImple.getInstance().getKey()).setValue(myContact);
        Log.i("ggqs","onTaskRemoved 종료");
        stopSelf();
    }
}

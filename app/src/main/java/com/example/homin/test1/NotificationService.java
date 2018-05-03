package com.example.homin.test1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import static com.example.homin.test1.FriendFragment.*;



public class NotificationService extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;
    Notification Notifi ;
    String user_name;
    String user_chat;
    String user_id;
    String type;
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("KIMMY","onBInd");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("KIMMY","onStartCommand");
        user_name = intent.getStringExtra("name");
        user_chat = intent.getStringExtra("chat");
        user_id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        thread.interrupt();
        Notifi_M.cancel(777);
        stopSelf();

        super.onTaskRemoved(rootIntent);


    }
//서비스가 종료될 때 할 작업

    public void onDestroy() {
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            PendingIntent pendingIntent = null;
            if(type!= null && type.equals("msg")) {
                Intent intent = new Intent(NotificationService.this, ChattingActivity.class);
                intent.putExtra(CHAT_YOURID, user_id);
                intent.putExtra(CHAT_YOURNAME, user_name);


                pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                if(user_chat!= null && user_name!= null) {
                    if (Notifi == null) {
                        Notifi = new Notification.Builder(getApplicationContext())
                                .setContentTitle(user_name)
                                .setContentText(user_chat)
                                .setSmallIcon(R.drawable.jt_logo)
                                .setTicker("TAM notification")
                                .setContentIntent(pendingIntent)
                                .build();

                    }
                    if(Notifi_M != null) {
                        Notifi_M.notify(777, Notifi);
                        Notifi = null;
                    }
                }


            }else if(type != null && type.equals("distance")){
                Log.i("KIMMY","notification");

                if(user_chat!= null && user_name!= null) {
                    if (Notifi == null) {
                        Notifi = new Notification.Builder(getApplicationContext())
                                .setContentTitle(user_name)
                                .setContentText(user_chat)
                                .setSmallIcon(R.drawable.jt_logo)
                                .setTicker("TAM notification")
                                .setContentIntent(pendingIntent)
                                .build();

                    }
                    if(Notifi_M != null) {
                        Notifi_M.notify(777, Notifi);
                        Notifi = null;
                    }
                }

            }



        }
    };



}

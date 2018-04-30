package com.example.homin.test1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context){
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void rendowWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        if(title == null){
            return;
        }
        if(!title.equals("")){
            tvTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView)view.findViewById(R.id.snippet);
        if(snippet == null){
            return;
        }
        if(!snippet.equals("")){
            tvSnippet.setText(snippet);
        }
    }
    @Override
    public View getInfoWindow(Marker marker) {
        if((DaoImple.getInstance().getContact().getUserId()).equals(marker.getTitle())){
            return null;
        }

        rendowWindowText(marker, mWindow);

        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {

        if((DaoImple.getInstance().getContact().getUserId()).equals(marker.getTitle())){

            return null;
        }
        rendowWindowText(marker,mWindow);
        return mWindow;
    }
}

package com.hosiluan.musicdemoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import static com.hosiluan.musicdemoapp.MainActivity.isProblemWhenDownloading;

/**
 * Created by User on 11/6/2017.
 */

public class MyBroadcast extends BroadcastReceiver {
    public final String IS_NETWORK_AVAILABLE = "is network available";

//    NetWorkStatusListener netWorkStatusListener = null;


    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            Toast.makeText(context, "network state change", Toast.LENGTH_SHORT).show();
            if (isNetworkAvailable(context)) {
            }
        }

        if (intent.getAction().equals("com.luan.PLAY_MUSIC")) {
            Toast.makeText(context, "nhận dc action bạn gửi ", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return ((networkInfo != null) && (networkInfo.isConnected()));
    }


}



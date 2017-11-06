package com.hosiluan.musicdemoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by User on 11/6/2017.
 */

public class MyBroadcast  extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
            Toast.makeText(context, "network state change", Toast.LENGTH_SHORT).show();
        }

        if (intent.getAction().equals("com.luan.MY_CUSTOM_BROADCAST")){
            Toast.makeText(context, "nhận dc action bạn gửi ", Toast.LENGTH_SHORT).show();
        }
    }
}

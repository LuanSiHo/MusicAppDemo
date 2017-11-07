package com.hosiluan.musicdemoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import static com.hosiluan.musicdemoapp.MainActivity.PATH;
import static com.hosiluan.musicdemoapp.MainActivity.isProblemWhenDownloading;

/**
 * Created by User on 11/7/2017.
 */

public class BaseActivity extends AppCompatActivity {


    DownloadMusicListener downloadMusicListener;

    public IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    public IntentFilter intentFilter1 = new IntentFilter("com.luan.PLAY_MUSIC");

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isNetworkAvailable(context)) {
                if (downloadMusicListener != null){
                    Log.d("Luan","retry download in base");
                    downloadMusicListener.retryDownload();
                }else {
                    Log.d("Luan","listener = null");
                }
            }

            if ( (intent != null) && (intent.getAction().equals("com.luan.PLAY_MUSIC")) ){
                Toast.makeText(context, "play music boysss", Toast.LENGTH_SHORT).show();
                playMusic("song1.mp3");
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, intentFilter);
        registerReceiver(mReceiver,intentFilter1);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    protected void playMusic(String songname) {

        String path = Environment.getExternalStorageDirectory() + "/" + songname;
        Log.d("Luan", "playMusic " + path);

        Intent intent = new Intent(BaseActivity.this, PlaySongService.class);
        intent.putExtra(PATH, path);
        this.startService(intent);

//
//        if (mediaPlayer.isPlaying()) {
//            mediaPlayer.stop();
//        }
//        try {
////            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(path));
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(path);
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    protected void sendBroadcast(String action){
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return ((networkInfo != null) && (networkInfo.isConnected()));
    }

    protected void setListener(Context context){
        downloadMusicListener = (DownloadMusicListener) context;
    }

    public interface DownloadMusicListener {
        void retryDownload();
    }
}

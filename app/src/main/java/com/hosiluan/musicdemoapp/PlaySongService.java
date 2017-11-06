package com.hosiluan.musicdemoapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;


import java.io.IOException;

import static com.hosiluan.musicdemoapp.MainActivity.PATH;

/**
 * Created by User on 11/6/2017.
 */

public class PlaySongService extends Service {

    private MediaPlayer mediaPlayer;

    public PlaySongService() {
        mediaPlayer = new MediaPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String path = intent.getStringExtra(PATH);

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
    }
}

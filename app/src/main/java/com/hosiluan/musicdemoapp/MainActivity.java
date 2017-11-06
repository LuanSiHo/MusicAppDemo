package com.hosiluan.musicdemoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ReyclerViewMusicAdapter.MusicAdapterListener {

    public static final String PATH = "music path";
    private final int REQUEST_PERMISSION = 1;
    private Button mDownloadMusicButton, mStopMusicButton, mSendBroadcastButton;
    private ArrayList<String> mMusicList;
    private ReyclerViewMusicAdapter mMusicAdapter;
    private RecyclerView mMusicecyclerView;
    private EditText mMusicLinkEditText;

    private int mSongNumber = 1;
    private MediaPlayer mediaPlayer;

    private ProgressBar progressBar;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setView();
        setEvent();

        checkAndRequestPermission();
        isExternalStorageReadable();
        getAllMp3File();

    }

    private void setEvent() {
        mDownloadMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadMusic().execute(mMusicLinkEditText.getText().toString().trim());
                getAllMp3File();
            }
        });

        mStopMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlaySongService.class);
                stopService(intent);
            }
        });

        mSendBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction("com.luan.MY_CUSTOM_BROADCAST");
                sendBroadcast(intent);
            }
        });
    }

    private void setView() {

        progressBar = findViewById(R.id.progressbar);

        mediaPlayer = new MediaPlayer();
        mMusicLinkEditText = findViewById(R.id.edt_music_link);

        mDownloadMusicButton = findViewById(R.id.btn_download_music);
        mStopMusicButton = findViewById(R.id.btn_stop_music);
        mSendBroadcastButton = findViewById(R.id.btn_send_broadcast);

        mMusicList = new ArrayList<>();
        mMusicecyclerView = findViewById(R.id.recyclerview_music);
        mMusicAdapter = new ReyclerViewMusicAdapter(getApplicationContext(), mMusicList,
                this);
        mMusicecyclerView.setAdapter(mMusicAdapter);
        mMusicecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));

    }


    private void playMusic(String songname) {

        String path = Environment.getExternalStorageDirectory() + "/" + songname;
        Log.d("Luan", "playMusic " + path);

        Intent intent = new Intent(MainActivity.this, PlaySongService.class);
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


    private void getAllMp3File() {
        String mp3Path = "";
        mMusicList.clear();
        Log.d("Luan", "size: " + mMusicList.size());

        String path = Environment.getExternalStorageDirectory() + "/";
        File dir = new File(path);

        if (dir.exists()) {
            if (dir.listFiles() != null) {
                for (File file : dir.listFiles()) {
                    if (file.isFile()) {
                        mp3Path = file.getPath();
                    }
                    if (mp3Path.contains(".mp3")) {
                        mMusicList.add(file.getName());
                        mMusicAdapter.notifyDataSetChanged();
                    }
                }
            }
        }

        Log.d("Luan", mMusicList.size() + " list size");
    }

    private void checkAndRequestPermission() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        ArrayList<String> neededPermissionList = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                neededPermissionList.add(permission);
            }
        }

        if (!neededPermissionList.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    neededPermissionList.toArray(new String[neededPermissionList.size()]), REQUEST_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                } else {

                }
            }
        }
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if ((Environment.MEDIA_MOUNTED.equals(state))
                || (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))) {
            return true;
        }
        return false;
    }

    @Override
    public void onPlayClick(int position) {
        playMusic(mMusicList.get(position));
    }

    class DownloadMusic extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                File file;
                FileOutputStream outputStream;
                file = new File(Environment.getExternalStorageDirectory(), "song" + mMusicList.size() + ".mp3");
                mMusicList.add(file.getName());

                Log.d("Luan", Environment.getExternalStorageDirectory().getAbsolutePath());
                outputStream = new FileOutputStream(file);

                byte[] data = new byte[1024];
                int count = 0;
                int total = 0;
                int lenghtOfFile = httpURLConnection.getContentLength();

                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data);
                    total += count;
                    publishProgress((int) (total * 100 / lenghtOfFile));
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Luan", "finish");
            Log.d("Luan", "here " + mMusicList.size());
            mMusicAdapter.notifyDataSetChanged();
            alertDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.processing_dialog, null);

            builder.setView(view1);
            progressBar = view1.findViewById(R.id.progressbar);

            alertDialog = builder.create();
            alertDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("Luan", values[0] + " percent");

            progressBar.setMax(100);
            progressBar.setProgress(values[0]);
        }
    }
}


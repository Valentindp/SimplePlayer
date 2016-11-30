package com.example.java.simpleplayer.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.compat.BuildConfig;
import android.util.Log;
import android.widget.Toast;

public class PlayBackService extends Service implements MediaPlayer.OnPreparedListener {

    public static final String TAG = PlayBackService.class.getSimpleName();

    private final IBinder mBinder = new PlayBackBinder();

    public static final String ACTION_PLAY = BuildConfig.APPLICATION_ID + ".action.PLAY";

    private MediaPlayer mMediaPlayer = null;

    public PlayBackService() {
    }

    public static Intent newInstance(Context context){
        return new Intent(context, PlayBackService.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "OnBind()", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mMediaPlayer.start();


    }

    public class PlayBackBinder extends Binder{
        public PlayBackService getService(){
            return PlayBackService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate");
        Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy");
        Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
        if (intent.getAction().equals(ACTION_PLAY)) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(this,getSongs());
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        }}catch (Exception e){
            e.printStackTrace();
        }
        return Service.START_STICKY;
    }

    private Uri getSongs(){
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
           // do {
            cursor.moveToNext();
            cursor.moveToNext();
            cursor.moveToNext();
            cursor.move(90);
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                Uri contentUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, thisId);
                return contentUri;
                // ...process entry...
          //  } while (cursor.moveToNext());
        }
        return uri;
    }
}

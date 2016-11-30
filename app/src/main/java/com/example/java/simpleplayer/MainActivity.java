package com.example.java.simpleplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.java.simpleplayer.services.PlayBackService;

public class MainActivity extends AppCompatActivity {

    private PlayBackService mService;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent playbackIntent = PlayBackService.newInstance(this);
        playbackIntent.setAction(PlayBackService.ACTION_PLAY);
        startService(playbackIntent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopService(PlayBackService.newInstance(MainActivity.this));
            }
        },2000);

    };

    private ServiceConnection mConnection  = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            PlayBackService.PlayBackBinder binder =
                    (PlayBackService.PlayBackBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent playBackIntent = PlayBackService.newInstance(this);
        bindService(playBackIntent,mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound){
            unbindService(mConnection);
            mBound = false;
        }
    }
}

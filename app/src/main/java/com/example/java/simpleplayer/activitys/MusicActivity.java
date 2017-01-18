package com.example.java.simpleplayer.activitys;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.java.simpleplayer.PreferencesUtility;
import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.Services.PlayBackService;


import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;


public class MusicActivity extends BaseActivity {


    public interface PlayBackInteraction {
        void pause();

        void play(long songId);

        boolean play();

        void  stopPlaying();

        boolean isPaused();

        Observable<Integer> gerDurationObservable();

        void onUserSeek(int progress);
    }

    protected PlayBackService mService;
    protected boolean mBound = false;

    private PlayBackInteraction mPlayBackInteraction;


    @Nullable
    public PlayBackInteraction getPlayBackInteraction() {
        return mService;
    }

    protected ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder iBinder) {
            PlayBackService.PlayBackBinder binder
                    = (PlayBackService.PlayBackBinder) iBinder;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public static Intent newIntent(Context context) {
        return new Intent(context, MusicActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Intent playBackService = PlayBackService.newInstance(this);
        startService(playBackService);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent playBackIntent = PlayBackService.newInstance(this);
        bindService(playBackIntent, mConnection, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }


    @Override
    public void onPause() {
        super.onPause();

    }

}

package com.example.java.simpleplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.java.simpleplayer.interfaces.SongsView;
import com.example.java.simpleplayer.model.Song;
import com.example.java.simpleplayer.presenters.SongPresenter;
import com.example.java.simpleplayer.services.PlayBackService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements SongsView {

    private PlayBackService mService;
    private boolean mBound = false;


    @BindView(R.id.songs_recycler_view)
    protected RecyclerView recyclerView;


    public static final String TAG = MainActivity.class.getSimpleName();

    private SongPresenter mSongPresenter = new SongPresenter();

    private ServiceConnection mConnection = new ServiceConnection() {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);


        mSongPresenter.onAttachToView(this);
        mSongPresenter.loadAllSongs();

//        Intent playBackIntent = PlayBackService.newInstance(this);
//        playBackIntent.setAction(PlayBackService.ACTION_PLAY);
//        startService(playBackIntent);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        Intent playBackIntent = PlayBackService.newInstance(this);
//        bindService(playBackIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSongPresenter.onDetachView();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onLoadListener(List<Song> songList) {
        Log.d(TAG, "" + songList.size());
        Toast.makeText(this,"" + songList.size(),Toast.LENGTH_SHORT).show();
    }

}

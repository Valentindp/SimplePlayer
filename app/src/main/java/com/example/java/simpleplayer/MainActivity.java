package com.example.java.simpleplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.java.simpleplayer.adapters.SongsAdapter;
import com.example.java.simpleplayer.interfaces.SongsView;
import com.example.java.simpleplayer.model.Song;
import com.example.java.simpleplayer.presenters.SongPresenter;
import com.example.java.simpleplayer.services.PlayBackService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements SongsView {

    private static final int SPAN_COUNT = 2;
    private PlayBackService mService;
    private boolean mBound = false;


    @BindView(R.id.songs_recycler_view)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.progres_bar)
    protected ProgressBar mProgressBar;


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

        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(
                this,
                SPAN_COUNT,
                RecyclerView.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        final SongsAdapter adapter = new SongsAdapter();
        adapter.setDataSource(songList);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final SongsAdapter.SongsViewHolder holder =
                        (SongsAdapter.SongsViewHolder) mRecyclerView.findContainingViewHolder(view);
                if(holder == null) return;
                final Song song = holder.getSong();
                final long songId = song.id;
                if(mBound) {
                    mService.playSongId(songId);
                }
            }
        });
    }


}

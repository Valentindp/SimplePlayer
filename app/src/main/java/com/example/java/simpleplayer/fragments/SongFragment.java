package com.example.java.simpleplayer.fragments;


import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.adapters.SongsAdapter;
import com.example.java.simpleplayer.interfaces.MusicView;
import com.example.java.simpleplayer.model.Song;
import com.example.java.simpleplayer.presenters.SongPresenter;
import com.example.java.simpleplayer.services.PlayBackService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.support.v4.app.Fragment;

/**
 * Created by Valentin on 10.12.2016.
 */

public class SongFragment extends Fragment implements MusicView {

    private static final int SPAN_COUNT = 2;
    private PlayBackService mService;
    private boolean mBound = false;

    @BindView(R.id.songs_recycler_view)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.progres_bar)
    protected ProgressBar mProgressBar;

    private SongPresenter mSongPresenter = new SongPresenter();


    @Override
    public void onLoadListener(List<Song> songList) {

        final SongsAdapter adapter = new SongsAdapter();
        adapter.setDataSource(songList);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(view -> {
            final SongsAdapter.SongsViewHolder holder =
                    (SongsAdapter.SongsViewHolder) mRecyclerView.findContainingViewHolder(view);
            if (holder == null) return;
            final Song song = holder.getSong();
            final long songId = song.id;
            if (mBound) {
                mService.playSongId(songId);
            }
        });


        adapter.setOnItemLongClickListener(view -> {
            if (mBound) {
                mService.stopPlaying();
                return true;
            }
            return false;
        });


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSongPresenter.onAttachToView(this);
        mSongPresenter.loadAllSongs();

        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(
                getActivity(),
                SPAN_COUNT,
                RecyclerView.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onPause() {
        super.onPause();
        mSongPresenter.onDetachView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_songs, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }


}

package com.example.java.simpleplayer.fragments;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.activitys.MusicActivity;
import com.example.java.simpleplayer.activitys.NavigateActivity;
import com.example.java.simpleplayer.adapters.SongsAdapter;
import com.example.java.simpleplayer.interfaces.MusicView;
import com.example.java.simpleplayer.model.Song;
import com.example.java.simpleplayer.presenters.SongPresenter;

import java.util.List;
import android.os.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

import android.support.v4.app.Fragment;

import static com.example.java.simpleplayer.activitys.MusicActivity.*;

/**
 * Created by Valentin on 10.12.2016.
 */

public class SongFragment extends Fragment implements MusicView {

    private PlayBackInteraction mPlayBackInteraction;

    private static final int SPAN_COUNT = 2;

    @BindView(R.id.songs_recycler_view)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.progres_bar)
    protected ProgressBar mProgressBar;

    private Observable<Song> mSongsObservable = null;

    protected SongsAdapter mSongsAdapter = new SongsAdapter();

    private SongPresenter mSongPresenter = new SongPresenter();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initPlayBackInteraction();
    }

    private void initPlayBackInteraction(){
        if(getActivity() instanceof MusicActivity) {
            mPlayBackInteraction = ((MusicActivity) getActivity())
                    .getPlayBackInteraction();
        }
    }


    @Override
    public void onLoadListener(List<Song> songList) {

        mSongsAdapter.setDataSource(songList);
        mProgressBar.setVisibility(View.GONE);

        mSongsAdapter.setOnItemClickListener(view -> {
            final SongsAdapter.SongsViewHolder holder =
                    (SongsAdapter.SongsViewHolder) mRecyclerView.findContainingViewHolder(view);
            if (holder == null) return;
            final Song song = holder.getSong();
            final long songId = song.getId();

            if (mPlayBackInteraction == null){
                initPlayBackInteraction();
            }

            if (mPlayBackInteraction != null) {
                mPlayBackInteraction.play(songId);
            }
        });



        mSongsAdapter.setOnItemLongClickListener(view -> {
            if (mPlayBackInteraction == null){
                initPlayBackInteraction();
            }

            if (mPlayBackInteraction != null) {
                mPlayBackInteraction.stopPlaying();
                return true;
            }
            return false;
        });

        mRecyclerView.setAdapter(mSongsAdapter);

        mSongsObservable = Observable.from(songList);


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

       new Handler().postDelayed(() -> {
        if(getActivity() instanceof NavigateActivity) {
            NavigateActivity menuActivity = (NavigateActivity) getActivity();
            menuActivity.getQueryObservable()
                    .doOnNext(query -> Log.d("TAG", query.toString()))
                    .flatMap(query -> mSongsObservable.filter(song -> song.getTitle().contains(query) ))
                    .toList()
                    .subscribe(songList -> {
                        mSongsAdapter.setDataSource(songList);});

        }
    }, 2000);
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

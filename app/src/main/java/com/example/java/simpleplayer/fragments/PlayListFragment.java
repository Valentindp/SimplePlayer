package com.example.java.simpleplayer.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.activitys.MusicActivity;
import com.example.java.simpleplayer.adapters.PlayListAdapter;
import com.example.java.simpleplayer.adapters.SongsAdapter;
import com.example.java.simpleplayer.interfaces.PlayListView;
import com.example.java.simpleplayer.interfaces.SongsView;
import com.example.java.simpleplayer.models.Song;
import com.example.java.simpleplayer.presenters.PlayListPresenter;
import com.example.java.simpleplayer.presenters.SongPresenter;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayListFragment extends Fragment implements PlayListView {

    @BindView(R.id.playlist_recycler_view)
    protected RecyclerView mRecyclerView;

    private PlayListPresenter mPresenter = new PlayListPresenter();


    public static PlayListFragment newInstance() {
        Bundle args = new Bundle();
        PlayListFragment fragment = new PlayListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_play_list, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.onAttachToView(this);
        mPresenter.loadPlayList();

    }

    @Override
    public void onPlayListLoaded(List<Song> songs) {
        PlayListAdapter adapter = new PlayListAdapter();
        adapter.setDataSource(songs);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDetach();
    }


}
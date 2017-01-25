package com.example.java.simpleplayer.interfaces;

import android.content.Context;

import com.example.java.simpleplayer.models.Song;

import java.util.List;

/**
 * Created by java on 23.01.2017.
 */

public interface PlayListView {

    Context getContext();

    void onPlayListLoaded(List<Song> List);
}

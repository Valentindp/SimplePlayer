package com.example.java.simpleplayer.interfaces;

import android.content.Context;

import com.example.java.simpleplayer.models.Song;

import java.util.List;

/**
 * Created by java on 05.12.2016.
 */

public interface SongsView {

    Context getContext();

    void onAllSongsLoaded(List<Song> List);

}

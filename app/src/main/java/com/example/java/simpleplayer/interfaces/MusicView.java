package com.example.java.simpleplayer.interfaces;

import android.content.Context;

import com.example.java.simpleplayer.model.Song;

import java.util.List;

/**
 * Created by java on 05.12.2016.
 */

public interface MusicView {

    Context getContext();

    void onLoadListener(List<Song> List);

}

package com.example.java.simpleplayer.presenters;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.java.simpleplayer.interfaces.SongsView;
import com.example.java.simpleplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by java on 05.12.2016.
 */

public class SongPresenter {

    private SongsView mView = null;


    public void onAttachToView(@NonNull SongsView songsView){
        mView = songsView;

    }

    public void loadAllSongs(){
        new AsyncTask<Void, Void, List<Song>>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<Song> doInBackground(Void... voids) {
                try {
                    return SongLoader.getAllSongs(mView.getContext());
                }catch (Exception e){
                    e.printStackTrace();
                }
                return new ArrayList<Song>();
            }

            @Override
            protected void onPostExecute(List<Song> songs) {
                super.onPostExecute(songs);
                if (mView == null) return;
                mView.onLoadListener(songs);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void onDetachView(){
        mView = null;
    }

}

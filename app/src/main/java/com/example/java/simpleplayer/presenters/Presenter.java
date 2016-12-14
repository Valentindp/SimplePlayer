package com.example.java.simpleplayer.presenters;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.java.simpleplayer.interfaces.MusicView;
import com.example.java.simpleplayer.model.Album;
import com.example.java.simpleplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by java on 05.12.2016.
 */

public class Presenter {

    private MusicView mView = null;


    public void onAttachToView(@NonNull MusicView musicView){
        mView = musicView;

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
                    return SongRepository.getAllSongs(mView.getContext());
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

    public void loadAllAlbums(){
        new AsyncTask<Void, Void, List<Album>>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<Album> doInBackground(Void... voids) {
                try {
                    return AlbumRepository.getAllAlbums(mView.getContext());
                }catch (Exception e){
                    e.printStackTrace();
                }
                return new ArrayList<Album>();
            }

            @Override
            protected void onPostExecute(List<Album> albums) {
                super.onPostExecute(albums);
                if (mView == null) return;
               // mView.onLoadListener(albums);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void onDetachView(){
        mView = null;
    }

}

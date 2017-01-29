package com.example.java.simpleplayer.presenters;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.java.simpleplayer.interfaces.SongsView;
import com.example.java.simpleplayer.models.Album;
import com.example.java.simpleplayer.models.Song;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by java on 05.12.2016.
 */

public class SongPresenter {

    private SongsView mView = null;

    private PlayListRepository mPlayListRepository = new PlayListRepository();

    private Subscription subscription = null;


    public void onAttachToView(@NonNull SongsView songsView){
        mView = songsView;

    }

   /* public void loadAllSongs(){
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
                mView.onAllSongsLoaded(songs);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }*/
    public void addToPlayList(Song song) {
        mPlayListRepository.addSong(song);
    }

    public void loadAllSongs() {
        subscription = Observable.just(SongRepository.getAllSongs(mView.getContext()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> { mView.onAllSongsLoaded(songs);},
                        Throwable::printStackTrace);

    }


    public void onDetachView(){
        mView = null;
        if(subscription != null)
            subscription.unsubscribe();
    }

}

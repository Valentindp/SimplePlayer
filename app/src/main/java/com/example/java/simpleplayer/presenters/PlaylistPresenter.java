package com.example.java.simpleplayer.presenters;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.java.simpleplayer.interfaces.PlayListView;
import com.example.java.simpleplayer.models.PlayListModel;
import com.example.java.simpleplayer.models.Song;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.internal.util.SubscriptionList;


/**
 * Created by java on 23.01.2017.
 */

public class PlayListPresenter {

    private PlayListView mView = null;

    private SubscriptionList mSubscriptionList = new SubscriptionList();

    private PlayListRepository mPlayListRepository = new PlayListRepository();

    public void onAttachToView(@NonNull PlayListView songsView) {
        mView = songsView;
    }

    public void loadPlayList() {
        Subscription subscription = mPlayListRepository.loadPlayList()
                .map(PlayListModel::getSongRealmList)
                .subscribe(songs -> mView.onPlayListLoaded(songs), Throwable::printStackTrace);
        mSubscriptionList.add(subscription);

//        new AsyncTask<Void, Void, List<Song>>(){
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected List<Song> doInBackground(Void... voids) {
//                try {
//                    return SongRepository.getAllSongs(mView.getContext());
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                return new ArrayList<Song>();
//            }
//
//            @Override
//            protected void onPostExecute(List<Song> songs) {
//                super.onPostExecute(songs);
//                if (mView == null) return;
//                mView.onPlayListLoaded(songs);
//            }
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    public void onDetach() {
        mSubscriptionList.unsubscribe();
    }

}

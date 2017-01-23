package com.example.java.simpleplayer.presenters;

import com.example.java.simpleplayer.models.PlayListModel;
import com.example.java.simpleplayer.models.Song;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import rx.Completable;
import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Action0;

/**
 * Created by java on 18.01.2017.
 */

public class PlayListRepository {

    private Realm mRealm = Realm.getDefaultInstance();

    public Single<PlayListModel> loadPlayList() {

        return Single.create(new Single.OnSubscribe<PlayListModel>() {
            @Override
            public void call(SingleSubscriber<? super PlayListModel> singleSubscriber) {
                mRealm.executeTransaction(realm -> {
                    try {
                        final PlayListModel result = realm
                                .where(PlayListModel.class)
                                .findFirst();
                        singleSubscriber.onSuccess(result);
                    } catch (Exception e) {
                        singleSubscriber.onError(e);
                    }
                });
            }
        });
    }

    public void addSong(Song song) {
        mRealm.executeTransactionAsync(realm -> {
            PlayListModel playListModel = realm.where(PlayListModel.class).findFirst();
            if (playListModel == null) {
                playListModel = new PlayListModel();
            }
            playListModel.getSongRealmList().add(song);
        });
    }


    private Completable clear() {
        return Completable.fromAction(() -> mRealm.executeTransactionAsync(realm -> {
            realm.delete(PlayListModel.class);
        }));
    }


    private Single<Song> getSongAfter(Long id) {
        return Single.create(singleSubscriber -> {
            mRealm.executeTransaction(realm -> {
                PlayListModel playList = realm.where(PlayListModel.class).findFirst();
                RealmList<Song> songs = playList.getSongRealmList();

                Song song = realm.where(Song.class).equalTo("id", id).findFirst();
                if (song == null) {
                    singleSubscriber
                            .onError(new IllegalArgumentException("Can`t find song whith id " + id + " in database"));
                }

                int currentSongIndex = songs.indexOf(song);

                if (currentSongIndex == -1){
                    singleSubscriber
                            .onError(new IllegalArgumentException("Can`t find song whith id " + id + " in play list"));
                }

                currentSongIndex++;
                if(currentSongIndex <= songs.size()) {
                    singleSubscriber.onSuccess(songs.get(currentSongIndex));
                }
                singleSubscriber.onError(new IllegalArgumentException("can't find song with id " + id));


            });
        });
    }

}

package com.example.java.simpleplayer.presenters;

import com.example.java.simpleplayer.model.PlayListModel;
import com.example.java.simpleplayer.model.Song;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Single;
import rx.SingleSubscriber;

/**
 * Created by java on 18.01.2017.
 */

public class PlaylistRepository {

    private Realm mRealm = Realm.getDefaultInstance();

    public Single<PlayListModel> loadPlayList(){

       return Single.create(new Single.OnSubscribe<PlayListModel>() {
            @Override
            public void call(SingleSubscriber<? super PlayListModel> singleSubscriber) {
                mRealm.executeTransaction(realm -> {
                    try {
                        final PlayListModel result = realm
                                .where(PlayListModel.class)
                                .findFirst();
                        singleSubscriber.onSuccess(result);
                    }catch (Exception e){
                        singleSubscriber.onError(e);
                    }
                });
            }
        });
    }

    public void addSongToPlayList(Song song){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

            }
        });

    }
}

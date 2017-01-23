package com.example.java.simpleplayer.models;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by java on 18.01.2017.
 */

@RealmClass
public class PlayListModel implements RealmModel {

    @PrimaryKey
    private long id;

    private RealmList<Song> songRealmList = new RealmList<>();

    public RealmList<Song> getSongRealmList() {
        return songRealmList;
    }

    public void setSongRealmList(RealmList<Song> songRealmList) {
        this.songRealmList = songRealmList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

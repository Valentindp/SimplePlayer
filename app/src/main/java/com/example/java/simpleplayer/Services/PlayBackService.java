package com.example.java.simpleplayer.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.compat.BuildConfig;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.java.simpleplayer.activitys.MusicActivity;
import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.presenters.PlayListRepository;

import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.subjects.PublishSubject;

public class PlayBackService extends Service implements
        MediaPlayer.OnPreparedListener,
        MusicActivity.PlayBackInteraction {

    public static final String TAG = PlayBackService.class.getSimpleName();

    private final IBinder mBinder = new PlayBackBinder();
    private static final int NOTIFICATION_ID = 101;

    public static final String ACTION_PLAY = BuildConfig.APPLICATION_ID + ".action.PLAY";

    private MediaPlayer mMediaPlayer = null;


    private boolean isPaused;

    private PublishSubject<Integer> mDurationSubject = PublishSubject.create();

    public PlayBackService() {
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, PlayBackService.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "OnBind()", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mMediaPlayer.start();

        PendingIntent pi = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                new Intent(getApplicationContext(), MusicActivity.class),
                PendingIntent.FLAG_NO_CREATE);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello world!")
                        .setContentIntent(pi);

        startForeground(NOTIFICATION_ID, builder.build());

    }

    @Override
    public void stopPlaying() {
        try {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public void pause() {
        try {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                isPaused = true;
                timer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void play(long songId) {
        playSongId(songId);
    }

    @Override
    public boolean play() {
        try {
            if(mMediaPlayer != null && isPaused) {
                mMediaPlayer.start();
                isPaused = false;

                timer.scheduleAtFixedRate(new DurationTimerTask(), 0, 1000);

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public class PlayBackBinder extends Binder {
        public PlayBackService getService() {
            return PlayBackService.this;
        }

    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public void playSongId(long songId) {
        // Song song = SongsRepository.getSongForID(this, songId);
        Uri contentUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                songId);
        try {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(this, contentUri);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnCompletionListener(mediaPlayer ->
                    new PlayListRepository()
                            .getNextSongAfter(songId)
                            .subscribe(song -> playSongId(song.getId()), throwable -> {
                                Toast.makeText(
                                        PlayBackService.this,
                                        throwable.getMessage(),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Observable<Integer> gerDurationObservable() {
        return mDurationSubject;
    }

    @Override
    public void onUserSeek(int progress) {
        try {
            if(mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                int seekPosition = (mMediaPlayer.getDuration() / 100) * progress;
                mMediaPlayer.seekTo(seekPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Timer timer = new Timer();

    private class DurationTimerTask extends TimerTask {

        @Override
        public void run() {
            int current = (mMediaPlayer.getCurrentPosition() * 100) / mMediaPlayer.getDuration();
            mDurationSubject.onNext(current);
        }

    }

}

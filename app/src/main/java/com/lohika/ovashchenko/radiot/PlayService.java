package com.lohika.ovashchenko.radiot;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by ovashchenko on 11/3/16.
 */
public class PlayService extends Service {
    private MediaPlayer mediaPlayer;
    private final IBinder mBinder = new PlayBinder();

    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        this.playMusic();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void playMusic() {
            mediaPlayer = MediaPlayer.create(this, Uri.parse("http://n4.radio-t.com/rtfiles/rt_podcast519.mp3"));
            mediaPlayer.start();
    }



    public class PlayBinder extends Binder {
        PlayService getService() {
            return PlayService.this;
        }
    }
}

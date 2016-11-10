package com.lohika.ovashchenko.radiot;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by ovashchenko on 11/3/16.
 */
public class PlayService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        this.playMusic();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void playMusic() {
        Resources res = getResources();

        if (mediaPlayer.isPlaying()) {
            //mPlayPause.setText(res.getString(R.string.play));
            mediaPlayer.pause();
        } else {
            mediaPlayer = MediaPlayer.create(this, Uri.parse("http://n4.radio-t.com/rtfiles/rt_podcast519.mp3"));
            mediaPlayer.start();
            //mPlayPause.setText(res.getString(R.string.pause));
        }

    }
}

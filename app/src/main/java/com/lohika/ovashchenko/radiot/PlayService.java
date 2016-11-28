package com.lohika.ovashchenko.radiot;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by ovashchenko on 11/3/16.
 */
public class PlayService extends Service implements MediaPlayer.OnCompletionListener {
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_foward";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";

    private MediaPlayer mediaPlayer;
    private String playURL = "";

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_STICKY;
    }

    private void handleIntent(Intent intent) {
        if( intent == null || intent.getAction() == null ) {
            return;
        }
        String action = intent.getAction();

        if( action.equalsIgnoreCase(ACTION_PLAY) ) {
            String url = intent.getStringExtra(Constants.SONG_URL);
            if (url.equals(playURL)) {
                mediaPlayer.start();
            } else {
                playURL = url;
                mediaPlayer = MediaPlayer.create(this, Uri.parse(url));
                mediaPlayer.start();
            }

        } else if(action.equalsIgnoreCase(ACTION_PAUSE)) {
            mediaPlayer.pause();
        } else if(action.equalsIgnoreCase(ACTION_STOP)) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer _mediaPlayer) {
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    public class PlayBinder extends Binder {
//        PlayService getService() {
//            return PlayService.this;
//        }
//    }
}

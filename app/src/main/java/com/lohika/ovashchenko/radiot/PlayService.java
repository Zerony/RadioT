package com.lohika.ovashchenko.radiot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Created by ovashchenko on 11/3/16.
 */
public class PlayService extends Service implements MediaPlayer.OnCompletionListener {
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_STOP = "action_stop";
    public static final int NOTIFICATION_ID = 1;
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
                PendingIntent pi = PendingIntent.getActivity(this, 0,
                        new Intent(getApplicationContext(), PlayService.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setAutoCancel(true).setTicker("Play music").setContentIntent(pi);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = builder.build();
                notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
                startForeground(NOTIFICATION_ID, notification);
                manager.notify(NOTIFICATION_ID, notification);
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
        stopForeground(true);
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

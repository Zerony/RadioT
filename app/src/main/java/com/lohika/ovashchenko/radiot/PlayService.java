package com.lohika.ovashchenko.radiot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by ovashchenko on 11/3/16.
 */
public class PlayService extends Service implements MediaPlayer.OnCompletionListener {
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_STOP = "action_stop";
    public static final int NOTIFICATION_ID = 1;

    public static final int START_ACTIVITY_PI = 0;
    public static final int PAUSE_PLAYING_PI = 1;
    public static final int STOP_PLAYING_PI = 2;
    public static final int START_PLAYING_PI = 3;

    private MediaPlayer mediaPlayer;
    private String playURL = "";
    private String songName = "";
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
        String action = intent.getStringExtra(Constants.SERVICE_ACTION);
        if( intent == null || action == null ) {
            return;
        }
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        if( action.equalsIgnoreCase(ACTION_PLAY) ) {
            String url = intent.getStringExtra(Constants.SONG_URL);
            if (mediaPlayer.isPlaying() && !url.equals(playURL)) {
                mediaPlayer.stop();
            }

            if (url.equals(this.playURL)) {
                this.mediaPlayer.start();
                NotificationCompat.Builder builder = getPauseStopBuilder();
                manager.notify(NOTIFICATION_ID, builder.build());
            } else {
                this.playURL = url;
                this.songName = intent.getStringExtra(Constants.SONG_NAME);
                this.mediaPlayer = MediaPlayer.create(this, Uri.parse(url));
                this.mediaPlayer.start();

                NotificationCompat.Builder builder = getPauseStopBuilder();
                Notification notification = builder.build();
                //notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

                startForeground(NOTIFICATION_ID, notification);
                manager.notify(NOTIFICATION_ID, notification);
            }

        } else if(action.equalsIgnoreCase(ACTION_PAUSE)) {
            this.mediaPlayer.pause();
            NotificationCompat.Builder builder = this.getPlayStopBuilder();

            manager.notify(NOTIFICATION_ID, builder.build());
        } else if(action.equalsIgnoreCase(ACTION_STOP)) {
            stopForeground(true);
            this.mediaPlayer.stop();
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

    private NotificationCompat.Builder getPauseStopBuilder() {
        return buildBaseBuilder()
                .addAction(this.buildPauseAction())
                .addAction(this.buildStopAction());
    }

    private NotificationCompat.Builder getPlayStopBuilder() {
        return buildBaseBuilder()
                .addAction(this.buildPlayAction())
                .addAction(this.buildStopAction());
    }

    private NotificationCompat.Builder buildBaseBuilder() {
        return new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setTicker(this.songName)
                .setContentIntent(this.buildShowActivityPI())
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(this.getResources().getString(R.string.notification_label))
                .setContentText(this.songName);
    }

    private PendingIntent buildShowActivityPI() {
        return PendingIntent.getActivity(this,
                START_ACTIVITY_PI,
                new Intent(getApplicationContext(), PlayMusicActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private NotificationCompat.Action buildPlayAction() {
        Intent playIntent = new Intent(this, PlayService.class);
        playIntent.putExtra(Constants.SERVICE_ACTION, ACTION_PLAY);
        playIntent.putExtra(Constants.SONG_URL, this.playURL);
        PendingIntent playPIntent = PendingIntent.getService(this, START_PLAYING_PI, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Action.Builder(android.R.drawable.ic_media_play,
                "Play", playPIntent).build();
    }

    private NotificationCompat.Action buildPauseAction() {
        Intent pauseIntent = new Intent(this, PlayService.class);
        pauseIntent.putExtra(Constants.SERVICE_ACTION, ACTION_PAUSE);
        PendingIntent pausePIntent = PendingIntent.getService(this, PAUSE_PLAYING_PI, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Action.Builder(android.R.drawable.ic_media_pause,
                "Pause", pausePIntent).build();
    }

    private NotificationCompat.Action buildStopAction() {
        Intent stopIntent = new Intent(this, PlayService.class);
        stopIntent.putExtra(Constants.SERVICE_ACTION, ACTION_STOP);
        PendingIntent stopPIntent = PendingIntent.getService(this, STOP_PLAYING_PI, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Action.Builder(R.drawable.ic_media_stop,
                "Stop", stopPIntent).build();
    }

}

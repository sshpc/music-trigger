package com.example.musictrigger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.util.Log;

public class MainActivity extends Activity {

    private static final String TAG = "MusicTrigger";

    // Common music apps in priority order
    private static final String[][] MUSIC_APPS = {
        {"com.tencent.qqmusic",      "QQ音乐"},
        {"com.netease.cloudmusic",    "网易云音乐"},
        {"com.kugou.android",         "酷狗音乐"},
        {"com.kuwomusic.kuwo",        "酷我音乐"},
        {"cn.kuwo.player",            "酷我音乐"},
        {"com.xiaomi.music",          "小米音乐"},
        {"com.samsung.android.music", "三星音乐"},
        {"com.apple.android.music",   "Apple Music"},
        {"com.spotify.music",         "Spotify"},
        {"deezer.android.app",        "Deezer"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launchMusicAppThenPlay();
    }

    private void launchMusicAppThenPlay() {
        String detectedPkg = null;
        String detectedName = null;

        PackageManager pm = getPackageManager();
        for (String[] app : MUSIC_APPS) {
            try {
                pm.getPackageInfo(app[0], 0);
                detectedPkg = app[0];
                detectedName = app[1];
                Log.i(TAG, "Found music app: " + detectedName + " (" + detectedPkg + ")");
                break;
            } catch (PackageManager.NameNotFoundException e) {
                // not installed, try next
            }
        }

        if (detectedPkg != null) {
            // Launch the music app
            try {
                Intent launchIntent = pm.getLaunchIntentForPackage(detectedPkg);
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);
                    Log.i(TAG, "Launched " + detectedName);
                }
            } catch (Exception e) {
                Log.w(TAG, "Failed to launch " + detectedName + ": " + e.getMessage());
            }

            // Wait for the app to initialize, then send play command
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                requestAudioFocusAndPlay();
                finish();
            }, 1500);
        } else {
            // No music app found, just try to send media play
            Log.w(TAG, "No music app found, sending media play directly");
            requestAudioFocusAndPlay();
            new Handler(Looper.getMainLooper()).postDelayed(this::finish, 500);
        }
    }

    private void requestAudioFocusAndPlay() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        audioManager.requestAudioFocus(
                focusChange -> {},
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
        );

        dispatchMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_PLAY);

        // Try play twice with a short gap to be more reliable
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            dispatchMediaKeyEvent(KeyEvent.KEYCODE_MEDIA_PLAY);
        }, 500);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            audioManager.abandonAudioFocus(focusChange -> {});
        }, 3000);
    }

    private void dispatchMediaKeyEvent(int keyCode) {
        long now = System.currentTimeMillis();
        KeyEvent downEvent = new KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0);
        KeyEvent upEvent = new KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.dispatchMediaKeyEvent(downEvent);
        audioManager.dispatchMediaKeyEvent(upEvent);
    }
}
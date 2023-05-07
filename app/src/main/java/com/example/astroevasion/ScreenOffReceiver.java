package com.example.astroevasion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class ScreenOffReceiver extends BroadcastReceiver {
    private MusicService musicService;

    public ScreenOffReceiver(MusicService service) {
        musicService = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            boolean musicWasPlaying = MusicService.isMusicPlaying();

            // Pause the music if it's currently playing
            if (musicWasPlaying) {
                musicService.pauseMusic();
            }

            // Save the music state so we can resume it later
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("musicWasPlaying", musicWasPlaying);
            editor.apply();
        }
    }
}

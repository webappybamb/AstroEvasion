package com.example.astroevasion;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {
    private SwitchCompat musicSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        musicSwitch = findViewById(R.id.music_switch);
        musicSwitch.setChecked(MusicService.isMusicPlaying());
        // Set the OnCheckedChangeListener for the music switch
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Show the confirmation dialog if the user turns off the music
                if (isChecked) {
                    startService(new Intent(SettingsActivity.this, MusicService.class));
                } else {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                    boolean confirmationDisabled = prefs.getBoolean("musicConfirmationDisabled", true);
                    if (confirmationDisabled) {
                        stopService(new Intent(SettingsActivity.this, MusicService.class));
                    } else {
                        showConfirmationDialog();
                    }
                }
            }
        });
    }

    private void showConfirmationDialog() {
        // Create the dialog using the built-in AlertDialog class
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Turn off music?")
                .setMessage("Are you sure you want to turn off the background music?")
                .setPositiveButton("Turn off", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "Turn off"
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("musicWasPlaying", false);
                        editor.apply();

                        musicSwitch.setChecked(false);
                        stopService(new Intent(SettingsActivity.this, MusicService.class));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "Cancel"
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("musicWasPlaying", true);
                        editor.apply();

                        musicSwitch.setChecked(true);
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Never ask me again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "Never ask me again"
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("musicConfirmationDisabled", true);
                        editor.putBoolean("musicWasPlaying", false);
                        editor.apply();

                        musicSwitch.setChecked(false);
                        stopService(new Intent(SettingsActivity.this, MusicService.class));
                        dialog.dismiss();
                    }
                })
                .create();

        // Show the dialog
        dialog.show();
    }
}

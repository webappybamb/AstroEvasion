package com.example.astroevasion;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    ImageView background;
    RelativeLayout menuLayout;
    ImageView menuButton;
    ImageView playImage;
    ImageView logoImage;
    EditText usernameEditText;
    private Button highScoreButton;
    private ScreenOffReceiver screenOffReceiver;
    private MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Initialize the music service
        resumeMusic();

        // Initialize the screen off receiver
        musicService = new MusicService();
        screenOffReceiver = new ScreenOffReceiver(musicService);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOffReceiver, filter);

//        menuLayout = (RelativeLayout) findViewById(R.id.menu_layout);
        menuButton = (ImageView) findViewById(R.id.menu_button);

        playImage = (ImageView) findViewById(R.id.play_image);
        logoImage = (ImageView) findViewById(R.id.name_image);

        usernameEditText = findViewById(R.id.username);
        usernameEditText.setText(getUsername());


        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(StartActivity.this, R.style.AppTheme_PopupMenu), menuButton);
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item1:
                                navigateToSettingsActivity();
                                return true;
                            case R.id.item2:
                                navigateToHighScoreActivity();
                                return true;
                            case R.id.item3:
                                // Do something for Item 3
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();  //showing popup menu
            }
        });

//        menuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(menuLayout.getVisibility() == View.GONE) {
//                    //Open the menu
//                    Animation logoAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_open);
//                    logoImage.startAnimation(logoAnimation);
//
//                    menuLayout.setVisibility(View.VISIBLE);
//                    Animation openAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_open);
//                    menuLayout.startAnimation(openAnimation);
//
//                    playImage.setVisibility(View.GONE);
//                    Animation playAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.play_open);
//                    playImage.startAnimation(playAnimation);
//                } else {
//                    //Close the menu
//                    Animation logoAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_close);
//                    logoImage.startAnimation(logoAnimation);
//
//                    Animation closeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_close);
//                    menuLayout.startAnimation(closeAnimation);
//                    menuLayout.setVisibility(View.GONE);
//
//                    playImage.setVisibility(View.VISIBLE);
//                    Animation playAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.play_close);
//                    playImage.startAnimation(playAnimation);
//
//                }
//            }
//        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;


        View screen = findViewById(R.id.layout);
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameText = usernameEditText;
                String playerName = usernameText.getText().toString().trim();
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("playerName", playerName);
                startActivity(intent);
            }
        });

        ValueAnimator animatorX = ValueAnimator.ofFloat(1f, 1.2f);
        animatorX.setDuration(1000);
        animatorX.setRepeatCount(ValueAnimator.INFINITE);
        animatorX.setRepeatMode(ValueAnimator.REVERSE);
        animatorX.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return (float) Math.sin(Math.PI * input);
            }
        });
        animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                playImage.setScaleX(scale);
            }
        });
        animatorX.start();
        ValueAnimator animatorY = ValueAnimator.ofFloat(1f, 1.2f);
        animatorY.setDuration(1000);
        animatorY.setRepeatCount(ValueAnimator.INFINITE);
        animatorY.setRepeatMode(ValueAnimator.REVERSE);
        animatorY.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return (float) Math.sin(Math.PI * input);
            }
        });
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                playImage.setScaleY(scale);
            }
        });
        animatorY.start();


        final ImageView planet1 = (ImageView) findViewById(R.id.planet1);
        final float startX = screenWidth;
        final float endX = (float) (- 3*screenWidth);


        ValueAnimator animator = ValueAnimator.ofFloat(startX, endX);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (float) animation.getAnimatedValue();
                planet1.setX(x);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                planet1.setX(startX - planet1.getWidth());
                animator.start();
            }
        });
        animator.start();

        final float startY = (int) (8 * screenHeight / 20);
        final float endY = (int) (-6 * screenHeight / 20);


        ValueAnimator animator2 = ValueAnimator.ofFloat(startY, endY);
        animator2.setInterpolator(new LinearInterpolator());
        animator2.setDuration(10000);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y = (float) animation.getAnimatedValue();
                planet1.setY(y);
            }
        });
        animator2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                planet1.setY(startY - planet1.getHeight());
                animator2.start();
            }
        });
        animator2.start();


        final ImageView planet2 = (ImageView) findViewById(R.id.planet2);
        final float startX2 = (float) (0.7*screenWidth);
        final float endX2 = (float) (-0.7*screenWidth);


        ValueAnimator animator3 = ValueAnimator.ofFloat(startX2, endX2);
        animator3.setInterpolator(new LinearInterpolator());
        animator3.setDuration(6400);
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (float) animation.getAnimatedValue();
                planet2.setX(x);
            }
        });
        animator3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                planet2.setX(startX2 - planet2.getWidth());
                animator3.start();
            }
        });
        animator3.start();

        final float startY2 = (float) - 3 * screenHeight/20;
        final float endY2 = (float) 43 * screenHeight/20;


        ValueAnimator animator4 = ValueAnimator.ofFloat(startY2, endY2);
        animator4.setInterpolator(new LinearInterpolator());
        animator4.setDuration(6400);
        animator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y = (float) animation.getAnimatedValue();
                planet2.setY(y);
            }
        });
        animator4.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                planet2.setY(startY2 - planet2.getHeight());
                animator4.start();
            }
        });
        animator4.start();


        final ImageView planet3 = (ImageView) findViewById(R.id.planet3);
        final float startX3 = (float) (-0.3*screenWidth);
        final float endX3 = (float) (3.6*screenWidth);


        ValueAnimator animator5 = ValueAnimator.ofFloat(startX3, endX3);
        animator5.setInterpolator(new LinearInterpolator());
        animator5.setDuration(14300);
        animator5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (float) animation.getAnimatedValue();
                planet3.setX(x);
            }
        });
        animator5.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                planet3.setX(startX3 - planet3.getWidth());
                animator5.start();
            }
        });
        animator5.start();

        final float startY3 = (float) 12 * screenHeight/20;
        final float endY3 = (float) -33 * screenHeight/20;


        ValueAnimator animator6 = ValueAnimator.ofFloat(startY3, endY3);
        animator6.setInterpolator(new LinearInterpolator());
        animator6.setDuration(14300);
        animator6.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y = (float) animation.getAnimatedValue();
                planet3.setY(y);
            }
        });
        animator6.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                planet3.setY(startY3 - planet3.getHeight());
                animator6.start();
            }
        });
        animator6.start();
    }

    private void navigateToHighScoreActivity() {
        Intent highScoreIntent = new Intent(StartActivity.this, ScoreTableActivity.class);
        startActivity(highScoreIntent);
    }

    private void navigateToSettingsActivity() {
        Intent settingsIntent = new Intent(StartActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String username = usernameEditText.getText().toString();
        saveUsername(username);
    }

    private void saveUsername(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    private String getUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenOffReceiver);
        stopService(new Intent(this, MusicService.class));
    }

    public void resumeMusic() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean musicWasPlaying = prefs.getBoolean("musicWasPlaying", true);

        if (musicWasPlaying) {
            Intent musicIntent = new Intent(this, MusicService.class);
            startService(musicIntent);
        }
    }

}


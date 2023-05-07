package com.example.astroevasion;

import com.google.firebase.FirebaseApp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements GameView.OnGameEventListener {

    private GameView mGameView;
    DatabaseReference highScoresRef = FirebaseDatabase.getInstance().getReference("highscores");
    String playerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        playerName = intent.getStringExtra("playerName");
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        mGameView = findViewById(R.id.game_view);
        mGameView.setOnGameEventListener(this);
    }



    @Override
    public void onItemCollision() {
        int lives = mGameView.getLives();
        if (lives > 0) {
            lives--;
            mGameView.setLives(lives);
            if (lives == 0) {
                gameOver();
            }
        }
    }

    @Override
    public void onItemCrossedScreen(int size) {
        int score = mGameView.getScore();
        score += size;
        mGameView.setScore(score);
    }

    public void gameOver() {
        Intent intent = new Intent(this, StartActivity.class);

        int playerScore = mGameView.getScore();
        savePlayerScore(playerName, playerScore);

        startActivity(intent);
    }

    private void savePlayerScore(String playerName, int playerScore) {
        if (playerScore < 1000) {return;}
        if (playerName.equals("")) {playerName = "anonymous_player";}
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("highscores");

        String playerId = databaseReference.push().getKey();
        HighScore highScore = new HighScore(playerId, playerName, playerScore);
        if (playerId != null) {
            databaseReference.child(playerId).setValue(highScore, new DatabaseReference.CompletionListener() {

                @Override
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                    if (error != null) {
                        Log.e("FirebaseSaveError", "Failed to save the high score", error.toException());
                    } else {
                        Log.d("FirebaseSaveSuccess", "High score saved successfully");
                    }
                }
            });

            Toast.makeText(this, "sent data to firebase!", Toast.LENGTH_SHORT).show();

        }
    }
}


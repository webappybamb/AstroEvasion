package com.example.astroevasion;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreTableActivity extends AppCompatActivity {

    private RecyclerView scoreRecyclerView;
    private HighScoreAdapter highScoreAdapter;
    private List<HighScore> highScoreList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);

        scoreRecyclerView = findViewById(R.id.scoreRecyclerView);
        scoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreRecyclerView.setHasFixedSize(true);

        highScoreList = new ArrayList<>();
        highScoreAdapter = new HighScoreAdapter(this, highScoreList);
        scoreRecyclerView.setAdapter(highScoreAdapter);

        fetchHighScores();
    }

    private void fetchHighScores() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("highscores");
        databaseReference.orderByChild("score").limitToLast(20).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                highScoreList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HighScore highScore = snapshot.getValue(HighScore.class);
                    if (highScore != null) {
                        highScoreList.add(highScore);
                    }
                }
                // Reverse the order of the highScoreList to display the scores in descending order
                Collections.reverse(highScoreList);
                HighScoreAdapter adapter = new HighScoreAdapter(ScoreTableActivity.this, highScoreList);
                scoreRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FetchHighScoresError", "Failed to fetch high scores", databaseError.toException());
            }
        });
    }
}

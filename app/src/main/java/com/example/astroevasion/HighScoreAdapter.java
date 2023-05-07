package com.example.astroevasion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.HighScoreViewHolder> {

    private Context context;
    private List<HighScore> highScoreList;

    public HighScoreAdapter(Context context, List<HighScore> highScoreList) {
        this.context = context;
        this.highScoreList = highScoreList;
    }

    @Override
    public HighScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.highscore_item, parent, false);
        return new HighScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HighScoreViewHolder holder, int position) {
        HighScore highScore = highScoreList.get(position);
        holder.playerNameTextView.setText(highScore.getPlayerName());
        holder.scoreTextView.setText(String.valueOf(highScore.getScore()));
    }

    @Override
    public int getItemCount() {
        return highScoreList.size();
    }

    public static class HighScoreViewHolder extends RecyclerView.ViewHolder {
        TextView playerNameTextView;
        TextView scoreTextView;

        public HighScoreViewHolder(View itemView) {
            super(itemView);
            playerNameTextView = itemView.findViewById(R.id.playerNameTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
        }
    }
}

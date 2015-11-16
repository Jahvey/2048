package com.dd.game1024;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dd.game1024.views.GameView;

public class MainActivity extends Activity {
    private GameView mGameView;
    private TextView mScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGameView = (GameView) findViewById(R.id.game_view);
        mScoreTextView = (TextView) findViewById(R.id.scroe);

        mGameView.setOnScoreListener(new GameView.onGameListener() {
            @Override
            public void onScore(int score) {
                mScoreTextView.setText(score+"");
            }
            @Override
            public void gameOver(int score) {
                mScoreTextView.setText(score+"");
            }
        });
    }

    public void onBegin(View view) {
        mGameView.startGame();
    }
}

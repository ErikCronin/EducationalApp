package com.erik.educationalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import static com.erik.educationalapp.MainActivity.deconstructedSavedScores;

public class HighscoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_highscore);

        ViewGroup scoreRows = findViewById(R.id.score_rows);
        getLayoutInflater().inflate(R.layout.highscore_text, scoreRows);

        TextView scoreView = findViewById(R.id.highscore_layout);
        scoreView.setText(deconstructedSavedScores);
    }
}
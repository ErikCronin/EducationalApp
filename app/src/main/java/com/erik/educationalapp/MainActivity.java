package com.erik.educationalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    private static final int REQUEST_CODE_QUIZ = 1;

    private Spinner spinnerDifficulty;

    private TextView textViewHighscore;

    private int highscore;

    private MediaPlayer player;
    public static SoundPool soundPool;
    private int soundConfirm, soundNewHighscore;

    public static ArrayList<String> savedScores = new ArrayList<>();
    public static String deconstructedSavedScores = "";
    public static int attemptNo = 0;

    static String convertToString(ArrayList<String> savedScores) {
        StringBuilder builder = new StringBuilder();
        // Append all integers in StringBuilder to the StringBuilder
        for (String number : savedScores) {
            builder.append("Attempt ").append(attemptNo).append(": ");
            builder.append(number);
            builder.append("\n");
            attemptNo++;
        }
        // Remove last delimiter with setLength
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        savedScores.add("");

        /* Creates what the user sees */
        textViewHighscore = findViewById(R.id.text_view_highscore);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);

        String[] difficultyLevels = Question.getAllDifficultyLevels();

        //Creates the layout for the difficulty spinner and displays it
        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        //adapterDifficulty for aesthetics
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);

        loadHighscore();

        /* Creates what the user hears - Background Music */
        //Checks if variable is empty and puts in background music, good for different song
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.bgm_game);

            //Restarts loop when music has finished
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.start();
                }
            });
        }

        //Max Streams variable to save having to updates twice in OS if statement
        int maxStreams = 4;

        //If statement to decide which variables are needed to play audio
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Will only run if the OS API 21 or higher
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(maxStreams)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            //Will only run if the OS API is 20 or lower
            soundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0);
        }

        //Loads in sounds from raw folder
        soundConfirm = soundPool.load(this, R.raw.ok, 1);
        soundNewHighscore = soundPool.load(this, R.raw.newhighscore, 1);


        player.start();
    }

    public void startQuiz(View view) {
        soundPool.play(soundConfirm, 1, 1, 0, 0, 1);

        //This string will pass the chosen difficulty
        String difficulty = spinnerDifficulty.getSelectedItem().toString();

        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);

    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openHighscore(View view) {
        deconstructedSavedScores = convertToString(savedScores);
        Intent intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                if (score > highscore) {
                    updateHighscore(score);
                }
            }
        }
    }

    private void loadHighscore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighscore.setText("Highscore: " + highscore);
    }

    private void updateHighscore(int highscoreNew) {
        highscore = highscoreNew;
        textViewHighscore.setBackgroundColor(Color.YELLOW);
        soundPool.play(soundNewHighscore, 1, 1, 0, 0, 1);

        textViewHighscore.setText("New Highscore: " + highscore + "!");

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }

    //Releases SoundPool so it doesn't consume system resources
    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }

    //Stops music and clears it from memory, use this to input another song
    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    //Stops music from playing when app is closed
    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}

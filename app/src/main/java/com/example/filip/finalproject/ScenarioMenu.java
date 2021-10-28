package com.example.filip.finalproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;

import java.util.ArrayList;

public class ScenarioMenu extends AppCompatActivity {

    protected static int[] starCounts = new int[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_menu);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainMenu.scenario = "Scenario 1";
                startActivity(new Intent(ScenarioMenu.this, FullscreenActivity.class)); //creates new scenario
                FullscreenActivity.memory = new ArrayList<>();
                GameEngine.replayMode = false;
                MainThread.run = true;
            }
        });
        for(int i = 0; i < starCounts.length;i++) {
            addStars(i,starCounts[i]);
        }
    }

    protected void addStars(int level, int starCount) {
        if (level == 0) {
            ImageView img1 = (ImageView) findViewById(R.id.imageView1);
            ImageView img2 = (ImageView) findViewById(R.id.imageView2);
            ImageView img3 = (ImageView) findViewById(R.id.imageView3);

            switch (starCount) {
                case 3: img3.setImageResource(android.R.drawable.star_on);
                case 2: img2.setImageResource(android.R.drawable.star_on);
                case 1: img1.setImageResource(android.R.drawable.star_on);
            }

        }
    }

    public static void adjustStarsAchieved(int level, int starCount) {
        if (starCounts[level] < starCount) {
            starCounts[level] = starCount;
        } else {
            return;//do something else?
        }
    }
}

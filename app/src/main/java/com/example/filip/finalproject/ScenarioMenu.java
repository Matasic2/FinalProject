package com.example.filip.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class ScenarioMenu extends AppCompatActivity {

    protected static byte[] starCounts = new byte[10];

    protected static boolean starsInitialized = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_menu);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainMenu.scenario = "Scenario 1";
                MapScenario.map_code = 0;
                startActivity(new Intent(ScenarioMenu.this, FullscreenActivity.class)); //creates new scenario
                FullscreenActivity.memory = new ArrayList<>();
                GameEngine.replayMode = false;
                MainThread.run = true;
            }
        });
        Button button13 = (Button) findViewById(R.id.button13);
        button13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainMenu.scenario = "Scenario 2";
                MapScenario.map_code = 1;
                startActivity(new Intent(ScenarioMenu.this, FullscreenActivity.class)); //creates new scenario
                FullscreenActivity.memory = new ArrayList<>();
                GameEngine.replayMode = false;
                MainThread.run = true;
            }
        });

        if (!starsInitialized) {
            loadStarProgress();
            starsInitialized = true;
        } else {
            saveStarProgress();
        }
        for(int i = 0; i < starCounts.length;i++) {
            addStars(i,starCounts[i]);
        }
    }

    protected void addStars(int level, byte starCount) {

        ImageView img1 = null;
        ImageView img2 = null;
        ImageView img3 = null;


        if (level == 0) {
            img1 = (ImageView) findViewById(R.id.imageView1);
            img2 = (ImageView) findViewById(R.id.imageView2);
            img3 = (ImageView) findViewById(R.id.imageView3);
        } else if (level == 1) {
            img1 = (ImageView) findViewById(R.id.imageView4);
            img2 = (ImageView) findViewById(R.id.imageView5);
            img3 = (ImageView) findViewById(R.id.imageView6);
        }

        switch (starCount) {
            case 3: img3.setImageResource(android.R.drawable.star_on);
            case 2: img2.setImageResource(android.R.drawable.star_on);
            case 1: img1.setImageResource(android.R.drawable.star_on);
        }

    }

    public static void adjustStarsAchieved(int level, byte starCount) {
        if (starCounts[level] < starCount) {
            starCounts[level] = starCount;
        } else {
            return;//do something else?
        }
    }

    public void saveStarProgress() {
        String[] files = fileList();
        if (files.length == 0) {
            File file = new File(getFilesDir(), "scenarioProgress");
        }
        try {
            FileOutputStream fos = openFileOutput("scenarioProgress", Context.MODE_PRIVATE);
            fos.write(starCounts);
            fos.close();
        } catch (Exception e) {

        }
    }

    public void loadStarProgress() {
        try {
            FileInputStream fis = openFileInput("scenarioProgress");
            for(int i = 0; i < starCounts.length; i++) {
                starCounts[i] = (byte) fis.read();
            }
            fis.close();
        } catch (Exception e) {
            return;
        }

    }

    public void onBackPressed() {
        startActivity(new Intent(ScenarioMenu.this, MainMenu.class));
    }
}

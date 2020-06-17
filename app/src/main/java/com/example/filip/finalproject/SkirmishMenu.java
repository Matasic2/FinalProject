package com.example.filip.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SkirmishMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skirmish_menu);
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainMenu.scenario = "Skirmish";
                startActivity(new Intent(SkirmishMenu.this, FullscreenActivity.class)); //new player vs player skirmish
                FullscreenActivity.memory = new ArrayList<>();
                //GameEngine.replayMode = false;
                MainThread.run = true;
            }
        });
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainMenu.scenario = "Skirmish vs AI";
                startActivity(new Intent(SkirmishMenu.this, FullscreenActivity.class)); // new player vs AI skirmish
                FullscreenActivity.memory = new ArrayList<>();
                //GameEngine.replayMode = false;
                MainThread.run = true;
            }
        });
        Button button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainMenu.scenario = "Skirmish vs AI_cheating";
                startActivity(new Intent(SkirmishMenu.this, FullscreenActivity.class)); // new player vs AI skirmish
                FullscreenActivity.memory = new ArrayList<>();
                GameEngine.replayMode = false;
                MainThread.run = true;
            }
        });
        Button button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Map.map_code == 0) {
                    return;
                }
                else if (Map.map_code >= Map.number_of_maps_available) {
                    Map.map_code = Map.number_of_maps_available - 1;
                } else {
                    Map.map_code--;
                }
                ((TextView)findViewById(R.id.textView2)).setText("Map mode : " + new Integer(Map.map_code).toString());
            }
        });
        Button button8 = (Button) findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Map.map_code < 0) {
                    Map.map_code = 0;
                }
                else if (Map.map_code >= Map.number_of_maps_available - 1) {
                    Map.map_code = Map.number_of_maps_available - 1;
                } else {
                    Map.map_code++;
                }
                ((TextView)findViewById(R.id.textView2)).setText("Map mode : " + new Integer(Map.map_code).toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((TextView)findViewById(R.id.textView2)).setText("Map mode : " + new Integer(Map.map_code).toString());
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((TextView)findViewById(R.id.textView2)).setText("Map mode : " + new Integer(Map.map_code).toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

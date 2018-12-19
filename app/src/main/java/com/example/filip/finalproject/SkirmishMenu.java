package com.example.filip.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
            }
        });
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainMenu.scenario = "Skirmish vs AI";
                startActivity(new Intent(SkirmishMenu.this, FullscreenActivity.class)); // new player vs AI skirmish
            }
        });
    }
}

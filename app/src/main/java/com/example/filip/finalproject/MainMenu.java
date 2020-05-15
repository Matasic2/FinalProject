package com.example.filip.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;


public class MainMenu extends AppCompatActivity  {

    //developer mode, should be set false on releases
    public static boolean ENABLE_DEV_MODE = false;

    //Name of scenario that is being played
    public static String scenario = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button button = (Button) findViewById(R.id.button); //button that switches main activity to skirmish menu
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                scenario = "Skirmish";
                startActivity(new Intent(MainMenu.this, SkirmishMenu.class));
            }
        });
        Button button2 = (Button) findViewById(R.id.button2); //button that switches main activity to scenario menu
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, ScenarioMenu.class));
            }
        });
        if (ENABLE_DEV_MODE) {
            Button devbutton = (Button) findViewById(R.id.devbutton); //button that switches main activity to skirmish menu
            devbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    scenario = "dev_mode";
                    startActivity(new Intent(MainMenu.this, FullscreenActivity.class));
                }
            });
        }

        Button switchFOW = (Button) findViewById(R.id.switch1); //button that changes if fog of war is visible
        switchFOW.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GameView.removeFogOfWar = !GameView.removeFogOfWar;
            }
        });
    }

    //@Override
    //public void onClick (View v) {
    //    GameView.removeFogOfWar = !GameView.removeFogOfWar;
    //}
}

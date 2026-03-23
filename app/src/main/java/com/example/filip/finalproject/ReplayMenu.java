package com.example.filip.finalproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ReplayMenu extends AppCompatActivity {

    public static int replayMapMode = 0;
    public static boolean replayTechEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay_menu);
        Button button12 = (Button) findViewById(R.id.button12);
        button12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainMenu.scenario = "Skirmish";
                GameEngine.replayMode = true;
                GameEngine.gameIsMultiplayer = false;
                //TechTree.techIsEnabled = replayTechEnabled;
                EditText simpleEditText = (EditText) findViewById(R.id.editText);
                String userText = simpleEditText.getText().toString();
                GameEngine.replayActionDelay = Integer.parseInt(userText);
                MapSkirmish.map_code = replayMapMode;
                startActivity(new Intent(ReplayMenu.this, FullscreenActivity.class));
            }
        });
    }

}

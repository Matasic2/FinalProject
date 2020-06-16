package com.example.filip.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MultiplayerMenu  extends AppCompatActivity {

    private static MultiplayerMenu instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.multiplayermenu);
        Button button10 = (Button) findViewById(R.id.button10);
        button10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MultiplayerConnection.hostGame();
            }
        });

        Button button11 = (Button) findViewById(R.id.button11);
        button11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MultiplayerConnection.joinGame();
            }
        });



    }

    public void changeTextInternal(String text) {
        setContentView(R.layout.multiplayermenu);
        TextView tv1 = (TextView)findViewById(R.id.textView);
        tv1.setText(text);
    }

    public static void changeText(String text) {
        instance.changeTextInternal(text);
    }

    public static void launchGame() {
        MainMenu.scenario = "Skirmish";
        FullscreenActivity.memory = new ArrayList<>();
        Map.map_code = 2;
        instance.startActivity(new Intent(instance, FullscreenActivity.class)); //new player vs player skirmish
    }
}
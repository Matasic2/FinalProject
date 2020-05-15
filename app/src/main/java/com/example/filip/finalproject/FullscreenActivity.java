package com.example.filip.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Picture;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.Display;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.graphics.Point;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FullscreenActivity extends Activity implements View.OnTouchListener{

    /*
    This method creates Fullscreen activity
     */
    public static float scaleFactor; //scale factor for all icons and tap coordinates
    public static int heightscreen; //height of the screen
    public static int widthfullscreen; //width of the screen
    public static FullscreenActivity theActivity; //stores the reference of the activity
    public static ArrayList<Integer> memory = new ArrayList<>();


    public static int lastDownX = 0;
    public static int lastDownY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState); //creates image

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //sets game to fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // flips game horizontally

        //takes height and width of the screen, and calculates the scale
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        heightscreen = height;
        int width = displayMetrics.widthPixels;
        widthfullscreen = width;
        float scale;

        //Scales down to smallest phone resolution
        if (height / 1440.0f > width / 2560.0f) {
            scale = width / 2560.0f;
        }   else {
            scale = height / 1440.0f;
        }
        scaleFactor = scale;


        GameView myGameView = new GameView(this); //creates context
        myGameView.setOnTouchListener(this); //sets listener
        setContentView(myGameView); //sets context
        theActivity = this;
        GameEngine.message = "Create";
    }

    //vibrates the phone
    public void vibrate() {
        long[] vibratepattern = new long[] {100,1500,1500};

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(vibratepattern, -1);
    }

    @Override
    public void onBackPressed() {
        if (GameEngine.message != null && GameEngine.message.equals("You are about to leave the battle, tap again to continue")) {
            FullscreenActivity.theActivity.backToMenu();
        }
        else {
            if (GameEngine.theUnit != null) {
                GameEngine.theUnit = null;
            }
            GameEngine.message = "You are about to leave the battle, tap again to continue";
        }
        return;
    }

    /*
    This will register the touch and send it to GameEngine for computing.
     */
    @Override
    public boolean onTouch (View view, MotionEvent event) {


        if (event.getAction() == MotionEvent.ACTION_UP) {
            GameEngine.tapProcessor((int) event.getX(), (int) event.getY(),0); //sends coordinates to GameEngine, which does everything.
            lastDownX = -1;
            lastDownY = -1;
        }

        else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            lastDownX = (int) event.getX() - GameView.cameraX;
            lastDownY = (int) event.getY() - GameView.cameraY;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            //don't allow moving screen around in air view
            if (GameView.showAir) {
                return true;
            }
            // TODO : Spremi u memoriu za spremanje aktivnosti
           // memory.add(new Integer((int) event.getX() * widthfullscreen + (int) event.getY()));
            GameView.targetCameraX = (int) event.getX() - lastDownX;
            GameView.targetCameraY = (int) event.getY() - lastDownY;
        }
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        MainThread.run = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MainThread.run = true;

    }

    @Override
    protected void onPause(){
        super.onPause();
        MainThread.run = false;

    }
    @Override
    protected void onResume(){
        super.onResume();
        MainThread.run = true;

        GameView myGameView = new GameView(this); //creates context
        myGameView.setOnTouchListener(this); //sets listener
        setContentView(myGameView); //sets context
        theActivity = this;
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        MainThread.run = false;

    }
    @Override
    protected void onStop() {
        super.onStop();
        MainThread.run = false;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        if (GameView.theContext != null && GameView.grid != null && GameView.thread != null) {
            int a = 0;
        }
        super.onSaveInstanceState(savedInstanceState);
    }
    public void backToMenu (){
        startActivity(new Intent(FullscreenActivity.this, MainMenu.class));
    }
}
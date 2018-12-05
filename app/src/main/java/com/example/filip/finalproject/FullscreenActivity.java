package com.example.filip.finalproject;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Picture;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.Display;
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

public class FullscreenActivity extends Activity implements View.OnTouchListener{

    /*
    This method should create the whole screen.
     */
    public static float scaleFactor;
    public static int heightscreen;
    public static int widthfullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState); //creates image?

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //sets game to fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // flips game horizontally

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        heightscreen = height;
        int width = displayMetrics.widthPixels;
        widthfullscreen = width;
        float scale;

        if (height / 1440.0f > width / 2560.0f) {
            scale = width / 2560.0f;
        }   else {
            scale = height / 1440.0f;
        }
        scaleFactor = scale;


        GameView myGameView = new GameView(this); //creates context
        myGameView.setOnTouchListener(this); //sets listener
        setContentView(myGameView); //sets context
    }

    /*
    This will register the touch and send it to GameEngine for computing.
     */
    @Override
    public boolean onTouch (View view, MotionEvent event) {
        GameEngine.tapProcessor((int) event.getX(), (int) event.getY()); //sends coordinates to GameEngine, which does everything.
        return true;
    }
    @Override
    protected void onPause(){
        super.onPause();


    }
    @Override
    protected void onResume(){
        super.onResume();

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        if (GameView.theContext != null && GameView.grid != null && GameView.thread != null) {
            int a = 0;
        }
        super.onSaveInstanceState(savedInstanceState);
    }
}
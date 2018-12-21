package com.example.filip.finalproject;

import android.graphics.Canvas;
import android.util.EventLog;
import android.view.SurfaceHolder;

////Most of the code from this class is taken from Java tutorial found online(https://www.youtube.com/watch?v=6prI4ZB_rXI). It's a great tutorial that got me started.

// This function keeps the game running and refreshes the screen.
public class MainThread extends Thread {

    public SurfaceHolder surfaceHolder;
    public GameView gameView;
    public static Canvas canvas;
    public static boolean run;

    public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {

        super();
        run = true;
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    @Override
    public void run() {

        while (run) {
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.update();
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) {
            }
            finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}

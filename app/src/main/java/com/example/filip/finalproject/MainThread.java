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
    public static boolean isRendering = false;

    public long previousTime = 0;

    public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {

        super();
        run = true;
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    @Override
    public void run() {
        while (run) {
            GameView.cameraY = GameView.targetCameraY;
            GameView.cameraX = GameView.targetCameraX;

            //adjust camera
            if (GameEngine.width > 15) {
                if (GameView.cameraX > 0) {
                    GameView.cameraX = 0;
                }
                if (GameView.cameraX < (15 - GameEngine.width) * GameEngine.squareLength) {
                    GameView.cameraX = (15 - GameEngine.width) * GameEngine.squareLength;
                }
            } else {
                GameView.cameraX = 0;
            }
            if (GameEngine.heigth > 9) {
                if (GameView.cameraY > 0) {
                    GameView.cameraY = 0;
                }
                if (GameView.cameraY < (9 - GameEngine.heigth) * GameEngine.squareLength) {
                    GameView.cameraY = (9 - GameEngine.heigth ) * GameEngine.squareLength;
                }
            } else {
                GameView.cameraY = 0;
            }

            //GameEngine.message = GameView.cameraX + " " + GameView.cameraY;

            synchronized (surfaceHolder) {
                this.gameView.update();
                canvas = surfaceHolder.lockCanvas();
                isRendering = true;
                this.gameView.draw(canvas);
                isRendering = false;
            }
            if (this.canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
                previousTime = System.currentTimeMillis();
            }
        }
        isRendering = false;
    }
}

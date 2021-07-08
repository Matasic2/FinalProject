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
    public static boolean shouldPause = false;

    public static boolean shouldCancelDrawing = false;
    public static int techScreenCameraXLimit = 0;

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
            renderFrame();
        }
    }

    public void renderFrame() {
        if (shouldPause) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
        }

        adjustCamera();
        //GameEngine.message = GameView.cameraX + " " + GameView.cameraY;

        boolean drawSucceeded = false;
        synchronized (surfaceHolder) {
            this.gameView.update();
            isRendering = true;
            canvas = surfaceHolder.lockCanvas();
            while (!drawSucceeded) {
                try {
                    this.gameView.draw(canvas);
                    drawSucceeded = true;
                } catch (Exception e) {

                }
            }
        }

        if (this.canvas != null && drawSucceeded && !shouldCancelDrawing) {
            surfaceHolder.unlockCanvasAndPost(canvas);
            previousTime = System.currentTimeMillis();
        } else if (shouldCancelDrawing) {
            shouldCancelDrawing = false;
        }
        isRendering = false;
    }

    public void adjustCamera() {
        GameView.cameraY = GameView.targetCameraY;
        GameView.cameraX = GameView.targetCameraX;

        //adjust camera
        if (GameView.activeScreen == GameView.Screen.TECH_SCREEN) {
            if (GameView.cameraX > 0) {
                GameView.cameraX = 0;
            } else if (GameView.cameraX < -techScreenCameraXLimit) {
                GameView.cameraX = -techScreenCameraXLimit;
            }

            GameView.cameraY = 0;
        } else {
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
            if (GameEngine.height > 9) {
                if (GameView.cameraY > 0) {
                    GameView.cameraY = 0;
                }
                if (GameView.cameraY < (9 - GameEngine.height) * GameEngine.squareLength) {
                    GameView.cameraY = (9 - GameEngine.height) * GameEngine.squareLength;
                }
            } else {
                GameView.cameraY = 0;
            }
        }
    }
}

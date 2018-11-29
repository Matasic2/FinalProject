package com.example.filip.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


//Most of the coded code as mentioned in comments is from this class is copied from Java tutorial found online(https://www.youtube.com/watch?v=6prI4ZB_rXI). It's a great tutorial that got me started.

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public static Context theContext; //context of the View, required for adding textures to units in other classes.
    public MainThread thread; //Game's thread. Copied from net, I have no idea why this is important :)
    public GameEngine grid = null; // Grid of the game
    public static SelectedUnit selected = null; //Player's selected unit
    public static SelectedUnit enemySelected = null; //opponent's selected unit
    public static Units[] units = new Units[0]; // Array of units that will be drawn, they don't have the physical location on board (In GameEngine class, BoardSprites does that).
    public static Resources[] resources = new Resources[0]; // Array of resources that will be drawn, they don't have the physical location on board (In GameEngine class, BoardResources does that).

    //also copied from internet, obviously important but I have no idea what it does.
    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

    }

    //another internet copy, doesn't seem to be doing anything except overriding a method.
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    //this is where the board and all starting units are initialized (with their respective textures).
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        GameEngine.green = new Player("green");
        GameEngine.red = new Player("red");
        BitmapFactory.Options o = new Options();
        o.inScaled = false;
        grid = new GameEngine(BitmapFactory.decodeResource(this.getResources(), R.mipmap.grid, o)); // these lines create the board.
        theContext = this.getContext(); // Stores the context, see the variable comment above


        // next lines generate green's "natural resources" (the resources which are expected to be controlled by green player).
        new Food(theContext,1,0,1,1);
        new Food(theContext,2,1,1,1);
        new Food(theContext,1,2,1,1);
        new Food(theContext,2,7,1,7);
        new Food(theContext,1,8,1,7);
        new Food(theContext,5,1,6,1);

        new oil(theContext, 6, 0, 6, 1);
        new oil(theContext, 1, 6, 1, 7);
        new oil(theContext, 0, 1, 1, 1);

        new Iron(theContext, 0, 7, 1, 7);
        new Iron(theContext, 6, 2, 6, 1);

        // next lines generate red's "natural resources" (the resources which are expected to be controlled by red player).
        new Food(theContext,13,8,13,7);
        new Food(theContext,12,7,13,7);
        new Food(theContext,13,6,13,7);
        new Food(theContext,12,1,13,1);
        new Food(theContext,13,0,13,1);
        new Food(theContext,9,7,8,7);

        new oil(theContext, 8, 8, 8, 7);
        new oil(theContext, 13, 2, 13, 1);
        new oil(theContext, 14, 7, 13, 7);

        new Iron(theContext, 14, 1, 13, 1);
        new Iron(theContext, 8, 6, 8, 7);



        new Headquaters(theContext, 1, 1, GameEngine.green);
        new Headquaters(theContext, 13, 7, GameEngine.red);

        // These for loops create starting units.
        for (int i = 0; i < 1; i++) {
            new Infantry(theContext, 2, 2, GameEngine.green);
        }

        for (int i = 0; i < 1; i++) {
            new Infantry(theContext, 12, 6, GameEngine.red);
        }

        for (int i = 1; i < 1; i++) {
            new Cavalry(theContext, 1, i * 2, GameEngine.green);
        }
        for (int i = 1; i < 1; i++) {
            new Cavalry(theContext, 12, i * 2, GameEngine.red);
        }

        for (int i = 5; i < 5; i++) {
            new Artillery(theContext, 0, i, GameEngine.green);
        }
        for (int i = 5; i < 5; i++) {
            new Artillery(theContext, 13, i, GameEngine.red);
        }
        selected = new SelectedUnit(theContext); // adds selected unit to the board, but doesn't show it until it has to.
        enemySelected = new SelectedUnit(theContext);
        GameEngine.playing = GameEngine.green;
        GameEngine.estimateResources();
        thread.start(); // starts the tread

    }

    //Destroys the (image of) board, also copied from internet.
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    // updates the grid, not sure if this is necessary.
    public void update() {
            grid.update();
    }

    //"Refreshes" the image, or (in other words) re-draws the whole screen.
    @Override
    public void draw (Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            grid.draw(canvas);  //draws the grid first, because that is the bottom layer.

            movableLocation pointers = new movableLocation(theContext, 0);
            movableLocation pointers2 = new movableLocation(theContext, 1);
            movableLocation pointers3 = new movableLocation(theContext, 2);
            pointers.draw(canvas,1,7);
            pointers.draw(canvas,13,1);
            pointers2.draw(canvas,6,1);
            pointers3.draw(canvas,8,7);

            for (int i = 0; i < resources.length; i++) {
                resources[i].draw(canvas); //draws the units from units array found in GameView class.
            }

            for (int i = 0; i < units.length; i++) {
                units[i].draw(canvas); //draws the units from units array found in GameView class.
            }

            if (GameEngine.selected != null) { //If no units are selected yet, do nothing
                GameEngine.selected.draw(canvas); //If they are, draw it.
            }

            if (GameEngine.enemySelected != null) { //If no units are selected yet, do nothing
                GameEngine.enemySelected.draw(canvas); //If they are, draw it.
            }

            //bottom five lines draw the text that displays the tap coordinates, should be removed in final version.
            Paint paint = new Paint();
            paint.setTextSize(60);
            paint.setColor(Color.RED);
            int[] tapCoord = GameEngine.getSquareCoordinates(GameEngine.lastTap[0], GameEngine.lastTap[1]);
            canvas.drawText( tapCoord[0]+ " " + tapCoord[1] + " " + Player.print(GameEngine.playing) + " " , 2000, 70, paint);

            //draws yellow squares where selected unit can move.
            if (GameEngine.theUnit != null && GameEngine.theUnit.hasMove == true) {
                for (int i = 0; i < GameEngine.BoardSprites.length; i++) { // TODO : optimize this
                    for (int j = 0; j < GameEngine.BoardSprites[i].length; j++) {
                        if (GameEngine.BoardSprites[i][j] == null &&
                                (GameEngine.theUnit.movement >= GameEngine.getSquareDistance           //also check if unit is in range.
                                (GameEngine.getCoordinates(GameEngine.theUnit)[0], i,
                                        GameEngine.getCoordinates(GameEngine.theUnit)[1], j))) {
                            movableLocation temp = new movableLocation(theContext);
                            temp.draw(canvas, i, j);
                        }
                    }
                }
            }


            //display info about player's selected unit
            if (GameEngine.theUnit != null) {
                GameEngine.theUnit.draw(canvas, 1950, 1040);
                paint = new Paint();
                paint.setTextSize(30);
                if (GameEngine.theUnit.owner == GameEngine.red) {
                    paint.setColor(Color.RED);
                }
                if (GameEngine.theUnit.owner == GameEngine.green) {
                    paint.setColor(Color.GREEN);
                }
                int[] unitInfo = {GameEngine.theUnit.attack1,
                        GameEngine.theUnit.attack1Range,
                        GameEngine.theUnit.attack2,
                        GameEngine.theUnit.attack2Range,
                        GameEngine.theUnit.HP,
                        GameEngine.theUnit.maxHP,
                        GameEngine.theUnit.defence,
                };
                String[] canMoveAndAttack = new String[2];
                if (GameEngine.theUnit.hasMove) {
                    canMoveAndAttack[0] = "can move this turn";
                }   else {
                    canMoveAndAttack[0] = "cannot move this turn";
                }
                if (GameEngine.theUnit.hasAttack) {
                    canMoveAndAttack[1] = "can attack this turn";
                }   else {
                    canMoveAndAttack[1] = "cannot attack this turn";
                }
                canvas.drawText( "Close attack damage :    " + unitInfo[0] + "  Range : "  + unitInfo[1], 2000, 1220, paint);
                canvas.drawText( "Ranged attack damage : " + unitInfo[2] + "  Range : "  + unitInfo[3], 2000, 1260, paint);
                canvas.drawText( "Health : " + unitInfo[4] + " / "  + unitInfo[5], 2000, 1300, paint);
                canvas.drawText( "Unit's defence : " + unitInfo[6], 2000, 1340, paint);
                if (GameEngine.theUnit.movement != 0) {
                    canvas.drawText("This unit " + canMoveAndAttack[0], 2000, 1380, paint);
                } else {
                    canvas.drawText("This unit cannot move", 2000, 1380, paint);
                }
                canvas.drawText( "This unit " + canMoveAndAttack[1], 2000, 1420, paint);
            }

            //display info about opponent's selected unit
            if (GameEngine.enemyTappedUnit != null) {
                GameEngine.enemyTappedUnit.draw(canvas, 1950, 640);
                paint = new Paint();
                paint.setTextSize(30);
                if (GameEngine.enemyTappedUnit.owner == GameEngine.red) {
                    paint.setColor(Color.RED);
                }
                if (GameEngine.enemyTappedUnit.owner == GameEngine.green) {
                    paint.setColor(Color.GREEN);
                }
                int[] unitInfo = {GameEngine.enemyTappedUnit.attack1,
                        GameEngine.enemyTappedUnit.attack1Range,
                        GameEngine.enemyTappedUnit.attack2,
                        GameEngine.enemyTappedUnit.attack2Range,
                        GameEngine.enemyTappedUnit.HP,
                        GameEngine.enemyTappedUnit.maxHP,
                        GameEngine.enemyTappedUnit. defence,
                };
                String[] canMoveAndAttack = new String[2];
                if (GameEngine.enemyTappedUnit.hasMove) {
                    canMoveAndAttack[0] = "can move this turn";
                }   else {
                    canMoveAndAttack[0] = "cannot move this turn";
                }
                if (GameEngine.enemyTappedUnit.hasAttack) {
                    canMoveAndAttack[1] = "can attack this turn";
                }   else {
                    canMoveAndAttack[1] = "cannot attack this turn";
                }
                canvas.drawText( "Close attack damage :    " + unitInfo[0] + "  Range : "  + unitInfo[1], 2000, 820, paint);
                canvas.drawText( "Ranged attack damage : " + unitInfo[2] + "  Range : "  + unitInfo[3], 2000, 860, paint);
                canvas.drawText( "Health : " + unitInfo[4] + " / "  + unitInfo[5], 2000, 900, paint);
                canvas.drawText( "Unit's defence : " + unitInfo[6], 2000, 940, paint);
                if (GameEngine.enemyTappedUnit.movement != 0) {
                    canvas.drawText("This unit " + canMoveAndAttack[0], 2000, 980, paint);
                } else {
                    canvas.drawText("This unit cannot move", 2000, 980, paint);
                }
                canvas.drawText( "This unit " + canMoveAndAttack[1], 2000, 1020, paint);
            }
        }

        Paint paint = new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.YELLOW);
        canvas.drawText( "Food storage : " + GameEngine.playing.foodStorage + " + " + GameEngine.lastAddedResounces[0], 50, 1360, paint);


        paint = new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.argb(255,204,102,0));
        canvas.drawText( "Iron storage : " + GameEngine.playing.ironStorage + " + " + GameEngine.lastAddedResounces[1], 50, 1280, paint);

        paint = new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.GRAY);
        canvas.drawText( "Oil storage : " + GameEngine.playing.oilStorage + " + " + GameEngine.lastAddedResounces[2], 50, 1200, paint);

    }

    //removes the sprite (drawing of a unit), called when unit is deleted.
    public static void removeSprite (int index) {
        Units[] toReplace = new Units[units.length - 1];
        for (int i = 0; i < index; i++) {
            toReplace[i] = units[i];
        }
        for (int i = index; i < toReplace.length; i++) {
            toReplace[i] = units[i + 1];
        }
        units = toReplace;
    }
}

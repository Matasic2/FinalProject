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
    public static MainThread thread; //Game's thread. Copied from net, I have no idea why this is important :)
    public static GameEngine grid = null; // Grid of the game
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

        GameEngine.green = new Player("green", true);
        GameEngine.red = new Player("red", true);
        if (MainMenu.scenario.equals("Skirmish vs AI")) {
            GameEngine.red.isHuman = false;
        }
        BitmapFactory.Options o = new Options();
        o.inScaled = false;
        Bitmap map = BitmapFactory.decodeResource(this.getResources(), R.mipmap.grid, o);
        map = Bitmap.createScaledBitmap(map,(int)(map.getWidth() * FullscreenActivity.scaleFactor), (int)(map.getHeight() * FullscreenActivity.scaleFactor), true);
        grid = new GameEngine(map); // these lines create the board.
        theContext = this.getContext(); // Stores the context, see the variable comment above

        if (MainMenu.scenario.equals("Skirmish") || MainMenu.scenario.equals("Skirmish vs AI")) {
            // next lines generate green's "natural resources" (the resources which are expected to be controlled by green player).
            new Food(theContext, 1, 0, 1, 1);
            new Food(theContext, 2, 1, 1, 1);
            new Food(theContext, 1, 2, 1, 1);
            new Food(theContext, 2, 7, 1, 7);
            new Food(theContext, 1, 8, 1, 7);
            new Food(theContext, 5, 1, 6, 1);

            new oil(theContext, 6, 0, 6, 1);
            new oil(theContext, 1, 6, 1, 7);
            new oil(theContext, 0, 1, 1, 1);

            new Iron(theContext, 0, 7, 1, 7);
            new Iron(theContext, 6, 2, 6, 1);

            // next lines generate red's "natural resources" (the resources which are expected to be controlled by red player).
            new Food(theContext, 13, 8, 13, 7);
            new Food(theContext, 12, 7, 13, 7);
            new Food(theContext, 13, 6, 13, 7);
            new Food(theContext, 12, 1, 13, 1);
            new Food(theContext, 13, 0, 13, 1);
            new Food(theContext, 9, 7, 8, 7);

            new oil(theContext, 8, 8, 8, 7);
            new oil(theContext, 13, 2, 13, 1);
            new oil(theContext, 14, 7, 13, 7);

            new Iron(theContext, 14, 1, 13, 1);
            new Iron(theContext, 8, 6, 8, 7);


            new Headquaters(theContext, 1, 1, GameEngine.green);
            new Headquaters(theContext, 13, 7, GameEngine.red);

            // These for loops create starting units.
            for (int i = 0; i < 2; i++) {
                new Infantry(theContext, 2, i * 2, GameEngine.green);
            }

            for (int i = 0; i < 1; i++) {
                new Infantry(theContext, 12, 6, GameEngine.red);
            }

            for (int i = 0; i < 0; i++) {
                new Cavalry(theContext, 1, i * 2, GameEngine.green);
            }
            for (int i = 0; i < 0; i++) {
                new Cavalry(theContext, 12, i * 2, GameEngine.red);
            }

            for (int i = 0; i < 0; i++) {
                new Artillery(theContext, 14, 8, GameEngine.green);
            }
            for (int i = 0; i < 0; i++) {
                new Artillery(theContext, 13, i, GameEngine.red);
            }
        }

        //Somme mission
        else if (MainMenu.scenario.equals("Somme")) {
            new Food(theContext, 1, 1, 1, 2);
            new Food(theContext, 0, 2, 1, 2);
            new Food(theContext, 1, 3, 1, 2);

            new Food(theContext, 1, 5, 1, 6);
            new Food(theContext, 2, 6, 1, 6);
            new Iron(theContext, 0, 6, 1, 6);
            new Iron(theContext, 1, 7, 1,6);

            new Food(theContext, 13, 1, 13, 2);
            new Food(theContext, 12, 2, 13, 2);
            new Food(theContext, 14, 2, 13, 2);
            new Food(theContext, 13, 3, 13, 2);

            new Food(theContext, 13, 5, 13, 6);
            new Iron(theContext, 14, 6, 13, 6);
            new Iron(theContext, 13, 7, 13,6);


            new Headquaters(theContext, 1, 2, GameEngine.green);
            new Headquaters(theContext, 1, 6, GameEngine.green);

            new Headquaters(theContext, 13, 2, GameEngine.red);
            new Headquaters(theContext, 13, 6, GameEngine.red);

            // These for loops create starting units.
            for (int i = 0; i < 9; i++) {
                new Infantry(theContext, 4, i, GameEngine.green);
            }

            for (int i = 0; i < 9; i++) {
                new Infantry(theContext, 10, i, GameEngine.red);
            }
            for (int i = 0; i < 4; i++) {
                new Infantry(theContext, 11,  1+ i * 2, GameEngine.red);
            }

            for (int i = 0; i < 9; i++) {
                new Cavalry(theContext, 3, i, GameEngine.green);
            }

            for (int i = 0; i < 2; i++) {
                new Artillery(theContext, 11, 2 + 4*i, GameEngine.red);
            }
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

            if (MainMenu.scenario.equals("Skirmish") || MainMenu.scenario.equals("Skirmish vs AI")) {
                movableLocation pointers = new movableLocation(theContext, 0);
                movableLocation pointers1 = new movableLocation(theContext, 1);
                movableLocation pointers2 = new movableLocation(theContext, 2);
                pointers.draw(canvas,1,7);
                pointers.draw(canvas,13,1);
                pointers1.draw(canvas,6,1);
                pointers2.draw(canvas,8,7);
            }
            movableLocation pointers3 = new movableLocation(theContext, 3);
            movableLocation pointers35 = new movableLocation(theContext, 14);
            movableLocation pointers4 = new movableLocation(theContext, 4);
            movableLocation pointers5 = new movableLocation(theContext, 5);
            movableLocation pointers6 = new movableLocation(theContext, 16);
            movableLocation pointers7 = new movableLocation(theContext, 17);
            movableLocation pointers8 = new movableLocation(theContext, 18);
            movableLocation pointers99 = new movableLocation(theContext, 22);
            pointers3.draw(canvas,16,1);
            pointers35.draw(canvas,18,1);
            pointers4.draw(canvas,16,3);
            pointers5.draw(canvas,5,10);
            pointers6.draw(canvas,18,3);
            pointers7.draw(canvas,2,2);
            pointers8.draw(canvas,12,6);
            pointers99.draw(canvas,5,9);

            Paint newPaint = new Paint();
            newPaint.setTextSize(65 * FullscreenActivity.scaleFactor);
            newPaint.setColor(Color.argb(255,154,52,0));

            if (GameEngine.theUnit != null) {
                movableLocation pointers9 = new movableLocation(theContext, 19);
                pointers9.draw(canvas,16,5);
                if (GameEngine.theUnit.unitType == "Infantry") {
                    if (GameEngine.theUnit.defence == Infantry.GreenDefence) {
                        canvas.drawText ("1", ((16*128)  * FullscreenActivity.scaleFactor + 52 * FullscreenActivity.scaleFactor),((5*128) * FullscreenActivity.scaleFactor + 84 * FullscreenActivity.scaleFactor),newPaint);
                    }
                }
                if (GameEngine.theUnit.unitType == "Cavalry") {
                    if (GameEngine.theUnit.defence == Cavalry.GreenDefence) {
                            canvas.drawText ("1", ((16*128) * FullscreenActivity.scaleFactor + 52 * FullscreenActivity.scaleFactor) ,((5*128) * FullscreenActivity.scaleFactor +84* FullscreenActivity.scaleFactor),newPaint);
                    }
                }
                if (GameEngine.theUnit.unitType == "Artillery") {
                    if (GameEngine.theUnit.defence == Artillery.GreenDefence) {
                            canvas.drawText("2", ((16 * 128)* FullscreenActivity.scaleFactor + 52 * FullscreenActivity.scaleFactor), ((5 * 128)  * FullscreenActivity.scaleFactor + 84* FullscreenActivity.scaleFactor), newPaint);
                        }
                }
                if (GameEngine.theUnit.unitType == "Armor") {
                    if (GameEngine.theUnit.defence == Armor.GreenDefence) {
                            canvas.drawText ("5", ((16*128) * FullscreenActivity.scaleFactor + 52 * FullscreenActivity.scaleFactor),((5*128)  * FullscreenActivity.scaleFactor +84* FullscreenActivity.scaleFactor),newPaint);
                        }
                }
                if (GameEngine.theUnit.unitType == "Headquarters") {
                    if (GameEngine.theUnit.defence == Headquaters.GreenDefence) {
                            canvas.drawText ("4", ((16*128) * FullscreenActivity.scaleFactor + 52 * FullscreenActivity.scaleFactor),((5*128)  * FullscreenActivity.scaleFactor +84* FullscreenActivity.scaleFactor),newPaint);
                    }
                }
            }

            if (GameEngine.showMarket) {
                if (GameEngine.playing == GameEngine.green) {
                    movableLocation inf = new movableLocation(theContext, 6);
                    movableLocation cav = new movableLocation(theContext, 7);
                    movableLocation art = new movableLocation(theContext, 8);
                    inf.draw(canvas, 7, 10);
                    cav.draw(canvas, 9, 10);
                    art.draw(canvas, 11, 10);
                }
                if (GameEngine.playing == GameEngine.red) {
                    movableLocation inf = new movableLocation(theContext, 9);
                    movableLocation cav = new movableLocation(theContext, 10);
                    movableLocation art = new movableLocation(theContext, 11);
                    inf.draw(canvas, 7, 10);
                    cav.draw(canvas, 9, 10);
                    art.draw(canvas, 11, 10);
                }
                Paint thePaint = new Paint();
                thePaint.setTextSize(40 * FullscreenActivity.scaleFactor);
                thePaint.setColor(Color.YELLOW);
                canvas.drawText("" + Infantry.foodPrice, 1000 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                canvas.drawText("" + Cavalry.foodPrice, 1255 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                canvas.drawText("" + Artillery.foodPrice, 1515 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                thePaint.setColor(Color.argb(255,204,102,0));
                canvas.drawText("" + Infantry.ironPrice, 950 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                canvas.drawText("" + Cavalry.ironPrice, 1205 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                canvas.drawText("" + Artillery.ironPrice, 1465 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                thePaint.setColor(Color.GRAY);
                canvas.drawText("" + Infantry.oilPrice, 905 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                canvas.drawText("" + Cavalry.oilPrice, 1160 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                canvas.drawText("" + Artillery.oilPrice, 1420 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);


            }

            else if (GameEngine.showFactory) {
                if (GameEngine.playing == GameEngine.green) {
                    movableLocation minf = new movableLocation(theContext, 20);
                    movableLocation arm = new movableLocation(theContext, 12);
                    movableLocation htank = new movableLocation(theContext, 23);
                    minf.draw(canvas, 7, 10);
                    arm.draw(canvas, 9, 10);
                    htank.draw(canvas, 11, 10);
                }
                if (GameEngine.playing == GameEngine.red) {
                    movableLocation minf = new movableLocation(theContext, 21);
                    movableLocation arm = new movableLocation(theContext, 13);
                    movableLocation htank = new movableLocation(theContext, 24);
                    minf.draw(canvas, 7, 10);
                    arm.draw(canvas, 9, 10);
                    htank.draw(canvas, 11, 10);
                }
                Paint thePaint = new Paint();
                thePaint.setTextSize(40 * FullscreenActivity.scaleFactor);
                thePaint.setColor(Color.YELLOW);
                canvas.drawText("" + MechanizedInfantry.foodPrice, 1000 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                canvas.drawText("" + Armor.foodPrice, 1255 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                canvas.drawText("" + HeavyTank.foodPrice, 1515 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                thePaint.setColor(Color.argb(255,204,102,0));
                canvas.drawText("" + MechanizedInfantry.ironPrice, 950 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                canvas.drawText("" + Armor.ironPrice, 1215 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                canvas.drawText("" + HeavyTank.ironPrice, 1465 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                thePaint.setColor(Color.GRAY);
                canvas.drawText("" + MechanizedInfantry.oilPrice, 905 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                canvas.drawText("" + Armor.oilPrice, 1160 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);
                canvas.drawText("" + HeavyTank.oilPrice, 1400 * FullscreenActivity.scaleFactor,1260 * FullscreenActivity.scaleFactor,thePaint);


            }   else {
                Paint thePaint = new Paint();
                thePaint.setTextSize(65 * FullscreenActivity.scaleFactor);
                if (GameEngine.playing == GameEngine.green) {
                    thePaint.setColor(Color.GREEN);
                }
                if (GameEngine.playing == GameEngine.red) {
                    thePaint.setColor(Color.RED);
                }
                canvas.drawText(GameEngine.message, 800 * FullscreenActivity.scaleFactor, 1330 * FullscreenActivity.scaleFactor, thePaint);
            }

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
            paint.setTextSize(60 * FullscreenActivity.scaleFactor);
            if (GameEngine.playing == GameEngine.green) {
                paint.setColor(Color.GREEN);
            }
            if (GameEngine.playing == GameEngine.red) {
                paint.setColor(Color.RED);
            }
            int[] tapCoord = GameEngine.getSquareCoordinates(GameEngine.lastTap[0], GameEngine.lastTap[1]);
            canvas.drawText( Player.print(GameEngine.playing) + " is playing", 2000 * FullscreenActivity.scaleFactor, 70 * FullscreenActivity.scaleFactor, paint);

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

            //draws targets.
            if (GameEngine.theUnit != null && GameEngine.theUnit.hasAttack == true) {
                for (int i = 0; i < GameEngine.BoardSprites.length; i++) { // TODO : optimize this
                    for (int j = 0; j < GameEngine.BoardSprites[i].length; j++) {
                        if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != GameEngine.playing &&
                                (GameEngine.theUnit.attack2Range >= GameEngine.getSquareDistance
                                        (GameEngine.getCoordinates(GameEngine.theUnit)[0], i,
                                                GameEngine.getCoordinates(GameEngine.theUnit)[1], j))) {
                            movableLocation temp = new movableLocation(theContext, 15);
                            temp.draw(canvas, i, j);
                        }
                    }
                }
            }


            //display info about player's selected unit
            if (GameEngine.theUnit != null) {
                GameEngine.theUnit.draw(canvas, 1950, 1140);
                paint = new Paint();
                paint.setTextSize(36 * FullscreenActivity.scaleFactor);
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
                canvas.drawText( "Close attack damage :   " + unitInfo[0] + " Range : "  + unitInfo[1], 1950 * FullscreenActivity.scaleFactor, 1300 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText( "Ranged attack damage : " + unitInfo[2] + "  Range : "  + unitInfo[3], 1950 * FullscreenActivity.scaleFactor, 1340 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText( "Health : " + unitInfo[4] + " / "  + unitInfo[5], 2100 * FullscreenActivity.scaleFactor, 1190 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText( "Unit's defence : " + unitInfo[6], 2100 * FullscreenActivity.scaleFactor, 1240 * FullscreenActivity.scaleFactor, paint);

                if (GameEngine.theUnit.movement != 0) {
                    canvas.drawText("This unit " + canMoveAndAttack[0], 1950 * FullscreenActivity.scaleFactor, 1380 * FullscreenActivity.scaleFactor, paint);
                } else {
                    canvas.drawText("This unit cannot move", 1950 * FullscreenActivity.scaleFactor, 1380 * FullscreenActivity.scaleFactor, paint);
                }
                canvas.drawText( "This unit " + canMoveAndAttack[1], 1950 * FullscreenActivity.scaleFactor, 1420 * FullscreenActivity.scaleFactor, paint);
            }

            //display info about opponent's selected unit
            if (GameEngine.enemyTappedUnit != null) {
                GameEngine.enemyTappedUnit.draw(canvas, 1950, 830);
                paint = new Paint();
                paint.setTextSize(36 * FullscreenActivity.scaleFactor);
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
                canvas.drawText( "Close attack damage :   " + unitInfo[0] + " Range : "  + unitInfo[1], 1950 * FullscreenActivity.scaleFactor, 1010 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText( "Ranged attack damage : " + unitInfo[2] + "  Range : "  + unitInfo[3], 1950 * FullscreenActivity.scaleFactor, 1050 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText( "Health : " + unitInfo[4] + " / "  + unitInfo[5], 2100 * FullscreenActivity.scaleFactor, 900 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText( "Unit's defence : " + unitInfo[6], 2100 * FullscreenActivity.scaleFactor, 940 * FullscreenActivity.scaleFactor, paint);
                if (GameEngine.enemyTappedUnit.movement != 0) {
                    canvas.drawText("This unit " + canMoveAndAttack[0], 1950 * FullscreenActivity.scaleFactor, 1090 * FullscreenActivity.scaleFactor, paint);
                } else {
                    canvas.drawText("This unit cannot move", 1950 * FullscreenActivity.scaleFactor, 1090 * FullscreenActivity.scaleFactor, paint);
                }
                canvas.drawText( "This unit " + canMoveAndAttack[1], 1950 * FullscreenActivity.scaleFactor, 1130 * FullscreenActivity.scaleFactor, paint);
            }
        }

        Paint paint = new Paint();
        paint.setTextSize(60 * FullscreenActivity.scaleFactor);
        paint.setColor(Color.YELLOW);
        canvas.drawText( "Food storage : " + GameEngine.playing.foodStorage + " (+" + GameEngine.lastAddedResounces[0] + ")", 50 * FullscreenActivity.scaleFactor, 1360 * FullscreenActivity.scaleFactor, paint);


        paint = new Paint();
        paint.setTextSize(60 * FullscreenActivity.scaleFactor);
        paint.setColor(Color.argb(255,204,102,0));
        canvas.drawText( "Iron storage : " + GameEngine.playing.ironStorage + " (+" + GameEngine.lastAddedResounces[1] + ")", 50 * FullscreenActivity.scaleFactor, 1280 * FullscreenActivity.scaleFactor, paint);

        paint = new Paint();
        paint.setTextSize(60 * FullscreenActivity.scaleFactor);
        paint.setColor(Color.GRAY);
        canvas.drawText( "Oil storage : " + GameEngine.playing.oilStorage + " (+" + GameEngine.lastAddedResounces[2] + ")", 50 * FullscreenActivity.scaleFactor, 1200 * FullscreenActivity.scaleFactor, paint);

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

package com.example.filip.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.graphics.Canvas;

import com.google.android.gms.nearby.Nearby;


//Most of this code is taken from Java tutorial found online(https://www.youtube.com/watch?v=6prI4ZB_rXI). It's a great tutorial that got me started.

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public static boolean shouldDrawUI = true;
    public static boolean showAir = false;
    public static Context theContext; //context of the View, required for adding textures to units in other classes.
    public static MainThread thread; //Game's thread.
    public static GameEngine grid = null; // Grid of the game
    public static SelectedUnit selected = null; //Player's selected unit
    public static SelectedUnit enemySelected = null; //opponent's selected unit
    public static Units[] units = new Units[0]; // Array of units that will be drawn, they don't have the physical location on board (In GameEngine class, BoardSprites does that).
    public static Resources[] resources = new Resources[0]; // Array of resources that will be drawn, they don't have the physical location on board (In GameEngine class, BoardResources does that).
    public static boolean showendTurnScreen = false;

    public static int cameraX = 0;
    public static int cameraY = 0;
    public static int targetCameraX = 0;
    public static int targetCameraY = 0;


    public static boolean removeFogOfWar = false; //removing fog of war

    //UI elements TODO : cleanup
    public static movableLocation pointers;
    public static movableLocation pointers1;
    public static movableLocation pointers2;
    public static movableLocation pointers7;
    public static movableLocation pointers8;
    public static movableLocation pointers12;
    public static movableLocation airline;
    public static movableLocation airliner;
    public static movableLocation hangar;
    public static movableLocation pointers9;
    public static movableLocation temp;
    public static movableLocation temp2;
    public static movableLocation pointers3;
    public static movableLocation pointers35;
    public static movableLocation pointers4;
    public static movableLocation pointers5;
    public static movableLocation pointers6;

    public static movableLocation pointers99;

    public static movableLocation infr;
    public static movableLocation cavr;
    public static movableLocation artr;
    public static movableLocation mgr;
    public static movableLocation inf;
    public static movableLocation cav;
    public static movableLocation art;
    public static movableLocation mg;
    public static movableLocation arm;
    public static movableLocation fit;
    public static movableLocation bom;
    public static movableLocation armr;
    public static movableLocation fitr;
    public static movableLocation bomr;

    public static movableLocation shield;
    public static movableLocation binoc;
    public static movableLocation shieldDark;
    public static movableLocation binocDark;

    //Starts the game thread
    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    //not used
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    //this is where the board and all starting units, resources are initialized (with their respective textures).
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        grid = null;
        units = new Units[0];
        resources = new Resources[0];
        theContext = this.getContext(); // Stores the context, see the variable comment above
        showAir = false;
        showendTurnScreen = false;

        pointers = new movableLocation(theContext, 0, false);
        pointers1 = new movableLocation(theContext, 1, false);
        pointers2 = new movableLocation(theContext, 2, false);
        pointers7 = new movableLocation(theContext, 17, false);
        pointers8 = new movableLocation(theContext, 18, false);
        pointers12 = new movableLocation(theContext, 25, true);
        airline = new movableLocation(theContext, 27, true);
        airliner = new movableLocation(theContext, 28, true);
        hangar = new movableLocation(theContext, 26, true);
        pointers9 = new movableLocation(theContext, 19, true);
        temp = new movableLocation(theContext);
        temp2 = new movableLocation(theContext, 15, false);
        pointers3 = new movableLocation(theContext, 3, true);
        pointers35 = new movableLocation(theContext, 14, true);
        pointers4 = new movableLocation(theContext, 4, true);
        pointers5 = new movableLocation(theContext, 5, true);
        pointers6 = new movableLocation(theContext, 16, true);

        pointers99 = new movableLocation(theContext, 22, true);

        infr = new movableLocation(theContext, 9, true);
        cavr = new movableLocation(theContext, 10, true);
        artr = new movableLocation(theContext, 11, true);
        mgr = new movableLocation(theContext, 21, true);
        inf = new movableLocation(theContext, 6, true);
        cav = new movableLocation(theContext, 7, true);
        art = new movableLocation(theContext, 8, true);
        mg = new movableLocation(theContext, 20, true);
        arm = new movableLocation(theContext, 12, true);
        fit = new movableLocation(theContext, 29, true);
        bom = new movableLocation(theContext, 31, true);
        armr = new movableLocation(theContext, 13, true);
        fitr = new movableLocation(theContext, 30, true);
        bomr = new movableLocation(theContext, 32, true);

        binoc = new movableLocation(theContext, 33, true);
        shield = new movableLocation(theContext, 34, true);
        binocDark = new movableLocation(theContext, 35, true);
        shieldDark = new movableLocation(theContext, 36, true);


        GameEngine.restart();
        GameEngine.green = new Player("green", true);
        GameEngine.red = new Player("red", true);
        if (MainMenu.scenario.equals("Skirmish vs AI") || MainMenu.scenario.equals("Skirmish vs AI_cheating") ||  MainMenu.scenario.equals("dev_mode")) {
            GameEngine.red.isHuman = false;
            GameEngine.AIPlayer = GameEngine.red;
        }



        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;
        Bitmap map = BitmapFactory.decodeResource(this.getResources(), R.mipmap.grid_land, o);
        map = Bitmap.createScaledBitmap(map, (int) (map.getWidth() * FullscreenActivity.scaleFactor), (int) (map.getHeight() * FullscreenActivity.scaleFactor), true);
        Bitmap mapAir = BitmapFactory.decodeResource(this.getResources(), R.mipmap.airline, o);
        mapAir = Bitmap.createScaledBitmap(mapAir, (int) (mapAir.getWidth() * FullscreenActivity.scaleFactor), (int) (mapAir.getHeight() * FullscreenActivity.scaleFactor), true);
        Bitmap square = BitmapFactory.decodeResource(this.getResources(), R.mipmap.square, o);
        square =  Bitmap.createScaledBitmap(square, (int) (square.getWidth() * FullscreenActivity.scaleFactor), (int) (square.getHeight() * FullscreenActivity.scaleFactor), true);

            //skirmish
            if (MainMenu.scenario.equals("Skirmish") || MainMenu.scenario.equals("Skirmish vs AI") ||  MainMenu.scenario.equals("Skirmish vs AI_cheating")) {
                Map.generateMap(map, mapAir, square);

            }  else if (MainMenu.scenario.equals("dev_mode")){
                AI.addUnit(new Cavalry(theContext, 10, 3, GameEngine.red),"moveTo_6_1");

                //AI.addUnit(new Armor(theContext, 6, 2, GameEngine.red),"moveTo_2_2");
                new Cavalry(theContext, 5, 4, GameEngine.green);
                new Cavalry(theContext, 6, 3, GameEngine.green);
                AI.turn = 18;
                AI.rng = 40;
                GameEngine.red.foodStorage = 0;
                GameEngine.red.ironStorage = 0;
                GameEngine.red.oilStorage = 0;
                GameEngine.message = Integer.toString(AI.turn);
            }

            //Somme mission
            else if (MainMenu.scenario.equals("Somme")) {
                grid = new GameEngine(map,mapAir,square, 15, 9); // these lines create the board.
                new Food(theContext, 1, 1, 1, 2);
                new Food(theContext, 0, 2, 1, 2);
                new Food(theContext, 1, 3, 1, 2);

                new Food(theContext, 1, 5, 1, 6);
                new Food(theContext, 2, 6, 1, 6);
                new Iron(theContext, 0, 6, 1, 6);
                new Iron(theContext, 1, 7, 1, 6);

                new Food(theContext, 13, 1, 13, 2);
                new Food(theContext, 12, 2, 13, 2);
                new Food(theContext, 14, 2, 13, 2);
                new Food(theContext, 13, 3, 13, 2);

                new Food(theContext, 13, 5, 13, 6);
                new Iron(theContext, 14, 6, 13, 6);
                new Iron(theContext, 13, 7, 13, 6);


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
                    new Infantry(theContext, 11, 1 + i * 2, GameEngine.red);
                }

                for (int i = 0; i < 9; i++) {
                    new Cavalry(theContext, 3, i, GameEngine.green);
                }

                for (int i = 0; i < 2; i++) {
                    new Artillery(theContext, 11, 2 + 4 * i, GameEngine.red);
                }
            }
            selected = new SelectedUnit(theContext); // adds selected unit to the board, but doesn't show it until it has to.
            enemySelected = new SelectedUnit(theContext);
            GameEngine.playing = GameEngine.green;
            GameEngine.estimateResources();
            thread.start(); // starts the tread

        if (!GameEngine.replayMode) {
            GameEngine.load(); //load previous game if exists
        }



        if (!GameEngine.red.isHuman) {
            AI.initializeAI();
        }

        if ( MainMenu.scenario.equals("Skirmish vs AI_cheating")) {
            AI.isCheating = true;
        } else {
            AI.isCheating = false;
        }
    }

    //Destroys the (image of) board, also copied from internet.
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        shouldDrawUI = false;
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
        shouldDrawUI = true;
    }

    // updates the grid, not sure if this is necessary.
    public void update() {
            grid.update();
    }

    //"Refreshes" the image, or (in other words) re-draws the whole screen.
    @Override
    public void draw (Canvas canvas) {
        super.draw(canvas);
        if (canvas != null && shouldDrawUI) {

            if (GameEngine.gameIsMultiplayer) {
                if ((GameEngine.isHostPhone && GameEngine.playing.equals(GameEngine.red))
                || ((!GameEngine.isHostPhone) && GameEngine.playing.equals(GameEngine.green))) {
                    if (!GameEngine.message.equals("You are about to leave the battle, tap again to continue")) {
                        GameEngine.message = "Waiting opponent";
                    }
                    Paint paint2 = new Paint();
                    paint2.setTextSize(80 * FullscreenActivity.scaleFactor);
                    if (GameEngine.playing.equals(GameEngine.red)) {
                        paint2.setColor(Color.RED);
                        canvas.drawText(GameEngine.message, 500 * FullscreenActivity.scaleFactor, 800 * FullscreenActivity.scaleFactor, paint2);
                    } else {
                        paint2.setColor(Color.GREEN);
                        canvas.drawText(GameEngine.message, 500 * FullscreenActivity.scaleFactor, 800 * FullscreenActivity.scaleFactor, paint2);
                    }
                    return;
                }
            }

            if (showendTurnScreen) {
                Paint paint2 = new Paint();
                paint2.setTextSize(80 * FullscreenActivity.scaleFactor);
                if (GameEngine.playing.equals(GameEngine.red)) {
                    paint2.setColor(Color.RED);
                    canvas.drawText(GameEngine.message, 500 * FullscreenActivity.scaleFactor, 800 * FullscreenActivity.scaleFactor, paint2);
                } else {
                    paint2.setColor(Color.GREEN);
                    canvas.drawText(GameEngine.message, 500 * FullscreenActivity.scaleFactor, 800 * FullscreenActivity.scaleFactor, paint2);
                }
                return;
            }


            if (showAir) {
                drawAir(canvas);
                return;
            }

            grid.draw(canvas, showAir);  //draws the grid first, because that is the bottom layer.
            if (MainMenu.scenario.equals("Skirmish") || MainMenu.scenario.equals("Skirmish vs AI") || MainMenu.scenario.equals("dev_mode") || MainMenu.scenario.equals("Skirmish vs AI_cheating")) {
                //draws markers

                Map.drawMapFeatures(canvas);
                pointers7.draw(canvas, GameEngine.greenDeployX, GameEngine.greenDeployY);
                pointers8.draw(canvas, GameEngine.redDeployX, GameEngine.redDeployY);
            }

            Paint newPaint = new Paint();
            newPaint.setTextSize(65 * FullscreenActivity.scaleFactor);
            newPaint.setColor(Color.argb(255, 154, 52, 0));

            if (GameEngine.theUnit != null) {
                //upgrade
                pointers9.draw(canvas, 16, 5);
                if (GameEngine.theUnit.unitType == "Infantry") {
                    if (GameEngine.theUnit.defence == Infantry.GreenDefence) {
                        canvas.drawText("1", ((16 * 128) * FullscreenActivity.scaleFactor + 52 * FullscreenActivity.scaleFactor), ((5 * 128) * FullscreenActivity.scaleFactor + 84 * FullscreenActivity.scaleFactor), newPaint);
                    }
                }
                else if (GameEngine.theUnit.unitType == "Cavalry") {
                    if (GameEngine.theUnit.defence == Cavalry.GreenDefence) {
                        canvas.drawText("1", ((16 * 128) * FullscreenActivity.scaleFactor + 52 * FullscreenActivity.scaleFactor), ((5 * 128) * FullscreenActivity.scaleFactor + 84 * FullscreenActivity.scaleFactor), newPaint);
                    }
                }
                else if (GameEngine.theUnit.unitType == "Artillery") {
                    if (GameEngine.theUnit.defence == Artillery.GreenDefence) {
                        canvas.drawText("2", ((16 * 128) * FullscreenActivity.scaleFactor + 52 * FullscreenActivity.scaleFactor), ((5 * 128) * FullscreenActivity.scaleFactor + 84 * FullscreenActivity.scaleFactor), newPaint);
                    }
                }
                else if (GameEngine.theUnit.unitType == "Armor") {
                    if (GameEngine.theUnit.defence == Armor.GreenDefence) {
                        canvas.drawText("5", ((16 * 128) * FullscreenActivity.scaleFactor + 52 * FullscreenActivity.scaleFactor), ((5 * 128) * FullscreenActivity.scaleFactor + 84 * FullscreenActivity.scaleFactor), newPaint);
                    }
                }
                else if (GameEngine.theUnit.unitType == "Headquarters") {
                    if (GameEngine.theUnit.defence == Headquaters.GreenDefence) {
                        canvas.drawText("4", ((16 * 128) * FullscreenActivity.scaleFactor + 52 * FullscreenActivity.scaleFactor), ((5 * 128) * FullscreenActivity.scaleFactor + 84 * FullscreenActivity.scaleFactor), newPaint);
                    }
                }
            }

            for (int i = 0; i < resources.length; i++) {
                int x = resources[i].coordinates[0];
                int y = resources[i].coordinates[1];
                resources[i].draw(canvas); //draws the units from units array found in GameView class.
            }

            for (int i = 0; i < units.length; i++) {
                int x = units[i].coordinates[0];
                int y = units[i].coordinates[1];
                if (x == 125 || y == 125) {
                    continue;
                }
                units[i].draw(canvas); //draws the units from units array found in GameView class.
            }

            if (GameEngine.selected != null) { //If no units are selected yet, do nothing
                GameEngine.selected.draw(canvas); //If they are, draw it.
            }

            if (GameEngine.enemySelected != null) { //If no units are selected yet, do nothing
                GameEngine.enemySelected.draw(canvas); //If they are, draw it.
            }

            //draws yellow squares where selected unit can move.
            if (GameEngine.theUnit != null && GameEngine.theUnit.hasMove == true) {
                int[] coordinates = GameEngine.getCoordinates(GameEngine.theUnit);
                boolean[][] reachableTiles = GameEngine.getReachableTiles(coordinates[0], coordinates[1], GameEngine.theUnit.movement);
                for (int i = 0; i < GameEngine.BoardSprites.length; i++) { // TODO : optimize this
                    for (int j = 0; j < GameEngine.BoardSprites[i].length; j++) {
                        if (reachableTiles[i][j]) {
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

                            temp2.draw(canvas, i, j);
                        }
                    }
                }
            }

            //draws fog of war
            boolean[][] fog_of_war = new boolean[0][0];

            if (!removeFogOfWar) {

                Paint paint = new Paint();

                if (GameEngine.playing.equals(GameEngine.green)) {
                    fog_of_war = GameEngine.getFogOfWar(GameEngine.green);
                } else {
                    fog_of_war = GameEngine.getFogOfWar(GameEngine.red);
                }
                for (int i = 0; i < fog_of_war.length; i++) {
                    for (int j = 0; j < fog_of_war[i].length; j++) {
                        if (!fog_of_war[i][j]) {
                            paint.setColor(Color.argb(255, 80, 80, 80));
                            Rect rectangle = new Rect(i * GameEngine.squareLength + cameraX,
                                    j * GameEngine.squareLength + cameraY,
                                    (i + 1) * GameEngine.squareLength + cameraX,
                                    (j + 1) * GameEngine.squareLength + cameraY);
                            canvas.drawRect(rectangle, paint);
                        }
                    }
                }
            }


            newPaint = new Paint();
            newPaint.setColor(Color.argb(255, 10, 10, 10));
            Rect rectangle = new Rect(0,
                    (int)(1152 * FullscreenActivity.scaleFactor),
                    (int)(3000 * FullscreenActivity.scaleFactor),
                    (int)(1500* FullscreenActivity.scaleFactor));
            canvas.drawRect(rectangle,newPaint);
            rectangle = new Rect ((int)(1920 * FullscreenActivity.scaleFactor),
                    (int)(0 * FullscreenActivity.scaleFactor),
                    (int)(3000 * FullscreenActivity.scaleFactor),
                    (int)(2000 * FullscreenActivity.scaleFactor));
            canvas.drawRect(rectangle,newPaint);

            //draws HUD elements
            pointers3.draw(canvas, 16, 1);
            pointers35.draw(canvas, 18, 1);
            pointers4.draw(canvas, 16, 3);
            pointers5.draw(canvas, 5, 10);
            pointers6.draw(canvas, 18, 3);
            pointers99.draw(canvas, 5, 9);
            pointers12.draw(canvas, 18, 5);

            if (GameEngine.showMarket) {
                if (GameEngine.playing == GameEngine.green) {

                    cav.draw(canvas, 7, 10);
                    inf.draw(canvas, 9, 10);
                    mg.draw(canvas, 11, 10);
                    art.draw(canvas, 13, 10);

                }
                if (GameEngine.playing == GameEngine.red) {

                    cavr.draw(canvas, 7, 10);
                    infr.draw(canvas, 9, 10);
                    mgr.draw(canvas, 11, 10);
                    artr.draw(canvas, 13, 10);
                }
                Paint thePaint = new Paint();
                thePaint.setTextSize(40 * FullscreenActivity.scaleFactor);
                if (GameEngine.playing == GameEngine.green) {
                    thePaint.setColor(Color.YELLOW);
                    canvas.drawText("" + Cavalry.greenFoodPrice, 1000 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Infantry.greenFoodPrice, 1255 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + MGInfantry.foodPrice, 1515 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Artillery.greenFoodPrice, 1770 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    thePaint.setColor(Color.argb(255, 204, 102, 0));
                    canvas.drawText("" + Cavalry.greenIronPrice, 950 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Infantry.greenIronPrice, 1205 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + MGInfantry.ironPrice, 1465 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Artillery.greenIronPrice, 1720 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    thePaint.setColor(Color.GRAY);
                    canvas.drawText("" + Cavalry.greenOilPrice, 905 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Infantry.greenOilPrice, 1160 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + MGInfantry.oilPrice, 1420 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Artillery.greenOilPrice, 1675 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                } else if (GameEngine.playing == GameEngine.red) {
                    thePaint.setColor(Color.YELLOW);
                    canvas.drawText("" + Cavalry.redFoodPrice, 1000 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Infantry.redFoodPrice, 1255 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + MGInfantry.foodPrice, 1515 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Artillery.redFoodPrice, 1770 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    thePaint.setColor(Color.argb(255, 204, 102, 0));
                    canvas.drawText("" + Cavalry.redIronPrice, 950 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Infantry.redIronPrice, 1205 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + MGInfantry.ironPrice, 1465 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Artillery.redIronPrice, 1720 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    thePaint.setColor(Color.GRAY);
                    canvas.drawText("" + Cavalry.redOilPrice, 905 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Infantry.redOilPrice, 1160 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + MGInfantry.oilPrice, 1420 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Artillery.redOilPrice, 1675 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                }

            } else if (GameEngine.showFactory) {
                if (GameEngine.playing == GameEngine.green) {

                    arm.draw(canvas, 7, 10);
                    fit.draw(canvas, 9, 10);
                    bom.draw(canvas, 11, 10);

                    Paint thePaint = new Paint();
                    thePaint.setTextSize(40 * FullscreenActivity.scaleFactor);
                    thePaint.setColor(Color.YELLOW);

                    canvas.drawText("" + Armor.greenFoodPrice, 1000 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + ReconPlane.foodPrice, 1255 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Bomber.foodPrice, 1515 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);

                    thePaint.setColor(Color.argb(255, 204, 102, 0));
                    canvas.drawText("" + Armor.greenIronPrice, 960 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + ReconPlane.ironPrice, 1205 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Bomber.ironPrice, 1460 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);

                    thePaint.setColor(Color.GRAY);
                    canvas.drawText("" + Armor.greenOilPrice, 900 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + ReconPlane.oilPrice, 1155 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Bomber.oilPrice, 1405 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                }
                if (GameEngine.playing == GameEngine.red) {

                    Paint thePaint = new Paint();
                    thePaint.setTextSize(40 * FullscreenActivity.scaleFactor);
                    thePaint.setColor(Color.YELLOW);

                    canvas.drawText("" + Armor.redFoodPrice, 1000 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + ReconPlane.foodPrice, 1255 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Bomber.foodPrice, 1515 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);

                    thePaint.setColor(Color.argb(255, 204, 102, 0));
                    canvas.drawText("" + Armor.redIronPrice, 960 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + ReconPlane.ironPrice, 1205 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Bomber.ironPrice, 1460 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);

                    thePaint.setColor(Color.GRAY);
                    canvas.drawText("" + Armor.redOilPrice, 900 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + ReconPlane.oilPrice, 1155 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);
                    canvas.drawText("" + Bomber.oilPrice, 1405 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, thePaint);

                    armr.draw(canvas, 7, 10);
                    fitr.draw(canvas, 9, 10);
                    bomr.draw(canvas, 11, 10);
                }

            } else {
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



            //bottom five lines draw the text that displays the tap coordinates, should be removed in final version.
            Paint paint = new Paint();
            paint.setTextSize(60 * FullscreenActivity.scaleFactor);
            if (GameEngine.playing == GameEngine.green) {
                paint.setColor(Color.GREEN);
            }
            if (GameEngine.playing == GameEngine.red) {
                paint.setColor(Color.RED);
            }
            canvas.drawText(Player.print(GameEngine.playing) + " is playing", 2000 * FullscreenActivity.scaleFactor, 70 * FullscreenActivity.scaleFactor, paint);

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
                } else {
                    canMoveAndAttack[0] = "cannot move this turn";
                }
                if (GameEngine.theUnit.hasAttack) {
                    canMoveAndAttack[1] = "can attack this turn";
                } else {
                    canMoveAndAttack[1] = "cannot attack this turn";
                }
                canvas.drawText("Close attack damage :   " + unitInfo[0] + " Range : " + unitInfo[1], 1950 * FullscreenActivity.scaleFactor, 1300 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Ranged attack damage : " + unitInfo[2] + "  Range : " + unitInfo[3], 1950 * FullscreenActivity.scaleFactor, 1340 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Health : " + unitInfo[4] + " / " + unitInfo[5], 2100 * FullscreenActivity.scaleFactor, 1175 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Unit's defence : " + unitInfo[6], 2100 * FullscreenActivity.scaleFactor, 1220 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Air attack : " + GameEngine.theUnit.airAttack, 2100 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, paint);

                if (GameEngine.theUnit.movement != 0) {
                    canvas.drawText("This unit " + canMoveAndAttack[0], 1950 * FullscreenActivity.scaleFactor, 1380 * FullscreenActivity.scaleFactor, paint);
                } else {
                    canvas.drawText("This unit cannot move", 1950 * FullscreenActivity.scaleFactor, 1380 * FullscreenActivity.scaleFactor, paint);
                }
                canvas.drawText("This unit " + canMoveAndAttack[1], 1950 * FullscreenActivity.scaleFactor, 1420 * FullscreenActivity.scaleFactor, paint);
            }

            //display info about opponent's selected unit
            if (GameEngine.enemyTappedUnit != null) {
                GameEngine.enemyTappedUnit.draw(canvas, 1950, 850);
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
                        GameEngine.enemyTappedUnit.defence,
                };
                String[] canMoveAndAttack = new String[2];
                if (GameEngine.enemyTappedUnit.hasMove) {
                    canMoveAndAttack[0] = "can move this turn";
                } else {
                    canMoveAndAttack[0] = "cannot move this turn";
                }
                if (GameEngine.enemyTappedUnit.hasAttack) {
                    canMoveAndAttack[1] = "can attack this turn";
                } else {
                    canMoveAndAttack[1] = "cannot attack this turn";
                }
                canvas.drawText("Close attack damage :   " + unitInfo[0] + " Range : " + unitInfo[1], 1950 * FullscreenActivity.scaleFactor, 1010 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Ranged attack damage : " + unitInfo[2] + "  Range : " + unitInfo[3], 1950 * FullscreenActivity.scaleFactor, 1050 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Health : " + unitInfo[4] + " / " + unitInfo[5], 2100 * FullscreenActivity.scaleFactor, 890 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Unit's defence : " + unitInfo[6], 2100 * FullscreenActivity.scaleFactor, 930 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Air attack : " + GameEngine.enemyTappedUnit.airAttack, 2100 * FullscreenActivity.scaleFactor, 970 * FullscreenActivity.scaleFactor, paint);
                if (GameEngine.enemyTappedUnit.movement != 0) {
                    canvas.drawText("This unit " + canMoveAndAttack[0], 1950 * FullscreenActivity.scaleFactor, 1090 * FullscreenActivity.scaleFactor, paint);
                } else {
                    canvas.drawText("This unit cannot move", 1950 * FullscreenActivity.scaleFactor, 1090 * FullscreenActivity.scaleFactor, paint);
                }
                canvas.drawText("This unit " + canMoveAndAttack[1], 1950 * FullscreenActivity.scaleFactor, 1130 * FullscreenActivity.scaleFactor, paint);
            }


            Paint paint2 = new Paint();
            paint2.setTextSize(60 * FullscreenActivity.scaleFactor);
            paint2.setColor(Color.YELLOW);
            canvas.drawText("Food storage : " + GameEngine.playing.foodStorage + " (+" + GameEngine.lastAddedResources[0] + ")", 50 * FullscreenActivity.scaleFactor, 1360 * FullscreenActivity.scaleFactor, paint2);


            paint2 = new Paint();
            paint2.setTextSize(60 * FullscreenActivity.scaleFactor);
            paint2.setColor(Color.argb(255, 204, 102, 0));
            canvas.drawText("Iron storage : " + GameEngine.playing.ironStorage + " (+" + GameEngine.lastAddedResources[1] + ")", 50 * FullscreenActivity.scaleFactor, 1280 * FullscreenActivity.scaleFactor, paint2);

            paint2 = new Paint();
            paint2.setTextSize(60 * FullscreenActivity.scaleFactor);
            paint2.setColor(Color.GRAY);
            canvas.drawText("Oil storage : " + GameEngine.playing.oilStorage + " (+" + GameEngine.lastAddedResources[2] + ")", 50 * FullscreenActivity.scaleFactor, 1200 * FullscreenActivity.scaleFactor, paint2);

            if (GameEngine.loadoutMenu) {
                newPaint.setColor(Color.argb(255, 10, 10, 10));
                rectangle = new Rect((int)(895 * FullscreenActivity.scaleFactor),
                        (int)(500 * FullscreenActivity.scaleFactor),
                        (int)(1850 * FullscreenActivity.scaleFactor),
                        (int)(1200 * FullscreenActivity.scaleFactor));
                canvas.drawRect(rectangle,newPaint);

                //buy and cancel
                pointers5.draw(canvas, 8,8);
                pointers3.draw(canvas, 10,8);


                //display info about unit
                Units temp = null;
                if (GameEngine.loadoutMenuUnit == "Cavalry") {
                    temp = new Cavalry(theContext, 125, 125, GameEngine.playing);
                }
                if (GameEngine.loadoutMenuUnit == "Infantry") {
                    temp = new Infantry(theContext, 125, 125, GameEngine.playing);
                }
                if (GameEngine.loadoutMenuUnit == "Artillery") {
                    temp = new Artillery(theContext, 125, 125, GameEngine.playing);
                }
                if (GameEngine.loadoutMenuUnit == "Armor") {
                    temp = new Armor(theContext, 125, 125, GameEngine.playing);
                }

                temp.draw(canvas, 900.0, 505.0);
                paint = new Paint();
                paint.setTextSize(36 * FullscreenActivity.scaleFactor);
                if (temp.owner == GameEngine.red) {
                    paint.setColor(Color.RED);
                }
                if (temp.owner == GameEngine.green) {
                    paint.setColor(Color.GREEN);
                }
                int[] unitInfo = {temp.attack1,
                        temp.attack1Range,
                        temp.attack2,
                        temp.attack2Range,
                        temp.HP,
                        temp.maxHP,
                        temp.defence,
                };
                canvas.drawText("Health : " + unitInfo[4], 1030 * FullscreenActivity.scaleFactor, 540 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Unit's defence : " + unitInfo[6], 1030 * FullscreenActivity.scaleFactor, 580 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Air attack : " + temp.airAttack, 1030 * FullscreenActivity.scaleFactor, 620 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Close attack damage :     " + unitInfo[0], 900 * FullscreenActivity.scaleFactor, 670 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Range : " + unitInfo[1], 900 * FullscreenActivity.scaleFactor, 710 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Ranged attack damage :  " + unitInfo[2], 900 * FullscreenActivity.scaleFactor, 760 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Range : " + unitInfo[3], 900 * FullscreenActivity.scaleFactor, 800 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Movement : " + temp.movement, 900 * FullscreenActivity.scaleFactor, 850 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Scouting range : " + temp.visibilityRange, 900 * FullscreenActivity.scaleFactor, 890 * FullscreenActivity.scaleFactor, paint);
                canvas.drawText("Heal amount : " +temp.healRate, 900 * FullscreenActivity.scaleFactor, 930 * FullscreenActivity.scaleFactor, paint);

                if (GameEngine.loadoutMenuUnit == "Cavalry") {
                    if (GameEngine.playing.upgrades[0][0]) {
                        binoc.draw(canvas, 12 ,4);
                    } else {
                        binocDark.draw(canvas,12,4);
                    }

                        if (GameEngine.playing.upgrades[0][1]) {
                            shield.draw(canvas, 12 ,6);
                        } else {
                            shieldDark.draw(canvas,12,6);
                        }

                        if (GameEngine.playing.upgrades[0][2]) {
                            shield.draw(canvas, 12 ,8);
                        } else {
                            shieldDark.draw(canvas,12,8);
                        }
                }

                    if (GameEngine.loadoutMenuUnit == "Infantry") {
                        if (GameEngine.playing.upgrades[1][0]) {
                            binoc.draw(canvas, 12 ,4);
                        } else {
                            binocDark.draw(canvas,12,4);
                        }

                        if (GameEngine.playing.upgrades[1][1]) {
                            shield.draw(canvas, 12 ,6);
                        } else {
                            shieldDark.draw(canvas,12,6);
                        }

                        if (GameEngine.playing.upgrades[1][2]) {
                            shield.draw(canvas, 12 ,8);
                        } else {
                            shieldDark.draw(canvas,12,8);
                        }
                    }

                    if (GameEngine.loadoutMenuUnit == "Artillery") {
                        if (GameEngine.playing.upgrades[2][0]) {
                            binoc.draw(canvas, 12 ,4);
                        } else {
                            binocDark.draw(canvas,12,4);
                        }

                        if (GameEngine.playing.upgrades[2][1]) {
                            shield.draw(canvas, 12 ,6);
                        } else {
                            shieldDark.draw(canvas,12,6);
                        }

                        if (GameEngine.playing.upgrades[2][2]) {
                            shield.draw(canvas, 12 ,8);
                        } else {
                            shieldDark.draw(canvas,12,8);
                        }
                    }

                    if (GameEngine.loadoutMenuUnit == "Armor") {
                        if (GameEngine.playing.upgrades[3][0]) {
                            binoc.draw(canvas, 12, 4);
                        } else {
                            binocDark.draw(canvas, 12, 4);
                        }

                        if (GameEngine.playing.upgrades[3][1]) {
                            shield.draw(canvas, 12, 6);
                        } else {
                            shieldDark.draw(canvas, 12, 6);
                        }

                        if (GameEngine.playing.upgrades[3][2]) {
                            shield.draw(canvas, 12, 8);
                        } else {
                            shieldDark.draw(canvas, 12, 8);
                        }
                    }
            }
        }
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

    public static void drawAir(Canvas canvas) {

        grid.draw(canvas, showAir);
        pointers12.draw(canvas, 18, 10);

        Paint paint = new Paint();
        paint.setAlpha(100);
        for (int i = 0; i < resources.length; i++) {
            resources[i].draw(canvas, paint,
                    GameEngine.squareLength * (resources[i].coordinates[0] * GameEngine.airLineXScaleFactor + 2.5f),
                    GameEngine.squareLength * resources[i].coordinates[1] * GameEngine.airLineYScaleFactor,
                    true); //draws the units from units array found in GameView class.
        }

        for (int i = 0; i < units.length; i++) {
            if (units[i].owner.equals(GameEngine.playing)) {
                units[i].draw(canvas, paint,
                        units[i].coordinates[0],
                        units[i].coordinates[1],
                        true); //draws the units from units array found in GameView class.
            }
        }

        //draws fog of war
        boolean[][] fog_of_war;
        if (GameEngine.playing.equals(GameEngine.green)) {
            fog_of_war = GameEngine.getFogOfWar(GameEngine.green);
        } else {
            fog_of_war = GameEngine.getFogOfWar(GameEngine.red);
        }
        for (int i = 0; i < fog_of_war.length; i++) {
            for (int j = 0; j < fog_of_war[i].length; j++) {
                if (!fog_of_war[i][j]) {
                    paint.setColor(Color.argb(100, 80, 80, 80));
                    Rect rectangle = new Rect((int) ((i * GameEngine.airLineXScaleFactor + 2.5) * GameEngine.squareLength),
                            (int) ((j * GameEngine.airLineYScaleFactor) * GameEngine.squareLength),
                            (int) ((i * GameEngine.airLineXScaleFactor + 2.5 + 1 * GameEngine.airLineXScaleFactor) * GameEngine.squareLength),
                            (int) ((j * GameEngine.airLineYScaleFactor + 1 * GameEngine.airLineYScaleFactor) * GameEngine.squareLength));
                    canvas.drawRect(rectangle, paint);
                } else if (fog_of_war[i][j] && GameEngine.BoardSprites[i][j] != null && !GameEngine.BoardSprites[i][j].owner.equals(GameEngine.playing)) {
                    paint.setAlpha(100);
                    GameEngine.BoardSprites[i][j].draw(canvas, paint,
                            GameEngine.BoardSprites[i][j].coordinates[0],
                            GameEngine.BoardSprites[i][j].coordinates[1],
                            true); //draws the units from units array found in GameView class.
                }
            }
        }
        //draw air line position for planes to place
        for (int i = 0; i < GameEngine.airLinesCount; i++) {
            airline.draw(canvas, 0, (int) (3 * i * GameEngine.airLineYScaleFactor * GameEngine.squareLength + GameEngine.airLineYScaleFactor * GameEngine.squareLength),null, true);
            airliner.draw(canvas, (int) (17.8f * GameEngine.squareLength), (int) (3 * i * GameEngine.airLineYScaleFactor * GameEngine.squareLength + GameEngine.airLineYScaleFactor * GameEngine.squareLength), null,true);
        }


        //draw hangar
        hangar.draw(canvas, 4, 10);
        if (GameEngine.playing != null) {
            for (int i = 0; i < GameEngine.playing.hangar.length; i++) {
                if (GameEngine.playing.hangar[i] != null) {
                    GameEngine.playing.hangar[i].draw(canvas, 4 + i, 10);
                }
            }
        }

        //draw planes on the lines
        for (int i = 0; i < GameEngine.planeLines.length; i++) {
            for (int j = 0; j < GameEngine.planeLines[i].length; j++) {
                if (GameEngine.planeLines[i][j] != null) {
                    GameEngine.planeLines[i][j].draw(canvas, 19*j, (3 * i * GameEngine.airLineYScaleFactor + GameEngine.airLineYScaleFactor));
                }
            }
        }

        //draw oil left
        Paint paint2 = new Paint();
        paint2.setTextSize(60 * FullscreenActivity.scaleFactor);
        paint2.setColor(Color.GRAY);
        canvas.drawText("Oil : " + GameEngine.playing.oilStorage + " (+" + GameEngine.lastAddedResources[2] + ")", 50 * FullscreenActivity.scaleFactor, 1360 * FullscreenActivity.scaleFactor, paint2);

        //draw info of selected plane

        if (GameEngine.selectedPlane != null) {
            paint = new Paint();
            paint.setTextSize(36 * FullscreenActivity.scaleFactor);
            if (GameEngine.playing == GameEngine.green) {
                paint.setColor(Color.GREEN);
            } else if (GameEngine.playing == GameEngine.red) {
                paint.setColor(Color.RED);
            }

            canvas.drawText("Air attack :   " + GameEngine.selectedPlane.airAttack + " Air defence : " + GameEngine.selectedPlane.defence, 1350 * FullscreenActivity.scaleFactor, 1260 * FullscreenActivity.scaleFactor, paint);
            canvas.drawText("Ground attack : " + GameEngine.selectedPlane.groundAttack, 1350 * FullscreenActivity.scaleFactor, 1310 * FullscreenActivity.scaleFactor, paint);
            canvas.drawText("Health : " + GameEngine.selectedPlane.HP + "/" + GameEngine.selectedPlane.maxHP + " Repair : " + GameEngine.selectedPlane.healingRate, 1350 * FullscreenActivity.scaleFactor, 1360 * FullscreenActivity.scaleFactor, paint);

        }
    }
}

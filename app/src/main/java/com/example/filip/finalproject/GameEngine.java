package com.example.filip.finalproject;


import android.graphics.Bitmap;
import android.graphics.Canvas;

// Class that will run the game and manage all the events.
public class GameEngine extends Thread{

    public enum PREV_MOVE {
        NONE,
        MOVE,
        ATTACK,
        BLIND_FIRE
    }

    public static boolean undoIsAllowed = true;
    public static boolean  returnFireEnabled = true;
    public static boolean noReturnFireIfNotRevealed = true;

    public static final double SMOKE_WIDTH = 0.71;
    public static final int SMOKE_DURATION = 4;
    public static final int SMOKE_PRICE_OIL = 1;
    public static final int RESEARCH_PER_TURN = 1;

    public static int width = 15;
    public static int height = 9;

    public static int greenDeployX = 2;
    public static int greenDeployY = 2;
    public static int redDeployX = 12;
    public static int redDeployY = 6;

    public static int turnCount = 0;
    public static int squareLength = (int) (128  * FullscreenActivity.scaleFactor); //scales square length, dependent on scale factor
    public Bitmap image; // Image of the grid
    public Bitmap emptySquare;
    public static Units lastUnit; //stores the last unit which made some action
    public static PREV_MOVE prevoiusMove = PREV_MOVE.NONE;
    public static Units lastEnemyUnit; //stores the last enemy unit which made some action
    public static Units theUnit = null; // Selected unit, unlike the selected unit in GameView class this unit is the actual selected unit.
    public static SelectedUnit selected = null; // Selected unit, reference is not the same as the (selected) Unit itself.
    public static Units enemyTappedUnit = null; //same as above, but of opponent
    public static SelectedUnit enemySelected = null; // same as above, but for opponent
    public static Units[][] boardUnits = new Units[15][9]; //A 2D array of Units that stores the units for game engine and data processing, unlike GameView's Units[] this isn't involved in drawing units.
    public static Player green; //stores the reference to a player that is in charge of Green units
    public static Player red;//stores the reference to a player that is in charge of Red units
    public static Player playing = null; //player that makes moves
    public static Resources[][] BoardResources = new Resources[15][9]; //a 2D array of Units that stores the resources in the game
    public static boolean showMarket = false; //shows market units which can be purchased
    public static boolean loadoutMenu = false;
    public static String loadoutMenuUnit = "";
    public static String message = ""; //stores message to user
    public static int c = 0; //offset for x and y coordinate message, if set to 0 top left will be 0,0 if set to 1 top left will be 1,1
    public static int[] lastCoordinates = new int[4]; //coordinates of last action
    public static Units[] queue = new Units[0]; // stores all units that will be deployed
    public static Player AIPlayer = null;
    public static boolean[] fogOfWarIsRevealedForGreen = new boolean[3];
    public static boolean[] fogOfWarIsRevealedForRed = new boolean[3];
    public static int[][] smokeMap = new int[15][9]; //represents if smoke exists on a tile, number represents how long should smoke remain
    public static boolean smokeFireActive = false;

    public static int[] lastAddedResources = new int[3]; //memorizes last added resources, to display next to storage
    public static boolean gameIsMultiplayer = false;
    public static boolean isHostPhone = false;
    public static boolean replayMode = false;
    public static int replayActionDelay = 500;

    //restarts board
    public static void restart(){
        fogOfWarIsRevealedForGreen = new boolean[fogOfWarIsRevealedForGreen.length];
        fogOfWarIsRevealedForRed = new boolean[fogOfWarIsRevealedForRed.length];
        prevoiusMove = PREV_MOVE.NONE;
        lastUnit = null; //stores the last unit which made some action
        lastEnemyUnit = null; //stores the last unit which made some action
        theUnit = null; // Selected unit, unlike the selected unit in GameView class this unit is the actual selected unit.
        selected = null; // Selected unit, reference is not the same as the (selected) Unit itself.
        enemyTappedUnit = null; //same as above, but of opponent
        enemySelected = null; // same as above, but for opponent
        boardUnits = new Units[15][9]; //A 2D array of Units that stores the units for game engine and data processing, unlike GameView's Units[] this isn't involved in drawing units.
        playing = null; //player that makes moves
        BoardResources = new Resources[15][9]; //a 2D array of resources that stores the resources in the game
        showMarket = false; //shows market units which can be purchased
        message = ""; //stores message to user
        green = new Player("green", true);
        red = new Player("red", true);
        smokeMap = new int[width][height];
        smokeFireActive = false;

        if (GameEngine.replayMode) {
            GameEngine.message = "Tap anywhere to start replay";
        }

        lastCoordinates = new int[4]; //coordinates of last action
        queue = new Units[0]; // stores all units that will be deployed
        c = 0;
        AI.unitOrders = new String[0];
        AI.units = new Units[0];
        AI.turn = 0;
        AI.aggresionLevel = 3;
        turnCount = 0;
        loadoutMenu = false;
        loadoutMenuUnit = "";
        lastAddedResources = new int[3];
        Infantry.restoreDefaultValues();
        Cavalry.restoreDefaultValues();
        Artillery.restoreDefaultValues();
        Armor.restoreDefaultValues();

        ReplayMenu.replayMapMode = Map.map_code; //store map for replay
        //ReplayMenu.replayTechEnabled = TechTree.techIsEnabled;

    }

    public static void load() {
        if (FullscreenActivity.memory != null && FullscreenActivity.memory.size() != 0) {
            if (!replayMode) {
                GameView.shouldDrawUI = false;
            }

            for (int i = 0; i < FullscreenActivity.memory.size(); i++) {
                int coord = FullscreenActivity.memory.get(i);
                int mode = 1;
                if (coord < 0) {
                    mode = 2;
                    coord *= -1;
                }
                int y = coord % FullscreenActivity.widthfullscreen;
                int x = coord  / FullscreenActivity.widthfullscreen;
                GameView.cameraX = 0;
                GameView.cameraY = 0;
                tapProcessor(x,y,mode);

                if (replayMode) {

                    try {
                        //FullscreenActivity.currentView.draw(MainThread.canvas);
                        Thread.sleep(replayActionDelay);
                        GameView.thread.renderFrame();
                    } catch (Exception e){

                    }
                }
            }

            if (replayMode) {
                GameView.shouldDrawUI = true;
            }

            GameView.shouldDrawUI = true;
            GameView.showendTurnScreen = true;
            if (playing.equals(green)) {
                message = "Green player's turn. Press anywhere to continue.";
            } else {
                message = "Red player's turn. Press anywhere to continue.";
            }
        }
    }

    //Constructor that creates the unit and it's image, doesn't set it's coordinates. Mostly used by onDraw function in GameView
    public GameEngine(Bitmap grid, Bitmap square,  int input_width, int input_heigth) {
        image = grid;
        emptySquare = square;
        this.width = input_width;
        this.height = input_heigth;
        boardUnits = new Units[width][height];
        BoardResources = new Resources[width][height];
        smokeMap = new int[width][height];
        smokeFireActive = false;
    }

    // updates board
    public void update() {
        return;
    }

    //draws the board when grid.draw(canvas) is called in GameView function.
    public void draw(Canvas canvas) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                canvas.drawBitmap(emptySquare, i * squareLength +  GameView.cameraX, j * squareLength +  GameView.cameraY, null);
            }
        }
            //canvas.drawBitmap(image, GameView.cameraX, GameView.cameraY, null);
    }
    /*
    A method that will process what happens with user's input (click/tap).
     */
    public static void tapProcessor (int x, int y, int mode) {

        if (mode == 0) {
            if (x < 0 || y < 0) {
                return;
            }
            //This makes sure that unit gets tapped only once.
            //if (selected != null && x/squareLength  == selected.coordinates[0] && y / squareLength  == selected.coordinates[1]) {
            //    return;
            //}
            if (GameView.showendTurnScreen) {
                GameView.showendTurnScreen = false;

                message = "";

                if (gameIsMultiplayer) {
                    MultiplayerConnection.sendGameData(0,5,5);
                }
                FullscreenActivity.memory.add(new Integer((-1000)));
                return;

            }
            // else if (GameView.activeScreen == GameView.Screen.TECH_SCREEN) {
            //    processTechTap(x,y);
            //    FullscreenActivity.memory.add(new Integer((x - GameView.cameraX) * FullscreenActivity.widthfullscreen + (y - GameView.cameraY)));
            //
            //   if (gameIsMultiplayer) {
            //        MultiplayerConnection.sendGameData(1,x- GameView.cameraX,y - GameView.cameraY);
            //    }

            else {
                 if (loadoutMenu) {
                    ProcessLoadoutTap(x,y);
                    FullscreenActivity.memory.add(new Integer((x * FullscreenActivity.widthfullscreen + y) * -1));

                    if (gameIsMultiplayer) {
                        MultiplayerConnection.sendGameData(2,x,y);
                    }
                }
                else if (x / squareLength < 15 && y / squareLength < 9) {
                    ProcessGroundTap(x, y);
                    FullscreenActivity.memory.add(new Integer((x - GameView.cameraX) * FullscreenActivity.widthfullscreen + (y - GameView.cameraY)));

                    if (gameIsMultiplayer) {
                        MultiplayerConnection.sendGameData(1,x- GameView.cameraX,y - GameView.cameraY);
                    }
                } else {
                    ProcessGroundUITap(x, y);
                    FullscreenActivity.memory.add(new Integer((x * FullscreenActivity.widthfullscreen + y) * -1));

                    if (gameIsMultiplayer) {
                        MultiplayerConnection.sendGameData(2,x,y);
                    }
                }
            }
        } else if (mode == 1) {
            ProcessGroundTap(x, y);
        } else if (mode == 2) {
            if (GameView.showendTurnScreen) {
                GameView.showendTurnScreen = false;
                return;
            }
            //else if (GameView.activeScreen == GameView.Screen.TECH_SCREEN) {
            //    processTechTap(x, y);
            //}
            else if (loadoutMenu) {
                ProcessLoadoutTap(x,y);
            } else {
                ProcessGroundUITap(x, y);
            }
        }
    }

    /** UNUSED
    public static void processTechTap(int x, int y) {
        if (x / squareLength == 18 && y / squareLength == 10) {
            switchToMainScreen();
            return;
        }
        x = x  - GameView.cameraX;
        y = y  - GameView.cameraY;
        TechNode tappedTech = TechTree.findTappedTechNode((int) (x * FullscreenActivity.scaleFactor), (int) (y * FullscreenActivity.scaleFactor));
        if (tappedTech != null) {
            TechTree.researchTech(tappedTech);
        }
    }
     */

    public static void ProcessGroundUITap(int x, int y) {

        //unit special action TODO: some of these should be in Units class
        if (x / squareLength == 16 && y / squareLength == 1 && (selected != null)) {

            if (theUnit.unitType.equals("Fort") && !theUnit.specialIsActivated) {
                message = "Fort abandoned.";
                theUnit.unitType = "Infantry";

                theUnit.defence -= 1;

                theUnit.movement = 2;

                int oldCoordinatex = theUnit.coordinates[0];
                int oldCoordinatey = theUnit.coordinates[1];
                unselectFriendly();
                theUnit = boardUnits[oldCoordinatex][oldCoordinatey];
                theUnit.brightenIcon();
                selected = new SelectedUnit(GameView.theContext, oldCoordinatex * squareLength, oldCoordinatey * squareLength, theUnit.owner, theUnit.unitType);
                showMarket = false;
                return;
            }

            if (theUnit.unitType.equals("Armor") && !theUnit.specialIsActivated && playing.oilStorage >= 2) {
                message = "Extra move activated.";
                theUnit.hasAttack = true;
                theUnit.hasMove = true;
                playing.oilStorage -= 2;
                showMarket = false;
                theUnit.specialIsActivated = true;
                checkAction(theUnit);
                return;
            }

            if (theUnit.unitType.equals("Artillery") && theUnit.hasAttack) {

                if (playing.oilStorage >= SMOKE_PRICE_OIL) {
                    playing.oilStorage -= SMOKE_PRICE_OIL;
                    theUnit.specialIsActivated = true;
                    smokeFireActive = true;
                    message = "Smoke fire activated";
                    showMarket = false;
                }
                return;
            }

            if (theUnit.hasAttack && theUnit.hasMove) {
                if (theUnit.unitType.equals("Cavalry")) {
                    message = "Scouting range extended.";
                    showMarket = false;
                }
                else if (theUnit.unitType.equals("Infantry") && playing.ironStorage >= 1) {
                    message = "Unit fortified.";
                    theUnit.unitType = "Fort";

                    theUnit.defence += 1;
                    theUnit.movement = 0;

                    playing.ironStorage -= 1;
                    showMarket = false;
                    theUnit.hasAttack = false;
                    theUnit.hasMove = false;
                }
                theUnit.hasAttack = false;
                theUnit.hasMove = false;
                theUnit.specialIsActivated = true;
                checkAction(theUnit);
                unselectFriendly();
                return;
            }

            return;
        }

        //heal the unit if the button is pressed
        if (x / squareLength == 18 && y / squareLength == 1 && selected != null && !theUnit.specialIsActivated) {
            if (theUnit.HP != theUnit.maxHP) {
                if (theUnit.hasAttack && theUnit.hasMove) {
                    theUnit.HP += theUnit.healRate;
                    theUnit.hasAttack = false;
                    theUnit.hasMove = false;
                    if (theUnit.unitType.equals("Armor")) {
                        theUnit.specialIsActivated = true; // armor can't heal and activate special
                    }
                }
                if (theUnit.HP > theUnit.maxHP) {
                    theUnit.HP = theUnit.maxHP;
                }
            }
            checkAction(theUnit);
            unselectFriendly();
            return;
        }

        //switches active player if the button is pressed.
        if (x / squareLength == 16 && y / squareLength == 3) {
            if (queue.length != 0 && message != "Any undeployed unit will be removed, tap again to continue") {
                message = "Any undeployed unit will be removed, tap again to continue";
                showMarket = false;
                unselectAll();
                return;
            }
            queue = new Units[0];
            if (playing.equals(red)) {
                message = "Green player's turn. Press anywhere to continue.";
            } else {
                message = "Red player's turn. Press anywhere to continue.";
            }
            GameView.showendTurnScreen = true;
            try {
                Thread.sleep(15); //syncing with other thread to avoid potential peeks into opponent's army
            } catch (InterruptedException e) {

            }
            switchPlayer();
            unselectAll();
            if (playing.isHuman == false) {
                AI.playTurn(red);
            }
            return;
        }

        /**deploy, not used atm

         if (selected == null && (((x / squareLength == 2 && y / squareLength == 2) && (playing == green && boardUnits[2][2] == null)) ||
                (((x / squareLength == 12 && y / squareLength == 6) && (playing == red && boardUnits[12][6] == null))))) {
            GameEngine.showMarket = !GameEngine.showMarket;
            if (showFactory == true) {
                showFactory = false;
            }
            return;
        }*/

        //Undo
        if (undoIsAllowed && x / squareLength == 18 && y / squareLength == 3 && prevoiusMove != PREV_MOVE.NONE && lastCoordinates[0] != 125 && lastCoordinates[1] != 125) {

            if (prevoiusMove == PREV_MOVE.MOVE) {
                boardUnits[theUnit.coordinates[0]][theUnit.coordinates[1]] = null;
                theUnit.coordinates[0] = lastCoordinates[0];
                theUnit.coordinates[1] = lastCoordinates[1];

                if (boardUnits[lastCoordinates[0]][lastCoordinates[1]] != null) {
                    Units[] newQueue = new Units[GameEngine.queue.length + 1];
                    for (int k = 0; k < GameEngine.queue.length; k++) {
                        newQueue[k] = GameEngine.queue[k];
                    }
                    newQueue[newQueue.length - 1] = boardUnits[lastCoordinates[0]][lastCoordinates[1]];
                    GameEngine.queue = newQueue;
                }

                boardUnits[lastCoordinates[0]][lastCoordinates[1]] = theUnit;
                playing.oilStorage += theUnit.fuelConsumption;
                theUnit.brightenIcon();
                theUnit.hasMove = true;
                unselectFriendly();
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastUnit = null;
                lastEnemyUnit = null;
                prevoiusMove = PREV_MOVE.NONE;
                estimateResources();
                return;
            } else if (prevoiusMove == PREV_MOVE.ATTACK) {
                Units u = boardUnits[lastCoordinates[0]][lastCoordinates[1]];
                Units u2 = boardUnits[lastCoordinates[2]][lastCoordinates[3]];
                GameView.removeSprite(u);
                GameView.removeSprite(u2);

                boardUnits[lastCoordinates[0]][lastCoordinates[1]] = lastUnit;
                lastUnit.coordinates[0] = lastCoordinates[0];
                lastUnit.coordinates[1] = lastCoordinates[1];

                boardUnits[lastCoordinates[2]][lastCoordinates[3]] = lastEnemyUnit;
                lastEnemyUnit.coordinates[0] = lastCoordinates[2];
                lastEnemyUnit.coordinates[1] = lastCoordinates[3];

                Units[] toReturn = new Units[GameView.units.length + 2];
                for (int k = 0; k < GameView.units.length; k++) {
                    toReturn[k] = GameView.units[k];
                }
                toReturn[toReturn.length - 2] = lastEnemyUnit;
                toReturn[toReturn.length - 1] = lastUnit;
                GameView.units = toReturn;

                if (theUnit == null) {
                    theUnit = lastEnemyUnit;
                }
                theUnit.brightenIcon();
                theUnit.hasAttack = true;
                unselectAll();
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastUnit = null;
                lastEnemyUnit = null;
                prevoiusMove = PREV_MOVE.NONE;
            }
        } else if (!undoIsAllowed && x / squareLength == 18 && y / squareLength == 3) {
            message = "Undo is not allowed";
            showMarket = false;
            return;
        }

        //tech screen
        //if (x / squareLength == 16 && y / squareLength == 5 && TechTree.techIsEnabled) {
        //    switchToTechScreen();
        //}


        //exit
        if (x / squareLength  == 18 && y / squareLength  == 5){
            if (message == "You are about to leave the battle, tap again to continue") {
                FullscreenActivity.theActivity.backToMenu();
            }
            else {
                unselectAll();
                message = "You are about to leave the battle, tap again to continue";
            }
        }
        //switches market visibility if the button is pressed.
        if (x / squareLength == 5 && y / squareLength == 10) {
            GameEngine.showMarket = !GameEngine.showMarket;
        }

        //switch loadout to cavalry
        if (x / squareLength == 7 && y / squareLength == 10 && showMarket) {
                if (playing == green) {
                    if (GameEngine.boardUnits[greenDeployX][greenDeployY] == null || (GameEngine.boardUnits[greenDeployX][greenDeployY] != null && GameEngine.boardUnits[greenDeployX][greenDeployY].owner == green)) {
                        loadoutMenu = true;
                        loadoutMenuUnit = "Cavalry";
                    }
                }
                if ((playing == red)) {
                    if (GameEngine.boardUnits[redDeployX][redDeployY] == null || (GameEngine.boardUnits[redDeployX][redDeployY] != null && GameEngine.boardUnits[redDeployX][redDeployY].owner == red)) {
                        loadoutMenu = true;
                        loadoutMenuUnit = "Cavalry";
                    }
                }

            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastUnit = null;
            lastEnemyUnit = null;
            prevoiusMove = PREV_MOVE.NONE;
        }
        //switch loadout to infantry
        if (x / squareLength == 9 && y / squareLength == 10 && showMarket) {
                if (playing == green) {
                    if (GameEngine.boardUnits[greenDeployX][greenDeployY] == null || (GameEngine.boardUnits[greenDeployX][greenDeployY] != null && GameEngine.boardUnits[greenDeployX][greenDeployY].owner == green)) {
                        loadoutMenu = true;
                        loadoutMenuUnit = "Infantry";
                    }
                }
                if (playing == red) {
                    if (GameEngine.boardUnits[redDeployX][redDeployY] == null || (GameEngine.boardUnits[redDeployX][redDeployY] != null && GameEngine.boardUnits[redDeployX][redDeployY].owner == red)) {
                        loadoutMenu = true;
                        loadoutMenuUnit = "Infantry";
                    }
                }
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastUnit = null;
            lastEnemyUnit = null;
            prevoiusMove = PREV_MOVE.NONE;
        }
        //switch loadout to Artillery
        if (x / squareLength == 11 && y / squareLength == 10 && showMarket) {
            if (playing == green) {
                if (GameEngine.boardUnits[greenDeployX][greenDeployY] == null || (GameEngine.boardUnits[greenDeployX][greenDeployY] != null && GameEngine.boardUnits[greenDeployX][greenDeployY].owner == green)) {
                    loadoutMenu = true;
                    loadoutMenuUnit = "Artillery";
                }
            }
            if ((playing == red)) {
                if (GameEngine.boardUnits[redDeployX][redDeployY] == null || (GameEngine.boardUnits[redDeployX][redDeployY] != null && GameEngine.boardUnits[redDeployX][redDeployY].owner == red)) {
                    loadoutMenu = true;
                    loadoutMenuUnit = "Artillery";
                }
            }
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastUnit = null;
            lastEnemyUnit = null;
            prevoiusMove = PREV_MOVE.NONE;
        }
        //switch loadout to armor
        if (x / squareLength == 13 && y / squareLength == 10 && showMarket) {
            if (playing == green) {
                if (GameEngine.boardUnits[greenDeployX][greenDeployY] == null || (GameEngine.boardUnits[greenDeployX][greenDeployY] != null && GameEngine.boardUnits[greenDeployX][greenDeployY].owner == green)) {
                    loadoutMenu = true;
                    loadoutMenuUnit = "Armor";
                }
            }
            if ((playing == red)) {
                if (GameEngine.boardUnits[redDeployX][redDeployY] == null || (GameEngine.boardUnits[redDeployX][redDeployY] != null && GameEngine.boardUnits[redDeployX][redDeployY].owner == red)) {
                    loadoutMenu = true;
                    loadoutMenuUnit = "Armor";
                }
            }
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastUnit = null;
            lastEnemyUnit = null;
            prevoiusMove = PREV_MOVE.NONE;
        }

    }

    public  static void ProcessGroundTap(int x, int y) {
        x -= GameView.cameraX;
        y -= GameView.cameraY;

        //in case of small maps, dont crash if user taps on empty square
        if (x/squareLength >= width || y/squareLength >= height) {
            return;
        }
        // If tap is outside the grid, do nothing. Not used anymore because this would mess up loading games in onResume function.
        //if (x / squareLength >= width || y / squareLength >= heigth) {
        //    return;
        //}

        //if user taps on empty square with no units selected, do nothing
        if (selected == null && enemySelected == null && boardUnits[x / squareLength][y / squareLength] == null) {
            message = "Coordinates: " + (x / squareLength) + ", " + (y / squareLength);
            return;
        }

        //artillery's smoke ability
        if (smokeFireActive && theUnit != null && theUnit.unitType.equals("Artillery")) {
            int distance = getSquareDistance(x / squareLength, theUnit.coordinates[0], y / squareLength, theUnit.coordinates[1]);
            if (distance > theUnit.attack2Range || distance == 0) {    //cancel action
                playing.oilStorage += SMOKE_PRICE_OIL;
                theUnit.specialIsActivated = false;
                message = "Smoke fire cancelled";
                showMarket = false;

            } else {
                message = "Smoke fired";
                showMarket = false;
                smokeMap[x / squareLength][y / squareLength] = SMOKE_DURATION;
                theUnit.hasAttack = false;
                checkAction(theUnit);
                theUnit.specialIsActivated = true;
            }
            smokeFireActive = false;
            return;
        }

        //If user taps on a unit, select it, or display info on enemy unit.
        if (boardUnits[x / squareLength][y / squareLength] != null) {
            if (boardUnits[x / squareLength][y / squareLength].owner == playing) {
                theUnit = boardUnits[x / squareLength][y / squareLength];
                selected = new SelectedUnit(GameView.theContext, x, y, theUnit.owner, theUnit.unitType);
                message = theUnit.unitType + " at " + ((x / squareLength) + c) + ", " + ((y / squareLength) + c);
                showMarket = false;
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastUnit = null;
                lastEnemyUnit = null;
                prevoiusMove = PREV_MOVE.NONE;
                return;
            } else if (boardUnits[x / squareLength][y / squareLength].owner != playing && getFogOfWar(playing)[x / squareLength][y / squareLength]) {
                enemyTappedUnit = boardUnits[x / squareLength][y / squareLength];
                enemySelected = new SelectedUnit(GameView.theContext, x, y, boardUnits[x / squareLength][y / squareLength].owner, boardUnits[x / squareLength][y / squareLength].unitType);
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastUnit = null;
                lastEnemyUnit = null;
                prevoiusMove = PREV_MOVE.NONE;
            }
        }

        // if user taps with unit selected on a square covered in FOW, try to fire blindly or do nothing
        if (theUnit != null && getFogOfWar(playing)[x / squareLength][y / squareLength] == false) {
            if (theUnit != null &&
                    (theUnit.attack2Range >= getSquareDistance           //and check if unit is in range of first (stronger) attack.
                            (getCoordinates(theUnit)[0], x / squareLength,
                                    getCoordinates(theUnit)[1], y / squareLength))
                    && theUnit.hasAttack == true) {
                if (boardUnits[x / squareLength][y / squareLength] != null && boardUnits[x / squareLength][y / squareLength].owner != theUnit.owner) {
                    attackUnit(theUnit, boardUnits[x / squareLength][y / squareLength]);
                }
                //DamageUnit(theUnit.attack1, boardUnits[x / squareLength][y / squareLength], x / squareLength, y / squareLength); //and then move the unit, and un-select it.
                message = "Fired on " + (x / squareLength) + ", " + (y / squareLength);
                //if unit has a move, don't un-select it yet.
                if (theUnit == null) {
                    return;
                }
                if (theUnit != null && theUnit.hasMove) {
                    theUnit.hasAttack = false;
                    return;
                }
                //if units doesn't have a move, un-select it
                if (theUnit != null && !theUnit.hasMove) {
                    theUnit.hasAttack = false;
                    checkAction(theUnit);
                    return;
                }
            } else if (!theUnit.hasAttack){
                message = "No attack left to blindly fire on " + (x / squareLength) + ", " + (y / squareLength);
                showMarket = false;
                return;
            } else {
                message = "Too far away to blindly fire on " + (x / squareLength) + ", " + (y / squareLength);
                showMarket = false;
                return;
            }
        }

        //if user taps with unit selected on an empty square, move it TODO : make sure unit cannot move over another unit
        if (theUnit != null && boardUnits[x / squareLength][y / squareLength] == null
                && getReachableTiles(getCoordinates(theUnit)[0],getCoordinates(theUnit)[1], theUnit.movement)[x / squareLength][y / squareLength]
                && theUnit.hasMove == true) {
            moveTo(theUnit, x / squareLength, y / squareLength); //and then move the unit, and un-select it.
            //if unit has attack, don't un-select it yet.
            if (theUnit.hasAttack) {
                theUnit.hasMove = false;
                return;
            }
            //if units doesn't have an attack, un-select it
            if (!(theUnit.hasAttack)) {
                theUnit.hasMove = false;
                checkAction(theUnit);
                return;
            }
        }

        //if player taps with unit selected on an opponent's unit, attack it
        if (theUnit != null && boardUnits[x / squareLength][y / squareLength] != null &&
                boardUnits[x / squareLength][y / squareLength].owner != theUnit.owner &&
                (theUnit.attack1Range >= getSquareDistance           //and check if unit is in range of first (stronger) attack.
                        (getCoordinates(theUnit)[0], x / squareLength,
                                getCoordinates(theUnit)[1], y / squareLength))
                && theUnit.hasAttack == true) {
            attackUnit(theUnit, boardUnits[x / squareLength][y / squareLength]);
            //DamageUnit(theUnit.attack1, boardUnits[x / squareLength][y / squareLength], x / squareLength, y / squareLength); //and then move the unit, and un-select it.

            //if unit has a move, don't un-select it yet.
            if (theUnit == null) {
                return;
            }
            if (theUnit != null && theUnit.hasMove) {
                theUnit.hasAttack = false;
                return;
            }
            //if units doesn't have a move, un-select it
            if (theUnit != null && !theUnit.hasMove) {
                theUnit.hasAttack = false;
                checkAction(theUnit);
                return;
            }
        }

        //if user taps with unit selected on an opponent's unit, attack it
        if (theUnit != null && boardUnits[x / squareLength][y / squareLength] != null &&
                boardUnits[x / squareLength][y / squareLength].owner != theUnit.owner &&
                (theUnit.attack2Range >= getSquareDistance           //and check if unit is in range of second (weaker) attack.
                        (getCoordinates(theUnit)[0], x / squareLength,
                                getCoordinates(theUnit)[1], y / squareLength))
                && theUnit.hasAttack == true) {
            attackUnit(theUnit, boardUnits[x / squareLength][y / squareLength]);
            //DamageUnit(theUnit.attack2, boardUnits[x / squareLength][y / squareLength], x / squareLength, y / squareLength);
            if (theUnit != null && theUnit.hasMove) {
                theUnit.hasAttack = false;
                checkAction(theUnit);
                return;
            }
            if (theUnit != null && !theUnit.hasMove) {
                theUnit.hasAttack = false;
                checkAction(theUnit);
                return;
            }
        }

        //If user taps on a square that is out of range while some unit is selected, unselect it
        if (boardUnits[x / squareLength][y / squareLength] == null) {
            //if (!FullscreenActivity.hasScrolled) {
            unselectAll();
            //}
            return;
        }
    }

    public static void ProcessLoadoutTap(int x, int y) {
        if (x / squareLength == 8 && y / squareLength == 8) {
            //loadoutMenu = false; //return back if buying a unit should close loadout menu

            if (playing == green && loadoutMenuUnit == "Cavalry") {
                if (playing.foodStorage < Cavalry.greenFoodPrice ||
                        playing.ironStorage < Cavalry.greenIronPrice ||
                        playing.oilStorage < Cavalry.greenOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                    return;
                }
                new Cavalry(GameView.theContext, greenDeployX, greenDeployY, playing);
                message = "New Cavalry at " + greenDeployX + ", " + greenDeployY;
                playing.foodStorage -= Cavalry.greenFoodPrice;
                playing.ironStorage -= Cavalry.greenIronPrice;
                playing.oilStorage -= Cavalry.greenOilPrice;
                showMarket = false;
                if (playing.foodStorage < Cavalry.greenFoodPrice ||
                        playing.ironStorage < Cavalry.greenIronPrice ||
                        playing.oilStorage < Cavalry.greenOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                }
            }

            else if (playing == red && loadoutMenuUnit == "Cavalry") {
                if (playing.foodStorage < Cavalry.redFoodPrice ||
                        playing.ironStorage < Cavalry.redIronPrice ||
                        playing.oilStorage < Cavalry.redOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                    return;
                }
                new Cavalry(GameView.theContext, redDeployX, redDeployY, playing);
                message = "New Cavalry at " + redDeployX + ", " + redDeployY;
                playing.foodStorage -= Cavalry.redFoodPrice;
                playing.ironStorage -= Cavalry.redIronPrice;
                playing.oilStorage -= Cavalry.redOilPrice;
                showMarket = false;
                if (playing.foodStorage < Cavalry.redFoodPrice ||
                        playing.ironStorage < Cavalry.redIronPrice ||
                        playing.oilStorage < Cavalry.redOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                }
            }

            else if (playing == green && loadoutMenuUnit == "Infantry") {
                if (playing.foodStorage < Infantry.greenFoodPrice ||
                        playing.ironStorage < Infantry.greenIronPrice ||
                        playing.oilStorage < Infantry.greenOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                    return;
                }
                new Infantry(GameView.theContext, greenDeployX, greenDeployY, playing);
                message = "New Infantry at " + greenDeployX + ", " + greenDeployY;
                playing.foodStorage -= Infantry.greenFoodPrice;
                playing.ironStorage -= Infantry.greenIronPrice;
                playing.oilStorage -= Infantry.greenOilPrice;
                showMarket = false;
                if (playing.foodStorage < Infantry.greenFoodPrice ||
                        playing.ironStorage < Infantry.greenIronPrice ||
                        playing.oilStorage < Infantry.greenOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                }
            }

            else if (playing == red && loadoutMenuUnit == "Infantry") {
                if (playing.foodStorage < Infantry.redFoodPrice ||
                        playing.ironStorage < Infantry.redIronPrice ||
                        playing.oilStorage < Infantry.redOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                    return;
                }
                new Infantry(GameView.theContext, redDeployX, redDeployY, playing);
                message = "New Infantry at " + redDeployX + ", " + redDeployY;
                playing.foodStorage -= Infantry.redFoodPrice;
                playing.ironStorage -= Infantry.redIronPrice;
                playing.oilStorage -= Infantry.redOilPrice;
                showMarket = false;
                if (playing.foodStorage < Infantry.redFoodPrice ||
                        playing.ironStorage < Infantry.redIronPrice ||
                        playing.oilStorage < Infantry.redOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                }
            }

            else if (playing == green && loadoutMenuUnit == "Artillery") {
                if (playing.foodStorage < Artillery.greenFoodPrice ||
                        playing.ironStorage < Artillery.greenIronPrice ||
                        playing.oilStorage < Artillery.greenOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                    return;
                }
                new Artillery(GameView.theContext, greenDeployX, greenDeployY, playing);
                message = "New Artillery at " + greenDeployX + ", " + greenDeployY;
                playing.foodStorage -= Artillery.greenFoodPrice;
                playing.ironStorage -= Artillery.greenIronPrice;
                playing.oilStorage -= Artillery.greenOilPrice;
                showMarket = false;
                if (playing.foodStorage < Artillery.greenFoodPrice ||
                        playing.ironStorage < Artillery.greenIronPrice ||
                        playing.oilStorage < Artillery.greenOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                }
            }

            else if (playing == red && loadoutMenuUnit == "Artillery") {
                if (playing.foodStorage < Artillery.redFoodPrice ||
                        playing.ironStorage < Artillery.redIronPrice ||
                        playing.oilStorage < Artillery.redOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                    return;
                }
                new Artillery(GameView.theContext, redDeployX, redDeployY, playing);
                message = "New Artillery at " + redDeployX + ", " + redDeployY;
                playing.foodStorage -= Artillery.redFoodPrice;
                playing.ironStorage -= Artillery.redIronPrice;
                playing.oilStorage -= Artillery.redOilPrice;
                showMarket = false;
                if (playing.foodStorage < Artillery.redFoodPrice ||
                        playing.ironStorage < Artillery.redIronPrice ||
                        playing.oilStorage < Artillery.redOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                }
            }

            else if (playing == green && loadoutMenuUnit == "Armor") {
                if (playing.foodStorage < Armor.greenFoodPrice ||
                        playing.ironStorage < Armor.greenIronPrice ||
                        playing.oilStorage < Armor.greenOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                    return;
                }
                new Armor(GameView.theContext, greenDeployX, greenDeployY, playing);
                message = "New Armor at " + greenDeployX + ", " + greenDeployY;
                playing.foodStorage -= Armor.greenFoodPrice;
                playing.ironStorage -= Armor.greenIronPrice;
                playing.oilStorage -= Armor.greenOilPrice;
                showMarket = false;
                if (playing.foodStorage < Armor.greenFoodPrice ||
                        playing.ironStorage < Armor.greenIronPrice ||
                        playing.oilStorage < Armor.greenOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                }
            }

            else if (playing == red && loadoutMenuUnit == "Armor") {
                if (playing.foodStorage < Armor.redFoodPrice ||
                        playing.ironStorage < Armor.redIronPrice ||
                        playing.oilStorage < Armor.redOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                    return;
                }
                new Armor(GameView.theContext, redDeployX, redDeployY, playing);
                message = "New Armor at " + redDeployX + ", " + redDeployY;
                playing.foodStorage -= Armor.redFoodPrice;
                playing.ironStorage -= Armor.redIronPrice;
                playing.oilStorage -= Armor.redOilPrice;
                showMarket = false;
                if (playing.foodStorage < Armor.redFoodPrice ||
                        playing.ironStorage < Armor.redIronPrice ||
                        playing.oilStorage < Armor.redOilPrice) {
                    showMarket = false;
                    loadoutMenu = false;
                }
            }

        } else if (x / squareLength == 10 && y / squareLength == 8) {
            loadoutMenu = false;
        } else if (x / squareLength == 12 && y / squareLength == 8) {
            playing.adjustUpgrades(loadoutMenuUnit,2);
        } else if (x / squareLength == 12 && y / squareLength == 6) {
            playing.adjustUpgrades(loadoutMenuUnit,1);
        } else if (x / squareLength == 12 && y / squareLength == 4) {
            playing.adjustUpgrades(loadoutMenuUnit,0);
        }

    }

    //gets the coordinates of the unit.
    public static int[] getCoordinates (Units u) {
        for (int i = 0; i < boardUnits.length; i++) {
            for (int j = 0; j < boardUnits[i].length; j++) {
                if (boardUnits[i][j] == u) {
                    return new int[] {i,j};
                }
            }
        }
        return null;
    }

    //moves the unit to x and y
    public static void moveTo(Units u, int x, int y) {
        if (u.fuelConsumption != 0) {
            if (u.owner.oilStorage < u.fuelConsumption) {
                message = "No oil left to move armor unit";
                return;
            } else {
                u.owner.oilStorage -= u.fuelConsumption;
            }
        }

        if (u.coordinates[0] == x && u.coordinates[1] == y) {
            return;
        }
        boolean isAtStartingPosition = false;
        if (u.owner == green && u.coordinates[0] == greenDeployX && u.coordinates[1] == greenDeployY && green.isHuman) {
            isAtStartingPosition = true;
        }
        if (u.owner == red && u.coordinates[0] == redDeployX && u.coordinates[1] == redDeployY && red.isHuman) {
            isAtStartingPosition = true;
        }
        int a = 0, b = 0;
        for (int i = 0; i < boardUnits.length; i++) {
            for (int j = 0; j < boardUnits[i].length; j++) {
                if (boardUnits[i][j] == u) {
                    a = i;
                    b = j;
                    break;
                }
            }
        }
        if (playing.isHuman) {
            lastCoordinates[0] = selected.coordinates[0];
            lastCoordinates[1] = selected.coordinates[1];
            lastUnit = null;
            lastEnemyUnit = null;
            prevoiusMove = PREV_MOVE.MOVE;
        }
        u.moveTo(x,y);
        boardUnits[x][y] = u;
        if (playing.isHuman) {
            selected = new SelectedUnit(GameView.theContext, x * squareLength, y * squareLength, theUnit.owner, theUnit.unitType);
        }
        boardUnits[a][b] = null;
        estimateResources();
        showMarket = false;
        message = u.unitType + " moved to " + (x + c) + ", " + (y + c);
        checkAction(u);
        checkIfAnyInRange(u);
        if (isAtStartingPosition) {
            nextInQueue();
        }
    }

    public static void attackUnit(Units attacker, Units defender) {
        lastUnit = new Units(defender, GameView.theContext);
        lastEnemyUnit = new Units(attacker, GameView.theContext);
        prevoiusMove = PREV_MOVE.ATTACK;

        lastCoordinates[0] = defender.coordinates[0];
        lastCoordinates[1] = defender.coordinates[1];
        lastCoordinates[2] = attacker.coordinates[0];
        lastCoordinates[3] = attacker.coordinates[1];

        int attackerDamage;
        int defenderDamage;

        if ((attacker.attack1Range >= getSquareDistance           //check if unit is in range of first (stronger) attack.
                (getCoordinates(attacker)[0], getCoordinates(defender)[0],
                        getCoordinates(attacker)[1], getCoordinates(defender)[1]))) {
            attackerDamage = attacker.attack1;
        } else {
            attackerDamage = attacker.attack2;
        }

        if (!returnFireEnabled) {
            DamageUnit(attackerDamage, defender, getCoordinates(defender)[0], getCoordinates(defender)[1], 0);
            return;
        }

        if ((defender.attack1Range >= getSquareDistance           //check if unit is in range of first (stronger) attack.
                (getCoordinates(defender)[0], getCoordinates(attacker)[0],
                        getCoordinates(defender)[1], getCoordinates(attacker)[1]))) {
            defenderDamage = defender.attack1 - 1;
        } else if ((defender.attack2Range >= getSquareDistance           //check if unit is in range of first (stronger) attack.
                (getCoordinates(defender)[0], getCoordinates(attacker)[0],
                        getCoordinates(defender)[1], getCoordinates(attacker)[1]))) {
            defenderDamage = defender.attack2 - 1;
        } else {
            defenderDamage = 0;
        }

        Player notPlaying = playing == red? green:red;
        int[] attackerCoordinates = getCoordinates(attacker);
        if (noReturnFireIfNotRevealed && getFogOfWar(notPlaying)[attackerCoordinates[0]][attackerCoordinates[1]] == false) {
            defenderDamage = 0;
        }

        //one unit would kill another, so partially damage other unit
        if (attackerDamage - defender.defence >= defender.HP || defenderDamage - attacker.defence >= attacker.HP) {
            double attackerTimeToKill;
            double defenderTimeToKill;
            if (attackerDamage <= defender.defence) {
                attackerTimeToKill = 9999;
            } else {
                attackerTimeToKill = (double) defender.HP / (double) (attackerDamage - defender.defence);
            }
            if (defenderDamage <= attacker.defence) {
                defenderTimeToKill = 9999;
            } else {
                defenderTimeToKill = (double)attacker.HP / (double) (defenderDamage - attacker.defence);
            }

            if (attackerTimeToKill == defenderTimeToKill) {
                DamageUnit(attackerDamage, defender, getCoordinates(defender)[0], getCoordinates(defender)[1], 1);
                message += " received: " + (attacker.HP - 1);
                attacker.HP = 1;
                return;
            }

            double finalDamageFactor;

            if (attackerTimeToKill < defenderTimeToKill) {
                finalDamageFactor = attackerTimeToKill;
                DamageUnit(attackerDamage, defender, getCoordinates(defender)[0], getCoordinates(defender)[1], 1);
                if ((int) ((defenderDamage - attacker.defence) * finalDamageFactor) > 0) {
                    attacker.HP -= (int) ((defenderDamage - attacker.defence) * finalDamageFactor);
                    if (attacker.HP <= 0) {
                        message += " received: " + (attacker.HP - 1);
                        attacker.HP = 1;
                    } else {
                        message += " received: " + (int) ((defenderDamage - attacker.defence) * finalDamageFactor);
                    }
                } else {
                    message += " received: 0";
                    return;
                }
            } else {
                finalDamageFactor = defenderTimeToKill;
                if ((int) ((attackerDamage - defender.defence) * finalDamageFactor) > 0) {
                    message = "Damage given: " + (int) ((attackerDamage - defender.defence) * finalDamageFactor);
                    defender.HP -=  (int) ((attackerDamage - defender.defence) * finalDamageFactor);
                    if (defender.HP <= 0) {
                        defender.HP = 1;
                    }
                } else {
                    message = "Damage given: 0";
                }
                DamageUnit(defenderDamage, attacker, getCoordinates(attacker)[0], getCoordinates(attacker)[1], 2);
            }

        } else { //both units will survive, so damage them both
            DamageUnit(attackerDamage, defender, getCoordinates(defender)[0], getCoordinates(defender)[1], 1);
            DamageUnit(defenderDamage, attacker, getCoordinates(attacker)[0], getCoordinates(attacker)[1], 2);
        }

    }
    //damages the unit at given coordinates
    //message codes: 0- only do damage to unit 1- first damage in duel (and first part of message) 2- second damage in duel (second part of message) 3- dont display messages
    public static void DamageUnit(int damage, Units u, int x, int y, int messageCode) {
        //lastUnit = new Units(u, GameView.theContext);
        //lastCoordinates[0] = u.coordinates[0];
        //lastCoordinates[1] = u.coordinates[1];

        if (damage <= boardUnits[x][y].defence) {
            showMarket = false;
            if (messageCode == 0) {
                message = u.unitType + " at " + (x + c) + ", " + (y + c) + " was not damaged";
            } else if (messageCode == 1) {
                message = "Damage given: 0";
            } else if (messageCode == 2) {
                message += " received: 0";
            }
            return;
        }

        boardUnits[x][y].HP = boardUnits[x][y].HP - (damage - boardUnits[x][y].defence);
        //if unit has less than 1HP, remove it
        showMarket = false;
        if (messageCode == 0) {
            message = u.unitType + " at " + (x + c) + ", " + (y + c) + " damaged by " + (damage - boardUnits[x][y].defence);
        } else if (messageCode == 1) {
            message = "Damage given: " + (damage - boardUnits[x][y].defence);
        } else if (messageCode == 2) {
            message += " received: " + (damage - boardUnits[x][y].defence);
        }
        if (boardUnits[x][y].HP <= 0) {
            boardUnits[x][y] = null;
            if (u.owner == GameEngine.playing) {
                unselectFriendly();
            } else {
                unselectEnemy();
            }
            GameView.removeSprite(u);
            showMarket = false;
            if (messageCode == 0) {
                message = u.unitType + " at " + (x + c) + ", " + (y + c) + " is destroyed";
            }
            if (u.unitType.equals("Headquarters")) {
                if (theUnit != null && theUnit.hasMove) {
                            theUnit.hasAttack = false;
                }
                //if units doesn't have a move, un-select it
                if (theUnit != null && !theUnit.hasMove) {
                    theUnit.hasAttack = false;
                    checkAction(theUnit);
                }
                unselectAll();
                message = "HQ has been destroyed, " + playing.color + " player wins!";
                FullscreenActivity.theActivity.vibrate();
                showMarket = false;
            }
        }

        if (theUnit != null && theUnit.HP < 0) {
            unselectFriendly();
        }
        if (enemyTappedUnit != null && enemyTappedUnit.HP < 0) {
            unselectEnemy();
        }
    }
    //unselects all units
    public static void unselectAll() {
        selected = null;
        theUnit = null;
        enemySelected = null;
        enemyTappedUnit = null;
    }
    //unselects enemy unit
    public static void unselectEnemy() {
        enemySelected = null;
        enemyTappedUnit = null;
    }
    //unselects friendly unit
    public static void unselectFriendly() {
        selected = null;
        theUnit = null;
    }

    //deploys next unit in queue
    public static void nextInQueue(){
        if (GameEngine.queue.length == 0) {
            return;
        }
        if (playing == green) {
            boardUnits[greenDeployX][greenDeployY] = GameEngine.queue[0];
            GameEngine.queue[0].coordinates[0] = greenDeployX;
            GameEngine.queue[0].coordinates[1] = greenDeployY;
        }
        if (playing == red) {
            boardUnits[redDeployX][redDeployY] = GameEngine.queue[0];
            GameEngine.queue[0].coordinates[0] = redDeployX;
            GameEngine.queue[0].coordinates[1] = redDeployY;
        }
        Units []toReturn = new Units[GameEngine.queue.length - 1];
        for (int k = 1; k < GameEngine.queue.length; k++) {
            toReturn[k - 1] = GameEngine.queue[k];
        }
        GameEngine.queue = toReturn;
    }
    //checks if unit has action, and darkens the icon if it doesn't
    public static void checkAction(Units u) {
        if (u.hasMove == false && u.hasAttack == false) {
            u.darkenIcon();
        }
    }

    //check if any unit is in range
    public static boolean checkIfAnyInRange(Units u) {
        boolean InRange = false;
        if (u != null && u.hasAttack == true) {
            for (int i = 0; i < GameEngine.boardUnits.length; i++) { // TODO : optimize this
                for (int j = 0; j < GameEngine.boardUnits[i].length; j++) {
                    if (GameEngine.boardUnits[i][j] != null && GameEngine.boardUnits[i][j].owner != GameEngine.playing &&
                            (u.attack2Range >= GameEngine.getSquareDistance
                                    (GameEngine.getCoordinates(u)[0], i,
                                            GameEngine.getCoordinates(u)[1], j))) {
                        InRange = true;
                        break;
                    }
                }
            }
        }
        if (!InRange) {
            u.darkenIcon();
            return false;
        }
        return true;
    }

    //returns the distance between two coordinates, calculated by summing up difference between x coordinates and difference between y coordinates.
    public static int getSquareDistance(int x1, int x2, int y1, int y2) {
        int deltaX = x1 - x2; //Math.abs doesn't seem to work on phones properly :(
        if (deltaX < 0) {
            deltaX *= - 1;
        }
        int deltaY = y1 - y2;
        if (deltaY < 0) {
            deltaY *= - 1;
        }
        return deltaX + deltaY;
    }

    //switches the player when called
    public static void switchPlayer() {

        for (int i = 0; i < smokeMap.length; i++) {
            for (int j = 0; j < smokeMap[i].length; j++) {
                if (smokeMap[i][j] > 0) {
                    smokeMap[i][j]--;
                }
            }
        }

        //autoheals player's units if unit has move and attack left
        for (int i = 0; i < boardUnits.length; i++) {
            for (int j = 0; j < boardUnits[i].length; j++) {
                if (boardUnits[i][j] != null && boardUnits[i][j].owner == playing) {
                    if (boardUnits[i][j].hasAttack && boardUnits[i][j].hasMove) {
                        boardUnits[i][j].HP += boardUnits[i][j].healRate;
                        if (boardUnits[i][j].HP > boardUnits[i][j].maxHP) {
                            boardUnits[i][j].HP = boardUnits[i][j].maxHP;
                        }
                        boardUnits[i][j].hasAttack = false;
                        boardUnits[i][j].hasMove = false;
                    }
                }
            }
        }

        if (playing == green) {
            for (int i = 0; i < fogOfWarIsRevealedForGreen.length; i++) {
                fogOfWarIsRevealedForGreen[i] = false;
            }

            playing = red;
            GameView.targetCameraX =  (15 - width)* squareLength;
            GameView.targetCameraY =  (9 - height)* squareLength;
        } else if (playing == red) {
            for (int i = 0; i < fogOfWarIsRevealedForGreen.length; i++) {
                fogOfWarIsRevealedForRed[i] = false;
            }
            playing = green;
            GameView.targetCameraX = 0;
            GameView.targetCameraY = 0;
        }

        // gives movement and attack to next player's units
        for (int i = 0; i < boardUnits.length; i++) {
            for (int j = 0; j < boardUnits[i].length; j++) {
                if (boardUnits[i][j] != null && boardUnits[i][j].owner == playing) {
                    boardUnits[i][j].hasMove = true;
                    boardUnits[i][j].hasAttack = true;
                    boardUnits[i][j].specialIsActivated = false;
                    boardUnits[i][j].brightenIcon();
                }
            }
        }
        //gives harvested resources to next player
        for (int i = 0; i < BoardResources.length; i++) {
            for (int j = 0; j < BoardResources[i].length; j++) {
                //this long if statement checks if BoardResource[i][j] should yield a resource to the player.
                if (BoardResources[i][j]!= null &&
                        boardUnits[BoardResources[i][j].collectorCoordinates[0]][BoardResources[i][j].collectorCoordinates[1]] != null &&
                        boardUnits[BoardResources[i][j].collectorCoordinates[0]][BoardResources[i][j].collectorCoordinates[1]].owner == playing) {
                    if (BoardResources[i][j].resourceType.equals("oil")) {
                        playing.oilStorage++;
                    }
                    if (BoardResources[i][j].resourceType.equals("iron")) {
                        playing.ironStorage++;
                    }
                    if (BoardResources[i][j].resourceType.equals("food")) {
                        playing.foodStorage++;
                    }
                }
            }
        }

        playing.researchPoints += RESEARCH_PER_TURN;
        playing.foodStorage += playing.bonusFood;
        playing.ironStorage += playing.bonusIron;
        playing.oilStorage += playing.bonusOil;
        playing.researchPoints += playing.bonusResearch;

        GameEngine.estimateResources();
        showMarket = false;
        if (playing == green) {
            message = "Green player's turn. Press anywhere to continue.";
        } else {
            message ="Red player's turn. Press anywhere to continue.";
        }
    }

    //estimates the resources that will be given to playing player next turn
    public static void estimateResources() {
        int addediron = 0;
        int addedfood = 0;
        int addedoil = 0;
        for (int i = 0; i < BoardResources.length; i++) {
            for (int j = 0; j < BoardResources[i].length; j++) {
                //this long if statement checks if BoardResource[i][j] should yield a resource to the player.
                if (BoardResources[i][j]!= null &&
                        boardUnits[BoardResources[i][j].collectorCoordinates[0]][BoardResources[i][j].collectorCoordinates[1]] != null &&
                        boardUnits[BoardResources[i][j].collectorCoordinates[0]][BoardResources[i][j].collectorCoordinates[1]].owner == playing) {
                    if (BoardResources[i][j].resourceType.equals("oil")) {
                        addedoil++;
                    }
                    if (BoardResources[i][j].resourceType.equals("iron")) {
                        addediron++;
                    }
                    if (BoardResources[i][j].resourceType.equals("food")) {
                        addedfood++;
                    }
                }
            }
        }

        addedfood += playing.bonusFood;
        addediron += playing.bonusIron;
        addedoil += playing.bonusOil;

        GameEngine.lastAddedResources[0] = addedfood;
        GameEngine.lastAddedResources[1] = addediron;
        GameEngine.lastAddedResources[2] = addedoil;
    }

    //True: tile is revealed
    public static boolean[][] getFogOfWar(Player player) {
        boolean[][] tile_is_visible = new boolean[width][height];

        if (GameView.removeFogOfWar) {
            for (int i = 0; i < tile_is_visible.length; i++) {
                for (int j = 0; j < tile_is_visible[i].length; j++) {
                    tile_is_visible[i][j] = true;
                }
            }
            return tile_is_visible;
        }

        for (int i = 0; i < tile_is_visible.length; i++) {
            for (int j = 0; j < tile_is_visible[i].length; j++) {
                tile_is_visible[i][j] = false;
            }
        }
        //green player
        if (player == green) {
            for (int i = 0; i < boardUnits.length; i++) {
                for (int j = 0; j < boardUnits[i].length; j++) {
                    if (boardUnits[i][j] != null && boardUnits[i][j].owner.color.equals("green")) {
                        for (int a = 0; a < boardUnits.length; a++) {
                            for (int b = 0; b < boardUnits[a].length; b++) {
                                if (!tile_is_visible[a][b] && boardUnits[i][j].getDistanceToPoint(a,b) <= boardUnits[i][j].getVisibilityRange()
                                    && unitCanSeeTile(i,j,a,b)) {
                                    tile_is_visible[a][b] = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        //red player
        if (player == red) {
            for (int i = 0; i < boardUnits.length; i++) {
                for (int j = 0; j < boardUnits[i].length; j++) {
                    if (boardUnits[i][j] != null && boardUnits[i][j].owner.color.equals("red")) {
                        for (int a = 0; a < boardUnits.length; a++) {
                            for (int b = 0; b < boardUnits[a].length; b++) {
                                if (!tile_is_visible[a][b] && boardUnits[i][j].getDistanceToPoint(a,b) <= boardUnits[i][j].getVisibilityRange()
                                        && unitCanSeeTile(i,j,a,b)) {
                                    tile_is_visible[a][b] = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        //reveal tiles that are scouted by planes
        if (player == green) {
            for (int a = 0; a < fogOfWarIsRevealedForGreen.length; a++) {
                if (fogOfWarIsRevealedForGreen[a]) {
                    for (int i = 0; i < GameEngine.boardUnits.length; i++) {
                        for (int j = a * 3; j < (a+1)*3; j++) {
                            tile_is_visible[i][j] = true;
                        }
                    }
                }
            }
        }
        if (player == red) {

            for (int a = 0; a < fogOfWarIsRevealedForRed.length; a++) {
                if (fogOfWarIsRevealedForRed[a]) {
                    for (int i = 0; i < GameEngine.boardUnits.length; i++) {
                        for (int j = a * 3; j < (a+1)*3; j++) {
                            tile_is_visible[i][j] = true;
                        }
                    }
                }
            }
        }

        //reveal a tile where enemy selected unit is
        if (enemySelected != null) {
            int[] coords = getCoordinates(enemyTappedUnit);
            tile_is_visible[coords[0]][coords[1]] = true;
        }

        return tile_is_visible;
    }

    //helper recursive function
    public static void getReachableTilesRecursive(int xCoord, int yCoord, int currentDistance, boolean[][] map) {
        if (currentDistance < 0
                || xCoord < 0 || yCoord < 0 || xCoord > width - 1 || yCoord > height - 1
                || (boardUnits[xCoord][yCoord] != null && boardUnits[xCoord][yCoord].owner != playing)) {
            return;
        }
        if (boardUnits[xCoord][yCoord] == null) {
            map[xCoord][yCoord] = true;
        }
        getReachableTilesRecursive(xCoord-1,yCoord,currentDistance - 1, map);
        getReachableTilesRecursive(xCoord,yCoord-1,currentDistance - 1, map);
        getReachableTilesRecursive(xCoord+1,yCoord,currentDistance - 1, map);
        getReachableTilesRecursive(xCoord,yCoord+1,currentDistance - 1, map);
    }

    //gets all tiles that are reachable by a unit without skipping over enemy units
    public static boolean[][] getReachableTiles(int xCoord, int yCoord, int range) {
        boolean[][] tiles = new boolean[width][height];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = false;
            }
        }

        getReachableTilesRecursive(xCoord, yCoord, range, tiles);
        tiles[xCoord][yCoord] = false;
        return tiles;
    }

    public static void revealLineOfFOW(Player player, int line) {
        if (player == green) {
            fogOfWarIsRevealedForGreen[line] = true;
        } else {
            fogOfWarIsRevealedForRed[line] = true;
        }
    }

    public static double distanceFromLineToPoint(double x1, double y1, double x2, double y2, double smokeX, double smokeY) {

        if (y2 == y1 || x2 == x1) return 0; //they are on the same line if this is true
        double a = (y2-y1)/(x2 - x1);
        double b = 1;
        double c = y1 - a*x1;
        a *= -1;
        c *= -1;
        return (Math.abs(a*smokeX+b*smokeY+c))/(Math.sqrt(a*a+b*b));
    }

    public static boolean unitCanSeeTile(double unitX, double unitY, double tileX, double tileY) {

        for (int i = 0; i < smokeMap.length; i++) {
            for (int j = 0; j < smokeMap[i].length; j++) {
                if (smokeMap[i][j] > 0) { //is smoke there

                    if (i == tileX && j == tileY) { //dont hide smoke itself
                        continue;
                    }

                    if (((unitX <= i && tileX >= i) || (unitX >= i && tileX <= i))
                        && ((unitY <= j && tileY >= j) || (unitY >= j && tileY <= j))) { //is smoke between the two
                        if (distanceFromLineToPoint(unitX, unitY, tileX, tileY, i,j) <= SMOKE_WIDTH) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static void switchToTechScreen() {
        GameView.activeScreen = GameView.Screen.TECH_SCREEN;
       // MainThread.shouldCancelDrawing = true;
        GameView.targetCameraX = 0;
        GameView.targetCameraY = 0;
    }

    public static void switchToMainScreen() {
        GameView.activeScreen = GameView.Screen.MAIN_SCREEN;
       // MainThread.shouldCancelDrawing = true;
        GameView.targetCameraX = 0;
        GameView.targetCameraY = 0;
    }

    //TODO: add display message function to avoid code repetition

}
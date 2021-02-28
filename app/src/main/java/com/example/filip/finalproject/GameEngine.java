package com.example.filip.finalproject;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.SystemClock;
import java.util.concurrent.Semaphore;

import static com.example.filip.finalproject.MainThread.canvas;

// Class that will run the game and manage all the events.
public class GameEngine extends Thread{

    public enum PREV_MOVE {
        NONE,
        MOVE,
        ATTACK
    }

    public static final double SMOKE_WIDTH = 0.71;
    public static final int SMOKE_DURATION = 4;
    public static final int SMOKE_PRICE_OIL = 2;
    public static final int RESEARCH_PER_TURN = 1;

    public static int width = 15;
    public static int heigth = 9;

    public static int greenDeployX = 2;
    public static int greenDeployY = 2;
    public static int redDeployX = 12;
    public static int redDeployY = 6;

    public static double airLineXScaleFactor;
    public static double airLineYScaleFactor;

    public static int airLinesCount;
    public static int turnCount = 0;
    public static int squareLength = (int) (128  * FullscreenActivity.scaleFactor); //scales square length, dependent on scale factor
    public Bitmap image; // Image of the grid
    public Bitmap emptySquare;
    public static Bitmap emptyAirLine; //air grid
    public static Units lastUnit; //stores the last unit which made some action
    public static PREV_MOVE prevoiusMove = PREV_MOVE.NONE;
    public static Units lastEnemyUnit; //stores the last enemy unit which made some action
    public static Units theUnit = null; // Selected unit, unlike the selected unit in GameView class this unit is the actual selected unit.
    public static SelectedUnit selected = null; // Selected unit, reference is not the same as the (selected) Unit itself.
    public static Planes selectedPlane = null; //selected plane
    public static Planes selectedEnemyPlane = null; //selected enemy plane
    public static Units enemyTappedUnit = null; //same as above, but of opponent
    public static SelectedUnit enemySelected = null; // same as above, but for opponent
    public static Planes[][] planeLines = new Planes[3][2];
    public static Units[][] BoardSprites = new Units[15][9]; //A 2D array of Units that stores the units for game engine and data processing, unlike GameView's Units[] this isn't involved in drawing units.
    public static Player green; //stores the reference to a player that is in charge of Green units
    public static Player red;//stores the reference to a player that is in charge of Red units
    public static Player playing = null; //player that makes moves
    public static Resources[][] BoardResources = new Resources[15][9]; //a 2D array of Units that stores the resources in the game
    public static boolean showFactory = false; //shows factory units which can be purchased
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

    public static boolean  returnFireEnabled = true;

    public static int[] lastAddedResources = new int[3]; //memorizes last added resources, to display next to storage
    public static boolean gameIsMultiplayer = false;
    public static boolean isHostPhone = false;
    public static boolean replayMode = false;
    public static int replayActionDelay = 500;

    //restarts board
    public static void restart(){
        fogOfWarIsRevealedForGreen = new boolean[fogOfWarIsRevealedForGreen.length];
        fogOfWarIsRevealedForRed = new boolean[fogOfWarIsRevealedForRed.length];
        planeLines = new Planes[planeLines.length][planeLines[0].length];
        prevoiusMove = PREV_MOVE.NONE;
        lastUnit = null; //stores the last unit which made some action
        lastEnemyUnit = null; //stores the last unit which made some action
        theUnit = null; // Selected unit, unlike the selected unit in GameView class this unit is the actual selected unit.
        selected = null; // Selected unit, reference is not the same as the (selected) Unit itself.
        enemyTappedUnit = null; //same as above, but of opponent
        enemySelected = null; // same as above, but for opponent
        BoardSprites = new Units[15][9]; //A 2D array of Units that stores the units for game engine and data processing, unlike GameView's Units[] this isn't involved in drawing units.
        playing = null; //player that makes moves
        BoardResources = new Resources[15][9]; //a 2D array of resources that stores the resources in the game
        showFactory = false; //shows factory units which can be purchased
        showMarket = false; //shows market units which can be purchased
        message = ""; //stores message to user
        green = new Player("green", true);
        red = new Player("red", true);
        selectedPlane = null;
        selectedEnemyPlane = null;
        smokeMap = new int[width][heigth];
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
        ReplayMenu.replayTechEnabled = TechTree.techIsEnabled;

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
    public GameEngine(Bitmap grid, Bitmap airGrid, Bitmap square,  int input_width, int input_heigth) {
        image = grid;
        emptyAirLine = airGrid;
        emptySquare = square;
        this.width = input_width;
        this.heigth = input_heigth;
        BoardSprites = new Units[width][heigth];
        BoardResources = new Resources[width][heigth];
        smokeMap = new int[width][heigth];
        smokeFireActive = false;
    }

    // updates board
    public void update() {
        return;
    }

    //draws the board when grid.draw(canvas) is called in GameView function.
    public void draw(Canvas canvas, boolean air) {
        if (air) {
            //Bitmap scaledGrid = Bitmap.createScaledBitmap(yourSelectedImage, width, height, false);
            for (int i = 0; i < airLinesCount; i++) {
                canvas.drawBitmap(emptyAirLine, 2.5f*(GameEngine.squareLength), (int) (3 * squareLength * i * airLineYScaleFactor), null);
            }
        }
        else {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < heigth; j++) {
                    canvas.drawBitmap(emptySquare, i * squareLength +  GameView.cameraX, j * squareLength +  GameView.cameraY, null);
                }
            }
            //canvas.drawBitmap(image, GameView.cameraX, GameView.cameraY, null);
        }
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

                if (playing == red) {
                    message = "";
                    for (int i = 0;i < airLinesCount; i++) {
                        if (planeLines[i][0] != null) {
                            message = "Enemy planes spotted! ";
                            break;
                        }
                    }
                }

                if (playing == green) {
                    message = "";
                    for (int i = 0;i < airLinesCount; i++) {
                        if (planeLines[i][1] != null) {
                            message = "Enemy planes spotted! ";
                            break;
                        }
                    }
                }

                if (gameIsMultiplayer) {
                    MultiplayerConnection.sendGameData(0,5,5);
                }
                FullscreenActivity.memory.add(new Integer((-1000)));
                return;

            }
            if (GameView.activeScreen == GameView.Screen.AIR_SCREEN) {
                processAirTap(x,y);
                FullscreenActivity.memory.add(new Integer((x * FullscreenActivity.widthfullscreen + y) * -1));

                if (gameIsMultiplayer) {
                    MultiplayerConnection.sendGameData(2,x,y);
                }

            } else if (GameView.activeScreen == GameView.Screen.TECH_SCREEN) {
                processTechTap(x,y);
                FullscreenActivity.memory.add(new Integer((x - GameView.cameraX) * FullscreenActivity.widthfullscreen + (y - GameView.cameraY)));

                if (gameIsMultiplayer) {
                    MultiplayerConnection.sendGameData(1,x- GameView.cameraX,y - GameView.cameraY);
                }

            } else {

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
            if (GameView.activeScreen == GameView.Screen.AIR_SCREEN) {
                processAirTap(x, y);
            } else if (GameView.activeScreen == GameView.Screen.TECH_SCREEN) {
                processTechTap(x, y);
            } else if (loadoutMenu) {
                ProcessLoadoutTap(x,y);
            } else {
                ProcessGroundUITap(x, y);
            }
        }
    }

    //process air input
    public static void processAirTap(int x, int y) {
        if (x / squareLength == 18 && y / squareLength == 10) {
            GameView.activeScreen = GameView.Screen.MAIN_SCREEN;
            unselectAllPlanes();
        }

        else if (x / squareLength > 3 && y / squareLength >= 10 && x / squareLength < 10 && selectedPlane == null) {
            playing.selectPlane((x/squareLength) - 4);
        }

        //green's air tap
        else if (x / squareLength > 3 && y / squareLength >= 10 && x / squareLength < 10 && selectedPlane != null && selectedPlane.isDeployed && playing == green) {

            if (playing.hangar[(x/squareLength) - 4] != null) {
                selectedPlane.unselect();
                playing.hangar[(x/squareLength) - 4].select();
            }

            else if (playing.hangarHasEmptySlots()) {
                selectedPlane.groundPlane();
                playing.oilStorage++;

                for (int i = 0; i < planeLines.length; i++) {
                    if (planeLines[i][0] == selectedPlane) {
                        planeLines[i][0] = null;
                    }
                }
                unselectFriendlyPlanes();
            }
        }

        else if (x/squareLength < 2 && playing == green && y / squareLength < 9) {

            //y represents plane line after this
            y = y / (int) (3 * GameEngine.airLineYScaleFactor * GameEngine.squareLength);
            if (planeLines[y][0] != null) {
                if (selectedPlane != null) {
                    unselectFriendlyPlanes();
                }
                planeLines[y][0].select();
            }

            else if (selectedPlane != null) {
                if (!selectedPlane.isDeployed && planeLines[y][0] == null && green.oilStorage > 0) {
                    planeLines[y][0] = selectedPlane;
                    green.removeFromHanger(selectedPlane);
                    selectedPlane.isDeployed = true;
                    selectedPlane = null;
                    green.oilStorage--;
                } else if (selectedPlane.isDeployed) {
                    for (int i = 0; i < planeLines.length; i++) {
                        if (planeLines[i][0] == selectedPlane) {
                            planeLines[i][0] = null;
                        }
                    }
                    selectedPlane.unselect();
                    planeLines[y][0] = selectedPlane;
                    selectedPlane = null;
                }
            }
        }

        else if (x/squareLength > 17 && playing == green && y / squareLength < 9) {
            y = y / (int) (3 * GameEngine.airLineYScaleFactor * GameEngine.squareLength);
            if (planeLines[y][1] != null) {
                if (selectedEnemyPlane != null) {
                    unselectEnemyPlanes();
                }
                planeLines[y][1].selectEnemy();
            }
        }

        //red's air tap
        else if (x / squareLength > 3 && y / squareLength >= 10 && x / squareLength < 10 && selectedPlane != null && selectedPlane.isDeployed && playing == red) {

            if (playing.hangar[(x/squareLength) - 4] != null) {
                selectedPlane.unselect();
                playing.hangar[(x/squareLength) - 4].select();
            }

            else if (playing.hangarHasEmptySlots()) {
                selectedPlane.groundPlane();
                playing.oilStorage++;

                for (int i = 0; i < planeLines.length; i++) {
                    if (planeLines[i][1] == selectedPlane) {
                        planeLines[i][1] = null;
                    }
                }
                unselectFriendlyPlanes();
            }
        }

        else if (x/squareLength > 17 && playing == red && y / squareLength < 9) {

            //y represents plane line after this
            y = y / (int) (3 * GameEngine.airLineYScaleFactor * GameEngine.squareLength);
            if (planeLines[y][1] != null) {
                if (selectedPlane != null) {
                    unselectFriendlyPlanes();
                }
                planeLines[y][1].select();
            }

            else if (selectedPlane != null) {
                if (!selectedPlane.isDeployed && planeLines[y][1] == null && red.oilStorage > 0) {
                    planeLines[y][1] = selectedPlane;
                    red.removeFromHanger(selectedPlane);
                    selectedPlane.isDeployed = true;
                    selectedPlane = null;
                    green.oilStorage--;
                } else if (selectedPlane.isDeployed) {
                    for (int i = 0; i < planeLines.length; i++) {
                        if (planeLines[i][1] == selectedPlane) {
                            planeLines[i][1] = null;
                        }
                    }
                    selectedPlane.unselect();
                    planeLines[y][1] = selectedPlane;
                    selectedPlane = null;
                }
            }
        }

        else if (x/squareLength < 2 && playing == red && y / squareLength < 9) {
            y = y / (int) (3 * GameEngine.airLineYScaleFactor * GameEngine.squareLength);
            if (planeLines[y][0] != null) {
                if (selectedEnemyPlane != null) {
                    unselectEnemyPlanes();
                }
                planeLines[y][0].selectEnemy();
            }
        }

        //end red's tap
        else {
            if (selectedPlane != null) {
                selectedPlane.unselect();
                selectedPlane = null;
            }
            else if (selectedEnemyPlane != null) {
                selectedEnemyPlane.unselect();
                selectedEnemyPlane = null;
            }
        }

    }

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

    public static void ProcessGroundUITap(int x, int y) {

        //unit special action
        if (x / squareLength == 16 && y / squareLength == 1 && (selected != null)) {

            if (theUnit.unitType.equals("Fort") && !theUnit.specialIsActivated) {
                message = "Fort abandoned.";
                theUnit.unitType = "Infantry";

                theUnit.defence -= 1;
                theUnit.airAttack -= 1;

                theUnit.movement = 2;

                int oldCoordinatex = theUnit.coordinates[0];
                int oldCoordinatey = theUnit.coordinates[1];
                unselectFriendly();
                theUnit = BoardSprites[oldCoordinatex][oldCoordinatey];
                theUnit.brightenIcon();
                selected = new SelectedUnit(GameView.theContext, oldCoordinatex * squareLength, oldCoordinatey * squareLength, theUnit.owner, theUnit.unitType);
                showFactory = false;
                showMarket = false;
                return;
            }

            if (theUnit.unitType.equals("Armor") && !theUnit.specialIsActivated && playing.oilStorage >= 2) {
                message = "Extra move activated.";
                theUnit.hasAttack = true;
                theUnit.hasMove = true;
                playing.oilStorage -= 2;
                showFactory = false;
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
                    showFactory = false;
                    showMarket = false;
                }
                return;
            }

            if (theUnit.hasAttack && theUnit.hasMove) {
                if (theUnit.unitType.equals("Cavalry")) {
                    message = "Scouting range extended.";
                    showFactory = false;
                    showMarket = false;
                }
                else if (theUnit.unitType.equals("Infantry") && playing.ironStorage >= 1) {
                    message = "Unit fortified.";
                    theUnit.unitType = "Fort";

                    theUnit.defence += 1;
                    theUnit.airAttack += 1;
                    theUnit.movement = 0;

                    playing.ironStorage -= 1;
                    showFactory = false;
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
        if (x / squareLength == 18 && y / squareLength == 1 && selected != null) {
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
                showFactory = false;
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
            unselectAllPlanes();
            if (playing.isHuman == false) {
                AI.playTurn(red);
            }
            return;
        }

        //deploy, not used atm
        /**if (selected == null && (((x / squareLength == 2 && y / squareLength == 2) && (playing == green && BoardSprites[2][2] == null)) ||
                (((x / squareLength == 12 && y / squareLength == 6) && (playing == red && BoardSprites[12][6] == null))))) {
            GameEngine.showMarket = !GameEngine.showMarket;
            if (showFactory == true) {
                showFactory = false;
            }
            return;
        }*/

        //Undo undo
        if (x / squareLength == 18 && y / squareLength == 3 && prevoiusMove != PREV_MOVE.NONE && lastCoordinates[0] != 125 && lastCoordinates[1] != 125) {

            if (prevoiusMove == PREV_MOVE.MOVE) {
                BoardSprites[theUnit.coordinates[0]][theUnit.coordinates[1]] = null;
                theUnit.coordinates[0] = lastCoordinates[0];
                theUnit.coordinates[1] = lastCoordinates[1];

                if (BoardSprites[lastCoordinates[0]][lastCoordinates[1]] != null) {
                    Units[] newQueue = new Units[GameEngine.queue.length + 1];
                    for (int k = 0; k < GameEngine.queue.length; k++) {
                        newQueue[k] = GameEngine.queue[k];
                    }
                    newQueue[newQueue.length - 1] = BoardSprites[lastCoordinates[0]][lastCoordinates[1]];
                    GameEngine.queue = newQueue;
                }

                BoardSprites[lastCoordinates[0]][lastCoordinates[1]] = theUnit;
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
                Units u = BoardSprites[lastCoordinates[0]][lastCoordinates[1]];
                Units u2 = BoardSprites[lastCoordinates[2]][lastCoordinates[3]];
                GameView.removeSprite(u);
                GameView.removeSprite(u2);

                BoardSprites[lastCoordinates[0]][lastCoordinates[1]] = lastUnit;
                lastUnit.coordinates[0] = lastCoordinates[0];
                lastUnit.coordinates[1] = lastCoordinates[1];

                BoardSprites[lastCoordinates[2]][lastCoordinates[3]] = lastEnemyUnit;
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
        }

        //tech screen
        if (x / squareLength == 16 && y / squareLength == 5 && TechTree.techIsEnabled) {
            switchToTechScreen();
        }


        //show air
        if (x / squareLength == 18 && y / squareLength == 5) {
            GameView.activeScreen = GameView.Screen.AIR_SCREEN;
        }
        //switches market visibility if the button is pressed.
        if (x / squareLength == 5 && y / squareLength == 10) {
            GameEngine.showMarket = !GameEngine.showMarket;
            if (showFactory == true) {
                showFactory = false;
            }
            return;
        }
        //switches factory visibility if the button is pressed.
        if (x / squareLength == 5 && y / squareLength == 9) {
            GameEngine.showFactory = !GameEngine.showFactory;
            if (showMarket == true) {
                showMarket = false;
            }
            return;
        }
        //switch loadout to cavalry
        if (x / squareLength == 7 && y / squareLength == 10 && showMarket) {
                if (playing == green) {
                    if (GameEngine.BoardSprites[greenDeployX][greenDeployY] == null || (GameEngine.BoardSprites[greenDeployX][greenDeployY] != null && GameEngine.BoardSprites[greenDeployX][greenDeployY].owner == green)) {
                        loadoutMenu = true;
                        loadoutMenuUnit = "Cavalry";
                    }
                }
                if ((playing == red)) {
                    if (GameEngine.BoardSprites[redDeployX][redDeployY] == null || (GameEngine.BoardSprites[redDeployX][redDeployY] != null && GameEngine.BoardSprites[redDeployX][redDeployY].owner == red)) {
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
                    if (GameEngine.BoardSprites[greenDeployX][greenDeployY] == null || (GameEngine.BoardSprites[greenDeployX][greenDeployY] != null && GameEngine.BoardSprites[greenDeployX][greenDeployY].owner == green)) {
                        loadoutMenu = true;
                        loadoutMenuUnit = "Infantry";
                    }
                }
                if (playing == red) {
                    if (GameEngine.BoardSprites[redDeployX][redDeployY] == null || (GameEngine.BoardSprites[redDeployX][redDeployY] != null && GameEngine.BoardSprites[redDeployX][redDeployY].owner == red)) {
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
        //buys new MGInfantry
        if (x / squareLength == 11 && y / squareLength == 10 && showMarket) {
            if (playing.foodStorage >= MGInfantry.foodPrice && playing.ironStorage >= MGInfantry.ironPrice && playing.oilStorage >= MGInfantry.oilPrice) {
                if (playing == green) {
                    if (GameEngine.BoardSprites[greenDeployX][greenDeployY] == null || (GameEngine.BoardSprites[greenDeployX][greenDeployY] != null && GameEngine.BoardSprites[greenDeployX][greenDeployY].owner == green)) {
                        new MGInfantry(GameView.theContext, greenDeployX, greenDeployY, playing);
                        playing.foodStorage -= MGInfantry.foodPrice;
                        playing.ironStorage -= MGInfantry.ironPrice;
                        playing.oilStorage -= MGInfantry.oilPrice;
                    }
                }
                if ((playing == red)) {
                    if (GameEngine.BoardSprites[redDeployX][redDeployY] == null || (GameEngine.BoardSprites[redDeployX][redDeployY] != null && GameEngine.BoardSprites[redDeployX][redDeployY].owner == red)) {
                        new MGInfantry(GameView.theContext, redDeployX, redDeployY, playing);
                        playing.foodStorage -= MGInfantry.foodPrice;
                        playing.ironStorage -= MGInfantry.ironPrice;
                        playing.oilStorage -= MGInfantry.oilPrice;
                    }
                }
            }
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastUnit = null;
            lastEnemyUnit = null;
            prevoiusMove = PREV_MOVE.NONE;
        }
        //switch loadout to artillery
        if (x / squareLength == 13 && y / squareLength == 10 && showMarket) {
                if (playing == green) {
                    if (GameEngine.BoardSprites[greenDeployX][greenDeployY] == null || (GameEngine.BoardSprites[greenDeployX][greenDeployY] != null && GameEngine.BoardSprites[greenDeployX][greenDeployY].owner == green)) {
                        loadoutMenu = true;
                        loadoutMenuUnit = "Artillery";
                    }
                }
                if ((playing == red)) {
                    if (GameEngine.BoardSprites[redDeployX][redDeployY] == null || (GameEngine.BoardSprites[redDeployX][redDeployY] != null && GameEngine.BoardSprites[redDeployX][redDeployY].owner == red)) {
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
        if (x / squareLength == 7 && y / squareLength == 10 && showFactory) {
                if (playing == green) {
                    if (GameEngine.BoardSprites[greenDeployX][greenDeployY] == null || (GameEngine.BoardSprites[greenDeployX][greenDeployY] != null && GameEngine.BoardSprites[greenDeployX][greenDeployY].owner == green)) {
                        loadoutMenu = true;
                        loadoutMenuUnit = "Armor";
                    }
                }
                if ((playing == red)) {
                    if (GameEngine.BoardSprites[redDeployX][redDeployY] == null || (GameEngine.BoardSprites[redDeployX][redDeployY] != null && GameEngine.BoardSprites[redDeployX][redDeployY].owner == red)) {
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

        //playing.addPlane(new ReconPlane(GameView.theContext, playing));
        //buys new fighter
        if (x / squareLength == 9 && y / squareLength == 10 && showFactory) {
            if (playing.foodStorage >= ReconPlane.foodPrice && playing.ironStorage >= ReconPlane.ironPrice && playing.oilStorage >= ReconPlane.oilPrice) {
                if (playing == green && green.hangarHasEmptySlots()) {
                    playing.addPlane(new ReconPlane(GameView.theContext, playing));
                    playing.foodStorage -= ReconPlane.foodPrice;
                    playing.ironStorage -= ReconPlane.ironPrice;
                    playing.oilStorage -= ReconPlane.oilPrice;
                }
                if ((playing == red) && red.hangarHasEmptySlots()) {
                    playing.addPlane(new ReconPlane(GameView.theContext, playing));
                    playing.foodStorage -= ReconPlane.foodPrice;
                    playing.ironStorage -= ReconPlane.ironPrice;
                    playing.oilStorage -= ReconPlane.oilPrice;
                }
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastUnit = null;
                lastEnemyUnit = null;
                prevoiusMove = PREV_MOVE.NONE;
            }
        }


        //buys new bomber
        if (x / squareLength == 11 && y / squareLength == 10 && showFactory) {
            if (playing.foodStorage >= Bomber.foodPrice && playing.ironStorage >= Bomber.ironPrice && playing.oilStorage >= Bomber.oilPrice) {
                if (playing == green && green.hangarHasEmptySlots()) {
                    playing.addPlane(new Bomber(GameView.theContext, playing));
                    playing.foodStorage -= Bomber.foodPrice;
                    playing.ironStorage -= Bomber.ironPrice;
                    playing.oilStorage -= Bomber.oilPrice;
                }
                if ((playing == red) && red.hangarHasEmptySlots()) {
                    playing.addPlane(new Bomber(GameView.theContext, playing));
                    playing.foodStorage -= Bomber.foodPrice;
                    playing.ironStorage -= Bomber.ironPrice;
                    playing.oilStorage -= Bomber.oilPrice;
                }
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastUnit = null;
                lastEnemyUnit = null;
                prevoiusMove = PREV_MOVE.NONE;
            }
        }

        //buys new Heavy Tank (UNUSED)
            /*if (x / squareLength == 11 && y / squareLength == 10 && showFactory) {
                if (playing.foodStorage >= HeavyTank.foodPrice && playing.ironStorage >= HeavyTank.ironPrice && playing.oilStorage >= HeavyTank.oilPrice) {
                    if (playing == green) {
                        if (GameEngine.BoardSprites[2][2] == null || (GameEngine.BoardSprites[2][2] != null && GameEngine.BoardSprites[2][2].owner == green)) {
                            new HeavyTank(GameView.theContext, 2, 2, playing);
                            playing.foodStorage -= HeavyTank.foodPrice;
                            playing.ironStorage -= HeavyTank.ironPrice;
                            playing.oilStorage -= HeavyTank.oilPrice;
                        }
                    }
                    if (playing == red) {
                        if (GameEngine.BoardSprites[12][6] == null || (GameEngine.BoardSprites[12][6] != null && GameEngine.BoardSprites[12][6].owner == red)) {
                            new HeavyTank(GameView.theContext, 12, 6, playing);
                            playing.foodStorage -= HeavyTank.foodPrice;
                            playing.ironStorage -= HeavyTank.ironPrice;
                            playing.oilStorage -= HeavyTank.oilPrice;
                        }
                    }
                }
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastUnit = null;
            } */

    }

    public  static void ProcessGroundTap(int x, int y) {
        x -= GameView.cameraX;
        y -= GameView.cameraY;

        //in case of small maps, dont crash if user taps on empty square
        if (x/squareLength >= width || y/squareLength >= heigth) {
            return;
        }
        // If tap is outside the grid, do nothing. Not used anymore because this would mess up loading games in onResume function.
        //if (x / squareLength >= width || y / squareLength >= heigth) {
        //    return;
        //}

        //if user taps on empty square with no units selected, do nothing
        if (selected == null && BoardSprites[x / squareLength][y / squareLength] == null) {
            return;
        }

        if (smokeFireActive && theUnit != null && theUnit.unitType.equals("Artillery")) {
            int distance = getSquareDistance(x / squareLength, theUnit.coordinates[0], y / squareLength, theUnit.coordinates[1]);
            if (distance > theUnit.attack2Range || distance == 0) {    //cancel action
                playing.oilStorage += SMOKE_PRICE_OIL;
                theUnit.specialIsActivated = false;
                message = "Smoke fire cancelled";
                showFactory = false;
                showMarket = false;

            } else {
                message = "Smoke fired";
                showFactory = false;
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
        if (BoardSprites[x / squareLength][y / squareLength] != null) {
            if (BoardSprites[x / squareLength][y / squareLength].owner == playing) {
                theUnit = BoardSprites[x / squareLength][y / squareLength];
                selected = new SelectedUnit(GameView.theContext, x, y, theUnit.owner, theUnit.unitType);
                message = theUnit.unitType + " at " + ((x / squareLength) + c) + ", " + ((y / squareLength) + c);
                showMarket = false;
                showFactory = false;
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastUnit = null;
                lastEnemyUnit = null;
                prevoiusMove = PREV_MOVE.NONE;
                return;
            } else if (BoardSprites[x / squareLength][y / squareLength].owner != playing && getFogOfWar(playing)[x / squareLength][y / squareLength]) {
                enemyTappedUnit = BoardSprites[x / squareLength][y / squareLength];
                enemySelected = new SelectedUnit(GameView.theContext, x, y, BoardSprites[x / squareLength][y / squareLength].owner, BoardSprites[x / squareLength][y / squareLength].unitType);
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastUnit = null;
                lastEnemyUnit = null;
                prevoiusMove = PREV_MOVE.NONE;
            }
        }

        //if user taps with unit selected on an empty square, move it TODO : make sure unit cannot move over another unit
        if (theUnit != null && BoardSprites[x / squareLength][y / squareLength] == null
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
        if (theUnit != null && BoardSprites[x / squareLength][y / squareLength] != null &&
                BoardSprites[x / squareLength][y / squareLength].owner != theUnit.owner &&
                (theUnit.attack1Range >= getSquareDistance           //and check if unit is in range of first (stronger) attack.
                        (getCoordinates(theUnit)[0], x / squareLength,
                                getCoordinates(theUnit)[1], y / squareLength))
                && theUnit.hasAttack == true) {
            attackUnit(theUnit, BoardSprites[x / squareLength][y / squareLength]);
            //DamageUnit(theUnit.attack1, BoardSprites[x / squareLength][y / squareLength], x / squareLength, y / squareLength); //and then move the unit, and un-select it.

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
        if (theUnit != null && BoardSprites[x / squareLength][y / squareLength] != null &&
                BoardSprites[x / squareLength][y / squareLength].owner != theUnit.owner &&
                (theUnit.attack2Range >= getSquareDistance           //and check if unit is in range of second (weaker) attack.
                        (getCoordinates(theUnit)[0], x / squareLength,
                                getCoordinates(theUnit)[1], y / squareLength))
                && theUnit.hasAttack == true) {
            attackUnit(theUnit, BoardSprites[x / squareLength][y / squareLength]);
            //DamageUnit(theUnit.attack2, BoardSprites[x / squareLength][y / squareLength], x / squareLength, y / squareLength);
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
        if (selected != null) {
            //if (!FullscreenActivity.hasScrolled) {
            unselectFriendly();
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
        for (int i = 0; i < BoardSprites.length; i++) {
            for (int j = 0; j < BoardSprites[i].length; j++) {
                if (BoardSprites[i][j] == u) {
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
        for (int i = 0; i < BoardSprites.length; i++) {
            for (int j = 0; j < BoardSprites[i].length; j++) {
                if (BoardSprites[i][j] == u) {
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
        BoardSprites[x][y] = u;
        if (playing.isHuman) {
            selected = new SelectedUnit(GameView.theContext, x * squareLength, y * squareLength, theUnit.owner, theUnit.unitType);
        }
        BoardSprites[a][b] = null;
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

        if (damage <= BoardSprites[x][y].defence) {
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

        BoardSprites[x][y].HP = BoardSprites[x][y].HP - (damage - BoardSprites[x][y].defence);
        //if unit has less than 1HP, remove it
        showMarket = false;
        if (messageCode == 0) {
            message = u.unitType + " at " + (x + c) + ", " + (y + c) + " damaged by " + (damage - BoardSprites[x][y].defence);
        } else if (messageCode == 1) {
            message = "Damage given: " + (damage - BoardSprites[x][y].defence);
        } else if (messageCode == 2) {
            message += " received: " + (damage - BoardSprites[x][y].defence);
        }
        if (BoardSprites[x][y].HP <= 0) {
            BoardSprites[x][y] = null;
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

    public static void unselectFriendlyPlanes() {
        if (selectedPlane == null) return;
        selectedPlane.unselect();
        selectedPlane = null;
    }

    public static void unselectEnemyPlanes() {
        if (selectedEnemyPlane == null) return;
        selectedEnemyPlane.unselect();
        selectedEnemyPlane = null;
    }

    public static void unselectAllPlanes() {
        unselectFriendlyPlanes();
        unselectEnemyPlanes();

    }
    //deploys next unit in queue
    public static void nextInQueue(){
        if (GameEngine.queue.length == 0) {
            return;
        }
        if (playing == green) {
            BoardSprites[greenDeployX][greenDeployY] = GameEngine.queue[0];
            GameEngine.queue[0].coordinates[0] = greenDeployX;
            GameEngine.queue[0].coordinates[1] = greenDeployY;
        }
        if (playing == red) {
            BoardSprites[redDeployX][redDeployY] = GameEngine.queue[0];
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
            for (int i = 0; i < GameEngine.BoardSprites.length; i++) { // TODO : optimize this
                for (int j = 0; j < GameEngine.BoardSprites[i].length; j++) {
                    if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != GameEngine.playing &&
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
    //returns the SquareCoordinates of coordinates. Every Square coordinate represents a square on the board, since every square is squareLength  pixels the coordinates have to be divided by squareLength .
    public static int[] getSquareCoordinates (int a, int b) {
        int[] toReturn = new int[] { a / squareLength , b / squareLength };
        return toReturn;
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

        if (playing == green) {
            for (int i = 0; i < playing.hangar.length; i++) {
                if (playing.hangar[i] != null) {
                    playing.hangar[i].HP += playing.hangar[i].healingRate;

                    if (playing.hangar[i].HP > playing.hangar[i].maxHP) {
                        playing.hangar[i].HP = playing.hangar[i].maxHP;
                    }
                }
            }

            for (int i = 0; i < airLinesCount; i++) {
                if (planeLines[i][0] != null) {
                    planeLines[i][0].takeGroundDamage(i);
                }
                if (planeLines[i][1] != null) {
                    planeLines[i][1].attack(i,0, planeLines[i][1].airAttack,  planeLines[i][1].groundAttack);
                    if (planeLines[i][0] != null) {
                        planeLines[i][1].attack(i,1, planeLines[i][0].airAttack,  planeLines[i][0].groundAttack);
                    }
                    if (planeLines[i][1] != null) {
                        fogOfWarIsRevealedForRed[i] = true;
                        planeLines[i][1].groundPlane();
                        planeLines[i][1] = null;
                    }
                }
            }
            for (int i = 0; i < fogOfWarIsRevealedForGreen.length; i++) {
                fogOfWarIsRevealedForGreen[i] = false;
            }

            playing = red;
            GameView.targetCameraX =  (15 - width)* squareLength;
            GameView.targetCameraY =  (9 - heigth)* squareLength;
        } else if (playing == red) {
            for (int i = 0; i < playing.hangar.length; i++) {
                if (playing.hangar[i] != null) {
                    playing.hangar[i].HP += playing.hangar[i].healingRate;

                    if (playing.hangar[i].HP > playing.hangar[i].maxHP) {
                        playing.hangar[i].HP = playing.hangar[i].maxHP;
                    }
                }
            }

            for (int i = 0; i < airLinesCount; i++) {
                if (planeLines[i][1] != null) {
                    planeLines[i][1].takeGroundDamage(i);
                }
                if (planeLines[i][0] != null) {
                    planeLines[i][0].attack(i,1, planeLines[i][0].airAttack,  planeLines[i][0].groundAttack);
                    if (planeLines[i][1] != null) {
                        planeLines[i][0].attack(i,0, planeLines[i][1].airAttack,  planeLines[i][1].groundAttack);
                    }
                    if (planeLines[i][0] != null) {
                        fogOfWarIsRevealedForGreen[i] = true;
                        planeLines[i][0].groundPlane();
                        planeLines[i][0] = null;
                    }
                }
            }
            for (int i = 0; i < fogOfWarIsRevealedForGreen.length; i++) {
                fogOfWarIsRevealedForRed[i] = false;
            }
            playing = green;
            GameView.targetCameraX = 0;
            GameView.targetCameraY = 0;
        }
        // gives movement and attack to next player's units
        for (int i = 0; i < BoardSprites.length; i++) {
            for (int j = 0; j < BoardSprites[i].length; j++) {
                if (BoardSprites[i][j] != null && BoardSprites[i][j].owner == playing) {
                    BoardSprites[i][j].hasMove = true;
                    BoardSprites[i][j].hasAttack = true;
                    BoardSprites[i][j].specialIsActivated = false;
                    BoardSprites[i][j].brightenIcon();
                }
                if (BoardSprites[i][j] != null && BoardSprites[i][j].owner != playing) {
                    if (BoardSprites[i][j].hasAttack && BoardSprites[i][j].hasMove) {
                        if (BoardSprites[i][j].unitType.equals("Armor")) {
                            BoardSprites[i][j].HP += Armor.healedBy;
                        }
                        else if (BoardSprites[i][j].unitType.equals("Cavalry")) {
                            BoardSprites[i][j].HP += Cavalry.healedBy;
                        }
                        else if (BoardSprites[i][j].unitType.equals("Infantry")) {
                            BoardSprites[i][j].HP += Infantry.healedBy;
                        }
                        else if (BoardSprites[i][j].unitType.equals("Artillery")) {
                            BoardSprites[i][j].HP += Artillery.healedBy;
                        }
                        else if (BoardSprites[i][j].unitType.equals("Anti air")) {
                            BoardSprites[i][j].HP += MGInfantry.healedBy;
                        }
                        else if (BoardSprites[i][j].unitType.equals("Heavy Tank")) {
                            BoardSprites[i][j].HP += HeavyTank.healedBy;
                        }
                        BoardSprites[i][j].hasAttack = false;
                        BoardSprites[i][j].hasMove = false;
                    }
                    if (BoardSprites[i][j].HP > BoardSprites[i][j].maxHP) {
                        BoardSprites[i][j].HP = BoardSprites[i][j].maxHP;
                    }
                }
            }
        }
        //gives harvested resources to next player
        for (int i = 0; i < BoardResources.length; i++) {
            for (int j = 0; j < BoardResources[i].length; j++) {
                //this long if statement checks if BoardResource[i][j] should yield a resource to the player.
                if (BoardResources[i][j]!= null &&
                        BoardSprites[BoardResources[i][j].collectorCoordinates[0]][BoardResources[i][j].collectorCoordinates[1]] != null &&
                        BoardSprites[BoardResources[i][j].collectorCoordinates[0]][BoardResources[i][j].collectorCoordinates[1]].owner == playing) {
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
        showFactory = false;
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
                        BoardSprites[BoardResources[i][j].collectorCoordinates[0]][BoardResources[i][j].collectorCoordinates[1]] != null &&
                        BoardSprites[BoardResources[i][j].collectorCoordinates[0]][BoardResources[i][j].collectorCoordinates[1]].owner == playing) {
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

    public static boolean[][] getFogOfWar(Player player) {
        boolean[][] tile_is_visible = new boolean[width][heigth];

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
            for (int i = 0; i < BoardSprites.length; i++) {
                for (int j = 0; j < BoardSprites[i].length; j++) {
                    if (BoardSprites[i][j] != null && BoardSprites[i][j].owner.color.equals("green")) {
                        for (int a = 0; a < BoardSprites.length; a++) {
                            for (int b = 0; b < BoardSprites[a].length; b++) {
                                if (!tile_is_visible[a][b] && BoardSprites[i][j].getDistanceToPoint(a,b) <= BoardSprites[i][j].getVisibilityRange()
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
            for (int i = 0; i < BoardSprites.length; i++) {
                for (int j = 0; j < BoardSprites[i].length; j++) {
                    if (BoardSprites[i][j] != null && BoardSprites[i][j].owner.color.equals("red")) {
                        for (int a = 0; a < BoardSprites.length; a++) {
                            for (int b = 0; b < BoardSprites[a].length; b++) {
                                if (!tile_is_visible[a][b] && BoardSprites[i][j].getDistanceToPoint(a,b) <= BoardSprites[i][j].getVisibilityRange()
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
                    for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                        for (int j = a * 3; j < (a+1)*3; j++) {
                            tile_is_visible[i][j] = true;
                        }
                    }
                }
            }
            /**
            if (fogOfWarIsRevealedForGreen[0]) {
                for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                    for (int j = 0; j < 4; j++) {
                        tile_is_visible[i][j] = true;
                    }
                }
            }
            if (fogOfWarIsRevealedForGreen[1]) {
                for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                    for (int j = 3; j < 6; j++) {
                        tile_is_visible[i][j] = true;
                    }
                }
            }
            if (fogOfWarIsRevealedForGreen[2]) {
                for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                    for (int j = 6; j < 9; j++) {
                        tile_is_visible[i][j] = true;
                    }
                }
            }
             */
        }
        if (player == red) {

            for (int a = 0; a < fogOfWarIsRevealedForRed.length; a++) {
                if (fogOfWarIsRevealedForRed[a]) {
                    for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                        for (int j = a * 3; j < (a+1)*3; j++) {
                            tile_is_visible[i][j] = true;
                        }
                    }
                }
            }
            /**
            if (fogOfWarIsRevealedForRed[0]) {
                for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                    for (int j = 0; j < 4; j++) {
                        tile_is_visible[i][j] = true;
                    }
                }
            }
            if (fogOfWarIsRevealedForRed[1]) {
                for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                    for (int j = 3; j < 6; j++) {
                        tile_is_visible[i][j] = true;
                    }
                }
            }
            if (fogOfWarIsRevealedForRed[2]) {
                for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                    for (int j = 6; j < 9; j++) {
                        tile_is_visible[i][j] = true;
                    }
                }
            }
             */
        }

        return tile_is_visible;
    }

    //helper recursive function
    public static void getReachableTilesRecursive(int xCoord, int yCoord, int currentDistance, boolean[][] map) {
        if (currentDistance < 0
                || xCoord < 0 || yCoord < 0 || xCoord > width - 1 || yCoord > heigth - 1
                || (BoardSprites[xCoord][yCoord] != null && BoardSprites[xCoord][yCoord].owner != playing)) {
            return;
        }
        if (BoardSprites[xCoord][yCoord] == null) {
            map[xCoord][yCoord] = true;
        }
        getReachableTilesRecursive(xCoord-1,yCoord,currentDistance - 1, map);
        getReachableTilesRecursive(xCoord,yCoord-1,currentDistance - 1, map);
        getReachableTilesRecursive(xCoord+1,yCoord,currentDistance - 1, map);
        getReachableTilesRecursive(xCoord,yCoord+1,currentDistance - 1, map);
    }

    //gets all tiles that are reachable by a unit without skipping over enemy units
    public static boolean[][] getReachableTiles(int xCoord, int yCoord, int range) {
        boolean[][] tiles = new boolean[width][heigth];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = false;
            }
        }

        getReachableTilesRecursive(xCoord, yCoord, range, tiles);
        tiles[xCoord][yCoord] = false;
        return tiles;
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

}
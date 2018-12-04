package com.example.filip.finalproject;

import android.graphics.Bitmap;
import android.util.EventLog;
import android.graphics.Canvas;
import java.lang.Math;

// Class that will run the game and manage all the events.
public class GameEngine {

    // Place to store the coordinates of last tap. lastTap[0] is x, lastTap[1] is y.
    static int[] lastTap = new int[]{(int) (16000  * FullscreenActivity.scaleFactor), (int) (16000  * FullscreenActivity.scaleFactor)};

    public static int squareLength = (int) (128  * FullscreenActivity.scaleFactor);
    public Bitmap image; // Image of the grid
    public static Units theUnit = null; // Selected unit, unlike the selected unit in GameView class this unit is the actual selected unit.
    public static SelectedUnit selected = null; // Selected unit, reference is not the same as the (selected) Unit itself.
    public static Units enemyTappedUnit = null; //same as above, but of opponent
    public static SelectedUnit enemySelected = null; // same as above, but for opponent
    public static Units[][] BoardSprites = new Units[15][9]; //A 2D array of Units that stores the units for game engine and data processing, unlike GameView's Units[] this isn't involved in drawing units.
    public static Player green; //stores the reference to a player that is in charge of Green units
    public static Player red;//stores the reference to a player that is in charge of Red units
    public static Player playing = null; //player that makes moves
    public static Resources[][] BoardResources = new Resources[15][9];
    public static boolean showSupport = false;
    public static boolean showMarket = false;
    public static String message = "";
    public static int c = 0;
    public static int[] lastCoordinates = new int[2];


    public static int[] lastAddedResounces = new int[3]; //memorizes last added resources, to display next to storage

    //Constructor that creates the unit and it's image, doesn't set it's coordinates. Mostly used by onDraw function in GameView
    public GameEngine(Bitmap bmp) {
        image = bmp;
    }

    // no idea what this is for, but code doesn't work without this for some reason.
    public void update() {
        return;
    }

    //draws the board when grid.draw(canvas) is called in GameView function.
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, 0.0f, 0.0f, null);
    }
    /*
    A method that will process what happens with user's input (click/tap).
     */
    public static void tapProcessor (int x, int y) {

        //This makes sure that unit gets tapped only once.
        if (selected != null && x/squareLength  == selected.coordinates[0] && y / squareLength  == selected.coordinates[1]) {
            return;
        }

        //un-selects the unit if the button is pressed.
        if (x / squareLength  == 16 && y / squareLength  == 1 && (selected != null || enemySelected  != null)) {
            selected = null;
            theUnit = null;
            enemySelected = null;
            enemyTappedUnit = null;
            lastTap[0] = x; //sets the lastTap coordinates
            lastTap[1] = y;
            return;
        }

        //heal the unit if the button is pressed
        if (x / squareLength  == 18 && y / squareLength  == 1 && selected != null) {
            if (theUnit.HP != theUnit.maxHP) {
                if (theUnit.hasAttack && theUnit.hasMove) {
                    theUnit.HP++;
                    if (theUnit.unitType.equals("Armor")) {
                        theUnit.HP++;
                    }
                    theUnit.hasAttack = false;
                    theUnit.hasMove = false;
                }
                if (theUnit.HP > theUnit.maxHP) {
                    theUnit.HP = theUnit.maxHP;
                }
            }
            lastTap[0] = x; //sets the lastTap coordinates
            lastTap[1] = y;
            selected = null;
            theUnit = null;
        }

        //switches active player if the button is pressed.
        if (x / squareLength  == 16 && y / squareLength  == 3  && ((lastTap[0] / squareLength  != x / squareLength ) || (lastTap[1] / squareLength  != y / squareLength ))) {
            switchPlayer();
            selected = null;
            theUnit = null;
            enemySelected = null;
            enemyTappedUnit = null;
            lastTap[0] = x; //sets the lastTap coordinates
            lastTap[1] = y;
            return;
        }

        //deploy
        if (selected == null && (((x / squareLength  == 2 && y / squareLength  == 2)  && (playing == green && BoardSprites[2][2] == null)) ||
                (((x / squareLength  == 12 && y / squareLength  == 6)  && (playing == red && BoardSprites[12][6] == null))))
                && ((lastTap[0] / squareLength  != x / squareLength ) || (lastTap[1] / squareLength  != y / squareLength ))) {
            GameEngine.showMarket =  !GameEngine.showMarket;
            if (showSupport == true) {
                showSupport = false;
            }
            lastTap[0] = x; //sets the lastTap coordinates
            lastTap[1] = y;
            return;
        }

        //Undo
        if (x / squareLength  == 18 && y / squareLength  == 3  && ((lastTap[0] / squareLength  != x / squareLength ) || (lastTap[1] / squareLength  != y / squareLength )) && theUnit != null && lastCoordinates[0] != 125 && lastCoordinates[1] != 125) {
            BoardSprites[theUnit.coordinates[0]][theUnit.coordinates[1]] = null;
            theUnit.coordinates[0] = lastCoordinates[0];
            theUnit.coordinates[1] = lastCoordinates[1];
            BoardSprites[lastCoordinates[0]][lastCoordinates[1]] = theUnit;
            theUnit.brightenIcon();
            theUnit.hasMove = true;
            theUnit = null;
            selected = null;
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastTap[0] = x; //sets the lastTap coordinates
            lastTap[1] = y;
            return;
        }

        //upgrade
        if (x / squareLength  == 16 && y / squareLength  == 5 && theUnit != null) {
            if (theUnit.unitType == "Infantry") {
                if (theUnit.defence == Infantry.GreenDefence) {
                    if (playing.ironStorage > 0) {
                        theUnit.defence++;
                        playing.ironStorage -= 1;
                    }
                }
            }
            if (theUnit.unitType == "Cavalry") {
                if (theUnit.defence == Cavalry.GreenDefence) {
                    if (playing.ironStorage > 1) {
                        theUnit.defence++;
                        playing.ironStorage -= 2;
                    }
                }
            }
            if (theUnit.unitType == "Artillery") {
                if (theUnit.defence == Artillery.GreenDefence) {
                    if (playing.ironStorage > 1) {
                        theUnit.defence++;
                        playing.ironStorage -= 2;
                    }
                }
            }
            if (theUnit.unitType == "Armor") {
                if (theUnit.defence == Armor.GreenDefence) {
                    if (playing.ironStorage > 4) {
                        theUnit.defence++;
                        playing.ironStorage -= 5;
                    }
                }
            }
            if (theUnit.unitType == "Headquarters") {
                if (theUnit.defence == Headquaters.GreenDefence) {
                    if (playing.ironStorage > 3) {
                        theUnit.defence++;
                        playing.ironStorage -= 4;
                    }
                }
            }
            lastTap[0] = x; //sets the lastTap coordinates
            lastTap[1] = y;
        }

        //switches market visibility if the button is pressed.
        if (x / squareLength  == 5 && y / squareLength  == 10  && ((lastTap[0] / squareLength  != x / squareLength ) || (lastTap[1] / squareLength  != y / squareLength ))) {
            GameEngine.showMarket =  !GameEngine.showMarket;
            if (showSupport == true) {
                showSupport = false;
            }
            lastTap[0] = x; //sets the lastTap coordinates
            lastTap[1] = y;
            return;
        }
        //switches support visibility if the button is pressed. TODO : Add support abilities in game
        if (x / squareLength  == 12 && y / squareLength  == 10  && ((lastTap[0] / squareLength  != x / squareLength ) || (lastTap[1] / squareLength  != y / squareLength ))) {
            GameEngine.showMarket =  !GameEngine.showMarket;
            if (showSupport == true) {
                showSupport = false;
            }
            lastTap[0] = x; //sets the lastTap coordinates
            lastTap[1] = y;
            return;
        }

        //buys new infantry for two food
        if (x / squareLength  == 7 && y / squareLength  == 10 && showMarket &&
                ((lastTap[0] / squareLength  != x / squareLength ) || (lastTap[1] / squareLength  != y / squareLength ))) {
            if (playing.foodStorage > 1) {
                if (playing == green && BoardSprites[2][2] == null) {
                    new Infantry(GameView.theContext, 2, 2, playing);
                    playing.foodStorage -=2;
                }
                if (playing == red && BoardSprites[12][6] == null) {
                    new Infantry(GameView.theContext, 12, 6, playing);
                    playing.foodStorage -=2;
                }
            }
            if (playing.foodStorage < 2) {
                showMarket = false;
            }
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastTap[0] = x; //sets the lastTap coordinates
            lastTap[1] = y;
        }
        //buys new cavalry for three food
        if (x / squareLength  == 9 && y / squareLength  == 10 && showMarket &&
                ((lastTap[0] / squareLength  != x / squareLength ) || (lastTap[1] / squareLength  != y / squareLength ))) {
            if (playing.foodStorage > 2) {
                if (playing == green && BoardSprites[2][2] == null) {
                    new Cavalry(GameView.theContext, 2, 2, playing);
                    playing.foodStorage -=3;
                }
                if ((playing == red && BoardSprites[12][6] == null)) {
                    new Cavalry(GameView.theContext, 12, 6, playing);
                    playing.foodStorage -=3;
                }

            }
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastTap[0] = x; //sets the lastTap coordinates
            lastTap[1] = y;
        }
        //buys new artillery for five food and four iron
        if (x / squareLength  == 11 && y / squareLength  == 10 && showMarket &&
                ((lastTap[0] / squareLength  != x / squareLength ) || (lastTap[1] / squareLength  != y / squareLength ))) {
            if (playing.foodStorage > 4 && playing.ironStorage > 3) {
                if (playing == green && BoardSprites[2][2] == null) {
                    new Artillery(GameView.theContext, 2, 2, playing);
                    playing.foodStorage -=5;
                    playing.ironStorage -=4;
                }
                if ((playing == red && BoardSprites[12][6] == null)) {
                    new Artillery(GameView.theContext, 12, 6, playing);
                    playing.foodStorage -=5;
                    playing.ironStorage -=4;
                }

            }
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastTap[0] = x; //sets the lastTap coordinates
            lastTap[1] = y;
        }

        //buys new armor for two food, four iron and twenty five oil
        if (x / squareLength  == 13 && y / squareLength  == 10 && showMarket &&
                ((lastTap[0] / squareLength  != x / squareLength ) || (lastTap[1] / squareLength  != y / squareLength ))) {
            if (playing.foodStorage > 1 && playing.ironStorage > 3 && playing.oilStorage > 19) {
                if (playing == green && BoardSprites[2][2] == null) {
                    new Armor(GameView.theContext, 2, 2, playing);
                    playing.foodStorage -=2;
                    playing.ironStorage -=4;
                    playing.oilStorage -=20;
                }
                if ((playing == red && BoardSprites[12][6] == null)) {
                    new Armor(GameView.theContext, 12, 6, playing);
                    playing.foodStorage -=2;
                    playing.ironStorage -=4;
                    playing.oilStorage -=20;
                }

            }
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastTap[0] = x; //sets the lastTap coordinates
            lastTap[1] = y;
        }

        // If tap is outside the grid, do nothing.
        if (x / squareLength  >= 15 || y / squareLength  >= 9) {
            return;
        }

        //if user taps on empty square with no units selected, do nothing
        if (selected == null && BoardSprites[x / squareLength ][y / squareLength ] == null) {
            return;
        }

        //If user taps on a unit, select it, or display info on enemy unit.
        if (BoardSprites[x / squareLength ][y / squareLength ] != null && ((lastTap[0] / squareLength  != x / squareLength ) || (lastTap[1] / squareLength  != y / squareLength ))) {
            if (BoardSprites[x / squareLength ][y / squareLength ].owner == playing) {
                theUnit = BoardSprites[x / squareLength ][y / squareLength ];
                selected = new SelectedUnit(GameView.theContext, x, y, theUnit.owner, theUnit.unitType);
                message = theUnit.unitType + " at " + ((x/squareLength ) + c) + ", " + ((y/squareLength ) + c);
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastTap[0] = x; //sets the lastTap coordinates
                lastTap[1] = y;
                return;
            }
            else if (BoardSprites[x / squareLength ][y / squareLength ].owner != playing) {
                enemyTappedUnit = BoardSprites[x / squareLength ][y / squareLength ];
                enemySelected = new SelectedUnit(GameView.theContext, x, y, BoardSprites[x / squareLength ][y / squareLength ].owner, BoardSprites[x / squareLength ][y / squareLength ].unitType);
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastTap[0] = x; //sets the lastTap coordinates
                lastTap[1] = y;
            }
        }

        //if user taps with unit selected on an empty square, move it TODO : make sure unit cannot move over another unit
        if (theUnit != null && BoardSprites[x / squareLength ][y / squareLength ] == null
                && (theUnit.movement >= getSquareDistance           //also check if unit is in range.
                        (getCoordinates(theUnit)[0], x / squareLength ,
                                getCoordinates(theUnit)[1], y / squareLength ))
                && theUnit.hasMove == true
                && ((lastTap[0] / squareLength  != x / squareLength ) || (lastTap[1] / squareLength  != y / squareLength ))) {
            moveTo(theUnit, x / squareLength , y / squareLength ); //and then move the unit, and un-select it.
            //if unit has attack, don't un-select it yet. TODO : if no units are in range, un-select it because it cannot attack anyway
            if (theUnit.hasAttack) {
                theUnit.hasMove = false;
                lastTap[0] = x; //sets the lastTap coordinates
                lastTap[1] = y;
                return;
            }
            //if units doesn't have an attack, un-select it
            if (!(theUnit.hasAttack)) {
                theUnit.hasMove = false;
                lastTap[0] = x; //sets the lastTap coordinates
                lastTap[1] = y;
                checkAction(theUnit);
                selected = null;
                theUnit = null;
                return;
            }
        }


        lastTap[0] = x; //sets the lastTap coordinates
        lastTap[1] = y;


        //if player taps with unit selected on an opponent's unit, attack it
        if (theUnit != null && BoardSprites[x / squareLength ][y / squareLength ] != null &&
                BoardSprites[x / squareLength ][y / squareLength ].owner != theUnit.owner &&
                (theUnit.attack1Range >= getSquareDistance           //and check if unit is in range of first (stronger) attack.
                        (getCoordinates(theUnit)[0], x / squareLength ,
                                getCoordinates(theUnit)[1], y / squareLength ))
                && theUnit.hasAttack == true) {
            DamageUnit(theUnit.attack1, BoardSprites[x / squareLength ][y / squareLength ], x / squareLength , y / squareLength ); //and then move the unit, and un-select it.
            //if unit has a move, don't un-select it yet.
            if (theUnit.hasMove) {
                theUnit.hasAttack = false;
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastTap[0] = x; //sets the lastTap coordinates
                lastTap[1] = y;
                return;
            }
            //if units doesn't have a move, un-select it
            if (!theUnit.hasMove) {
                theUnit.hasAttack = false;
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                checkAction(theUnit);
                selected = null;
                theUnit = null;
                lastTap[0] = x; //sets the lastTap coordinates
                lastTap[1] = y;
                return;
            }
        }

        //if user taps with unit selected on an opponent's unit, attack it
        if (theUnit != null && BoardSprites[x / squareLength ][y / squareLength ] != null &&
                BoardSprites[x / squareLength ][y / squareLength ].owner != theUnit.owner &&
                (theUnit.attack2Range >= getSquareDistance           //and check if unit is in range of second (weaker) attack.
                        (getCoordinates(theUnit)[0], x / squareLength ,
                                getCoordinates(theUnit)[1], y / squareLength ))
                && theUnit.hasAttack == true) {
            DamageUnit(theUnit.attack2, BoardSprites[x / squareLength ][y / squareLength ], x / squareLength , y / squareLength );
            if (theUnit.hasMove) {
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                theUnit.hasAttack = false;
                lastTap[0] = x; //sets the lastTap coordinates
                lastTap[1] = y;
                checkAction(theUnit);
                return;
            }
            if (!theUnit.hasMove) {
                theUnit.hasAttack = false;
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastTap[0] = x; //sets the lastTap coordinates
                lastTap[1] = y;
                checkAction(theUnit);
                selected = null;
                theUnit = null;
                return;
            }
        }

        //If user taps on a square that is out of range while some unit is selected, do nothing (this could be changed later)
        if (selected != null) {
            return;
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
        lastCoordinates[0] = selected.coordinates[0];
        lastCoordinates[1] = selected.coordinates[1];
        u.moveTo(x,y);
        BoardSprites[x][y] = u;
        selected = new SelectedUnit(GameView.theContext, x * squareLength , y * squareLength , theUnit.owner, theUnit.unitType);
        BoardSprites[a][b] = null;
        estimateResources();
        showMarket = false;
        message = u.unitType + " moved to " + (x + c) + ", " + (y + c);
        checkAction(u);
        checkIfAnyInRange(u);
    }

    //damages the unit at given coordinates
    public static void DamageUnit(int damage, Units u, int x, int y) {
        if (BoardSprites[x][y].defence >= damage) {
            showMarket = false;
            message = u.unitType + " at "+ (x + c) + ", " + (y + c) + " was not damaged";
        }   else {
            BoardSprites[x][y].HP = BoardSprites[x][y].HP - (damage - BoardSprites[x][y].defence);
            //if unit has less than 1HP, remove it
            showMarket = false;
            message = u.unitType + " at " + (x + c) + ", " + (y + c) + " damaged by " + (damage - BoardSprites[x][y].defence);
            if (BoardSprites[x][y].HP <= 0) {
                BoardSprites[x][y] = null;
                for (int i = 0; i < GameView.units.length; i++) {
                    if (GameView.units[i] == u) {
                        GameView.removeSprite(i);
                        enemyTappedUnit = null;
                        enemySelected = null;
                        showMarket = false;
                        message = u.unitType + " at " + (x + c) + ", " + (y + c) + " is destroyed";
                        if (u.unitType.equals("Headquarters")) {
                            message = "HQ has been destroyed, " + playing.color + " player wins!";
                            showMarket = false;
                        }
                        return;
                    }
                }
            }
        }
    }

    public static void checkAction(Units u) {
        if (u.hasMove == false && u.hasAttack == false) {
            u.darkenIcon();
        }
    }
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
        if (playing == green) {
            playing = red;
        } else if (playing == red) {
            playing = green;
        }
        // gives movement and attack to next player's units
        for (int i = 0; i < BoardSprites.length; i++) {
            for (int j = 0; j < BoardSprites[i].length; j++) {
                if (BoardSprites[i][j] != null && BoardSprites[i][j].owner == playing) {
                    BoardSprites[i][j].hasMove = true;
                    BoardSprites[i][j].hasAttack = true;
                    BoardSprites[i][j].brightenIcon();
                }
                if (BoardSprites[i][j] != null && BoardSprites[i][j].owner != playing) {
                    if (BoardSprites[i][j].hasAttack && BoardSprites[i][j].hasMove) {
                        BoardSprites[i][j].HP++;
                        if (BoardSprites[i][j].unitType.equals("Armor")) {
                            BoardSprites[i][j].HP++;
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
        GameEngine.estimateResources();
        showMarket = false;
        message = "";
    }
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
        GameEngine.lastAddedResounces[0] = addedfood;
        GameEngine.lastAddedResounces[1] = addediron;
        GameEngine.lastAddedResounces[2] = addedoil;
    }
}

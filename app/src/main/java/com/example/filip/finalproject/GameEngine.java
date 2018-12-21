package com.example.filip.finalproject;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;

// Class that will run the game and manage all the events.
public class GameEngine {


    public static int squareLength = (int) (128  * FullscreenActivity.scaleFactor); //scales square length, dependent on scale factor
    public Bitmap image; // Image of the grid
    public static Units lastUnit; //stores the last unit which made some action
    public static Units theUnit = null; // Selected unit, unlike the selected unit in GameView class this unit is the actual selected unit.
    public static SelectedUnit selected = null; // Selected unit, reference is not the same as the (selected) Unit itself.
    public static Units enemyTappedUnit = null; //same as above, but of opponent
    public static SelectedUnit enemySelected = null; // same as above, but for opponent
    public static Units[][] BoardSprites = new Units[15][9]; //A 2D array of Units that stores the units for game engine and data processing, unlike GameView's Units[] this isn't involved in drawing units.
    public static Player green; //stores the reference to a player that is in charge of Green units
    public static Player red;//stores the reference to a player that is in charge of Red units
    public static Player playing = null; //player that makes moves
    public static Resources[][] BoardResources = new Resources[15][9]; //a 2D array of Units that stores the resources in the game
    public static boolean showFactory = false; //shows factory units which can be purchased
    public static boolean showMarket = false; //shows market units which can be purchased
    public static String message = ""; //stores message to user
    public static int c = 0; //offset for x and y coordinate message, if set to 0 top left will be 0,0 if set to 1 top left will be 1,1
    public static int[] lastCoordinates = new int[2]; //coordinates of last action
    public static Units[] queue = new Units[0]; // stores all units that will be deployed


    public static int[] lastAddedResources = new int[3]; //memorizes last added resources, to display next to storage


    //restarts board
    public static void restart(){
        lastUnit = null; //stores the last unit which made some action
        theUnit = null; // Selected unit, unlike the selected unit in GameView class this unit is the actual selected unit.
        selected = null; // Selected unit, reference is not the same as the (selected) Unit itself.
        enemyTappedUnit = null; //same as above, but of opponent
        enemySelected = null; // same as above, but for opponent
        BoardSprites = new Units[15][9]; //A 2D array of Units that stores the units for game engine and data processing, unlike GameView's Units[] this isn't involved in drawing units.
        playing = null; //player that makes moves
        BoardResources = new Resources[15][9]; //a 2D array of Units that stores the resources in the game
        showFactory = false; //shows factory units which can be purchased
        showMarket = false; //shows market units which can be purchased
        message = ""; //stores message to user
        lastCoordinates = new int[2]; //coordinates of last action
        queue = new Units[0]; // stores all units that will be deployed
        AI.unitOrders = new String[0];
        AI.units = new Units[0];
        AI.turn = 0;
        AI.aggresionLevel = 3;
    }
    //Constructor that creates the unit and it's image, doesn't set it's coordinates. Mostly used by onDraw function in GameView
    public GameEngine(Bitmap bmp) {
        image = bmp;
    }

    // updates board
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
            unselectAll();
            return;
        }

        //heal the unit if the button is pressed
        if (x / squareLength  == 18 && y / squareLength  == 1 && selected != null) {
            if (theUnit.HP != theUnit.maxHP) {
                if (theUnit.hasAttack && theUnit.hasMove) {
                    theUnit.HP++;
                    if (theUnit.unitType.equals("Armor")) {
                        theUnit.HP += Armor.healedBy;
                    }
                    if (theUnit.unitType.equals("Cavalry")) {
                        theUnit.HP += Cavalry.healedBy;
                    }
                    if (theUnit.unitType.equals("Infantry")) {
                        theUnit.HP += Infantry.healedBy;
                    }
                    if (theUnit.unitType.equals("Artillery")) {
                        theUnit.HP += Artillery.healedBy;
                    }
                    if (theUnit.unitType.equals("Mech Infantry")) {
                        theUnit.HP += MechanizedInfantry.healedBy;
                    }
                    if (theUnit.unitType.equals("Heavy Tank")) {
                        theUnit.HP += HeavyTank.healedBy;
                    }
                    theUnit.hasAttack = false;
                    theUnit.hasMove = false;
                }
                if (theUnit.HP > theUnit.maxHP) {
                    theUnit.HP = theUnit.maxHP;
                }
            }
            unselectFriendly();
        }

        //switches active player if the button is pressed.
        if (x / squareLength  == 16 && y / squareLength  == 3) {
            if (queue.length != 0 && message != "Any undeployed unit will be removed, tap again to continue") {
                message = "Any undeployed unit will be removed, tap again to continue";
                showFactory = false;
                showMarket = false;
                unselectAll();
                return;
            }
            queue = new Units[0];
            switchPlayer();
            unselectAll();
            if (playing.isHuman == false) {
                AI.playTurn(red);
            }
            return;
        }

        //deploy
        if (selected == null && (((x / squareLength  == 2 && y / squareLength  == 2)  && (playing == green && BoardSprites[2][2] == null)) ||
                (((x / squareLength  == 12 && y / squareLength  == 6)  && (playing == red && BoardSprites[12][6] == null))))) {
            GameEngine.showMarket =  !GameEngine.showMarket;
            if (showFactory == true) {
                showFactory = false;
            }
            return;
        }

        //Undo
        if (x / squareLength  == 18 && y / squareLength  == 3 && theUnit != null && lastCoordinates[0] != 125 && lastCoordinates[1] != 125) {
            if (lastUnit == null) {
                BoardSprites[theUnit.coordinates[0]][theUnit.coordinates[1]] = null;
                theUnit.coordinates[0] = lastCoordinates[0];
                theUnit.coordinates[1] = lastCoordinates[1];
                BoardSprites[lastCoordinates[0]][lastCoordinates[1]] = theUnit;
                theUnit.brightenIcon();
                theUnit.hasMove = true;
                unselectFriendly();
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                return;
            }
            else {

                Units u = BoardSprites[lastCoordinates[0]][lastCoordinates[1]];
                for (int i = 0; i < GameView.units.length; i++) {
                    if (GameView.units[i] == u) {
                        GameView.removeSprite(i);
                    }
                }

                BoardSprites[lastCoordinates[0]][lastCoordinates[1]] = lastUnit;
                lastUnit.coordinates[0] = lastCoordinates[0];
                lastUnit.coordinates[1] = lastCoordinates[1];

                Units[] toReturn = new Units[GameView.units.length + 1];
                for (int k = 0; k < GameView.units.length; k++) {
                    toReturn[k] = GameView.units[k];
                }
                toReturn[toReturn.length - 1] = lastUnit;
                GameView.units = toReturn;

                theUnit.brightenIcon();
                theUnit.hasAttack = true;
                unselectAll();
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
            }
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
                    if (playing.ironStorage >= 0) {
                        theUnit.defence++;
                        playing.ironStorage -= 1;
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
            if (theUnit.unitType == "Mech Infantry") {
                if (theUnit.defence == MechanizedInfantry.GreenDefence) {
                    if (playing.ironStorage >= 1) {
                        theUnit.defence++;
                        playing.ironStorage -= 1;
                    }
                }
            }
        }


        //back to menu
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
        if (x / squareLength  == 5 && y / squareLength  == 10) {
            GameEngine.showMarket =  !GameEngine.showMarket;
            if (showFactory == true) {
                showFactory = false;
            }
            return;
        }
        //switches factory visibility if the button is pressed.
        if (x / squareLength  == 5 && y / squareLength  == 9) {
            GameEngine.showFactory =  !GameEngine.showFactory;
            if (showMarket == true) {
                showMarket = false;
            }
            return;
        }

        //buys new infantry
        if (x / squareLength  == 7 && y / squareLength  == 10 && showMarket) {
            if (playing.foodStorage >= Infantry.foodPrice) {
                if (playing == green) {
                    if (GameEngine.BoardSprites[2][2]== null || (GameEngine.BoardSprites[2][2]!= null && GameEngine.BoardSprites[2][2].owner == green)) {
                        new Infantry(GameView.theContext, 2, 2, playing);
                        playing.foodStorage -= Infantry.foodPrice;
                    }
                }
                if (playing == red) {
                    if (GameEngine.BoardSprites[2][2] == null || (GameEngine.BoardSprites[12][6]!= null && GameEngine.BoardSprites[12][6].owner == red)) {
                        new Infantry(GameView.theContext, 12, 6, playing);
                        playing.foodStorage -= Infantry.foodPrice;
                    }
                }
            }
            if (playing.foodStorage < Infantry.foodPrice) {
                showMarket = false;
            }
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastUnit = null;
        }
        //buys new cavalry
        if (x / squareLength  == 9 && y / squareLength  == 10 && showMarket) {
            if (playing.foodStorage >= Cavalry.foodPrice) {
                if (playing == green) {
                    if (GameEngine.BoardSprites[2][2] == null || (GameEngine.BoardSprites[2][2]!= null && GameEngine.BoardSprites[2][2].owner == green)) {
                        new Cavalry(GameView.theContext, 2, 2, playing);
                        playing.foodStorage -= Cavalry.foodPrice;
                    }
                }
                if ((playing == red)) {
                    if (GameEngine.BoardSprites[2][2] == null || (GameEngine.BoardSprites[12][6]!= null && GameEngine.BoardSprites[12][6].owner == red)) {
                        new Cavalry(GameView.theContext, 12, 6, playing);
                        playing.foodStorage -= Cavalry.foodPrice;
                    }
                }

            }
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastUnit = null;
        }
        //buys new artillery
        if (x / squareLength  == 11 && y / squareLength  == 10 && showMarket) {
            if (playing.foodStorage >= Artillery.foodPrice && playing.ironStorage >= Artillery.ironPrice) {
                if (playing == green) {
                    if (GameEngine.BoardSprites[2][2] == null || (GameEngine.BoardSprites[2][2]!= null && GameEngine.BoardSprites[2][2].owner == green)) {
                        new Artillery(GameView.theContext, 2, 2, playing);
                        playing.foodStorage -= Artillery.foodPrice;
                        playing.ironStorage -= Artillery.ironPrice;
                    }
                }
                if ((playing == red)) {
                    if (GameEngine.BoardSprites[2][2] == null || (GameEngine.BoardSprites[12][6]!= null && GameEngine.BoardSprites[12][6].owner == red)) {
                        new Artillery(GameView.theContext, 12, 6, playing);
                        playing.foodStorage -= Artillery.foodPrice;
                        playing.ironStorage -= Artillery.ironPrice;
                    }
                }

            }
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastUnit = null;
        }

        //buys new armor
        if (x / squareLength  == 9 && y / squareLength  == 10 && showFactory) {
            if (playing.foodStorage >= Armor.foodPrice && playing.ironStorage >= Armor.ironPrice && playing.oilStorage >= Armor.oilPrice) {
                if (playing == green) {
                    if (GameEngine.BoardSprites[2][2] == null || (GameEngine.BoardSprites[2][2]!= null && GameEngine.BoardSprites[2][2].owner == green)) {
                        new Armor(GameView.theContext, 2, 2, playing);
                        playing.foodStorage -= Armor.foodPrice;
                        playing.ironStorage -= Armor.ironPrice;
                        playing.oilStorage -= Armor.oilPrice;
                    }
                }
                if ((playing == red)) {
                    if (GameEngine.BoardSprites[2][2] == null || (GameEngine.BoardSprites[12][6]!= null && GameEngine.BoardSprites[12][6].owner == red)) {
                        new Armor(GameView.theContext, 12, 6, playing);
                        playing.foodStorage -= Armor.foodPrice;
                        playing.ironStorage -= Armor.ironPrice;
                        playing.oilStorage -= Armor.oilPrice;
                    }
                }

            }
            lastCoordinates[0] = 125;
            lastCoordinates[1] = 125;
            lastUnit = null;
        }

        //buys new MechInfantry
        if (x / squareLength  == 7 && y / squareLength  == 10 && showFactory) {
                if (playing.foodStorage >= MechanizedInfantry.foodPrice && playing.ironStorage >= MechanizedInfantry.ironPrice && playing.oilStorage >= MechanizedInfantry.oilPrice) {
                    if (playing == green) {
                        if (GameEngine.BoardSprites[2][2] == null || (GameEngine.BoardSprites[2][2]!= null && GameEngine.BoardSprites[2][2].owner == green)) {
                            new MechanizedInfantry(GameView.theContext, 2, 2, playing);
                            playing.foodStorage -= MechanizedInfantry.foodPrice;
                            playing.ironStorage -= MechanizedInfantry.ironPrice;
                            playing.oilStorage -= MechanizedInfantry.oilPrice;
                        }
                    }
                    if ((playing == red)) {
                        if (GameEngine.BoardSprites[2][2] == null || (GameEngine.BoardSprites[12][6]!= null && GameEngine.BoardSprites[12][6].owner == red)) {
                            new MechanizedInfantry(GameView.theContext, 12, 6, playing);
                            playing.foodStorage -= MechanizedInfantry.foodPrice;
                            playing.ironStorage -= MechanizedInfantry.ironPrice;
                            playing.oilStorage -= MechanizedInfantry.oilPrice;
                        }
                    }
                }
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastUnit = null;
        }
        //buys new Heavy Tank
        if (x / squareLength  == 11 && y / squareLength  == 10 && showFactory) {
            if (playing.foodStorage >= HeavyTank.foodPrice && playing.ironStorage >= HeavyTank.ironPrice && playing.oilStorage >= HeavyTank.oilPrice) {
                if (playing == green) {
                    if (GameEngine.BoardSprites[2][2] == null || (GameEngine.BoardSprites[2][2]!= null && GameEngine.BoardSprites[2][2].owner == green)) {
                        new HeavyTank(GameView.theContext, 2, 2, playing);
                        playing.foodStorage -= HeavyTank.foodPrice;
                        playing.ironStorage -= HeavyTank.ironPrice;
                        playing.oilStorage -= HeavyTank.oilPrice;
                    }
                }
                if (playing == red) {
                    if (GameEngine.BoardSprites[2][2] == null || (GameEngine.BoardSprites[12][6]!= null && GameEngine.BoardSprites[12][6].owner == red)) {
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
        if (BoardSprites[x / squareLength ][y / squareLength ] != null) {
            if (BoardSprites[x / squareLength ][y / squareLength ].owner == playing) {
                theUnit = BoardSprites[x / squareLength ][y / squareLength ];
                selected = new SelectedUnit(GameView.theContext, x, y, theUnit.owner, theUnit.unitType);
                message = theUnit.unitType + " at " + ((x/squareLength ) + c) + ", " + ((y/squareLength ) + c);
                showMarket = false;
                showFactory = false;
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastUnit = null;
                return;
            }
            else if (BoardSprites[x / squareLength ][y / squareLength ].owner != playing) {
                enemyTappedUnit = BoardSprites[x / squareLength ][y / squareLength ];
                enemySelected = new SelectedUnit(GameView.theContext, x, y, BoardSprites[x / squareLength ][y / squareLength ].owner, BoardSprites[x / squareLength ][y / squareLength ].unitType);
                lastCoordinates[0] = 125;
                lastCoordinates[1] = 125;
                lastUnit = null;
            }
        }

        //if user taps with unit selected on an empty square, move it TODO : make sure unit cannot move over another unit
        if (theUnit != null && BoardSprites[x / squareLength ][y / squareLength ] == null
                && (theUnit.movement >= getSquareDistance           //also check if unit is in range.
                        (getCoordinates(theUnit)[0], x / squareLength ,
                                getCoordinates(theUnit)[1], y / squareLength ))
                && theUnit.hasMove == true) {
            moveTo(theUnit, x / squareLength , y / squareLength ); //and then move the unit, and un-select it.
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
        if (theUnit != null && BoardSprites[x / squareLength ][y / squareLength ] != null &&
                BoardSprites[x / squareLength ][y / squareLength ].owner != theUnit.owner &&
                (theUnit.attack1Range >= getSquareDistance           //and check if unit is in range of first (stronger) attack.
                        (getCoordinates(theUnit)[0], x / squareLength ,
                                getCoordinates(theUnit)[1], y / squareLength ))
                && theUnit.hasAttack == true) {
            DamageUnit(theUnit.attack1, BoardSprites[x / squareLength ][y / squareLength ], x / squareLength , y / squareLength ); //and then move the unit, and un-select it.
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
        if (theUnit != null && BoardSprites[x / squareLength ][y / squareLength ] != null &&
                BoardSprites[x / squareLength ][y / squareLength ].owner != theUnit.owner &&
                (theUnit.attack2Range >= getSquareDistance           //and check if unit is in range of second (weaker) attack.
                        (getCoordinates(theUnit)[0], x / squareLength ,
                                getCoordinates(theUnit)[1], y / squareLength ))
                && theUnit.hasAttack == true) {
            DamageUnit(theUnit.attack2, BoardSprites[x / squareLength ][y / squareLength ], x / squareLength , y / squareLength );
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
        boolean isAtStartingPosition = false;
        if (u.owner == green && u.coordinates[0] == 2 && u.coordinates[1] == 2 && green.isHuman) {
            isAtStartingPosition = true;
        }
        if (u.owner == red && u.coordinates[0] == 12 && u.coordinates[1] == 6 && red.isHuman) {
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

    //damages the unit at given coordinates
    public static void DamageUnit(int damage, Units u, int x, int y) {
        lastUnit = new Units(u, GameView.theContext);
        lastCoordinates[0] = u.coordinates[0];
        lastCoordinates[1] = u.coordinates[1];
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
                        unselectEnemy();
                        showMarket = false;
                        message = u.unitType + " at " + (x + c) + ", " + (y + c) + " is destroyed";
                        if (u.unitType.equals("Headquarters")) {
                            if (theUnit.hasMove) {
                                theUnit.hasAttack = false;
                            }
                            //if units doesn't have a move, un-select it
                            if (!theUnit.hasMove) {
                                theUnit.hasAttack = false;
                                checkAction(theUnit);
                            }
                            unselectAll();
                            message = "HQ has been destroyed, " + playing.color + " player wins!";
                            FullscreenActivity.theActivity.vibrate();
                            showMarket = false;
                        }
                        return;
                    }
                }
            }
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
            BoardSprites[2][2] = GameEngine.queue[0];
            GameEngine.queue[0].coordinates[0] = 2;
            GameEngine.queue[0].coordinates[1] = 2;
        }
        if (playing == red) {
            BoardSprites[12][6] = GameEngine.queue[0];
            GameEngine.queue[0].coordinates[0] = 12;
            GameEngine.queue[0].coordinates[1] = 6;
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
        showFactory = false;
        message = "";
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
        GameEngine.lastAddedResources[0] = addedfood;
        GameEngine.lastAddedResources[1] = addediron;
        GameEngine.lastAddedResources[2] = addedoil;
    }
}

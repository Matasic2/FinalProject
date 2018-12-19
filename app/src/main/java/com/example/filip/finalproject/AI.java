package com.example.filip.finalproject;

public class AI {

    public static int aggresionLevel = 3; // 5 is desperate attack, 4 is attack, 3 is hold, 2 is defend, 1 is desperate defense
    public static int turn = 0; //keeps track of the current turn

    public static Units[] units = new Units[0]; //list of all units the AI owns
    public static String[] unitOrders = new String[0]; //list of orders for every unit AI owns, corresponds to units in the previous list

//plays AI's turn
    public static void playTurn(Player AIPlayer) {
        //Takes AI's initial units and adds them to Units list
        if (turn == 0) {
            addUnit(GameEngine.BoardSprites[12][6], "moveTo_8_7");
        }
        resetOrders(); //at the start of the every turn, decide what to do with all units
        turn++; //add turn to turn counter

        //plays all units
        for (int i = 0; i < units.length; i++) {
            if (unitOrders[i] != null) {
                playUnit(units[i], unitOrders[i]);
            }
        }

        //keep buying and playing units as long as AI has resources for it
        while (AIPlayer.foodStorage >= 2 && GameEngine.BoardSprites[12][6] == null) {
            if (AIPlayer.foodStorage >= 3 && turn == 1) {
                buyCavalry(AIPlayer, "moveTo_13_1");
            }
            else if (AIPlayer.foodStorage >= 2 && turn != 1 && GameEngine.BoardSprites[12][6] == null) {
                buyInfantry(AIPlayer, "moveTowards_2_2");
            }
        }
        //end turn at the end
        GameEngine.switchPlayer();
    }
    //buys and plays Infantry
    public static boolean buyInfantry(Player AIPlayer, String order){
        if (GameEngine.BoardSprites[12][6] != null || AIPlayer.foodStorage < Infantry.foodPrice) {
            return false;
        }
        AIPlayer.foodStorage -= Infantry.foodPrice;
        Units temp = new Infantry(GameView.theContext, 12, 6, AIPlayer);
        addUnit(temp,order);
        playUnit(temp,order);
        return true;
    }
    //buys and plays Cavalry
    public static boolean buyCavalry(Player AIPlayer, String order) {
        if (GameEngine.BoardSprites[12][6] != null || AIPlayer.foodStorage < Cavalry.foodPrice) {
            return false;
        }
        AIPlayer.foodStorage -= Cavalry.foodPrice;
        Units temp = new Cavalry(GameView.theContext, 12, 6, AIPlayer);
        addUnit(temp,order);
        playUnit(temp,order);
        return true;
    }
    //plays unit
    public static void playUnit(Units u, String orders){
        //if his order is to move :
        if (orders.startsWith("moveTo")) {

            //take coordinates of where it should move towards
            String[] moveCoordinates = orders.split("_");
            int[] orderCoordinates = {Integer.parseInt(moveCoordinates[1]), Integer.parseInt(moveCoordinates[2])};

            //if enemy unit can be attacked, prioritize the attack
            int bestDamageValue = -999;
            int bestX = 125;
            int bestY = 125;
            for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                for (int j = 0; j < GameEngine.BoardSprites[i].length; j++) {
                    if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != u.owner
                            && (u.movement + u.attack1Range >= GameEngine.getSquareDistance(u.coordinates[0], i, u.coordinates[1], j))) {
                        if (Math.min(GameEngine.BoardSprites[i][j].maxHP,u.attack1) > bestDamageValue) {
                            bestDamageValue = GameEngine.BoardSprites[i][j].maxHP; //assert the value of the attack
                            bestX = i;
                            bestY = j;
                        }
                    }
                }
            }
            if (bestX != 125 && bestY != 125) {
                orderCoordinates[0] = bestX;
                orderCoordinates[1] = bestY;
            }

            //move towards coordinates
            if (orderCoordinates[0] != u.coordinates[0] || orderCoordinates[1] != u.coordinates[1]) {
                moveTowards(u, orderCoordinates[0], orderCoordinates[1]);
            }
        }
    }

    //adds unit to the list of units and order to list of orders
    public static void addUnit(Units u, String order) {
        Units[] toReturn = new Units[units.length + 1];
        for (int k = 0; k < units.length; k++) {
            toReturn[k] = units[k];
        }
        toReturn[toReturn.length - 1] = u;
        units = toReturn;

        String[] toReturn2 = new String[unitOrders.length + 1];
        for (int k = 0; k < unitOrders.length; k++) {
            toReturn2[k] = unitOrders[k];
        }
        toReturn2[toReturn2.length - 1] = order;
        unitOrders = toReturn2;
    }

    //move towards the given coordinates
    public static void moveTowards(Units u, int x, int y) {
        int currentX = u.coordinates[0];
        int currentY = u.coordinates[1];

        //estimate the best path to ordered coordinates
        int bestDistance = 9999;
        int bestX = 125;
        int bestY = 125;
        for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
            for (int j = 0; j < GameEngine.BoardSprites[i].length; j++) {
                if (GameEngine.BoardSprites[i][j] == null && (u.movement >= GameEngine.getSquareDistance(currentX, i, currentY, j))
                        && GameEngine.getSquareDistance(i, x, j, y) < GameEngine.getSquareDistance(currentX, x, currentY, y) ) {
                    if (GameEngine.getSquareDistance(i, x, j, y) < bestDistance) {
                        bestDistance = GameEngine.getSquareDistance(i, x, j, y);
                        bestX = i;
                        bestY = j;
                    }
                }
            }
        }
        //move unit to best coordinates
        if (bestX != 125 && bestY != 125) {
            GameEngine.moveTo(u, bestX, bestY);
        }

        // if the unit can attack, use the attack
        if (u.hasAttack) {
            //estimate which attack is most valuable
            int attackType = 0;
            int bestDamageValue = -999;
            bestX = 125;
            bestY = 125;
            for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                for (int j = 0; j < GameEngine.BoardSprites[i].length; j++) {
                    if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != u.owner
                            && (u.attack2Range >= GameEngine.getSquareDistance(u.coordinates[0], i, u.coordinates[1], j))) {
                        if ((u.attack1Range >= GameEngine.getSquareDistance(u.coordinates[0],i, u.coordinates[1],j))
                                && Math.min(GameEngine.BoardSprites[i][j].maxHP,u.attack1) > bestDamageValue) {
                                attackType = 1;
                                bestDamageValue = Math.min(GameEngine.BoardSprites[i][j].maxHP,u.attack1); //estimates the most valuable attack
                                bestX = i;
                                bestY = j;
                        }
                        else if (Math.min(GameEngine.BoardSprites[i][j].maxHP,u.attack2) > bestDamageValue){
                            attackType = 2;
                            bestDamageValue = Math.min(GameEngine.BoardSprites[i][j].maxHP,u.attack2); //estimates the most valuable attack
                            bestX = i;
                            bestY = j;
                        }
                    }
                }
            }
            //if best attack is melee attack, use melee attack
            if (attackType == 1) {
                GameEngine.DamageUnit(u.attack1,GameEngine.BoardSprites[bestX][bestY],bestX,bestY);
            }
            //if best attack is ranged attack, damage it with ranged attack
            else if (attackType == 2)  {
                GameEngine.DamageUnit(u.attack2,GameEngine.BoardSprites[bestX][bestY],bestX,bestY);
            }
            u.hasAttack = false;
        }
    }
    //re-asserts orders
    public static void resetOrders(){
        //remove any null or dead units from the units list
        for (int i = 0; i < units.length; i++) {
            if (units[i] == null || units[i].HP <= 0) {
                Units[] toReplace = new Units[units.length - 1];
                for (int j = 0; j < i; j++) {
                    toReplace[j] = units[j];
                }
                for (int j = i; j < toReplace.length; j++) {
                    toReplace[j] = units[j + 1];
                }
                units = toReplace;

                String[] toReplace2 = new String[unitOrders.length - 1];
                for (int j = 0; j < i; j++) {
                    toReplace2[j] = unitOrders[j];
                }
                for (int j = i; j < toReplace2.length; j++) {
                    toReplace2[j] = unitOrders[j + 1];
                }
                unitOrders = toReplace2;
                i--;
            }
        }
    }

}

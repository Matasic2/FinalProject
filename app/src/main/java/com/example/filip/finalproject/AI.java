package com.example.filip.finalproject;

import java.util.Random;

public class AI {

    public static int aggresionLevel = 3; // 5 is desperate attack, 4 is attack, 3 is hold, 2 is defend, 1 is desperate defense
    public static int turn = 0; //keeps track of the current turn
    public static boolean makeUnitFor13_1 = false;
    public static boolean makeUnitFor8_7 = false;

    public static int rng;

    public static Units[] units = new Units[0]; //list of all units the AI owns
    public static String[] unitOrders = new String[0]; //list of orders for every unit AI owns, corresponds to units in the previous list

//plays AI's turn
    public static void playTurn(Player AIPlayer) {
        //Takes AI's initial units and adds them to Units list
        if (turn == 0) {
            addUnit(GameEngine.BoardSprites[12][6], "moveTo_8_7");
            Random rand = new Random();
            AI.rng = 30;
        }
        resetOrders(); //at the start of the every turn, decide what to do with all units
        turn++; //add turn to turn counter

        //is a resource point is not garrisoned, find nearest unit and garrison it (active after round 4), if no unit is found make a new one
        if (turn >= 4) {
            if (GameEngine.BoardSprites[13][1] == null || GameEngine.BoardSprites[13][1].owner != GameEngine.AIPlayer) {
                Units closest = closestUnitTo(13, 1, true);
                if (closest == null) {
                    makeUnitFor13_1 = true;
                } else {
                    for (int ij = 0; ij < units.length; ij++) {
                        if (closest == units[ij]) {
                            unitOrders[ij] = "moveTo_13_1";
                            break;
                        }
                    }
                }
            }
            if (GameEngine.BoardSprites[8][7] == null || GameEngine.BoardSprites[8][7].owner != GameEngine.AIPlayer) {
                Units closest = closestUnitTo(8, 7, true);
                if (closest == null) {
                    makeUnitFor8_7 = true;
                } else {
                    for (int ij = 0; ij < units.length; ij++) {
                        if (closest == units[ij]) {
                            unitOrders[ij] = "moveTo_8_7";
                            break;
                        }
                    }
                }
            }
        }

        //plays all units
        for (int i = 0; i < units.length; i++) {
            if (unitOrders[i] != null) {
                if ((units[i].coordinates[0] == 8 && units[i].coordinates[1] == 7)) {
                    unitOrders[i] = "moveTo_8_7";
                }
                if ((units[i].coordinates[0] == 13 && units[i].coordinates[1] == 1)) {
                    unitOrders[i] = "moveTo_13_1";
                }
                playUnit(units[i], unitOrders[i]);
            }
        }

        //keep buying and playing units as long as AI has resources for it. TODO : Add save method!
        if (turn >= 4 && turn % 2 == 0) {
            AIPlayer.foodStorage -=1;
        }

        boolean[] viableUnits = determineUnitToProduce();

        boolean hasEnoughFood = true;
        while (AIPlayer.foodStorage >= 2 && GameEngine.BoardSprites[12][6] == null) {

            if (hasEnoughFood && (turn == 1 || makeUnitFor13_1 || makeUnitFor8_7) && AIPlayer.foodStorage >= Cavalry.foodPrice) {
                if (turn  == 1){
                    buyCavalry(AIPlayer, "moveTo_13_1");
                }
                else if (makeUnitFor13_1){
                    makeUnitFor13_1 = false;
                    buyCavalry(AIPlayer, "moveTo_13_1");
                }
                else if (makeUnitFor8_7){
                    makeUnitFor8_7 = false;
                    buyCavalry(AIPlayer, "moveTo_8_7");
                }
            }
            else if (AIPlayer.oilStorage >= Armor.oilPrice && AIPlayer.ironStorage >= Armor.ironPrice && AIPlayer.foodStorage >= Armor.foodPrice && viableUnits[4]) {
                buyArmor(AIPlayer, "moveTo_2_2");
            }
            else if (AIPlayer.ironStorage >= Artillery.ironPrice && AIPlayer.foodStorage >= Artillery.foodPrice && viableUnits[2]) {
                buyArtillery(AIPlayer, "moveTo_2_2");
            }

            //mass inf, cav if enemy has 2 cannons
            else if (AIPlayer.foodStorage >= Infantry.foodPrice && turn != 1 && GameEngine.BoardSprites[12][6] == null && rng < 50 && viableUnits[0]) {
                buyInfantry(AIPlayer, "moveTo_2_2");
                if (AIPlayer.foodStorage < 2) {
                    hasEnoughFood = false;
                }
            }
            else if (AIPlayer.foodStorage >= Cavalry.foodPrice && turn != 1 && GameEngine.BoardSprites[12][6] == null && rng < 50 && viableUnits[1]) {
                if (GameEngine.BoardSprites[6][1] == null || GameEngine.BoardSprites[6][1].owner != AIPlayer) {
                    buyCavalry(AIPlayer, "moveTo_6_1");
                }
                else {
                    buyCavalry(AIPlayer, "moveTo_2_2");
                }
                if (AIPlayer.foodStorage < 3) {
                    hasEnoughFood = false;
                }
            }

            //mass cav
            else if (AIPlayer.foodStorage >= Cavalry.foodPrice && turn != 1 && GameEngine.BoardSprites[12][6] == null && rng > 60 && viableUnits[1]) {
                if (GameEngine.BoardSprites[6][1] == null || GameEngine.BoardSprites[6][1].owner != AIPlayer) {
                    buyCavalry(AIPlayer, "moveTo_6_1");
                }
                else {
                    buyCavalry(AIPlayer, "moveTo_2_2");
                }
                if (AIPlayer.foodStorage < 3) {
                    hasEnoughFood = false;
                }
            }

            //fake inf into cav
            else if (AIPlayer.foodStorage >= Cavalry.foodPrice && turn != 1 && GameEngine.BoardSprites[12][6] == null && rng >= 50 && rng <= 60) {
                if (turn == 2) {
                    buyInfantry(AIPlayer, "moveTo_2_2");
                    if (AIPlayer.foodStorage < 2) {
                        hasEnoughFood = false;
                    }
                }
                else if (GameEngine.BoardSprites[6][1] == null || GameEngine.BoardSprites[6][1].owner != AIPlayer) {
                    buyCavalry(AIPlayer, "moveTo_6_1");
                }
                else {
                    buyCavalry(AIPlayer, "moveTo_2_2");
                }
                if (AIPlayer.foodStorage < 3) {
                    hasEnoughFood = false;
                }
            }
            else {
                break;
            }
        }
        if (turn >= 4 && turn % 2 == 0) {
            AIPlayer.foodStorage +=1;
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
    //buys and plays Artillery
    public static boolean buyArtillery(Player AIPlayer, String order) {
        if (GameEngine.BoardSprites[12][6] != null || AIPlayer.foodStorage < Artillery.foodPrice) {
            return false;
        }
        AIPlayer.foodStorage -= Artillery.foodPrice;
        AIPlayer.ironStorage -= Artillery.ironPrice;
        Units temp = new Artillery(GameView.theContext, 12, 6, AIPlayer);
        addUnit(temp,order);
        playUnit(temp,order);
        return true;
    }
    //buys and plays Armor
    public static boolean buyArmor(Player AIPlayer, String order){
        if (GameEngine.BoardSprites[12][6] != null || AIPlayer.foodStorage < Armor.foodPrice || AIPlayer.ironStorage < Armor.ironPrice || AIPlayer.oilStorage < Armor.oilPrice) {
            return false;
        }
        AIPlayer.foodStorage -= Armor.foodPrice;
        AIPlayer.ironStorage -= Armor.ironPrice;
        AIPlayer.oilStorage -= Armor.oilPrice;
        Units temp = new Armor(GameView.theContext, 12, 6, AIPlayer);
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
            float bestDamageValue = -999;
            int bestX = 125;
            int bestY = 125;
            for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                for (int j = 0; j < GameEngine.BoardSprites[i].length; j++) {
                    if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != u.owner
                            && (u.movement + u.attack1Range >= GameEngine.getSquareDistance(u.coordinates[0], i, u.coordinates[1], j))) {
                        float willDie = 1.0f;
                        if (GameEngine.BoardSprites[i][j].HP <= u.attack1 - GameEngine.BoardSprites[i][j].defence) {
                            willDie = 1.5f;
                        }
                        float damageValue = getDamageValue(GameEngine.BoardSprites[i][j], u.attack1) * willDie;
                        if (damageValue > bestDamageValue) {
                            bestDamageValue = damageValue;
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
        else if (orders.startsWith("Garrison")) {
            //take coordinates of where it should move towards
            String[] moveCoordinates = orders.split("_");
            int[] orderCoordinates = {Integer.parseInt(moveCoordinates[1]), Integer.parseInt(moveCoordinates[2])};
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
                    if (GameEngine.getSquareDistance(i, x, j, y) <= bestDistance) {
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
            float bestDamageValue = -999f;
            bestX = 125;
            bestY = 125;
            for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                for (int j = 0; j < GameEngine.BoardSprites[i].length; j++) {
                    if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != u.owner
                            && (u.attack2Range >= GameEngine.getSquareDistance(u.coordinates[0], i, u.coordinates[1], j))) {
                        float willDie = 1.0f;
                        int attackDamage;
                        if ((u.attack1Range >= GameEngine.getSquareDistance(u.coordinates[0], i, u.coordinates[1], j)) && GameEngine.BoardSprites[i][j].HP <= u.attack1 - GameEngine.BoardSprites[i][j].defence) {
                            willDie = 1.5f;
                        }
                        else if ((u.attack2Range >= GameEngine.getSquareDistance(u.coordinates[0], i, u.coordinates[1], j)) && GameEngine.BoardSprites[i][j].HP <= u.attack2 - GameEngine.BoardSprites[i][j].defence) {
                            willDie = 1.5f;
                        }
                        if (u.attack1Range >= GameEngine.getSquareDistance(u.coordinates[0], i, u.coordinates[1], j)) {
                            attackDamage = u.attack1;
                        }
                        else {
                            attackDamage = u.attack2;
                        }
                        float damageValue = getDamageValue(GameEngine.BoardSprites[i][j], attackDamage) * willDie;
                        if ((u.attack1Range >= GameEngine.getSquareDistance(u.coordinates[0],i, u.coordinates[1],j))
                                && damageValue > bestDamageValue) {
                                attackType = 1;
                                bestDamageValue = damageValue; //estimates the most valuable attack
                                bestX = i;
                                bestY = j;
                        }
                        else if (damageValue > bestDamageValue){
                            attackType = 2;
                            bestDamageValue = damageValue; //estimates the most valuable attack
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


    public static Units closestUnitTo(int x, int y, boolean bool) {
        float time = 2;
        Units temp = null;
        for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
            for (int j = 0; j < GameEngine.BoardSprites[i].length; j++) {
                if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner == GameEngine.AIPlayer && Math.ceil(GameEngine.getSquareDistance(i, x, j, y) / 3) < time ) {
                    if (bool && Math.ceil(( (float)GameEngine.getSquareDistance(i, x, j, y) / GameEngine.BoardSprites[i][j].movement)) < time
                            && (GameEngine.BoardSprites[i][j].unitType == "Infantry" ||  GameEngine.BoardSprites[i][j].unitType == "Cavalry")) {
                        time = (float) Math.ceil(( (float) GameEngine.getSquareDistance(i, x, j, y)) / GameEngine.BoardSprites[i][j].movement);
                        temp = GameEngine.BoardSprites[i][j];
                    }
                }
            }
        }
        if (time < 2 && temp != null) {
            return temp;
        }
        return null;
    }

    public static float getUnitValue(Units u) {
        if (u.unitType == "Infantry") {
            return 1.0f;
        }
        if (u.unitType == "Cavalry") {
            return 1.5f;
        }
        if (u.unitType == "Artillery") {
            return 3.0f;
        }
        if (u.unitType == "Mech Infantry") {
            return 2.0f;
        }
        if (u.unitType == "Headquarters") {
            return 16.0f;
        }
        if (u.unitType == "Armor") {
            return 10.0f;
        }
        if (u.unitType == "Heavy Tank") {
            return 15.0f;
        }
        return 1.0f;
    }

    public static float getDamageValue(Units u, int damage) {
        if (damage < u.defence) {
            return 0;
        }
        if (u.HP <= damage - u.defence) {
            return getUnitValue(u);
        }
        return (Math.min(u.maxHP, damage - u.defence) / u.maxHP) * getUnitValue(u);
    }

    public static int[] getEnemyUnitsCount(String whichUnit) {
        int infCount = 0;
        int cavCount = 0;
        int artCount = 0;
        int mecCount = 0;
        int armCount = 0;
        int heaCount = 0;
        for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
            for (int j = 0; j < GameEngine.BoardSprites[i].length; j++) {
                if (GameEngine.BoardSprites[i][j] == null || GameEngine.BoardSprites[i][j].owner == GameEngine.AIPlayer) {
                    continue;
                }
               else if (GameEngine.BoardSprites[i][j].unitType == "Infantry") {
                   infCount++;
               }
               else if (GameEngine.BoardSprites[i][j].unitType == "Cavalry") {
                   cavCount++;
               }
               else if (GameEngine.BoardSprites[i][j].unitType == "Artillery") {
                   artCount++;
               }
               else if (GameEngine.BoardSprites[i][j].unitType == "Mech Infantry") {
                   mecCount++;
               }
               else if (GameEngine.BoardSprites[i][j].unitType == "Armor") {
                   armCount++;
               }
               else if (GameEngine.BoardSprites[i][j].unitType == "Heavy Tank") {
                   heaCount++;
               }
            }
        }
        if (whichUnit == "Infantry")  {
            return new int[] {infCount};
        }
        if (whichUnit == "Cavalry")  {
            return new int[] {cavCount};
        }
        if (whichUnit == "Artillery")  {
            return new int[] {artCount};
        }
        if (whichUnit == "Mech Infantry")  {
            return new int[] {mecCount};
        }
        if (whichUnit == "Armor")  {
            return new int[] {armCount};
        }
        if (whichUnit == "Heavy Tank")  {
            return new int[] {heaCount};
        }
        return new int[] {infCount, cavCount, artCount, mecCount, armCount, heaCount};
    }
    public static boolean[] determineUnitToProduce() {
        boolean[] toReturn = new boolean[6];
        for (int i = 0; i < toReturn.length; i++) {
            toReturn[i] = true;
        }
        int[] unitCount = getEnemyUnitsCount("All");
        if (unitCount[2] >= 1) {
            toReturn[0] = false;
        }
        return toReturn;
    }
}

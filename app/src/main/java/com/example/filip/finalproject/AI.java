package com.example.filip.finalproject;

import java.util.Random;

public class AI {

    //public static int aggresionLevel = 3; // 5 is desperate attack, 4 is attack, 3 is hold, 2 is defend, 1 is desperate defense
    public static int turn = 0; //keeps track of the current turn
    public static String[] resourcePointCoordinates = new String[0];
    public static boolean[] makeUnitForResourcePoints = new boolean[0];

    public static int rng;

    public static Units[] units = new Units[0]; //list of all units the AI owns
    public static String[] unitOrders = new String[0]; //list of orders for every unit AI owns, corresponds to units in the previous list

    public static boolean isCheating = false;

    public static int aiStartingX = GameEngine.redDeployX;
    public static int aiStartingY = GameEngine.redDeployY;
    public static int opponentStartingX = GameEngine.greenDeployX;
    public static int opponentStartingY = GameEngine.greenDeployY;

    public static boolean hasContestedPoint = false;
    public static boolean ignoreFOW = true;

    public static void initializeAI() {
        turn = 0;
        AI.rng = 30;
        units = new Units[0];
        unitOrders = new String[0];
        aiStartingX = GameEngine.redDeployX;
        aiStartingY = GameEngine.redDeployY;
        opponentStartingX = GameEngine.greenDeployX;
        opponentStartingY = GameEngine.greenDeployY;
        ignoreFOW = true;
        if (GameEngine.isSkirmish) {
            MapSkirmish.initializeMapAI();
        } else {
            MapScenario.initializeMapAI();
        }
    }

//plays AI's turn
    public static void playTurn(Player AIPlayer) {
        //Takes AI's initial units and adds them to Units list
        if (turn == 0 && resourcePointCoordinates.length > 0) {
            GameEngine.loadoutMenuUnit = "Armor";
            AIPlayer.adjustUpgrades("Armor", 1);
            GameEngine.loadoutMenuUnit = "";
        }
        resetOrders(); //at the start of the every turn, decide what to do with all units


        //if AI is cheating, give free resources
        if (isCheating) {
            GameEngine.AIPlayer.foodStorage += 2;
            GameEngine.AIPlayer.ironStorage += 1;
            GameEngine.AIPlayer.oilStorage += 2;
        }

        //is a resource point is not garrisoned, find nearest unit and garrison it (active after round 4), if no unit is found make a new one
        if (turn >= 4) {
            for (int i = 0; i <resourcePointCoordinates.length; i++) {
                String[] substring = resourcePointCoordinates[i].split(",");
                int x = Integer.parseInt(substring[0]);
                int y = Integer.parseInt(substring[1]);
                if (GameEngine.boardUnits[x][y] == null || GameEngine.boardUnits[x][y].owner != GameEngine.AIPlayer) {
                    Units closest = closestUnitTo(x, y, true);
                    if (closest == null) {
                        makeUnitForResourcePoints[i] = true;  //TODO: once this is true, i dont think it ever becomes false so this code is useless.
                    } else {
                        for (int ij = 0; ij < units.length; ij++) {
                            if (closest == units[ij]) {
                                unitOrders[ij] = "moveTo_" + substring[0] + "_" + substring[1];
                                break;
                            }
                        }
                    }
                }
            }
        }

        //plays all units
        for (int i = 0; i < units.length; i++) {
            if (unitOrders[i] != null) {
                if ((units[i].coordinates[0] == 8 && units[i].coordinates[1] == 7)) {
                    unitOrders[i] = "Garrison_8_7";
                }
                if ((units[i].coordinates[0] == 13 && units[i].coordinates[1] == 1)) {
                    unitOrders[i] = "Garrison_13_1";
                }
                playUnit(units[i], unitOrders[i]);
            }
        }

        //keep buying and playing units as long as AI has resources for it. TODO : Add save method!
        if (turn >= 4 && turn % 2 == 0) {
            //AIPlayer.foodStorage -=1;
        }

        boolean[] viableUnits = determineUnitToProduce();

        boolean hasEnoughFood = true;
        while (AIPlayer.foodStorage >= 2 && GameEngine.boardUnits[aiStartingX][aiStartingY]== null && hasEnoughFood) {

            //filling resources is highest priority
            for (int i = 0; i < resourcePointCoordinates.length; i++) {
                String[] substring = resourcePointCoordinates[i].split(",");
                int x = Integer.parseInt(substring[0]);
                int y = Integer.parseInt(substring[1]);
                String safety = "";
                if (substring.length > 2) {
                    safety = substring[2];
                }
                if (makeUnitForResourcePoints[i] && AIPlayer.foodStorage >= Cavalry.redFoodPrice) {
                    makeUnitForResourcePoints[i] = false;
                    buyCavalry(AIPlayer, "moveTo_" + substring[0] + "_" + substring[1]);
                }
            }

            /**if (hasEnoughFood && (turn == 1 || makeUnitFor13_1 || makeUnitFor8_7) && AIPlayer.foodStorage >= Cavalry.redFoodPrice) {
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
            }*/

            //armor and artillery is a priority
            if (AIPlayer.oilStorage >= Armor.redOilPrice && AIPlayer.ironStorage >= Armor.redIronPrice && AIPlayer.foodStorage >= Armor.redFoodPrice && viableUnits[4]) {
                buyArmor(AIPlayer, "moveTo_2_2");
            }
            else if (turn > 8 && AIPlayer.ironStorage >= Artillery.redIronPrice && AIPlayer.foodStorage >= Artillery.redFoodPrice && viableUnits[2]) {
                buyArtillery(AIPlayer, "moveTo_2_2");
            }

            //mass inf
            else if (AIPlayer.foodStorage >= Infantry.redFoodPrice && turn != 1 && GameEngine.boardUnits[12][6] == null && rng < 50 && viableUnits[0]) {
                buyInfantry(AIPlayer, "moveTo_2_2");
                if (AIPlayer.foodStorage < 2) {
                    hasEnoughFood = false;
                }
            }

            else if (AIPlayer.foodStorage >= Cavalry.redFoodPrice && turn != 1 && GameEngine.boardUnits[12][6] == null && rng < 50 && viableUnits[1]) {
                if (GameEngine.boardUnits[6][1] == null || GameEngine.boardUnits[6][1].owner != AIPlayer) {
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
            else if (AIPlayer.foodStorage >= Cavalry.redFoodPrice && turn != 1 && GameEngine.boardUnits[12][6] == null && rng > 60 && viableUnits[1]) {
                if (GameEngine.boardUnits[6][1] == null || GameEngine.boardUnits[6][1].owner != AIPlayer) {
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
            else if (AIPlayer.foodStorage >= Cavalry.redFoodPrice && turn != 1 && GameEngine.boardUnits[12][6] == null && rng >= 50 && rng <= 60) {
                if (turn == 2) {
                    buyInfantry(AIPlayer, "moveTo_2_2");
                    if (AIPlayer.foodStorage < 2) {
                        hasEnoughFood = false;
                    }
                }
                else if (GameEngine.boardUnits[6][1] == null || GameEngine.boardUnits[6][1].owner != AIPlayer) {
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
           // AIPlayer.foodStorage +=1;
        }
        //end turn at the end
        turn++; //add turn to turn counter
        GameEngine.switchPlayer();
    }
    //buys and plays Infantry
    public static boolean buyInfantry(Player AIPlayer, String order){
        if (GameEngine.boardUnits[aiStartingX][aiStartingY] != null || AIPlayer.foodStorage < Infantry.redFoodPrice) {
            return false;
        }
        AIPlayer.foodStorage -= Infantry.redFoodPrice;
        Units temp = new Infantry(GameView.theContext, aiStartingX, aiStartingY, AIPlayer);
        addUnit(temp,order);
        playUnit(temp,order);
        return true;
    }
    //buys and plays Cavalry
    public static boolean buyCavalry(Player AIPlayer, String order) {
        if (GameEngine.boardUnits[aiStartingX][aiStartingY] != null || AIPlayer.foodStorage < Cavalry.redFoodPrice) {
            return false;
        }
        AIPlayer.foodStorage -= Cavalry.redFoodPrice;
        Units temp = new Cavalry(GameView.theContext, aiStartingX, aiStartingY, AIPlayer);
        addUnit(temp,order);
        playUnit(temp,order);
        return true;
    }
    //buys and plays Artillery
    public static boolean buyArtillery(Player AIPlayer, String order) {
        if (GameEngine.boardUnits[aiStartingX][aiStartingY] != null || AIPlayer.foodStorage < Artillery.redFoodPrice) {
            return false;
        }
        AIPlayer.foodStorage -= Artillery.redFoodPrice;
        AIPlayer.ironStorage -= Artillery.redIronPrice;
        Units temp = new Artillery(GameView.theContext, aiStartingX, aiStartingY, AIPlayer);
        addUnit(temp,order);
        playUnit(temp,order);
        return true;
    }
    //buys and plays Armor
    public static boolean buyArmor(Player AIPlayer, String order){
        if (GameEngine.boardUnits[aiStartingX][aiStartingY] != null || AIPlayer.foodStorage < Armor.redFoodPrice || AIPlayer.ironStorage < Armor.redIronPrice || AIPlayer.oilStorage < Armor.redOilPrice) {
            return false;
        }
        AIPlayer.foodStorage -= Armor.redFoodPrice;
        AIPlayer.ironStorage -= Armor.redIronPrice;
        AIPlayer.oilStorage -= Armor.redOilPrice;
        Units temp = new Armor(GameView.theContext, aiStartingX, aiStartingY, AIPlayer);
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
            float bestDamageValue = 0;
            int bestX = 125;
            int bestY = 125;
            int bestXTarget = 125;
            int bestYTarget = 125;
            int x = u.coordinates[0];
            int y = u.coordinates[1];

            int[] oldCoords = new int[]{u.coordinates[0],u.coordinates[1]};
            for (int i = 0; i < GameEngine.boardUnits.length; i++) {
                for (int j = 0; j < GameEngine.boardUnits[i].length; j++) {

                    if (GameEngine.getSquareDistance(i,x,j,y) <= u.movement && GameEngine.boardUnits[i][j] == null) {
                        u.coordinates = new int[]{i,j};
                        GameEngine.boardUnits[i][j] = u;
                        GameEngine.boardUnits[oldCoords[0]][oldCoords[1]] = null;
                        double[] bestIJ = bestAttack(u);

                        if (bestIJ[2] > bestDamageValue) {
                            bestDamageValue = (float) bestIJ[2];
                            bestX = i;
                            bestY = j;
                            bestXTarget = (int) bestIJ[0];
                            bestYTarget = (int) bestIJ[1];
                        }

                        GameEngine.boardUnits[i][j] = null;
                        GameEngine.boardUnits[oldCoords[0]][oldCoords[1]] = u;
                    }
                }
            }

            u.coordinates = oldCoords;
            if (bestX != 125 && bestY != 125) {
                orderCoordinates[0] = bestX;
                orderCoordinates[1] = bestY;
            }

            //move towards coordinates
            if (orderCoordinates[0] != u.coordinates[0] || orderCoordinates[1] != u.coordinates[1]) {
                if (bestXTarget == 125) {
                    moveTowards(u, orderCoordinates[0], orderCoordinates[1]);
                } else {
                    moveToAndAttack(u, orderCoordinates[0], orderCoordinates[1], bestXTarget, bestYTarget);
                }
            } else {
                moveTowards(u, bestX, bestY);
            }
        }
        else if (orders.startsWith("Garrison")) {
            //take coordinates of where it should move towards
            String[] moveCoordinates = orders.split("_");
            int[] orderCoordinates = {Integer.parseInt(moveCoordinates[1]), Integer.parseInt(moveCoordinates[2])};
            if (orderCoordinates[0] != u.coordinates[0] || orderCoordinates[1] != u.coordinates[1]) {
                moveTowards(u, orderCoordinates[0], orderCoordinates[1]);
            } else {
                attackBest(u);
            }
        }
    }

    //find which target is the most valuable to attack
    public static double[] bestAttack(Units u) {
        int x = u.coordinates[0];
        int y = u.coordinates[1];

        int bestX = -1;
        int bestY = -1;
        double bestVal = 0;
        boolean[][] visibleTiles = GameEngine.getFogOfWar(GameEngine.red);

        for (int i = 0; i < GameEngine.boardUnits.length; i++) {
            for (int j = 0; j < GameEngine.boardUnits[i].length; j++) {
                if (!(ignoreFOW || visibleTiles[i][j]) || (GameEngine.boardUnits[i][j] == null || GameEngine.boardUnits[i][j].owner == u.owner)) continue;

                int distToIJ = GameEngine.getSquareDistance(i,x,j,y);
                if (distToIJ <= u.attack2Range) {
                    Units enemy = GameEngine.boardUnits[i][j];
                    int[] damages = GameEngine.assertDamage(u, enemy);

                    if (damages[0] == 0) continue;
                    double damageValueGiven = (double) damages[0]/enemy.maxHP*enemy.AI_value*getLocationFactor(i,j);
                    double damageValueReceived =  (double) damages[1]/u.maxHP*u.AI_value;
                    double damageValueDiff = damageValueGiven - damageValueReceived;
                    if (damageValueGiven > u.AI_value && bestVal < damageValueGiven - u.AI_value) {
                        bestX = i;
                        bestY = j;
                        bestVal = damageValueGiven - u.AI_value;
                    }
                    if (bestVal < damageValueDiff && !checkDangerInRange(u,i,j)) {
                        bestX = i;
                        bestY = j;
                        bestVal = damageValueDiff;
                    }
                }
            }
        }

        return new double[]{bestX,bestY, bestVal};
    }

    //move towards the given coordinates, but don't get too close to enemy units
    public static void moveToAndAttack(Units u, int x, int y, int targetX, int targetY) {

        //move unit to best coordinates
        GameEngine.moveTo(u, x, y);
        u.hasMove = false;

        // if the unit can attack, use the attack
        if (u.hasAttack) {
            u.hasAttack = false;
            GameEngine.attackUnit(u,GameEngine.boardUnits[targetX][targetY]);
        }
    }

    public static boolean checkDangerInRange(Units u, int i, int j) {
        if ((u.unitType == "Armor") && (!(EnemyUnitIsInMeeleRange(i,j)))) { // if the unit is valuable, don't push with it.
            return false;
        } else if ((u.unitType == "Artillery" || u.unitType == "Cavalry") && !(EnemyUnitIsInRange(i,j))) {
            return false;
        } else if ((u.unitType == "Infantry" && !(EnemyUnitIsInMeeleRange(i,j)))) {
            return false;
        } else if (!(u.unitType == "Armor" || u.unitType == "Artillery")) {
            return false;
        }
        return true;
    }


    //move towards the given coordinates, but don't get too close to enemy units
    public static void moveTowards(Units u, int x, int y) {
        int currentX = u.coordinates[0];
        int currentY = u.coordinates[1];

        //estimate the best path to ordered coordinates
        int bestDistance = 9999;
        int bestX = 125;
        int bestY = 125;

        boolean moveAndDestroy = false;
        int targetToDestroyX = -1;
        int targetToDestroyY = -1;

        for (int i = 0; i < GameEngine.boardUnits.length; i++) {
            for (int j = 0; j < GameEngine.boardUnits[i].length; j++) {
                if (GameEngine.boardUnits[i][j] == null && (u.movement >= GameEngine.getSquareDistance(currentX, i, currentY, j))) {
                    if (GameEngine.getSquareDistance(i, x, j, y) <= bestDistance) {

                        if (!checkDangerInRange(u,i,j)) { // if the unit is valuable, don't push with it.
                            bestDistance = GameEngine.getSquareDistance(i, x, j, y);
                            bestX = i;
                            bestY = j;
                        }

                        if ((u.unitType == "Armor") && (EnemyUnitIsInMeeleRange(i,j))) {
                            int[] count = countEnemyUnitIsInMeeleRange(i,j);
                            if (count[0] == 1 && GameEngine.boardUnits[count[1]][count[2]].HP <= u.attack2 - GameEngine.boardUnits[count[1]][count[2]].defence) {
                                moveAndDestroy = true;
                                bestDistance = GameEngine.getSquareDistance(i, x, j, y);
                                targetToDestroyX = count[1];
                                targetToDestroyY = count[2];
                                bestX = i;
                                bestY = j;
                                break;
                            }
                        }
                    }
                }
            }

            if (moveAndDestroy) {
                break;
            }
        }
        //move unit to best coordinates
        if (bestX != 125 && bestY != 125) {
            GameEngine.moveTo(u, bestX, bestY);
            u.hasMove = false;
        }

        // if the unit can attack, use the attack
        if (u.hasAttack) {
            attackBest(u);//estimate which attack is most valuable
        }
    }

    public static void attackTarget(Units u, int x, int y) {
        GameEngine.attackUnit(u,GameEngine.boardUnits[x][y]);
    }

    public static void attackBest(Units u) {
        if (!u.hasAttack) {
            return;
        }

        double[] best = bestAttack(u);

        if (best[2] > 0) {
            GameEngine.attackUnit(u, GameEngine.boardUnits[(int)(best[0])][(int)(best[1])]);
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
        for (int i = 0; i < GameEngine.boardUnits.length; i++) {
            for (int j = 0; j < GameEngine.boardUnits[i].length; j++) {
                if (GameEngine.boardUnits[i][j] != null && GameEngine.boardUnits[i][j].owner == GameEngine.AIPlayer && Math.ceil(GameEngine.getSquareDistance(i, x, j, y) / 3) < time ) {
                    if (bool && Math.ceil(( (float)GameEngine.getSquareDistance(i, x, j, y) / GameEngine.boardUnits[i][j].movement)) < time
                            && (GameEngine.boardUnits[i][j].unitType == "Infantry" ||  GameEngine.boardUnits[i][j].unitType == "Cavalry")) {
                        time = (float) Math.ceil(( (float) GameEngine.getSquareDistance(i, x, j, y)) / GameEngine.boardUnits[i][j].movement);
                        temp = GameEngine.boardUnits[i][j];
                    }
                }
            }
        }
        if (time < 2 && temp != null) {
            return temp;
        }
        return null;
    }

    public static float getDamageValue(Units u, int damage) {
        if (damage < u.defence) {
            return 0;
        }
        if (u.HP <= damage - u.defence) {
            return (float) u.AI_value;
        }
        return (Math.min(u.maxHP, damage - u.defence) / u.maxHP) * ((float) u.AI_value) * getLocationFactor(u.coordinates[0], u.coordinates[1]);
    }

    public static float getLocationFactor(int x, int y) {
        if (x == 6 && y == 1) {
            return 1.8f;
        }

        if (x == 13 && y == 1) {
            return 1.8f;
        }

        if (x == 8 && y == 7) {
            return 1.8f;
        }

        if (x == 1 && y == 7) {
            return 1.8f;
        }
        return 1.0f;
    }

    public static int[] getEnemyUnitsCount(String whichUnit) {
        int infCount = 0;
        int cavCount = 0;
        int artCount = 0;
        int mecCount = 0;
        int armCount = 0;
        int heaCount = 0;
        for (int i = 0; i < GameEngine.boardUnits.length; i++) {
            for (int j = 0; j < GameEngine.boardUnits[i].length; j++) {
                if (GameEngine.boardUnits[i][j] == null || GameEngine.boardUnits[i][j].owner == GameEngine.AIPlayer) {
                    continue;
                }
               else if (GameEngine.boardUnits[i][j].unitType == "Infantry") {
                   infCount++;
               }
               else if (GameEngine.boardUnits[i][j].unitType == "Cavalry") {
                   cavCount++;
               }
               else if (GameEngine.boardUnits[i][j].unitType == "Artillery") {
                   artCount++;
               }
               else if (GameEngine.boardUnits[i][j].unitType == "Mech Infantry") {
                   mecCount++;
               }
               else if (GameEngine.boardUnits[i][j].unitType == "Armor") {
                   armCount++;
               }
               else if (GameEngine.boardUnits[i][j].unitType == "Heavy Tank") {
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
            toReturn[0] = true;
        }
        return toReturn;
    }

        public static boolean EnemyUnitIsInMeeleRange(int x, int y) {
            for (int i = 0; i < GameEngine.boardUnits.length; i++) {
                for (int j = 0; j < GameEngine.boardUnits[i].length; j++) {
                    if (GameEngine.boardUnits[i][j] != null && GameEngine.boardUnits[i][j].owner != GameEngine.AIPlayer && GameEngine.boardUnits[i][j].movement + GameEngine.boardUnits[i][j].attack1Range >= GameEngine.getSquareDistance(x,i,y,j)) {
                        return true;
                    }
                }
            }
            return false;
        }

    public static int[] countEnemyUnitIsInMeeleRange(int x, int y) {
        int[] count = new int[3];
        for (int i = 0; i < GameEngine.boardUnits.length; i++) {
            for (int j = 0; j < GameEngine.boardUnits[i].length; j++) {
                if (GameEngine.boardUnits[i][j] != null && GameEngine.boardUnits[i][j].owner != GameEngine.AIPlayer && GameEngine.boardUnits[i][j].movement + GameEngine.boardUnits[i][j].attack1Range >= GameEngine.getSquareDistance(x,i,y,j)) {
                    count[0]++;
                    if (count[0] > 1) {
                        continue;
                    }
                    count[1] = i;
                    count[2] = j;
                }
            }
        }
        return count;
    }

    public static boolean EnemyUnitIsInRange(int x, int y) {
        for (int i = 0; i < GameEngine.boardUnits.length; i++) {
            for (int j = 0; j < GameEngine.boardUnits[i].length; j++) {                                                                                                                                                                              //this should be >=, but AI is too passive then.
                if (GameEngine.boardUnits[i][j] != null && GameEngine.boardUnits[i][j].owner != GameEngine.AIPlayer && GameEngine.boardUnits[i][j].unitType != "Artillery" && GameEngine.boardUnits[i][j].movement + GameEngine.boardUnits[i][j].attack2Range > GameEngine.getSquareDistance(x,i,y,j)) {
                    return true;
                }
            }
        }
        return false;
    }
}

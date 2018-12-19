package com.example.filip.finalproject;

public class AI {

    public static int aggresionLevel = 3; // 5 is desperate attack, 4 is attack, 3 is hold, 2 is defend, 1 is desperate defense
    public static int turn = 0;

    public static Units[] units = new Units[0];
    public static String[] unitOrders = new String[0];


    public static void playTurn(Player AIPlayer) {
        if (turn == 0) {
            addUnit(GameEngine.BoardSprites[12][6], "moveTo_8_7");
        }
        resetOrders();
        turn++;
        for (int i = 0; i < units.length; i++) {
            if (unitOrders[i] != null) {
                playUnit(units[i], unitOrders[i]);
            }
        }
        while (AIPlayer.foodStorage >= 2 && GameEngine.BoardSprites[12][6] == null) {
            if (AIPlayer.foodStorage >= 3 && turn == 1) {
                buyCavalry(AIPlayer, "moveTo_13_1");
            }
            else if (AIPlayer.foodStorage >= 2 && turn != 1 && GameEngine.BoardSprites[12][6] == null) {
                buyInfantry(AIPlayer, "moveTowards_2_2");
            }
        }
        GameEngine.switchPlayer();
    }
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
    public static void playUnit(Units u, String orders){
        if (orders.startsWith("moveTo")) {

            String[] moveCoordinates = orders.split("_");
            int[] orderCoordinates = {Integer.parseInt(moveCoordinates[1]), Integer.parseInt(moveCoordinates[2])};


            int bestDamageValue = -999;
            int bestX = 125;
            int bestY = 125;
            for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                for (int j = 0; j < GameEngine.BoardSprites[i].length; j++) {
                    if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != u.owner
                            && (u.movement + u.attack1Range >= GameEngine.getSquareDistance(u.coordinates[0], i, u.coordinates[1], j))) {
                        if (Math.min(GameEngine.BoardSprites[i][j].maxHP,u.attack1) > bestDamageValue) {
                            bestDamageValue = GameEngine.BoardSprites[i][j].maxHP;
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

            if (orderCoordinates[0] != u.coordinates[0] || orderCoordinates[1] != u.coordinates[1]) {
                moveTowards(u, orderCoordinates[0], orderCoordinates[1]);
            }
        }
    }

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

    public static void moveTowards(Units u, int x, int y) {
        int currentX = u.coordinates[0];
        int currentY = u.coordinates[1];

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
        if (bestX != 125 && bestY != 125) {
            GameEngine.moveTo(u, bestX, bestY);
        }
        if (u.hasAttack) {
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
                                bestDamageValue = Math.min(GameEngine.BoardSprites[i][j].maxHP,u.attack1);
                                bestX = i;
                                bestY = j;
                        }
                        else if (Math.min(GameEngine.BoardSprites[i][j].maxHP,u.attack2) > bestDamageValue){
                            attackType = 2;
                            bestDamageValue = Math.min(GameEngine.BoardSprites[i][j].maxHP,u.attack2);
                            bestX = i;
                            bestY = j;
                        }
                    }
                }
            }
            if (attackType == 1) {
                GameEngine.DamageUnit(u.attack1,GameEngine.BoardSprites[bestX][bestY],bestX,bestY);
            }
            else if (attackType == 2)  {
                GameEngine.DamageUnit(u.attack2,GameEngine.BoardSprites[bestX][bestY],bestX,bestY);
            }
            u.hasAttack = false;
        }
    }
    public static void resetOrders(){
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

package com.example.filip.finalproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class MapScenario {

    public static int map_code = 0; //default
    public static int number_of_maps_available = 4;

    public static void generateMap(Bitmap map, Bitmap square) {

        GameEngine.isSkirmish = false;
        //TechTree.initializeTechTree("skirmish");
        if (map_code == 0) {

            GameView.grid = new GameEngine(map, square, 10, 7); // these lines create the board.

            GameEngine.redDeployX = 9;
            GameEngine.redDeployY = 3;

            GameEngine.greenDeployX = 0;
            GameEngine.greenDeployY = 3;
            GameEngine.playing = GameEngine.green;

            // These for loops create starting units.

            for (int i = 0; i < 6; i++) {
                new Infantry(GameView.theContext, 0, i, GameEngine.green);
            }

            GameEngine.green.adjustUpgrades("Cavalry",0);
            for (int i = 0; i < 2; i++) {
                new Cavalry(GameView.theContext, 3, 6*i, GameEngine.green);
            }

            new Headquaters(GameView.theContext,8,3,GameEngine.red);

            createFortUnit(8,2,GameEngine.red);
            createFortUnit(8,4,GameEngine.red);
            createFortUnit(7,2,GameEngine.red);
            createFortUnit(7,3,GameEngine.red);
            createFortUnit(7,4,GameEngine.red);

        } else if (map_code == 1) {
            GameView.grid = new GameEngine(map, square, 14, 7); // these lines create the board.

            GameEngine.redDeployX = 13;
            GameEngine.redDeployY = 3;

            GameEngine.greenDeployX = 0;
            GameEngine.greenDeployY = 3;
            GameEngine.playing = GameEngine.green;

            new Food(GameView.theContext, 1, 1, 2, 1);
            new Food(GameView.theContext, 3, 1, 2, 1);
            new Food(GameView.theContext, 2, 0, 2, 1);
            new Food(GameView.theContext, 2, 2, 2, 1);

            new Food(GameView.theContext, 1, 5, 2, 5);
            new Food(GameView.theContext, 3, 5, 2, 5);
            new Food(GameView.theContext, 2, 4, 2, 5);
            new Food(GameView.theContext, 2, 6, 2, 5);

            new Food(GameView.theContext, 8, 2, 8, 3);
            new Food(GameView.theContext, 8, 4, 8, 3);
            new Food(GameView.theContext, 7, 3, 8, 3);
            new Food(GameView.theContext, 9, 3, 8, 3);

            GameEngine.green.adjustUpgrades("Cavalry",0);
            for (int i = 0; i < 2; i++) {
                new Cavalry(GameView.theContext, 3, 6*i, GameEngine.green);
            }
            GameEngine.green.adjustUpgrades("Cavalry",0);
            for (int i = 0; i < 3; i++) {
                new Infantry(GameView.theContext, 3, 2+i, GameEngine.green);
            }
            new Artillery(GameView.theContext, 2, 3, GameEngine.green);

            new Infantry(GameView.theContext, 8, 3, GameEngine.red);
            for (int i = 0; i < 5; i++) {
                if (i == 2) continue;
                new Infantry(GameView.theContext, 9, 1+i, GameEngine.red);
            }

            new Headquaters(GameView.theContext,12,3,GameEngine.red);
            createFortUnit(12,2,GameEngine.red);
            createFortUnit(12,4,GameEngine.red);
            createFortUnit(11,3,GameEngine.red);

            new Infantry(GameView.theContext, 11, 2, GameEngine.red);
            new Infantry(GameView.theContext, 11, 4, GameEngine.red);
            new Infantry(GameView.theContext, 13, 2, GameEngine.red);
            new Infantry(GameView.theContext, 13, 4, GameEngine.red);
        }

        AI.initializeAI();
        GameEngine.red.isHuman = false;
        GameEngine.AIPlayer = GameEngine.red;
        GameView.showendTurnScreen = true;

    }

    public static void drawMapFeatures(Canvas canvas) {
        if (map_code == -1) return;
        if (map_code == 0) {
            return;
        } if (map_code == 1) {
            GameView.harvesterMid.draw(canvas, 2, 1);
            GameView.harvesterMid.draw(canvas, 2, 5);
            GameView.harvesterMid.draw(canvas, 8, 3);
        }
    }

    public static String[] getAIResourcePoints() {
        String[] toReturn = new String[0];
        if (map_code == 0) {
            return toReturn;
        }
        if (map_code == 1) {
            toReturn = new String[]{"8,3"};
        }
        if (map_code == 2) {
            toReturn = new String[]{"18,5", "12,10", "17,1"};
        }
        return toReturn;
    }

    public static void initializeMapAI() {
        AI.resourcePointCoordinates = getAIResourcePoints();
        AI.makeUnitForResourcePoints = new boolean[AI.resourcePointCoordinates.length];

        for (int i = 0; i <  AI.makeUnitForResourcePoints.length; i++) {
            AI.makeUnitForResourcePoints[i] = true;
        }

        if (map_code == 0) {
            AI.addUnit(GameEngine.boardUnits[8][2], "Garrison_8_2");
            AI.addUnit(GameEngine.boardUnits[8][3], "Garrison_8_3");
            AI.addUnit(GameEngine.boardUnits[8][4], "Garrison_8_4");
            AI.addUnit(GameEngine.boardUnits[7][2], "Garrison_7_2");
            AI.addUnit(GameEngine.boardUnits[7][3], "Garrison_7_3");
            AI.addUnit(GameEngine.boardUnits[7][4], "Garrison_7_4");

            AI.ignoreFOW = false;
        } else if (map_code == 1) {
            AI.addUnit(GameEngine.boardUnits[8][3], "Garrison_8_3");
            AI.addUnit(GameEngine.boardUnits[9][1], "Garrison_8_3");
            AI.addUnit(GameEngine.boardUnits[9][2], "Garrison_8_3");
            AI.addUnit(GameEngine.boardUnits[9][4], "Garrison_8_3");
            AI.addUnit(GameEngine.boardUnits[9][5], "Garrison_8_3");

            AI.addUnit(GameEngine.boardUnits[12][2], "Garrison_12_2");
            AI.addUnit(GameEngine.boardUnits[12][4], "Garrison_12_4");
            AI.addUnit(GameEngine.boardUnits[11][3], "Garrison_11_3");
            AI.addUnit(GameEngine.boardUnits[12][3], "Garrison_12_3");

            AI.addUnit(GameEngine.boardUnits[11][2], "moveTo_0_3");
            AI.addUnit(GameEngine.boardUnits[11][4], "moveTo_0_3");
            AI.addUnit(GameEngine.boardUnits[13][2], "moveTo_0_3");
            AI.addUnit(GameEngine.boardUnits[13][4], "moveTo_0_3");

            AI.ignoreFOW = false;
        }
    }

    //how many turns are required for each level
    public static int[] turnsForStars() {
        int level = map_code;
        switch (level) {
            case 0: return new int[]{6,7,10};
            case 1: return new int[]{8,10,12};
        }
        return new int[]{0,0,0};
    }

    public static byte[] starsAchieved(int turnCount) {
        int[] starReq = turnsForStars();
        if (turnCount <= starReq[0]) {
            return new byte[]{3,(byte) map_code};
        } else if (turnCount <= starReq[1]) {
            return new byte[]{2,(byte) map_code};
        } else if (turnCount <= starReq[2]) {
            return new byte[]{1,(byte) map_code};
        } else {
            return new byte[]{0,(byte) map_code};
        }
    }

    public static void createFortUnit(int x, int y, Player p) {
        new Infantry(GameView.theContext, x, y, p);
        GameEngine.boardUnits[x][y].unitType = "Fort";
        GameEngine.boardUnits[x][y].defence += 1;
        GameEngine.boardUnits[x][y].movement = 0;
        GameEngine.boardUnits[x][y].AI_value += 0.5;
        GameEngine.boardUnits[x][y].brightenIcon();
    }
}

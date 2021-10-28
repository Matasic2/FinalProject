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

            new Infantry(GameView.theContext, 8, 2, GameEngine.red);
            GameEngine.boardUnits[8][2].unitType = "Fort";
            GameEngine.boardUnits[8][2].defence += 1;
            GameEngine.boardUnits[8][2].movement = 0;
            GameEngine.boardUnits[8][2].AI_value += 0.5;
            GameEngine.boardUnits[8][2].brightenIcon();

            new Infantry(GameView.theContext, 8, 4, GameEngine.red);
            GameEngine.boardUnits[8][4].unitType = "Fort";
            GameEngine.boardUnits[8][4].defence += 1;
            GameEngine.boardUnits[8][4].movement = 0;
            GameEngine.boardUnits[8][4].AI_value += 0.5;
            GameEngine.boardUnits[8][4].brightenIcon();

            new Infantry(GameView.theContext, 7, 2, GameEngine.red);
            GameEngine.boardUnits[7][2].unitType = "Fort";
            GameEngine.boardUnits[7][2].defence += 1;
            GameEngine.boardUnits[7][2].movement = 0;
            GameEngine.boardUnits[7][2].AI_value += 0.5;
            GameEngine.boardUnits[7][2].brightenIcon();

            new Infantry(GameView.theContext, 7, 3, GameEngine.red);
            GameEngine.boardUnits[7][3].unitType = "Fort";
            GameEngine.boardUnits[7][3].defence += 1;
            GameEngine.boardUnits[7][3].movement = 0;
            GameEngine.boardUnits[7][3].AI_value += 0.5;
            GameEngine.boardUnits[7][3].brightenIcon();

            new Infantry(GameView.theContext, 7, 4, GameEngine.red);
            GameEngine.boardUnits[7][4].unitType = "Fort";
            GameEngine.boardUnits[7][4].defence += 1;
            GameEngine.boardUnits[7][4].movement = 0;
            GameEngine.boardUnits[7][4].AI_value += 0.5;
            GameEngine.boardUnits[7][4].brightenIcon();

            AI.initializeAI();
            GameEngine.red.isHuman = false;
            GameEngine.AIPlayer = GameEngine.red;
        }

    }

    public static void drawMapFeatures(Canvas canvas) {
        if (map_code == -1) return;
        if (map_code == 0) {
            return;
        } if (map_code == 1) {
            return;
        }
    }

    public static String[] getAIResourcePoints() {
        String[] toReturn = new String[0];
        if (map_code == 0) {
            return toReturn;
        }
        if (map_code == 1) {
            return toReturn;
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
        }
    }

    //how many turns are required for each level
    public static int[] turnsForStars() {
        int level = map_code;
        switch (level) {
            case 0: return new int[]{6,8,10};
        }
        return new int[]{0,0,0};
    }

    public static int[] starsAchieved(int turnCount) {
        int[] starReq = turnsForStars();
        if (turnCount <= starReq[0]) {
            return new int[]{3,map_code};
        } else if (turnCount <= starReq[1]) {
            return new int[]{2,map_code};
        } else if (turnCount <= starReq[2]) {
            return new int[]{1,map_code};
        } else {
            return new int[]{0,map_code};
        }
    }
}

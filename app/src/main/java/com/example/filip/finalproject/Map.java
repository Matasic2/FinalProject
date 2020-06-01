package com.example.filip.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Map {

    public static int map_code = 0;
    public static int number_of_maps_available = 3;

    public static void generateMap(Bitmap map, Bitmap mapAir, Bitmap square) {
        if (map_code == 0) {

            GameView.grid = new GameEngine(map, mapAir, square, 21, 12); // these lines create the board.

            GameEngine.redDeployX = 17;
            GameEngine.redDeployY = 9;

            GameEngine.airLinesCount = 4;
            GameEngine.airLineXScaleFactor = 15.0f/21.0f; //15 is standard number of x squares and 21 is map's number of x squares
            GameEngine.airLineYScaleFactor = 9.0f/12.0f; //same as above
            GameEngine.planeLines = new Planes[4][2];
            GameEngine.fogOfWarIsRevealedForGreen = new boolean[4];
            GameEngine.fogOfWarIsRevealedForRed = new boolean[4];

            // next lines generate green's "natural resources" (the resources which are expected to be controlled by green player).
            new Food(GameView.theContext, 1, 0, 1, 1);
            new Food(GameView.theContext, 2, 1, 1, 1);
            new Food(GameView.theContext, 1, 2, 1, 1);
            new oil(GameView.theContext, 0, 1, 1, 1);

            new Food(GameView.theContext, 2, 6, 1, 6);
            new Food(GameView.theContext, 1, 7, 1, 6);
            new oil(GameView.theContext, 1, 5, 1, 6);
            new Iron(GameView.theContext, 0, 6, 1, 6);

            new Food(GameView.theContext, 7, 1, 8, 1);
            new oil(GameView.theContext, 8, 0, 8, 1);
            new Iron(GameView.theContext, 8, 2, 8, 1);

            // next lines generate red's "natural resources" (the resources which are expected to be controlled by red player).
            new Food(GameView.theContext, 18, 11, 18, 10);
            new Food(GameView.theContext, 17, 10, 18, 10);
            new Food(GameView.theContext, 18, 9, 18, 10);
            new oil(GameView.theContext, 19, 10, 18, 10);

            new Food(GameView.theContext, 17, 5, 18, 5);
            new Food(GameView.theContext, 18, 4, 18, 5);
            new Iron(GameView.theContext, 19, 5, 18, 5);
            new oil(GameView.theContext, 18, 6, 18, 5);

            new Food(GameView.theContext, 13, 10, 12, 10);
            new oil(GameView.theContext, 12, 11, 12, 10);
            new Iron(GameView.theContext, 12, 9, 12, 10);

            //contested resources, the ones which both players should have equal chances to collect. Will probably be main area for battles.
            //new Iron(GameView.theContext, 9, 5, 10, 5);
            //new Iron(GameView.theContext, 11, 5, 10, 5);
            //new oil(GameView.theContext, 10, 4, 10, 5);
            //new oil(GameView.theContext, 10, 6, 10, 5);

            new Food(GameView.theContext, 3, 11, 3, 10);
            new Food(GameView.theContext, 3, 9, 3, 10);
            new Iron(GameView.theContext, 2, 10, 3, 10);

            new Food(GameView.theContext, 17, 0, 17, 1);
            new Food(GameView.theContext, 17, 2, 17, 1);
            new Iron(GameView.theContext, 18, 1, 17, 1);


            if (MainMenu.scenario.equals("Skirmish") || MainMenu.scenario.equals("Skirmish vs AI") || MainMenu.scenario.equals("Skirmish vs AI_cheating")) {
                new Headquaters(GameView.theContext, 1, 1, GameEngine.green);
                new Headquaters(GameView.theContext, 18, 10, GameEngine.red);

                // These for loops create starting units.
                for (int i = 0; i < 0; i++) {
                    new Infantry(GameView.theContext, 2, i * 2, GameEngine.green);
                }

                for (int i = 0; i < 0; i++) {
                    new Infantry(GameView.theContext, 12, 6, GameEngine.red);
                }

                for (int i = 0; i < 1; i++) {
                    new Cavalry(GameView.theContext, 2, 2, GameEngine.green);
                }
                for (int i = 0; i < 1; i++) {
                    new Cavalry(GameView.theContext, 17, 9, GameEngine.red);
                }

                for (int i = 0; i < 0; i++) {
                    new Artillery(GameView.theContext, 14, 8, GameEngine.green);
                }
                for (int i = 0; i < 0; i++) {
                    new Artillery(GameView.theContext, 13, i, GameEngine.red);
                }
            }
        } else if (map_code == 1) {
            GameView.grid = new GameEngine(map, mapAir, square, 15, 3); // these lines create the board.
            GameEngine.redDeployX = 12;
            GameEngine.redDeployY = 2;
            GameEngine.airLinesCount = 1;
            GameEngine.airLineXScaleFactor = 1;
            GameEngine.airLineYScaleFactor = 1;

            // next lines generate green's "natural resources" (the resources which are expected to be controlled by green player).
            new Food(GameView.theContext, 1, 0, 1, 1);
            new Food(GameView.theContext, 2, 1, 1, 1);
            new Food(GameView.theContext, 1, 2, 1, 1);
            new oil(GameView.theContext, 0, 1, 1, 1);


            // next lines generate red's "natural resources" (the resources which are expected to be controlled by red player).
            new Food(GameView.theContext, 13, 0, 13, 1);
            new Food(GameView.theContext, 14, 1, 13, 1);
            new Food(GameView.theContext, 13, 2, 13, 1);
            new oil(GameView.theContext, 12, 1, 13, 1);

            //contested resources, the ones which both players should have equal chances to collect. Will probably be main area for battles.


            if (MainMenu.scenario.equals("Skirmish") || MainMenu.scenario.equals("Skirmish vs AI") || MainMenu.scenario.equals("Skirmish vs AI_cheating")) {
                new Headquaters(GameView.theContext, 1, 1, GameEngine.green);
                new Headquaters(GameView.theContext, 13, 1, GameEngine.red);

                // These for loops create starting units.
                for (int i = 0; i < 0; i++) {
                    new Infantry(GameView.theContext, 2, i * 2, GameEngine.green);
                }

                for (int i = 0; i < 0; i++) {
                    new Infantry(GameView.theContext, 12, 6, GameEngine.red);
                }

                for (int i = 0; i < 1; i++) {
                    new Cavalry(GameView.theContext, 2, 2, GameEngine.green);
                }
                for (int i = 0; i < 1; i++) {
                    new Cavalry(GameView.theContext, 12, 2, GameEngine.red);
                }

                for (int i = 0; i < 0; i++) {
                    new Artillery(GameView.theContext, 14, 8, GameEngine.green);
                }
                for (int i = 0; i < 0; i++) {
                    new Artillery(GameView.theContext, 13, i, GameEngine.red);
                }
            }
        } else if (map_code == 2) {

            GameView.grid = new GameEngine(map, mapAir, square, 15, 9); // these lines create the board.
            GameEngine.redDeployX = 12;
            GameEngine.redDeployY = 6;
            GameEngine.airLinesCount = 3;
            GameEngine.airLineXScaleFactor = 1;
            GameEngine.airLineYScaleFactor = 1;

            // next lines generate green's "natural resources" (the resources which are expected to be controlled by green player).
            new Food(GameView.theContext, 1, 0, 1, 1);
            new Food(GameView.theContext, 2, 1, 1, 1);
            new Food(GameView.theContext, 1, 2, 1, 1);
            new oil(GameView.theContext, 0, 1, 1, 1);

            new Food(GameView.theContext, 2, 7, 1, 7);
            new Food(GameView.theContext, 1, 8, 1, 7);
            new oil(GameView.theContext, 1, 6, 1, 7);
            new Iron(GameView.theContext, 0, 7, 1, 7);

            new Food(GameView.theContext, 6, 1, 7, 1);
            new oil(GameView.theContext, 7, 0, 7, 1);
            new Iron(GameView.theContext, 7, 2, 7, 1);

            // next lines generate red's "natural resources" (the resources which are expected to be controlled by red player).
            new Food(GameView.theContext, 13, 6, 13, 7);
            new Food(GameView.theContext, 12, 7, 13, 7);
            new Food(GameView.theContext, 13, 8, 13, 7);
            new oil(GameView.theContext, 14, 7, 13, 7);

            new Food(GameView.theContext, 12, 1, 13, 1);
            new Food(GameView.theContext, 13, 0, 13, 1);
            new Iron(GameView.theContext, 14, 1, 13, 1);
            new oil(GameView.theContext, 13, 2, 13, 1);

            new Food(GameView.theContext, 9, 7, 8, 7);
            new oil(GameView.theContext, 8, 8, 8, 7);
            new Iron(GameView.theContext, 8, 6, 8, 7);

            if (MainMenu.scenario.equals("Skirmish") || MainMenu.scenario.equals("Skirmish vs AI") || MainMenu.scenario.equals("Skirmish vs AI_cheating")) {
                new Headquaters(GameView.theContext, 1, 1, GameEngine.green);
                new Headquaters(GameView.theContext, 13, 7, GameEngine.red);

                // These for loops create starting units.
                for (int i = 0; i < 0; i++) {
                    new Infantry(GameView.theContext, 2, i * 2, GameEngine.green);
                }

                for (int i = 0; i < 0; i++) {
                    new Infantry(GameView.theContext, 12, 6, GameEngine.red);
                }

                for (int i = 0; i < 1; i++) {
                    new Cavalry(GameView.theContext, 2, 2, GameEngine.green);
                }
                for (int i = 0; i < 1; i++) {
                    new Cavalry(GameView.theContext, 12, 6, GameEngine.red);
                }

                for (int i = 0; i < 0; i++) {
                    new Artillery(GameView.theContext, 14, 8, GameEngine.green);
                }
                for (int i = 0; i < 0; i++) {
                    new Artillery(GameView.theContext, 13, i, GameEngine.red);
                }
            }
        }
        // this will scale air layout                         //add  * GameEngine.airLineXScaleFactor is x also needs scaling
         GameEngine.emptyAirLine = Bitmap.createScaledBitmap(mapAir, (int) (mapAir.getWidth()), (int) (mapAir.getHeight() * GameEngine.airLineYScaleFactor), true);
    }

    public static void drawMapFeatures(Canvas canvas) {
        if (map_code == 0) {
            GameView.pointers.draw(canvas, 1, 6);
            GameView.pointers.draw(canvas, 18, 5);
            GameView.pointers1.draw(canvas, 8, 1);
            GameView.pointers1.draw(canvas, 3, 10);
            GameView.pointers2.draw(canvas, 12, 10);
            GameView.pointers2.draw(canvas, 17, 1);

            //GameView.pointers.draw(canvas, 10, 5);
        } else if (map_code == 1) {
            return;
        } else if (map_code == 2) {
            GameView.pointers.draw(canvas, 1, 7);
            GameView.pointers.draw(canvas, 13, 1);
            GameView.pointers1.draw(canvas, 7, 1);
            GameView.pointers2.draw(canvas, 8, 7);
        }
    }

    public static String[] getAIResourcePoints() {
        String[] toReturn = new String[0];
        if (map_code == 0) {
            toReturn = new String[]{"18,5", "12,10", "17,1"};
        }
        if (map_code == 1) {
            return toReturn;
        }
        if (map_code == 2) {
            toReturn = new String[]{"13,1", "8,7"};
        }
        return toReturn;
    }

}

package com.example.filip.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Map {

    public static int map_code = 0;

    public static void generateMap(int map_code,Bitmap map, Bitmap mapAir, Bitmap square) {
        Map.map_code = map_code;


        if (map_code == 0) {

            GameView.grid = new GameEngine(map, mapAir, square, 21, 11); // these lines create the board.
            GameView.grid.redDeployX = 17;
            GameView.grid.redDeployY = 8;

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
            new Food(GameView.theContext, 18, 10, 18, 9);
            new Food(GameView.theContext, 17, 9, 18, 9);
            new Food(GameView.theContext, 18, 8, 18, 9);
            new oil(GameView.theContext, 19, 9, 18, 9);

            new Food(GameView.theContext, 17, 4, 18, 4);
            new Food(GameView.theContext, 18, 3, 18, 4);
            new Iron(GameView.theContext, 19, 4, 18, 4);
            new oil(GameView.theContext, 18, 5, 18, 4);

            new Food(GameView.theContext, 13, 9, 12, 9);
            new oil(GameView.theContext, 12, 10, 12, 9);
            new Iron(GameView.theContext, 12, 8, 12, 9);

            //contested resources, the ones which both players should have equal chances to collect. Will probably be main area for battles.
            new Iron(GameView.theContext, 9, 5, 10, 5);
            new Iron(GameView.theContext, 11, 5, 10, 5);
            new oil(GameView.theContext, 10, 4, 10, 5);
            new oil(GameView.theContext, 10, 6, 10, 5);


            if (MainMenu.scenario.equals("Skirmish") || MainMenu.scenario.equals("Skirmish vs AI") || MainMenu.scenario.equals("Skirmish vs AI_cheating")) {
                new Headquaters(GameView.theContext, 1, 1, GameEngine.green);
                new Headquaters(GameView.theContext, 18, 9, GameEngine.red);

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
                    new Cavalry(GameView.theContext, 17, 8, GameEngine.red);
                }

                for (int i = 0; i < 0; i++) {
                    new Artillery(GameView.theContext, 14, 8, GameEngine.green);
                }
                for (int i = 0; i < 0; i++) {
                    new Artillery(GameView.theContext, 13, i, GameEngine.red);
                }
            }
        } else if (map_code == 1) {
            GameView.grid = new GameEngine(map, mapAir, square, 10, 5); // these lines create the board.
            GameView.grid.redDeployX = 7;
            GameView.grid.redDeployY = 2;

            // next lines generate green's "natural resources" (the resources which are expected to be controlled by green player).
            new Food(GameView.theContext, 1, 0, 1, 1);
            new Food(GameView.theContext, 2, 1, 1, 1);
            new Food(GameView.theContext, 1, 2, 1, 1);
            new oil(GameView.theContext, 0, 1, 1, 1);


            // next lines generate red's "natural resources" (the resources which are expected to be controlled by red player).
            new Food(GameView.theContext, 8, 2, 8, 3);
            new Food(GameView.theContext, 9, 3, 8, 3);
            new Food(GameView.theContext, 8, 4, 8, 3);
            new oil(GameView.theContext, 7, 3, 8, 3);

            //contested resources, the ones which both players should have equal chances to collect. Will probably be main area for battles.


            if (MainMenu.scenario.equals("Skirmish") || MainMenu.scenario.equals("Skirmish vs AI") || MainMenu.scenario.equals("Skirmish vs AI_cheating")) {
                new Headquaters(GameView.theContext, 1, 1, GameEngine.green);
                new Headquaters(GameView.theContext, 8, 3, GameEngine.red);

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
                    new Cavalry(GameView.theContext, 7, 2, GameEngine.red);
                }

                for (int i = 0; i < 0; i++) {
                    new Artillery(GameView.theContext, 14, 8, GameEngine.green);
                }
                for (int i = 0; i < 0; i++) {
                    new Artillery(GameView.theContext, 13, i, GameEngine.red);
                }
            }
        }
    }

    public static void drawMapFeatures(Canvas canvas) {
        if (map_code == 0) {
            GameView.pointers.draw(canvas, 1, 6);
            GameView.pointers.draw(canvas, 18, 4);
            GameView.pointers1.draw(canvas, 8, 1);
            GameView.pointers2.draw(canvas, 12, 9);

            GameView.pointers.draw(canvas, 10, 5);
        } else if (map_code == 1) {
            return;
        }
    }

}

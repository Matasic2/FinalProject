package com.example.filip.finalproject;


import android.graphics.Canvas;

public class TechTree {

    public static TechNode rootTech;
    public static String scenario = "";

    public static void initializeTechTree(String scenario) {
        if (!scenario.equals("skirmish")) {
            return;
        }

        TechTree.scenario = scenario;

        MainThread.techScreenCameraXLimit = (int) (4000 * FullscreenActivity.scaleFactor);

        GameEngine.green.techs = new boolean[20];
        GameEngine.red.techs = new boolean[20];
        GameEngine.green.techs[0] = true;
        GameEngine.red.techs[0] = true;
        rootTech = new TechNode();
        rootTech.name = "rootTech";

        for (int i = 0; i < GameEngine.green.upgradesUnlocked.length; i++) {
            for (int j = 0; j < GameEngine.green.upgradesUnlocked[i].length; j++) {
                if (i == 1 || i == 3) {
                    GameEngine.green.upgradesUnlocked[i][j] = false;
                }
            }
        }
        for (int i = 0; i < GameEngine.red.upgradesUnlocked.length; i++) {
            for (int j = 0; j < GameEngine.red.upgradesUnlocked[i].length; j++) {
                if (i == 1 || i == 3) {
                    GameEngine.red.upgradesUnlocked[i][j] = false;
                }
            }
        }

        GameEngine.green.bonusResearch = 2;
        GameEngine.red.bonusResearch = 2;

        //----------------------------------Economy Upgrades---------------------------------------
        rootTech.children = new TechNode[3];
        TechNode food1 = new TechNode(
                rootTech, null, 1,
                100, 100,
                "food1", "+1 food per turn", null, null,
                0, 1, 0, 4
        );

        rootTech.children[0] = food1;

        food1.children = new TechNode[2];
        TechNode food2 = new TechNode(
                food1, null, 3,
                100, 400,
                "food2", "+1 food per turn", null, null,
                0, 2, 2, 4
        );
        TechNode iron1 = new TechNode(
                food1, null, 4,
                550, 400,
                "iron1", "+1 iron per turn", null, null,
                0, 2, 0, 4
        );
        food1.children[0] = food2;
        food1.children[1] = iron1;

        food2.children = new TechNode[1];
        iron1.children = new TechNode[2];
        TechNode food3 = new TechNode(
                food2, null, 5,
                100, 700,
                "food3", "+1 food per turn", null, null,
                0, 4, 4, 6
        );
        TechNode iron2 = new TechNode(
                iron1, null, 6,
                550, 700,
                "iron1", "+1 iron per turn", null, null,
                0, 4, 4, 6
        );
        TechNode oil1 = new TechNode(
                iron1, null, 7,
                1000, 700,
                "oil2", "+1 oil per turn", null, null,
                0, 4, 4, 6
        );
        food2.children[0] = food3;
        iron1.children[0] = iron2;
        iron1.children[1] = oil1;

        TechNode food4 = new TechNode(
                food3, null, 8,
                100, 1000,
                "food4", "+1 food per turn", null, null,
                0, 4, 4, 6
        );
        food3.children = new TechNode[1];
        food3.children[0] = food4;


        //----------------------------------Infantry Upgrades---------------------------------------
        TechNode infantry1 = new TechNode(
                rootTech, null, 2,
                1900, 100,
                "infantry1", "+1 defence", "", "",
                0, 2, 0, 6
        );
        rootTech.children[1] = infantry1;
        infantry1.children = new TechNode[3];

        TechNode infantryStorm = new TechNode(
                infantry1, null, 9,
                1450, 400,
                "infantryStorm", "Unlock upgrade 1", "", "",
                0, 2, 0, 6
        );
        TechNode infantryArmor = new TechNode(
                infantry1, null, 10,
                1900, 400,
                "infantryArmor", "+1 defence", "Unlock upgrade 2", "",
                0, 2, 0, 6
        );
        TechNode infantryAT = new TechNode(
                infantry1, null, 11,
                2350, 400,
                "infantryAT", "Unlock upgrade 3", "", "",
                0, 2, 0, 6
        );
        infantry1.children[0] = infantryStorm;
        infantry1.children[1] = infantryArmor;
        infantry1.children[2] = infantryAT;

        //----------------------------------Armor Upgrades---------------------------------------

        TechNode armor1 = new TechNode(
                rootTech, null, 12,
                3200, 100,
                "armor1", "unlock all upgrades", "", "",
                0, 2, 0, 6
        );
        rootTech.children[2] = armor1;
        //armor1.children = new TechNode[3];

    }

    private static TechNode findTappedNodeRecursive(TechNode current, int x, int y) {
        if (current == null) {
            return null;
        }

        if (current.x < x && current.x + 500 * FullscreenActivity.scaleFactor > x
            && current.y < y && current.y + 256 * FullscreenActivity.scaleFactor > y) {
            return current;
        }

        if (current.children == null) {
            return null;
        }

        for (int i = 0; i < current.children.length; i++) {
            TechNode searchResult = findTappedNodeRecursive(current.children[i],x,y);
            if (searchResult != null) {
                return searchResult;
            }
        }

        return null;
    }

    public static TechNode findTappedTechNode(int x, int y) {
       if (rootTech == null || rootTech.children == null) {
           return null;
       }

       for (int i = 0; i < rootTech.children.length; i++) {
           TechNode searchResult = findTappedNodeRecursive(rootTech.children[i],x,y);
           if (searchResult != null) {
               return searchResult;
           }
       }

        return null;
    }

    public static boolean playerHasTech(TechNode node, Player player) {
        return player.techs[node.techNumber];
    }

    public static void researchTech(TechNode node) {
        if (GameEngine.playing.foodStorage >= node.foodPrice
            && GameEngine.playing.ironStorage >= node.ironPrice
            && GameEngine.playing.oilStorage >= node.oilPrice
            && GameEngine.playing.researchPoints >= node.researchPrice
            && !playerHasTech(node, GameEngine.playing)
            && playerHasTech(node.parent, GameEngine.playing)) {

            GameEngine.playing.foodStorage -= node.foodPrice;
            GameEngine.playing.ironStorage -= node.ironPrice;
            GameEngine.playing.oilStorage -= node.oilPrice;
            GameEngine.playing.researchPoints -= node.researchPrice;
            addTechToPlayer(node, GameEngine.playing);
        }
    }

    public static void draw(Canvas canvas) {
        rootTech.draw(canvas);
    }

    public static void addTechToPlayer(TechNode node, Player player) {
        int techNum = node.techNumber;
        player.techs[techNum] = true;

        if (techNum == 1) {
            GameEngine.playing.bonusFood++;
            GameEngine.estimateResources();
        }

        if (techNum == 2) {
            if (player == GameEngine.green) {
                Infantry.GreenDefence++;
            } else {
                Infantry.RedDefence++;
            }
        }

        if (techNum == 3) {
            GameEngine.playing.bonusFood++;
            GameEngine.estimateResources();
        }

        if (techNum == 4) {
            GameEngine.playing.bonusIron++;
            GameEngine.estimateResources();
        }

        if (techNum == 5) {
            GameEngine.playing.bonusFood++;
            GameEngine.estimateResources();
        }

        if (techNum == 6) {
            GameEngine.playing.bonusIron++;
            GameEngine.estimateResources();
        }

        if (techNum == 7) {
            GameEngine.playing.bonusOil++;
            GameEngine.estimateResources();
        }

        if (techNum == 8) {
            GameEngine.playing.bonusFood++;
            GameEngine.estimateResources();
        }

        if (techNum == 9) {
            GameEngine.playing.upgradesUnlocked[1][0] = true;
        }

        if (techNum == 10) {
            GameEngine.playing.upgradesUnlocked[1][1] = true;
            if (player == GameEngine.green) {
                Infantry.GreenDefence++;
            } else {
                Infantry.RedDefence++;
            }
        }

        if (techNum == 11) {
            GameEngine.playing.upgradesUnlocked[1][2] = true;

        }

        if (techNum == 12) {
            GameEngine.playing.upgradesUnlocked[3][0] = true;
            GameEngine.playing.upgradesUnlocked[3][1] = true;
            GameEngine.playing.upgradesUnlocked[3][2] = true;
        }
    }
}

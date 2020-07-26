package com.example.filip.finalproject;


import android.graphics.Canvas;

public class TechTree {

    public static TechNode rootTech;

    public static void initializeTechTree(String scenario) {
        if (!scenario.equals("skirmish")) {
            return;
        }

        GameEngine.green.techs = new boolean[9];
        GameEngine.red.techs = new boolean[9];
        GameEngine.green.techs[0] = true;
        GameEngine.red.techs[0] = true;
        rootTech = new TechNode();
        rootTech.name = "rootTech";

        rootTech.children = new TechNode[2];
        TechNode food1 = new TechNode(
                rootTech, null, 1,
                100, 100,
                "food1", "+1 food per turn", null, null,
                0, 6, 6, 5
        );
        TechNode infantry1 = new TechNode(
                rootTech, null, 2,
                1600, 100,
                "infantry1", "+1 defence", "+1 ranged", "attack damage",
                0, 6, 0, 5
        );
        rootTech.children[0] = food1;
        rootTech.children[1] = infantry1;

        food1.children = new TechNode[2];
        TechNode food2 = new TechNode(
                food1, null, 3,
                100, 400,
                "food2", "+1 food per turn", null, null,
                0, 6, 6, 5
        );
        TechNode iron1 = new TechNode(
                food1, null, 4,
                550, 400,
                "iron1", "+1 iron per turn", null, null,
                0, 6, 0, 5
        );
        food1.children[0] = food2;
        food1.children[1] = iron1;

        food2.children = new TechNode[1];
        iron1.children = new TechNode[2];
        TechNode food3 = new TechNode(
                food2, null, 5,
                100, 700,
                "food3", "+1 food per turn", null, null,
                0, 6, 6, 5
        );
        TechNode iron2 = new TechNode(
                iron1, null, 6,
                550, 700,
                "iron1", "+1 iron per turn", null, null,
                0, 6, 0, 5
        );
        TechNode oil1 = new TechNode(
                iron1, null, 7,
                1000, 700,
                "oil1", "+1 oil per turn", null, null,
                0, 6, 0, 5
        );
        food2.children[0] = food3;
        iron1.children[0] = iron2;
        iron1.children[1] = oil1;
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
    }
}

package com.example.filip.finalproject;


import android.graphics.Canvas;

public class TechTree {

    public static TechNode rootTech;

    public static void initializeTechTree(String scenario) {
        if (!scenario.equals("skirmish")) {
            return;
        }

        GameEngine.green.techs = new boolean[3];
        GameEngine.red.techs = new boolean[3];
        rootTech = new TechNode();
        rootTech.name = "rootTech";

        rootTech.children = new TechNode[2];
        rootTech.children[0] = new TechNode(
                rootTech, null, 1,
                100, 100,
                "economy1", "+1 food per turn", "+1 iron per turn", "+1 oil per turn",
                0, 6, 6, 5
        );
        rootTech.children[1] = new TechNode(
                rootTech, null, 2,
                700, 100,
                "infantry1", "+1 defence", null, null,
                0, 6, 0, 5
        );
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
            && !playerHasTech(node, GameEngine.playing)) {

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
            GameEngine.playing.bonusIron++;
            GameEngine.playing.bonusOil++;
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

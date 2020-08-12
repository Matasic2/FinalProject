package com.example.filip.finalproject;

public class Player {

    public String color; //Player's color

    public int oilStorage = 0; //Player's oil storage value
    public int ironStorage = 0;//Player's iron storage value
    public int foodStorage = 0;//Player's food storage value
    public int researchPoints = 0;

    public int bonusFood = 0; //extra food that player gets at the start of every turn
    public int bonusIron = 0;
    public int bonusOil = 0;
    public int bonusResearch = 0;

    public Planes[] hangar = new Planes[6];
    public boolean[][] upgrades = new boolean[5][3]; //Player's loadout menu upgrades
    public boolean[][] upgradesUnlocked = new boolean[5][3]; //Player's loadout menu upgrades TODO: FIX
    public boolean[] techs = new boolean[0];

    public boolean isHuman; //is the player human

    Player (String str, boolean human) {
        color = str;
        isHuman = human; //is the player human?

        //upgrades are disabled by default
        for (int i = 0; i < upgrades.length; i++) {
            for (int j = 0; j < upgrades[i].length; j++) {
                upgrades[i][j] = false;
            }
        }

        for (int i = 0; i < upgradesUnlocked.length; i++) {
            for (int j = 0; j < upgradesUnlocked[i].length; j++) {
                upgradesUnlocked[i][j] = true;
            }
        }
    }

    //adjust upgrades, x is unit, y is upgrade slot
    public void adjustUpgrades(String unitType, int y) {
        int x = 0;
        if (unitType == "Cavalry") {
            x = 0;
        } else if (unitType == "Infantry") {
            x = 1;
        } else if (unitType == "Artillery") {
            x = 2;
        } else if (unitType == "Armor") {
            x = 3;
        }

        if (!upgradesUnlocked[x][y]) {
            return;
        }

        upgrades[x][y] = !upgrades[x][y];

        int factor = 1;
        if (!upgrades[x][y]) {
            factor = -1;
        }

        if (x == 0) {
            Cavalry.adjustUpgrade(this, factor, y);
        }
        if (x == 1) {
            Infantry.adjustUpgrade(this, factor, y);
        }
        if (x == 2) {
            Artillery.adjustUpgrade(this, factor, y);
        }
        if (x == 3) {
            Armor.adjustUpgrade(this, factor, y);
        }
    }

    public static String print(Player p) {
        return p.color;
    }

    public void addPlane(Planes plane) {
        for (int i = 0; i < hangar.length; i++) {
            if (hangar[i] == null) {
                hangar[i] = plane;
                return;
            }
        }
    }

    public boolean hangarHasEmptySlots() {
        for (int i = 0; i < hangar.length; i++) {
            if (hangar[i] == null) {
                return true;
            }
        }
        return false;
    }

    public void selectPlane(int index) {
        if (hangar[index] != null) {
            hangar[index].select();
        }
    }

    public void removeFromHanger(Planes plane) {
        for (int i = 0; i < hangar.length; i++) {
            if (hangar[i] != null && hangar[i].equals(plane)) {
                hangar[i].unselect();
                hangar[i] = null;
            }
        }
    }
}

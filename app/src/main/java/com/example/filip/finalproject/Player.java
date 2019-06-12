package com.example.filip.finalproject;

public class Player {

    public String color; //Player's color
    public int oilStorage = 0; //Player's oil storage value
    public int ironStorage = 0;//Player's iron storage value
    public int foodStorage = 0;//Player's food storage value
    public Planes[] hangar = new Planes[6];
    public int[] upgrades = new int[10]; //Player's upgrades, empty and unused for now

    public boolean isHuman; //is the player human

    Player (String str, boolean human) {
        color = str;
        isHuman = human; //is the player human?
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

    public void selectPlane(int index) {
        if (hangar[index] != null) {hangar[index].select();}
    }
    public void removeFromHanger(Planes plane) {
        for (int i = 0; i < hangar.length; i++) {
            if (hangar[i] != null && hangar[i].equals(plane)) {
                hangar[i] = null;
            }
        }
    }
}

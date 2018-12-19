package com.example.filip.finalproject;

public class Player {

    public String color; //Player's color
    public int oilStorage = 0; //Player's oil storage value
    public int ironStorage = 0;
    public int foodStorage = 0;
    public int[] upgrades = new int[10]; //Player's upgrades, empty and unused for now

    public boolean isHuman;

    Player (String str, boolean human) {
        color = str;
        isHuman = human;
    }
    public static String print(Player p) {
        return p.color;
    }
}

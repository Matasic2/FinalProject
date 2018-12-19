package com.example.filip.finalproject;

public class Player {

    public String color; //Player's color
    public int oilStorage = 0; //Player's oil storage value
    public int ironStorage = 0;//Player's iron storage value
    public int foodStorage = 0;//Player's food storage value
    public int[] upgrades = new int[10]; //Player's upgrades, empty and unused for now

    public boolean isHuman; //is the player human

    Player (String str, boolean human) {
        color = str;
        isHuman = human; //is the player human?
    }
    public static String print(Player p) {
        return p.color;
    }
}

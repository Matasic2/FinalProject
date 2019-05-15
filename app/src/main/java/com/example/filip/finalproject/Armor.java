package com.example.filip.finalproject;

import android.content.Context;

public class Armor extends Units {

    // TODO : fix these comments
    public static int GreenAttack1 = 15; // Green Armor's first attack value, this value can be changed
    public static int GreenAttack2 = 10; //  Green Armor's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green Armor's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 4; //  Green Armor's second attack value, this value can be changed
    public static int GreenDefence = 3; // Defence value of Green's Armor, this value can be changed
    public static int GreenHP = 25; // HP value of Green's Armor, this value can be changed
    public static int GreenMovement = 2; // Movement value of Green's Armor, this value can be changed
    public static int GreenVisibility = 4; // Movement value of Green's Infantry, this value can be changed

    //cost of Armor unit
    public static int foodPrice = 2;
    public static int ironPrice = 4;
    public static int oilPrice = 20;

    //How much health will the unit gain if healed
    public static int healedBy = 4;

    public static int RedAttack1 = 15; // Red Armor's first attack value, this value can be changed
    public static int RedAttack2 = 10; //  Red Armor's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red Armor's first attack range, this value can be changed
    public static int RedSecondAttackRange = 4; //  Red Armor's second attack value, this value can be changed
    public static int RedDefence = 3; // Defence value of Red's Armor, this value can be changed
    public static int RedHP = 25; // HP value of Red's Armor, this value can be changed
    public static int RedMovement = 2; // Movement value of Red's Armor, this value can be changed
    public static int RedVisibility = 4; // Movement value of Green's Infantry, this value can be changed

    Armor(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Armor");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement, GreenVisibility);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement,RedVisibility);
        }
    }
}

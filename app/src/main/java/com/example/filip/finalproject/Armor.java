package com.example.filip.finalproject;

import android.content.Context;

public class Armor extends Units {

    // TODO : fix these comments
    public static int GreenAttack1 = 15; // Green Armor's first attack value, this value can be changed
    public static int GreenAttack2 = 6; //  Green Armor's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green Armor's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 4; //  Green Armor's second attack value, this value can be changed
    public static int GreenDefence = 2; // Defence value of Green's Armor, this value can be changed
    public static int GreenHP = 20; // HP value of Green's Armor, this value can be changed
    public static int GreenMovement = 2; // Movement value of Green's Armor, this value can be changed
    public static int GreenVisibility = 4; // Movement value of Green's Infantry, this value can be changed
    public static int greenAirAttack = 0;

    //cost of Armor unit
    public static int foodPrice = 2;
    public static int ironPrice = 4;
    public static int oilPrice = 2;

    //How much health will the unit gain if healed
    public static int healedBy = 6;

    public static int RedAttack1 = 15; // Red Armor's first attack value, this value can be changed
    public static int RedAttack2 = 6; //  Red Armor's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red Armor's first attack range, this value can be changed
    public static int RedSecondAttackRange = 4; //  Red Armor's second attack value, this value can be changed
    public static int RedDefence = 2; // Defence value of Red's Armor, this value can be changed
    public static int RedHP = 20; // HP value of Red's Armor, this value can be changed
    public static int RedMovement = 2; // Movement value of Red's Armor, this value can be changed
    public static int RedVisibility = 4; // Movement value of Green's Infantry, this value can be changed
    public static int redAirAttack = 0;

    Armor(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Armor");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement, GreenVisibility, greenAirAttack);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement,RedVisibility, redAirAttack);
        }
    }
}

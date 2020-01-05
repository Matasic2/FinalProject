package com.example.filip.finalproject;

import android.content.Context;

public class Infantry extends Units {

    // TODO : fix these comments
    public static int GreenAttack1 = 12; // Green Infantry's first attack value, this value can be changed
    public static int GreenAttack2 = 4; //  Green Infantry's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green Infantry's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 5; //  Green Infantry's second attack value, this value can be changed
    public static int GreenDefence = 1; // Defence value of Green's Infantry, this value can be changed
    public static int GreenHP = 14; // HP value of Green's Infantry, this value can be changed
    public static int GreenMovement = 2; // Movement value of Green's Infantry, this value can be changed
    public static int GreenVisibility = 4; // Movement value of Green's Infantry, this value can be changed
    public static int greenAirAttack = 1;
    
    //cost of Infantry unit
    public static int foodPrice = 6;
    public static int ironPrice = 1;
    public static int oilPrice = 0;

    //How much health will the unit gain if healed
    public static int healedBy = 4;

    public static int RedAttack1 = 12; // Red Infantry's first attack value, this value can be changed
    public static int RedAttack2 = 4; //  Red Infantry's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red Infantry's first attack range, this value can be changed
    public static int RedSecondAttackRange = 5; //  Red Infantry's second attack value, this value can be changed
    public static int RedDefence = 1; // Defence value of Red's Infantry, this value can be changed
    public static int RedHP = 14; // HP value of Red's Infantry, this value can be changed
    public static int RedMovement = 2; // Movement value of Red's Infantry, this value can be changed
    public static int RedVisibility = 4; // Movement value of Green's Infantry, this value can be changed
    public static int redAirAttack = 1;

    Infantry(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Infantry");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement, GreenVisibility, greenAirAttack);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement, RedVisibility, redAirAttack);
        }
    }
}

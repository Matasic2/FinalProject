package com.example.filip.finalproject;

import android.content.Context;

public class Infantry extends Units {

    // TODO : fix these comments
    public static int GreenAttack1 = 12; // Green Infantry's first attack value, this value can be changed
    public static int GreenAttack2 = 3; //  Green Infantry's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green Infantry's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 4; //  Green Infantry's second attack value, this value can be changed
    public static int GreenDefence = 1; // Defence value of Green's Infantry, this value can be changed
    public static int GreenHP = 6; // HP value of Green's Infantry, this value can be changed
    public static int GreenMovement = 2; // Movement value of Green's Infantry, this value can be changed
    public static int GreenVisibility = 4; // Movement value of Green's Infantry, this value can be changed
    
    //cost of Cavalry unit
    public static int foodPrice = 3;
    public static int ironPrice = 1;
    public static int oilPrice = 0;

    //How much health will the unit gain if healed
    public static int healedBy = 3;

    public static int RedAttack1 = 12; // Red Infantry's first attack value, this value can be changed
    public static int RedAttack2 = 3; //  Red Infantry's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red Infantry's first attack range, this value can be changed
    public static int RedSecondAttackRange = 4; //  Red Infantry's second attack value, this value can be changed
    public static int RedDefence = 1; // Defence value of Red's Infantry, this value can be changed
    public static int RedHP = 6; // HP value of Red's Infantry, this value can be changed
    public static int RedMovement = 2; // Movement value of Red's Infantry, this value can be changed
    public static int RedVisibility = 4; // Movement value of Green's Infantry, this value can be changed

    Infantry(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Infantry");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement, GreenVisibility);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement, RedVisibility);
        }
    }
}

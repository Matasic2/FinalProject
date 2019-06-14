package com.example.filip.finalproject;

import android.content.Context;

public class Artillery extends Units {
    // TODO : fix these comments
    public static int GreenAttack1 = 50; // Green artillery's first attack value , this value can be changed
    public static int GreenAttack2 = 4;  // Green artillery's second attack value , this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green artillery's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 7; //  Green artillery's second attack range, this value can be changed
    public static int GreenDefence = 1; // Defence value of Green's artillery, this value can be changed
    public static int GreenHP = 6; // HP value of Green's artillery, this value can be changed
    public static int GreenMovement = 1; // Movement value of Green's artillery, this value can be changed
    public static int GreenVisibility = 4; // Movement value of Green's Infantry, this value can be changed
    public static int greenAirAttack = 0;

    //cost of artillery unit
    public static int foodPrice = 7;
    public static int ironPrice = 4;
    public static int oilPrice = 0;

    //How much health will the unit gain if healed
    public static int healedBy = 2;

    public static int RedAttack1 = 50; // Red artillery's first attack value, this value can be changed
    public static int RedAttack2 = 4; //  Red artillery's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red artillery's first attack range, this value can be changed
    public static int RedSecondAttackRange = 7; //  Red artillery's second attack value, this value can be changed
    public static int RedDefence = 1; // Defence value of Red's artillery, this value can be changed
    public static int RedHP = 6; // HP value of Red's artillery, this value can be changed
    public static int RedMovement = 1; // Movement value of Red's artillery, this value can be changed
    public static int RedVisibility = 4; // Movement value of Green's Infantry, this value can be changed
    public static int redAirAttack = 0;

    Artillery(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Artillery");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement,GreenVisibility,greenAirAttack);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement,RedVisibility, redAirAttack);
        }
    }
}
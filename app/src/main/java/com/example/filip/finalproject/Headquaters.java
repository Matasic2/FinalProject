package com.example.filip.finalproject;

import android.content.Context;

public class Headquaters extends Units{

    // TODO : fix these comments
    public static int GreenAttack1 = 6; //  Green HQ's first attack value, this value can be changed
    public static int GreenAttack2 = 5; //  Green HQ's second attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green HQ's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 5; //  Green HQ's second attack value, this value can be changed
    public static int GreenDefence = 2; // Defence value of Green's HQ, this value can be changed
    public static int GreenHP = 30; // HP value of Green's HQ, this value can be changed
    public static int GreenMovement = 0; // Movement value of Green's HQ, this value can be changed
    public static int GreenVisibility = 6; // Movement value of Green's Infantry, this value can be changed

    public static int healedBy = 4;


    public static int RedAttack1 = 6; // Red HQ's first attack value, this value can be changed
    public static int RedAttack2 = 5; //  Red HQ's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red HQ's first attack range, this value can be changed
    public static int RedSecondAttackRange = 5; //  Red HQ's second attack value, this value can be changed
    public static int RedDefence = 2; // Defence value of Red's HQ, this value can be changed
    public static int RedHP = 30; // HP value of Red's HQ, this value can be changed
    public static int RedMovement = 0; // Movement value of Red's HQ, this value can be changed
    public static int RedVisibility = 6; // Movement value of Green's Infantry, this value can be changed

    public static int fuelConsumption = 0;

    Headquaters(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Headquarters");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement,GreenVisibility, healedBy, fuelConsumption);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement,RedVisibility, healedBy, fuelConsumption);
        }

    }
}

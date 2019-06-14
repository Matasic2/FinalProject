package com.example.filip.finalproject;

import android.content.Context;

public class Headquaters extends Units{

    // TODO : fix these comments
    public static int GreenAttack1 = 50; //  Green HQ's first attack value, this value can be changed
    public static int GreenAttack2 = 5; //  Green HQ's second attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green HQ's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 4; //  Green HQ's second attack value, this value can be changed
    public static int GreenDefence = 2; // Defence value of Green's HQ, this value can be changed
    public static int GreenHP = 20; // HP value of Green's HQ, this value can be changed
    public static int GreenMovement = 0; // Movement value of Green's HQ, this value can be changed
    public static int greenAirAttack = 1;


    public static int RedAttack1 = 50; // Red HQ's first attack value, this value can be changed
    public static int RedAttack2 = 5; //  Red HQ's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red HQ's first attack range, this value can be changed
    public static int RedSecondAttackRange = 4; //  Red HQ's second attack value, this value can be changed
    public static int RedDefence = 2; // Defence value of Red's HQ, this value can be changed
    public static int RedHP = 20; // HP value of Red's HQ, this value can be changed
    public static int RedMovement = 0; // Movement value of Red's HQ, this value can be changed
    public static int redAirAttack = 1;

    Headquaters(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Headquarters");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement,3,greenAirAttack);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement,3,redAirAttack);
        }

    }
}

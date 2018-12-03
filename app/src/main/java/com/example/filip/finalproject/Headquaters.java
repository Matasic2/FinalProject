package com.example.filip.finalproject;

import android.content.Context;

public class Headquaters extends Units{

    // TODO : fix these comments
    public static int GreenAttack1 = 50; //  Green infantry's first attack value, this value can be changed
    public static int GreenAttack2 = 5; //  Green infantry's second attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green infantry's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 4; //  Green infantry's second attack value, this value can be changed
    public static int GreenDefence = 2; // Defence value of Green's infantry, this value can be changed
    public static int GreenHP = 20; // HP value of Green's infantry, this value can be changed
    public static int GreenMovement = 0; // Movement value of Green's infantry, this value can be changed


    public static int RedAttack1 = 50; // Red Cavalry's first attack value, this value can be changed
    public static int RedAttack2 = 5; //  Red Cavalry's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red Cavalry's first attack range, this value can be changed
    public static int RedSecondAttackRange = 4; //  Red Cavalry's second attack value, this value can be changed
    public static int RedDefence = 2; // Defence value of Red's Cavalry, this value can be changed
    public static int RedHP = 20; // HP value of Red's Cavalry, this value can be changed
    public static int RedMovement = 0; // Movement value of Red's Cavalry, this value can be changed

    Headquaters(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Headquarters");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement);
        }

    }
}

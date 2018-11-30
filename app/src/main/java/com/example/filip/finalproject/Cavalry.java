package com.example.filip.finalproject;

import android.content.Context;

public class Cavalry extends Units{

    // TODO : fix these comments
    public static int GreenAttack1 = 9; // Green Cavalry's first attack value, this value can be changed
    public static int GreenAttack2 = 1; //  Green Cavalry's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green Cavalry's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 3; //  Green Cavalry's second attack value, this value can be changed
    public static int GreenDefence = 1; // Defence value of Green's Cavalry, this value can be changed
    public static int GreenHP = 10; // HP value of Green's Cavalry, this value can be changed
    public static int GreenMovement = 3; // Movement value of Green's Cavalry, this value can be changed

    public static int RedAttack1 = 9; // Red Cavalry's first attack value, this value can be changed
    public static int RedAttack2 = 1; //  Red Cavalry's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red Cavalry's first attack range, this value can be changed
    public static int RedSecondAttackRange = 3; //  Red Cavalry's second attack value, this value can be changed
    public static int RedDefence = 1; // Defence value of Red's Cavalry, this value can be changed
    public static int RedHP = 10; // HP value of Red's Cavalry, this value can be changed
    public static int RedMovement = 3; // Movement value of Red's Cavalry, this value can be changed




    Cavalry(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Cavalry");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement);
        }

    }
}

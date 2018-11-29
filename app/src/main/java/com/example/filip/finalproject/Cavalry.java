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
    public static int GreenMovement = 4; // Movement value of Green's Cavalry, this value can be changed

    Cavalry(Context context, int x, int y, Player player) {
        super(context,x,y,player, "Cavalry", GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement);
    }
}

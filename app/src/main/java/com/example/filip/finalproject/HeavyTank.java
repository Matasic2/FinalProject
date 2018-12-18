package com.example.filip.finalproject;

import android.content.Context;

public class HeavyTank extends Units {

    // TODO : fix these comments
    public static int GreenAttack1 = 20; // Green Cavalry's first attack value, this value can be changed
    public static int GreenAttack2 = 12; //  Green Cavalry's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 2; //  Green Cavalry's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 5; //  Green Cavalry's second attack value, this value can be changed
    public static int GreenDefence = 2; // Defence value of Green's Cavalry, this value can be changed
    public static int GreenHP = 60; // HP value of Green's Cavalry, this value can be changed
    public static int GreenMovement = 1; // Movement value of Green's Cavalry, this value can be changed

    public static int foodPrice = 10;
    public static int ironPrice = 9;
    public static int oilPrice = 45;

    public static int RedAttack1 = 20; // Red Cavalry's first attack value, this value can be changed
    public static int RedAttack2 = 12; //  Red Cavalry's first attack value, this value can be changed
    public static int RedFirstAttackRange = 2; //  Red Cavalry's first attack range, this value can be changed
    public static int RedSecondAttackRange = 5; //  Red Cavalry's second attack value, this value can be changed
    public static int RedDefence = 2; // Defence value of Red's Cavalry, this value can be changed
    public static int RedHP = 60; // HP value of Red's Cavalry, this value can be changed
    public static int RedMovement = 1; // Movement value of Red's Cavalry, this value can be changed

    HeavyTank(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Heavy Tank");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement);
        }
    }
}

package com.example.filip.finalproject;

import android.content.Context;

public class Armor extends Units {

    // TODO : fix these comments
    public static int GreenAttack1 = 15; // Green Cavalry's first attack value, this value can be changed
    public static int GreenAttack2 = 11; //  Green Cavalry's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green Cavalry's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 4; //  Green Cavalry's second attack value, this value can be changed
    public static int GreenDefence = 3; // Defence value of Green's Cavalry, this value can be changed
    public static int GreenHP = 25; // HP value of Green's Cavalry, this value can be changed
    public static int GreenMovement = 2; // Movement value of Green's Cavalry, this value can be changed

    public static int RedAttack1 = 15; // Red Cavalry's first attack value, this value can be changed
    public static int RedAttack2 = 11; //  Red Cavalry's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red Cavalry's first attack range, this value can be changed
    public static int RedSecondAttackRange = 4; //  Red Cavalry's second attack value, this value can be changed
    public static int RedDefence = 3; // Defence value of Red's Cavalry, this value can be changed
    public static int RedHP = 25; // HP value of Red's Cavalry, this value can be changed
    public static int RedMovement = 2; // Movement value of Red's Cavalry, this value can be changed

    Armor(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Armor");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement);
        }
    }
}

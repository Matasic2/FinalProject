package com.example.filip.finalproject;

import android.content.Context;

public class Infantry extends Units {

    // TODO : fix these comments
    public static int GreenAttack1 = 20; // Green Infantry's first attack value, this value can be changed
    public static int GreenAttack2 = 4; //  Green Infantry's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green Infantry's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 5; //  Green Infantry's second attack value, this value can be changed
    public static int GreenDefence = 1; // Defence value of Green's Infantry, this value can be changed
    public static int GreenHP = 8; // HP value of Green's Infantry, this value can be changed
    public static int GreenMovement = 2; // Movement value of Green's Infantry, this value can be changed
    public static int GreenVisibility = 4; // Movement value of Green's Infantry, this value can be changed
    public static int greenAirAttack = 2;
    
    //cost of Infantry unit
    public static int greenFoodPrice = 4;
    public static int greenIronPrice = 1;
    public static int greenOilPrice = 0;

    public static int redFoodPrice = 4;
    public static int redIronPrice = 1;
    public static int redOilPrice = 0;

    //How much health will the unit gain if healed
    public static int healedBy = 3;

    public static int RedAttack1 = 20; // Red Infantry's first attack value, this value can be changed
    public static int RedAttack2 = 4; //  Red Infantry's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red Infantry's first attack range, this value can be changed
    public static int RedSecondAttackRange = 5; //  Red Infantry's second attack value, this value can be changed
    public static int RedDefence = 1; // Defence value of Red's Infantry, this value can be changed
    public static int RedHP = 8; // HP value of Red's Infantry, this value can be changed
    public static int RedMovement = 2; // Movement value of Red's Infantry, this value can be changed
    public static int RedVisibility = 4; // Movement value of Green's Infantry, this value can be changed
    public static int redAirAttack = 2;

    Infantry(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Infantry");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement, GreenVisibility, greenAirAttack, healedBy);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement, RedVisibility, redAirAttack, healedBy);
        }
    }

    public static void adjustUpgrade(Player player, int factor, int number) {
        if (player == GameEngine.green) {
            if (number == 0) {
                //Infantry.greenFoodPrice += 1 * factor;
                Infantry.greenIronPrice += 1 * factor;
                Infantry.GreenFirstAttackRange += 1 * factor;
                Infantry.GreenAttack1 += 10 * factor;
            }
            if (number == 1) {
                Infantry.greenIronPrice += 2 * factor;
                Infantry.GreenDefence += 1 * factor;
            }
            if (number == 2) {
                Infantry.greenFoodPrice += 2 * factor;
                Infantry.GreenAttack2 += 1 * factor;
            }
        } else if (player == GameEngine.red) {
            if (number == 0) {
                //Infantry.redFoodPrice += 1 * factor;
                Infantry.redIronPrice += 1 * factor;
                Infantry.RedFirstAttackRange += 1 * factor;
                Infantry.RedAttack1 += 10 * factor;
            }
            if (number == 1) {
                Infantry.redIronPrice += 2 * factor;
                Infantry.RedDefence += 1 * factor;
            }
            if (number == 2) {
                Infantry.redFoodPrice += 2 * factor;
                Infantry.RedAttack2 += 1 * factor;
            }
        }
    }

    public static void restoreDefaultValues() {
        GreenAttack1 = 20; // Green Infantry's first attack value, this value can be changed
        GreenAttack2 = 4; //  Green Infantry's first attack value, this value can be changed
        GreenFirstAttackRange = 1; //  Green Infantry's first attack range, this value can be changed
        GreenSecondAttackRange = 5; //  Green Infantry's second attack value, this value can be changed
        GreenDefence = 1; // Defence value of Green's Infantry, this value can be changed
        GreenHP = 8; // HP value of Green's Infantry, this value can be changed
        GreenMovement = 2; // Movement value of Green's Infantry, this value can be changed
        GreenVisibility = 4; // Movement value of Green's Infantry, this value can be changed
        greenAirAttack = 1;

        //cost of Infantry unit
        greenFoodPrice = 4;
        greenIronPrice = 1;
        greenOilPrice = 0;

        redFoodPrice = 4;
        redIronPrice = 1;
        redOilPrice = 0;

        //How much health will the unit gain if healed
        healedBy = 3;

        RedAttack1 = 20; // Red Infantry's first attack value, this value can be changed
        RedAttack2 = 4; //  Red Infantry's first attack value, this value can be changed
        RedFirstAttackRange = 1; //  Red Infantry's first attack range, this value can be changed
        RedSecondAttackRange = 5; //  Red Infantry's second attack value, this value can be changed
        RedDefence = 1; // Defence value of Red's Infantry, this value can be changed
        RedHP = 8; // HP value of Red's Infantry, this value can be changed
        RedMovement = 2; // Movement value of Red's Infantry, this value can be changed
        RedVisibility = 4; // Movement value of Green's Infantry, this value can be changed
        redAirAttack = 1;
    }
}

package com.example.filip.finalproject;

import android.content.Context;

public class Artillery extends Units {
    // TODO : fix these comments
    public static int GreenAttack1 = 6; // Green artillery's first attack value , this value can be changed
    public static int GreenAttack2 = 4;  // Green artillery's second attack value , this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green artillery's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 7; //  Green artillery's second attack range, this value can be changed
    public static int GreenDefence = 1; // Defence value of Green's artillery, this value can be changed
    public static int GreenHP = 6; // HP value of Green's artillery, this value can be changed
    public static int GreenMovement = 1; // Movement value of Green's artillery, this value can be changed
    public static int GreenVisibility = 4; // Movement value of Green's Infantry, this value can be changed

    //cost of artillery unit
    public static int greenFoodPrice = 7;
    public static int greenIronPrice = 4;
    public static int greenOilPrice = 0;

    public static int redFoodPrice = 7;
    public static int redIronPrice = 4;
    public static int redOilPrice = 0;

    //How much health will the unit gain if healed
    public static int healedBy = 2;

    public static int RedAttack1 = 6; // Red artillery's first attack value, this value can be changed
    public static int RedAttack2 = 4; //  Red artillery's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red artillery's first attack range, this value can be changed
    public static int RedSecondAttackRange = 7; //  Red artillery's second attack value, this value can be changed
    public static int RedDefence = 1; // Defence value of Red's artillery, this value can be changed
    public static int RedHP = 6; // HP value of Red's artillery, this value can be changed
    public static int RedMovement = 1; // Movement value of Red's artillery, this value can be changed
    public static int RedVisibility = 4; // Movement value of Green's Infantry, this value can be changed

    Artillery(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Artillery");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement,GreenVisibility, healedBy);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement,RedVisibility, healedBy);
        }
    }

    public static void adjustUpgrade(Player player, int factor, int number) {
        if (player == GameEngine.green) {
            if (number == 0) {
                Artillery.greenFoodPrice -= 1 * factor;
                Artillery.greenIronPrice -= 2 * factor;
                Artillery.GreenSecondAttackRange -= 1 * factor;
            }
            if (number == 1) {
                Artillery.greenIronPrice += 1 * factor;
                Artillery.GreenDefence += 1 * factor;
            }
            if (number == 2) {
                Artillery.greenFoodPrice += 2 * factor;
                Artillery.GreenSecondAttackRange += 1 * factor;
            }
        } else if (player == GameEngine.red) {
            if (number == 0) {
                Artillery.redFoodPrice -= 1 * factor;
                Artillery.redIronPrice -= 2 * factor;
                Artillery.RedSecondAttackRange -= 1 * factor;
            }
            if (number == 1) {
                Artillery.redIronPrice += 1 * factor;
                Artillery.RedDefence += 1 * factor;
            }
            if (number == 2) {
                Artillery.redFoodPrice += 2 * factor;
                Artillery.RedSecondAttackRange += 1 * factor;
            }
        }
    }

    public static void restoreDefaultValues() {
        GreenAttack1 = 6; // Green artillery's first attack value , this value can be changed
        GreenAttack2 = 4;  // Green artillery's second attack value , this value can be changed
        GreenFirstAttackRange = 1; //  Green artillery's first attack range, this value can be changed
        GreenSecondAttackRange = 7; //  Green artillery's second attack range, this value can be changed
        GreenDefence = 1; // Defence value of Green's artillery, this value can be changed
        GreenHP = 6; // HP value of Green's artillery, this value can be changed
        GreenMovement = 1; // Movement value of Green's artillery, this value can be changed
        GreenVisibility = 4; // Movement value of Green's Infantry, this value can be changed

        //cost of artillery unit
        greenFoodPrice = 7;
        greenIronPrice = 4;
        greenOilPrice = 0;

        redFoodPrice = 7;
        redIronPrice = 4;
        redOilPrice = 0;

        //How much health will the unit gain if healed
        healedBy = 2;

        RedAttack1 = 6; // Red artillery's first attack value, this value can be changed
        RedAttack2 = 4; //  Red artillery's first attack value, this value can be changed
        RedFirstAttackRange = 1; //  Red artillery's first attack range, this value can be changed
        RedSecondAttackRange = 7; //  Red artillery's second attack value, this value can be changed
        RedDefence = 1; // Defence value of Red's artillery, this value can be changed
        RedHP = 6; // HP value of Red's artillery, this value can be changed
        RedMovement = 1; // Movement value of Red's artillery, this value can be changed
        RedVisibility = 4; // Movement value of Green's Infantry, this value can be changed
    }
}
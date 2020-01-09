package com.example.filip.finalproject;

import android.content.Context;

public class Cavalry extends Units{

    // TODO : fix these comments
    public static int GreenAttack1 = 10; // Green Cavalry's first attack value, this value can be changed
    public static int GreenAttack2 = 2; //  Green Cavalry's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green Cavalry's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 4; //  Green Cavalry's second attack value, this value can be changed
    public static int GreenDefence = 0; // Defence value of Green's Cavalry, this value can be changed
    public static int GreenHP = 10; // HP value of Green's Cavalry, this value can be changed
    public static int GreenMovement = 3; // Movement value of Green's Cavalry, this value can be changed
    public static int GreenVisibility = 6; // Movement value of Green's Infantry, this value can be changed
    public static int greenAirAttack = 0;

    //cost of Cavalry unit
    public static int greenFoodPrice = 3;
    public static int greenIronPrice = 0;
    public static int greenOilPrice = 0;

    public static int redFoodPrice = 3;
    public static int redIronPrice = 0;
    public static int redOilPrice = 0;

    //How much health will the unit gain if healed
    public static int healedBy = 3;

    public static int RedAttack1 = 10; // Red Cavalry's first attack value, this value can be changed
    public static int RedAttack2 = 2; //  Red Cavalry's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red Cavalry's first attack range, this value can be changed
    public static int RedSecondAttackRange = 4; //  Red Cavalry's second attack value, this value can be changed
    public static int RedDefence = 0; // Defence value of Red's Cavalry, this value can be changed
    public static int RedHP = 10; // HP value of Red's Cavalry, this value can be changed
    public static int RedMovement = 3; // Movement value of Red's Cavalry, this value can be changed
    public static int RedVisibility = 6; // Movement value of Green's Infantry, this value can be changed
    public static int redAirAttack = 0;


    Cavalry(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Cavalry");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement,GreenVisibility,greenAirAttack, healedBy);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement,RedVisibility,redAirAttack, healedBy);
        }

    }

    public static void adjustUpgrade(Player player, int factor, int number) {
        if (player == GameEngine.green) {
            if (number == 0) {
                Cavalry.greenFoodPrice += 1 * factor;
                Cavalry.GreenVisibility += 2 * factor;
            }
            if (number == 1) {
                Cavalry.greenIronPrice += 1 * factor;
                Cavalry.GreenDefence += 1 * factor;
            }
            if (number == 2) {
                Cavalry.greenFoodPrice += 1 * factor;
                Cavalry.GreenAttack2 += 1 * factor;
            }
        } else if (player == GameEngine.red) {
            if (number == 0) {
                Cavalry.redFoodPrice += 1 * factor;
                Cavalry.RedVisibility += 2 * factor;
            }
            if (number == 1) {
                Cavalry.redIronPrice += 1 * factor;
                Cavalry.RedDefence += 1 * factor;
            }
            if (number == 2) {
                Cavalry.redFoodPrice += 1 * factor;
                Cavalry.RedAttack2 += 1 * factor;
            }
        }
    }

    public static void restoreDefaultValues() {
        GreenAttack1 = 10; // Green Cavalry's first attack value, this value can be changed
        GreenAttack2 = 2; //  Green Cavalry's first attack value, this value can be changed
        GreenFirstAttackRange = 1; //  Green Cavalry's first attack range, this value can be changed
        GreenSecondAttackRange = 4; //  Green Cavalry's second attack value, this value can be changed
        GreenDefence = 0; // Defence value of Green's Cavalry, this value can be changed
        GreenHP = 10; // HP value of Green's Cavalry, this value can be changed
        GreenMovement = 3; // Movement value of Green's Cavalry, this value can be changed
        GreenVisibility = 6; // Movement value of Green's Infantry, this value can be changed
        greenAirAttack = 0;

        //cost of Cavalry unit
        greenFoodPrice = 3;
        greenIronPrice = 0;
        greenOilPrice = 0;

        redFoodPrice = 3;
        redIronPrice = 0;
        redOilPrice = 0;

        //How much health will the unit gain if healed
        healedBy = 3;

        RedAttack1 = 10; // Red Cavalry's first attack value, this value can be changed
        RedAttack2 = 2; //  Red Cavalry's first attack value, this value can be changed
        RedFirstAttackRange = 1; //  Red Cavalry's first attack range, this value can be changed
        RedSecondAttackRange = 4; //  Red Cavalry's second attack value, this value can be changed
        RedDefence = 0; // Defence value of Red's Cavalry, this value can be changed
        RedHP = 10; // HP value of Red's Cavalry, this value can be changed
        RedMovement = 3; // Movement value of Red's Cavalry, this value can be changed
        RedVisibility = 6; // Movement value of Green's Infantry, this value can be changed
        redAirAttack = 0;
    }

}

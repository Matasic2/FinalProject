package com.example.filip.finalproject;

import android.content.Context;

public class Armor extends Units {

    // TODO : fix these comments
    public static int GreenAttack1 = 10; // Green Armor's first attack value, this value can be changed
    public static int GreenAttack2 = 5; //  Green Armor's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green Armor's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 4; //  Green Armor's second attack value, this value can be changed
    public static int GreenDefence = 2; // Defence value of Green's Armor, this value can be changed
    public static int GreenHP = 20; // HP value of Green's Armor, this value can be changed
    public static int GreenMovement = 2; // Movement value of Green's Armor, this value can be changed
    public static int GreenVisibility = 4; // Movement value of Green's Infantry, this value can be changed

    //cost of Armor unit
    public static int greenFoodPrice = 6;
    public static int greenIronPrice = 5;
    public static int greenOilPrice = 15;

    public static int redFoodPrice = 6;
    public static int redIronPrice = 5;
    public static int redOilPrice = 15;

    //How much health will the unit gain if healed
    public static int healedBy = 3;

    public static int RedAttack1 = 10; // Red Armor's first attack value, this value can be changed
    public static int RedAttack2 = 5; //  Red Armor's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red Armor's first attack range, this value can be changed
    public static int RedSecondAttackRange = 4; //  Red Armor's second attack value, this value can be changed
    public static int RedDefence = 2; // Defence value of Red's Armor, this value can be changed
    public static int RedHP = 20; // HP value of Red's Armor, this value can be changed
    public static int RedMovement = 2; // Movement value of Red's Armor, this value can be changed
    public static int RedVisibility = 4; // Movement value of Green's Infantry, this value can be changed

    Armor(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Armor");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement, GreenVisibility, healedBy);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement,RedVisibility, healedBy);
        }
        this.fuelConsumption = 1;
    }

    public static void adjustUpgrade(Player player, int factor, int number) {
        if (player == GameEngine.green) {
            if (number == 0) {
                Armor.greenFoodPrice += 1 * factor;
                Armor.GreenVisibility += 2 * factor;
            }
            if (number == 1) {
                Armor.greenIronPrice += 3 * factor;
                Armor.GreenDefence += 1 * factor;
            }
            if (number == 2) {
                Armor.greenFoodPrice += 1 * factor;
                Armor.GreenAttack2 += 1 * factor;
            }
        } else if (player == GameEngine.red) {
            if (number == 0) {
                Armor.redFoodPrice += 1 * factor;
                Armor.RedVisibility += 2 * factor;
            }
            if (number == 1) {
                Armor.redIronPrice += 3 * factor;
                Armor.RedDefence += 1 * factor;
            }
            if (number == 2) {
                Armor.redFoodPrice += 1 * factor;
                Armor.RedAttack2 += 1 * factor;
            }
        }
    }

    public static void restoreDefaultValues() {
        GreenAttack1 = 10; // Green Armor's first attack value, this value can be changed
        GreenAttack2 = 5; //  Green Armor's first attack value, this value can be changed
        GreenFirstAttackRange = 1; //  Green Armor's first attack range, this value can be changed
        GreenSecondAttackRange = 4; //  Green Armor's second attack value, this value can be changed
        GreenDefence = 2; // Defence value of Green's Armor, this value can be changed
        GreenHP = 20; // HP value of Green's Armor, this value can be changed
        GreenMovement = 2; // Movement value of Green's Armor, this value can be changed
        GreenVisibility = 4; // Movement value of Green's Infantry, this value can be changed

        //cost of Armor unit
        greenFoodPrice = 6;
        greenIronPrice = 5;
        greenOilPrice = 15;

        redFoodPrice = 6;
        redIronPrice = 5;
        redOilPrice = 15;

        //How much health will the unit gain if healed
        healedBy = 3;

        RedAttack1 = 10; // Red Armor's first attack value, this value can be changed
        RedAttack2 = 5; //  Red Armor's first attack value, this value can be changed
        RedFirstAttackRange = 1; //  Red Armor's first attack range, this value can be changed
        RedSecondAttackRange = 4; //  Red Armor's second attack value, this value can be changed
        RedDefence = 2; // Defence value of Red's Armor, this value can be changed
        RedHP = 20; // HP value of Red's Armor, this value can be changed
        RedMovement = 2; // Movement value of Red's Armor, this value can be changed
        RedVisibility = 4; // Movement value of Green's Infantry, this value can be changed
    }

}

package com.example.filip.finalproject;

import android.content.Context;

public class HeavyTank extends Units {

    // TODO : fix these comments
    public static int GreenAttack1 = 20; // Green Heavy tank's first attack value, this value can be changed
    public static int GreenAttack2 = 12; //  Green Heavy tank's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 2; //  Green Heavy tank's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 5; //  Green Heavy tank's second attack value, this value can be changed
    public static int GreenDefence = 2; // Defence value of Green's Heavy tank, this value can be changed
    public static int GreenHP = 70; // HP value of Green's Heavy tank, this value can be changed
    public static int GreenMovement = 1; // Movement value of Green's Heavy tank, this value can be changed

    //cost of Heavy tank unit
    public static int foodPrice = 10;
    public static int ironPrice = 9;
    public static int oilPrice = 30;

    //How much health will the unit gain if healed
    public static int healedBy = 6;

    public static int RedAttack1 = 20; // Red Heavy tank's first attack value, this value can be changed
    public static int RedAttack2 = 12; //  Red Heavy tank's first attack value, this value can be changed
    public static int RedFirstAttackRange = 2; //  Red Heavy tank's first attack range, this value can be changed
    public static int RedSecondAttackRange = 5; //  Red Heavy tank's second attack value, this value can be changed
    public static int RedDefence = 2; // Defence value of Red's Heavy tank, this value can be changed
    public static int RedHP = 70; // HP value of Red's Heavy tank, this value can be changed
    public static int RedMovement = 1; // Movement value of Red's Heavy tank, this value can be changed

    HeavyTank(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Heavy Tank");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement,8);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement,8);
        }
    }
}

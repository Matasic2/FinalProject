package com.example.filip.finalproject;

import android.content.Context;

public class MechanizedInfantry extends Units {

    // TODO : fix these comments
    public static int GreenAttack1 = 15; // Green Mech Infantry's first attack value, this value can be changed
    public static int GreenAttack2 = 2; //  Green Mech Infantry's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green Mech Infantry's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 4; //  Green Mech Infantry's second attack value, this value can be changed
    public static int GreenDefence = 1; // Defence value of Green's Mech Infantry, this value can be changed
    public static int GreenHP = 3; // HP value of Green's Mech Infantry, this value can be changed
    public static int GreenMovement = 4; // Movement value of Green's Mech Infantry, this value can be changed

    //cost of  Mech Infantry unit
    public static int foodPrice = 2;
    public static int ironPrice = 1;
    public static int oilPrice = 3;


    //How much health will the unit gain if healed
    public static int healedBy = 1;

    public static int RedAttack1 = 15; // Red Mech Infantry's first attack value, this value can be changed
    public static int RedAttack2 = 2; //  Red Mech Infantry's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red Mech Infantry's first attack range, this value can be changed
    public static int RedSecondAttackRange = 4; //  Red Mech Infantry's second attack value, this value can be changed
    public static int RedDefence = 1; // Defence value of Red's Mech Infantry, this value can be changed
    public static int RedHP = 3; // HP value of Red's Mech Infantry, this value can be changed
    public static int RedMovement = 4; // Movement value of Red's Mech Infantry, this value can be changed

    MechanizedInfantry(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Mech Infantry");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement);
        }
    }
}

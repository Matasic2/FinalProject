package com.example.filip.finalproject;

import android.content.Context;

public class MGInfantry extends Units {

    // TODO : fix these comments
    public static int GreenAttack1 = 15; // Green Machine Gun Infantry's first attack value, this value can be changed
    public static int GreenAttack2 = 4; //  Green  Machine Gun Infantry's first attack value, this value can be changed
    public static int GreenFirstAttackRange = 1; //  Green  Machine Gun Infantry's first attack range, this value can be changed
    public static int GreenSecondAttackRange = 5; //  Green  Machine Gun Infantry's second attack value, this value can be changed
    public static int GreenDefence = 2; // Defence value of Green's  Machine Gun Infantry, this value can be changed
    public static int GreenHP = 4; // HP value of Green's  Machine Gun Infantry, this value can be changed
    public static int GreenMovement = 1; // Movement value of Green's  Machine Gun Infantry, this value can be changed

    //cost of  MGInfantry unit
    public static int foodPrice = 5;
    public static int ironPrice = 2;
    public static int oilPrice = 0;


    //How much health will the unit gain if healed
    public static int healedBy = 2;

    public static int RedAttack1 = 15; // Red  Machine Gun Infantry's first attack value, this value can be changed
    public static int RedAttack2 = 4; //  Red  Machine Gun Infantry's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red  Machine Gun Infantry's first attack range, this value can be changed
    public static int RedSecondAttackRange = 5; //  Red  Machine Gun Infantry's second attack value, this value can be changed
    public static int RedDefence = 2; // Defence value of Red's  Machine Gun Infantry, this value can be changed
    public static int RedHP = 4; // HP value of Red's  Machine Gun Infantry, this value can be changed
    public static int RedMovement = 1; // Movement value of Red's  Machine Gun Infantry, this value can be changed

    MGInfantry(Context context, int x, int y, Player player) {
        super(context, x, y, player, "MGInfantry");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement,3);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement,3);
        }
    }
}

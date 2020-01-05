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
    public static int GreenVisibility = 7; // Movement value of Green's Infantry, this value can be changed
    public static int greenAirAttack = 0;

    //cost of Cavalry unit
    public static int foodPrice = 4;
    public static int ironPrice = 0;
    public static int oilPrice = 0;

    //How much health will the unit gain if healed
    public static int healedBy = 3;

    public static int RedAttack1 = 10; // Red Cavalry's first attack value, this value can be changed
    public static int RedAttack2 = 2; //  Red Cavalry's first attack value, this value can be changed
    public static int RedFirstAttackRange = 1; //  Red Cavalry's first attack range, this value can be changed
    public static int RedSecondAttackRange = 4; //  Red Cavalry's second attack value, this value can be changed
    public static int RedDefence = 0; // Defence value of Red's Cavalry, this value can be changed
    public static int RedHP = 10; // HP value of Red's Cavalry, this value can be changed
    public static int RedMovement = 3; // Movement value of Red's Cavalry, this value can be changed
    public static int RedVisibility = 7; // Movement value of Green's Infantry, this value can be changed
    public static int redAirAttack = 0;





    Cavalry(Context context, int x, int y, Player player) {
        super(context, x, y, player, "Cavalry");
        if (player.color.equals("green")) {
            this.setParameters(GreenAttack1, GreenAttack2, GreenFirstAttackRange, GreenSecondAttackRange, GreenDefence, GreenHP, GreenHP, GreenMovement,GreenVisibility,greenAirAttack);
        } else {
            this.setParameters(RedAttack1, RedAttack2, RedFirstAttackRange, RedSecondAttackRange, RedDefence, RedHP, RedHP, RedMovement,RedVisibility,redAirAttack);
        }

    }
}

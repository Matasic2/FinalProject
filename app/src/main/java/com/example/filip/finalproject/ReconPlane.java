package com.example.filip.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ReconPlane extends Planes {

    public static int airAttack =  10;//Damage done by first attack.
    public static int groundAttack = 1; // Damage done by second attack, usually does less damage but has higher range then first attack.
    public static int defence = 1; //Unit's defence value. Damage to HP is calculated by HP -= (damage - defence). For example, if unit has 3hp, 1 defence and gets attacked by unit which has 2 attack damage, new HP will be 2.
    public static int maxHP = 20; //Max HP of the unit
    public static int repairRate = 4;



    ReconPlane(Context context, Player player) {
        super(context, player, null, "Recon Plane", airAttack, groundAttack, defence, maxHP, repairRate);
        if (owner == GameEngine.green) {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            Bitmap icontemp = BitmapFactory.decodeResource(context.getResources(), R.drawable.fitg, o);
            this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (owner == GameEngine.red) {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            Bitmap icontemp = BitmapFactory.decodeResource(context.getResources(), R.drawable.fitr, o);
            this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
        }

    }
}

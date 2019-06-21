package com.example.filip.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ReconPlane extends Planes {

    public static int greenAirAttack =  10;//Damage done by first attack.
    public static int greenGroundAttack = 1; // Damage done by second attack, usually does less damage but has higher range then first attack.
    public static int greenDefence = 1; //Unit's defence value. Damage to HP is calculated by HP -= (damage - defence). For example, if unit has 3hp, 1 defence and gets attacked by unit which has 2 attack damage, new HP will be 2.
    public static int greenMaxHP = 20; //Max HP of the unit
    public static int greenRepairRate = 4;

    public static int foodPrice = 2;
    public static int ironPrice = 2;
    public static int oilPrice = 1;

    public static int redAirAttack =  10;//Damage done by first attack.
    public static int redGroundAttack = 1; // Damage done by second attack, usually does less damage but has higher range then first attack.
    public static int redDefence = 1; //Unit's defence value. Damage to HP is calculated by HP -= (damage - defence). For example, if unit has 3hp, 1 defence and gets attacked by unit which has 2 attack damage, new HP will be 2.
    public static int redMaxHP = 20; //Max HP of the unit
    public static int redRepairRate = 4;

    ReconPlane(Context context, Player player) {
        super(context, player, null, "Fighter");
        if (owner == GameEngine.green) {
            this.airAttack = greenAirAttack;
            this.groundAttack = greenGroundAttack;
            this.defence = greenDefence;
            this.HP = greenMaxHP;
            this.maxHP = greenMaxHP;
            this.healingRate = greenRepairRate;

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            Bitmap icontemp = BitmapFactory.decodeResource(context.getResources(), R.drawable.fitg, o);
            this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (owner == GameEngine.red) {
            this.airAttack = redAirAttack;
            this.groundAttack = redGroundAttack;
            this.defence = redDefence;
            this.HP = redMaxHP;
            this.maxHP = redMaxHP;
            this.healingRate = redRepairRate;

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            Bitmap icontemp = BitmapFactory.decodeResource(context.getResources(), R.drawable.fitr, o);
            this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
        }

    }
}

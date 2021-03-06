package com.example.filip.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bomber extends  Planes{


    public static int greenAirAttack =  4;//Damage done by first attack.
    public static int greenGroundAttack = 4; // Damage done by second attack, usually does less damage but has higher range then first attack.
    public static int greenDefence = 2; //Unit's defence value. Damage to HP is calculated by HP -= (damage - defence). For example, if unit has 3hp, 1 defence and gets attacked by unit which has 2 attack damage, new HP will be 2.
    public static int greenMaxHP = 25; //Max HP of the unit
    public static int greenRepairRate = 6;

    public static int foodPrice = 2;
    public static int ironPrice = 3;
    public static int oilPrice = 4;

    public static int redAirAttack =  4;//Damage done by first attack.
    public static int redGroundAttack = 4; // Damage done by second attack, usually does less damage but has higher range then first attack.
    public static int redDefence = 2; //Unit's defence value. Damage to HP is calculated by HP -= (damage - defence). For example, if unit has 3hp, 1 defence and gets attacked by unit which has 2 attack damage, new HP will be 2.
    public static int redMaxHP = 25; //Max HP of the unit
    public static int redRepairRate = 6;

    Bomber(Context context, Player player) {
        super(context, player, null, "Bomber");
        if (owner == GameEngine.green) {
            this.airAttack = greenAirAttack;
            this.groundAttack = greenGroundAttack;
            this.defence = greenDefence;
            this.HP = greenMaxHP;
            this.maxHP = greenMaxHP;
            this.healingRate = greenRepairRate;

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            Bitmap icontemp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomg, o);
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
            Bitmap icontemp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomr, o);
            this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
        }

    }
}

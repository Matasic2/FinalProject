package com.example.filip.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Planes {
    public Bitmap icon; // Unit's icon
    public String planeType; // type of unit, for example if this unit is infantry, string will equal "Infantry"
    public Player owner; //Player that owns the figure
    public int airAttack; //Damage done by first attack.
    public int groundAttack; // Damage done by second attack, usually does less damage but has higher range then first attack.
    public int defence; //Unit's defence value. Damage to HP is calculated by HP -= (damage - defence). For example, if unit has 3hp, 1 defence and gets attacked by unit which has 2 attack damage, new HP will be 2.
    public int HP; //Current HP of the unit
    public int maxHP; //Max HP of the unit

    Planes(Context context, Player player, Bitmap icon, String name) {
        this.owner = player;
        this.icon = icon;
        this.planeType = name;
    }
    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(icon, x * GameEngine.squareLength, y * GameEngine.squareLength, null);
    }

    public void select() {
        GameEngine.selectedPlane = this;
        getSelectedIcon();
    }

    public void getSelectedIcon() {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;
        Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.fitr, o);
        this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
    }

    public void unselect() {
        GameEngine.selectedPlane = null;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;
        Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.fitg, o);
        this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
    }
}

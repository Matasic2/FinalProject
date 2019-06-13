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
    public int healingRate; //how many health is gained after spending one turn in hangar

    Planes(Context context, Player player, Bitmap icon, String name, int airAttack, int groundAttack, int defence, int maxHP, int healingRate) {
        this.owner = player;
        this.icon = icon;
        this.planeType = name;
        this.airAttack = airAttack;
        this.groundAttack = groundAttack;
        this.defence = defence;
        this.HP = maxHP;
        this.maxHP = maxHP;
        this.healingRate = healingRate;
    }

    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(icon, x * GameEngine.squareLength, y * GameEngine.squareLength, null);
    }

    public void select() {
        GameEngine.selectedPlane = this;
        getSelectedIcon();
    }
    public void attack(int row, int column, int airAttack, int groundAttack){
        if (GameEngine.planeLines[row][column] != null) {
            GameEngine.planeLines[row][column].HP -= (airAttack - GameEngine.planeLines[row][column].defence);
            if (GameEngine.planeLines[row][column].HP <= 0) {
                GameEngine.planeLines[row][column] = null;
            }
        }
        else {
            return; //TODO: implement air attack
        }
    }

    public void groundPlane() {
        for (int i = 0; i < owner.hangar.length; i++) {
            if (owner.hangar[i] == null) {
                owner.hangar[i] = this;
                return;
            }
        }
    }

    public void getSelectedIcon() {
        if (this.owner.equals(GameEngine.green)) {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.fitgs, o);
            this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
        } else {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.fitrs, o);
            this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
        }
    }

    public void unselect() {
        if (this.owner.equals(GameEngine.green)) {
            GameEngine.selectedPlane = null;
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.fitg, o);
            this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
        } else {
            GameEngine.selectedPlane = null;
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.fitr, o);
            this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
        }
    }
}
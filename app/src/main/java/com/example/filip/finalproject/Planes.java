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

    Planes(Context context, Player player, Bitmap icon, String name) {
        this.owner = player;
        this.icon = icon;
        this.planeType = name;
    }

    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(icon, x * GameEngine.squareLength, y * GameEngine.squareLength, null);
    }

    public void draw(Canvas canvas, double x, double y) {
        canvas.drawBitmap(icon, (int)(x * GameEngine.squareLength), (int) (y * GameEngine.squareLength), null);
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
            for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                for (int j = row * 3; j < (row + 1) * 3; j++) {
                    if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != this.owner) {
                        GameEngine.DamageUnit(groundAttack, GameEngine.BoardSprites[i][j],i,j, 3);
                    }
                }
            }

            /**
            if (row == 0) {
                for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != this.owner) {
                            GameEngine.DamageUnit(groundAttack, GameEngine.BoardSprites[i][j],i,j);
                        }
                    }
                }
            }
            if (row == 1) {
                for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                    for (int j = 3; j < 6; j++) {
                        if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != this.owner) {
                            GameEngine.DamageUnit(groundAttack, GameEngine.BoardSprites[i][j],i,j);
                        }
                    }
                }
            }
            if (row == 2) {
                for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                    for (int j = 6; j < 9; j++) {
                        if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != this.owner) {
                            GameEngine.DamageUnit(groundAttack, GameEngine.BoardSprites[i][j],i,j);
                        }
                    }
                }
            }
             */
        }
    }

    public void takeGroundDamage(int line) {

        for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
            for (int j = line * 3; j < (line+1) * 3; j++) {
                if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != this.owner) {
                    this.HP -= GameEngine.BoardSprites[i][j].airAttack;
                }
            }
        }

        /**
        if (line == 0) {
            for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                for (int j = 0; j < 3; j++) {
                    if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != this.owner) {
                        this.HP -= GameEngine.BoardSprites[i][j].airAttack;
                    }
                }
            }
        }
        if (line == 1) {
            for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                for (int j = 3; j < 6; j++) {
                    if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != this.owner) {
                        this.HP -= GameEngine.BoardSprites[i][j].airAttack;
                    }
                }
            }
        }
        if (line == 2) {
            for (int i = 0; i < GameEngine.BoardSprites.length; i++) {
                for (int j = 6; j < 9; j++) {
                    if (GameEngine.BoardSprites[i][j] != null && GameEngine.BoardSprites[i][j].owner != this.owner) {
                        this.HP -= GameEngine.BoardSprites[i][j].airAttack;
                    }
                }
            }
        }
         */
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
        if (planeType.equals("Fighter")) {
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
        } else if (planeType.equals("Bomber")) {
            if (this.owner.equals(GameEngine.green)) {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inScaled = false;
                Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.bomgs, o);
                this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
            } else {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inScaled = false;
                Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.bomrs, o);
                this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
            }
        }
    }

    public void unselect() {
        if (planeType.equals("Fighter")) {
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
        } else if (planeType.equals("Bomber")) {
            if (this.owner.equals(GameEngine.green)) {
                GameEngine.selectedPlane = null;
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inScaled = false;
                Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.bomg, o);
                this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
            } else {
                GameEngine.selectedPlane = null;
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inScaled = false;
                Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.bomr, o);
                this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
            }
        }
    }
}
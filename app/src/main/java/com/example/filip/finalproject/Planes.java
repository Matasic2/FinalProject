package com.example.filip.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Planes {

    public static Bitmap[] missionIcons = new Bitmap[5]; //icons for air missions, 0 is reserved for empty
    public Bitmap icon; // Unit's icon
    public String planeType; // type of unit, for example if this unit is infantry, string will equal "Infantry"
    public Player owner; //Player that owns the figure
    public int airAttack; //Damage done by first attack.
    public int groundAttack; // Damage done by second attack, usually does less damage but has higher range then first attack.
    public int defence; //Unit's defence value. Damage to HP is calculated by HP -= (damage - defence). For example, if unit has 3hp, 1 defence and gets attacked by unit which has 2 attack damage, new HP will be 2.
    public int HP; //Current HP of the unit
    public int maxHP; //Max HP of the unit
    public int healingRate; //how many health is gained after spending one turn in hangar
    public boolean isDeployed = false; //is unit deployed on the line or is it in hangar
    public int airMission = 0; //air mission performed by plane
    //air missions: 0=none, 1=scout, 2=intercept, 3=ground attack, 4=line attack

    Planes(Context context, Player player, Bitmap icon, String name) {
        this.owner = player;
        this.icon = icon;
        this.planeType = name;
    }

    public int getAirAttack(){
        if (airMission == 2) {
            return airAttack + 1;
        }

        else if (airMission == 3) {
            return airAttack / 2;
        }

        return airAttack;
    }

    public int getGroundAttack(){
        if (airMission == 1 || airMission == 2) {
            return 0;
        }

        if (airMission == 4) {
            return groundAttack * 2;
        }

        return groundAttack;
    }

    public int getDefence(){

        if (airMission == 3) {
            return defence - 1;
        }

        return defence;
    }

    public void draw(Canvas canvas, int x, int y) {
        Bitmap toDraw = icon;
        toDraw = movableLocation.cutIconTransparency(toDraw, (double) this.HP / (double) this.maxHP);
        canvas.drawBitmap(toDraw, x * GameEngine.squareLength, y * GameEngine.squareLength, null);
    }

    public void drawWithMission(Canvas canvas, double x, double y, boolean missionIconIsToTheRight) {
        Bitmap toDraw = icon;
        toDraw = movableLocation.cutIconTransparency(toDraw, (double) this.HP / (double) this.maxHP);
        canvas.drawBitmap(toDraw, (int)(x * GameEngine.squareLength), (int) (y * GameEngine.squareLength), null);

        int factor = missionIconIsToTheRight? 1:-1;
        Bitmap airMissionIcon = missionIcons[this.airMission];
        canvas.drawBitmap(airMissionIcon, (int)((x+factor) * GameEngine.squareLength), (int) (y * GameEngine.squareLength), null);
    }

    public void draw(Canvas canvas, double x, double y) {
        Bitmap toDraw = icon;
        toDraw = movableLocation.cutIconTransparency(toDraw, (double) this.HP / (double) this.maxHP);
        canvas.drawBitmap(toDraw, (int)(x * GameEngine.squareLength), (int) (y * GameEngine.squareLength), null);
    }

    public void select() {
        GameEngine.selectedPlane = this;
        getSelectedIcon();
    }
    public void selectEnemy() {
        GameEngine.selectedEnemyPlane = this;
        getSelectedIcon();
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
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inScaled = false;
                Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.fitg, o);
                this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
            } else {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inScaled = false;
                Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.fitr, o);
                this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
            }
        } else if (planeType.equals("Bomber")) {
            if (this.owner.equals(GameEngine.green)) {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inScaled = false;
                Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.bomg, o);
                this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
            } else {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inScaled = false;
                Bitmap icontemp = BitmapFactory.decodeResource(GameView.theContext.getResources(), R.drawable.bomr, o);
                this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
            }
        }
    }
}
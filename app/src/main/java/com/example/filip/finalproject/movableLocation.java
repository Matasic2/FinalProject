package com.example.filip.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.EventLog;
import android.content.Context;
import android.graphics.Canvas;

public class movableLocation {

    public Bitmap icon; //adds icon
    public int displacement = 0;

    public movableLocation(Context context) {

        BitmapFactory.Options o = new Options(); //get resource
        o.inScaled = false;
        icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellow, o);
        displacement = 37;
    }


    public movableLocation(Context context, int number) {

        if (number == 0) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.harvester, o);
        }
        if (number == 1) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.harvesternoright, o);
        }
        if (number == 2) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.harvesternoleft, o);
        }
        if (number == 3) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.unselect, o);
        }
        if (number == 4) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.nextplayer, o);
        }
        if (number == 5) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.buy, o);
        }
        if (number == 6) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.infg, o);
        }
        if (number == 7) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.cavg, o);
        }
        if (number == 8) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.artg, o);
        }
        if (number == 9) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.infr, o);
        }
        if (number == 10) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.cavr, o);
        }
        if (number == 11) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.artr, o);
        }
        if (number == 12) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.armg, o);
        }
        if (number == 13) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.armr, o);
        }
    }
    //draw yellow circles at given coordinates, +37 centers the image
    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(icon, (x*128) + displacement,(y*128) + displacement, null);
    }
}

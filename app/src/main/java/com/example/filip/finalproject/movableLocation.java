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
    }
    //draw yellow circles at given coordinates, +37 centers the image
    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(icon, (x*128) + displacement,(y*128) + displacement, null);
    }
}

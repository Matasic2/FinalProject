package com.example.filip.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint;
import android.util.EventLog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

public class movableLocation {

    public Bitmap icon; //adds icon
    public int displacement = 0; //displacement of the icon, used if the icon has to be centered

    public movableLocation(Context context) {

        BitmapFactory.Options o = new Options(); //get resource
        o.inScaled = false;
        icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.yellow, o);
        icon = replaceColor(icon);
        icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        displacement = (int) (37  * FullscreenActivity.scaleFactor);
    }

    // replaces all white pixels with empty pixels
    public Bitmap replaceColor(Bitmap src){
        if(src == null)
            return null;
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);
        for(int x = 0;x < pixels.length;++x){
            if(pixels[x] == Color.WHITE){
                pixels[x] = ~(pixels[x] << 8 & 0xFF000000) & Color.BLACK;
            }
        }
        Bitmap result = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        return result;
    }

    //adds texture to the object, icon depends on number inputed
    public movableLocation(Context context, int number) {

        if (number == 0) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.harvester, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 1) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.harvesternoright, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 2) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.harvesternoleft, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 3) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.unselect, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 4) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.nextplayer, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 5) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.buy, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 6) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.infg, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 7) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.cavg, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 8) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.artg, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 9) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.infr, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 10) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.cavr, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 11) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.artr, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 12) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.armg, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 13) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.armr, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 20) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.minfg, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 21) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.minfr, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 23) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.htankg, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 24) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.htankr, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 14) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.heal, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 15) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.target, o);
            icon = replaceColor(icon);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
            displacement = (int) (14  * FullscreenActivity.scaleFactor);
        }
        if (number == 16) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.undo, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 17) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.greenarrow, o);
            icon = replaceColor(icon);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
            displacement = (int) (39  * FullscreenActivity.scaleFactor);
        }
        if (number == 18) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.redarrow, o);
            icon = replaceColor(icon);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
            displacement = (int) (39  * FullscreenActivity.scaleFactor);
        }

        if (number == 19) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.upgrade, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 22) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.produce, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 25) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.leave, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 26) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.hangar, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 27) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.airlineposition, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
        if (number == 28) {
            BitmapFactory.Options o = new Options(); //get resource
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.airlinepositionr, o);
            icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
        }
    }
    //draw yellow circles at given coordinates, +37 centers the image
    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(icon, (x*GameEngine.squareLength) + displacement,(y*GameEngine.squareLength) + displacement, null);
    }
    public void draw(Canvas canvas, float x, int y) {
        canvas.drawBitmap(icon, (x*GameEngine.squareLength) + displacement,(y*GameEngine.squareLength) + displacement, null);
    }
    public void draw(Canvas canvas, int x, int y, Paint paint) {
        canvas.drawBitmap(icon, (x*GameEngine.squareLength) + displacement,(y*GameEngine.squareLength) + displacement, paint);
    }
}

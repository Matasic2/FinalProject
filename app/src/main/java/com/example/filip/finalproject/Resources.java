package com.example.filip.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.EventLog;
import android.content.Context;
import android.graphics.Canvas;

public class Resources {

    /* When creating a new resource type :
    1. create a new class, and create (or copy) stats and constructor
    2. Create an image of your resource and add it as icon in resource class (and in resource's constructor)
    3. Make storage in player class and make it yield resources in GameEngine.switchPlayer
*/

    public String resourceType;
    public int[] coordinates = new int[]{10, 8};// coordinates of a Resource
    public int[] collectorCoordinates = new int[]{11, 8};// coordinates of a base collecting a resource

    private Bitmap icon; // resource's icon

        public Resources(Context context, int x, int y, int collectorX, int collectorY, String resourcetype) {
            if (resourcetype.equals("oil")) {
                BitmapFactory.Options o = new Options();
                o.inScaled = false;
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.oil_drill, o);
                icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
            }
            if (resourcetype.equals("iron")) {
                BitmapFactory.Options o = new Options();
                o.inScaled = false;
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.iron_mine, o);
                icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
            }
            if (resourcetype.equals("food")) {
                BitmapFactory.Options o = new Options();
                o.inScaled = false;
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.food_farm, o);
                icon = Bitmap.createScaledBitmap(icon,(int)(icon.getWidth() * FullscreenActivity.scaleFactor), (int)(icon.getHeight() * FullscreenActivity.scaleFactor), true);
            }
            Resources[] toReturn = new Resources[GameView.resources.length + 1];
            for (int k = 0; k < GameView.resources.length; k++) {
                toReturn[k] = GameView.resources[k];
            }
            toReturn[toReturn.length - 1] = this;
            GameView.resources = toReturn;
            GameEngine.BoardResources[x][y] = this;

            //sets the starting coordinates of the resource
            coordinates[0] = x;
            coordinates[1] = y;
            collectorCoordinates[0] = collectorX;
            collectorCoordinates[1] = collectorY;
            this.resourceType = resourcetype;
        }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(icon, coordinates[0] * GameEngine.squareLength, coordinates[1] * GameEngine.squareLength, null);
    }

    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(icon, x, y, null);
    }
}

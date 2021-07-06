package com.example.filip.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint;
import android.content.Context;
import android.graphics.Canvas;

public class Resources {

    /* When creating a new resource type :
    1. create a new class, and create (or copy) stats and constructor
    2. Create an image of your resource and add it as icon in resource class (and in resource's constructor)
    3. Make storage in player class and make it yield resources in GameEngine.switchPlayer
*/

    public String resourceType; //string which stores resource type
    public int[] coordinates = new int[]{10, 8};// coordinates of a Resource
    public int[] collectorCoordinates = new int[]{11, 8};// coordinates of a base collecting a resource

    private Bitmap icons[] = new Bitmap[3]; // resource's icon

        public Resources(Context context, int x, int y, int collectorX, int collectorY, String resourcetype) {
            if (resourcetype.equals("oil")) {
                BitmapFactory.Options o = new Options();
                o.inScaled = false;
                icons[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.oil_drill, o);
                icons[0] = Bitmap.createScaledBitmap(icons[0],(int)(icons[0].getWidth() * FullscreenActivity.scaleFactor), (int)(icons[0].getHeight() * FullscreenActivity.scaleFactor), true);

                icons[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.oil_drill_green, o);
                icons[1] = Bitmap.createScaledBitmap(icons[1],(int)(icons[1].getWidth() * FullscreenActivity.scaleFactor), (int)(icons[1].getHeight() * FullscreenActivity.scaleFactor), true);

                icons[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.oil_drill_red, o);
                icons[2] = Bitmap.createScaledBitmap(icons[2],(int)(icons[2].getWidth() * FullscreenActivity.scaleFactor), (int)(icons[2].getHeight() * FullscreenActivity.scaleFactor), true);
            }
            if (resourcetype.equals("iron")) {
                BitmapFactory.Options o = new Options();
                o.inScaled = false;
                icons[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.iron_mine, o);
                icons[0] = Bitmap.createScaledBitmap(icons[0],(int)(icons[0].getWidth() * FullscreenActivity.scaleFactor), (int)(icons[0].getHeight() * FullscreenActivity.scaleFactor), true);

                icons[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.iron_mine_green, o);
                icons[1] = Bitmap.createScaledBitmap(icons[1],(int)(icons[1].getWidth() * FullscreenActivity.scaleFactor), (int)(icons[1].getHeight() * FullscreenActivity.scaleFactor), true);

                icons[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.iron_mine_red, o);
                icons[2] = Bitmap.createScaledBitmap(icons[2],(int)(icons[2].getWidth() * FullscreenActivity.scaleFactor), (int)(icons[2].getHeight() * FullscreenActivity.scaleFactor), true);
            }
            if (resourcetype.equals("food")) {
                BitmapFactory.Options o = new Options();
                o.inScaled = false;
                icons[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.food_farm, o);
                icons[0] = Bitmap.createScaledBitmap(icons[0],(int)(icons[0].getWidth() * FullscreenActivity.scaleFactor), (int)(icons[0].getHeight() * FullscreenActivity.scaleFactor), true);

                icons[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.food_farm_green, o);
                icons[1] = Bitmap.createScaledBitmap(icons[1],(int)(icons[1].getWidth() * FullscreenActivity.scaleFactor), (int)(icons[1].getHeight() * FullscreenActivity.scaleFactor), true);

                icons[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.food_farm_red, o);
                icons[2] = Bitmap.createScaledBitmap(icons[2],(int)(icons[2].getWidth() * FullscreenActivity.scaleFactor), (int)(icons[2].getHeight() * FullscreenActivity.scaleFactor), true);
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

         Units collectorUnit = GameEngine.boardUnits[collectorCoordinates[0]][collectorCoordinates[1]];

         if (collectorUnit == null) {
             canvas.drawBitmap(icons[0], coordinates[0] * GameEngine.squareLength + GameView.cameraX, coordinates[1] * GameEngine.squareLength + GameView.cameraY, null);
         } else if (collectorUnit.owner.equals(GameEngine.green)) {
             canvas.drawBitmap(icons[1], coordinates[0] * GameEngine.squareLength + GameView.cameraX, coordinates[1] * GameEngine.squareLength + GameView.cameraY, null);
         } else if (collectorUnit.owner.equals(GameEngine.red)) {
             canvas.drawBitmap(icons[2], coordinates[0] * GameEngine.squareLength + GameView.cameraX, coordinates[1] * GameEngine.squareLength + GameView.cameraY, null);
         }

    }
    /**
    public void draw(Canvas canvas,Paint paint, float displacement) {
        canvas.drawBitmap(icon, coordinates[0] * GameEngine.squareLength + displacement, coordinates[1] * GameEngine.squareLength , paint);
    }

    public void draw(Canvas canvas, Paint paint, double x, double y) { canvas.drawBitmap(icon, (int) x, (int) y, paint); }
     */
}

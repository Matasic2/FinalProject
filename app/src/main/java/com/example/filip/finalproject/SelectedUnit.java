package com.example.filip.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.Canvas;

//selected unit on the board
public class SelectedUnit {

    private Bitmap icon; // Unit's icon
    public int[] coordinates = new int[]{0, 0}; //Unit's coordinates
    public Player owner;

    SelectedUnit(Context context,int x ,int y, Player player, String unitType) { //Creates a selected unit on given coordinates

        this.owner = player;
        //sets the unit at coordinates
        coordinates[0] = x / 128;
        coordinates[1] = y / 128;

        // TODO : fix the comments in this part
        if (GameEngine.BoardSprites[x/128][y/128] instanceof Units && player == GameEngine.green && unitType.equals("Infantry")) { //if selected unit is Green Infantry, use infgs (INFantry Green Selected) texture
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.infgs, o);
            return;
        }

        if (GameEngine.BoardSprites[x/128][y/128] instanceof Units && player == GameEngine.red && unitType.equals("Infantry")) { //if selected unit is Red Infantry, use infrs (INFantry Red Selected) texture
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.infrs, o);
            return;
        }

        if (GameEngine.BoardSprites[x/128][y/128] instanceof Units && player == GameEngine.green && unitType.equals("Cavalry")) { //if selected unit is Green Cavalry, use cavgs (CAValry Green Selected) texture
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.cavgs, o);
            return;
        }

        if (GameEngine.BoardSprites[x/128][y/128] instanceof Units && player == GameEngine.red &&  unitType.equals("Cavalry")) { //if selected unit is Red Cavalry, use cavrs (CAValry Red Selected) texture
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.cavrs, o);
            return;
        }

        if (GameEngine.BoardSprites[x/128][y/128] instanceof Units && player == GameEngine.green && unitType.equals("Artillery")) { //if selected unit is Green Cavalry, use cavgs (CAValry Green Selected) texture
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.artgs, o);
            return;
        }

        if (GameEngine.BoardSprites[x/128][y/128] instanceof Units && player == GameEngine.red &&  unitType.equals("Artillery")) { //if selected unit is Red Cavalry, use cavrs (CAValry Red Selected) texture
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.artrs, o);
            return;
        }

        if (GameEngine.BoardSprites[x/128][y/128] instanceof Units && player == GameEngine.green && unitType.equals("Headquarters")) { //if selected unit is Green Cavalry, use cavgs (CAValry Green Selected) texture
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.hqgs, o);
            return;
        }

        if (GameEngine.BoardSprites[x/128][y/128] instanceof Units && player == GameEngine.red &&  unitType.equals("Headquarters")) { //if selected unit is Red Cavalry, use cavrs (CAValry Red Selected) texture
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.hqrs, o);
            return;
        }

        if (GameEngine.BoardSprites[x/128][y/128] instanceof Units && player == GameEngine.green && unitType.equals("Armor")) { //if selected unit is Green Cavalry, use cavgs (CAValry Green Selected) texture
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.armgs, o);
            return;
        }

        if (GameEngine.BoardSprites[x/128][y/128] instanceof Units && player == GameEngine.red &&  unitType.equals("Armor")) { //if selected unit is Red Cavalry, use cavrs (CAValry Red Selected) texture
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.armrs, o);
            return;
        }
        BitmapFactory.Options o = new BitmapFactory.Options(); //if selected unit is Unknown, null texture instead of trying to draw with null and crashing the app. Null texture should never appear, because it's caused by a bug!
        o.inScaled = false;
        icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.nulll, o);
    }

    SelectedUnit(Context context) { //creates the selected unit with "null" texture, used by GameView to create a selected unit variable before any unit is selected. Null texture should never appear, because it's caused by a bug!
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;
        icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.nulll, o);
    }

    //draws the selected unit when selected.draw(canvas) is called in GameView function.
    public void draw(Canvas canvas) {
        canvas.drawBitmap(icon, coordinates[0] * 128, coordinates[1] * 128, null);
    }
}

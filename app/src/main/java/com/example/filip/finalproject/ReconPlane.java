package com.example.filip.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ReconPlane extends Planes {
    ReconPlane(Context context, Player player) {
        super(context, player, null, "Recon Plane");
        if (owner == GameEngine.green) {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inScaled = false;
            Bitmap icontemp = BitmapFactory.decodeResource(context.getResources(), R.drawable.fitg, o);
            this.icon = Bitmap.createScaledBitmap(icontemp, (int) (icontemp.getWidth() * FullscreenActivity.scaleFactor), (int) (icontemp.getHeight() * FullscreenActivity.scaleFactor), true);
        }
    }
}

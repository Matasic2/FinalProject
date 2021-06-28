package com.example.filip.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
/** UNUSED
public class TechNode {

    public static Bitmap icon;

    TechNode parent;
    TechNode[] children;
    int techNumber; //used to identify techs

    int x;
    int y;

    String name;
    String bonus1;
    String bonus2;
    String bonus3;

    int foodPrice;
    int ironPrice;
    int oilPrice;
    int researchPrice;

    TechNode() {
        this.parent = null;
    }

    TechNode(TechNode parent, TechNode[] children, int techNum, int x, int y, String name, String bonus1, String bonus2, String bonus3, int fp, int ip, int op, int rp) {

        this.parent = parent;
        this.children = children;
        this.techNumber = techNum;

        this.x = x;
        this.y = y;

        this.name = name;
        this.bonus1 = bonus1;
        this.bonus2 = bonus2;
        this.bonus3 = bonus3;

        foodPrice = fp;
        ironPrice = ip;
        oilPrice = op;
        researchPrice = rp;
    }

    public void draw(Canvas canvas) {
        if (parent != null) { //if its null its root tech so skip it
            int xRoot = (int) (x * FullscreenActivity.scaleFactor);
            int yRoot = (int) (y * FullscreenActivity.scaleFactor);
            xRoot += GameView.cameraX;

            Paint thePaint = new Paint();
            thePaint.setTextSize(40 * FullscreenActivity.scaleFactor);
            thePaint.setColor(Color.BLACK);


            canvas.drawBitmap(icon, xRoot,yRoot , null); //TODO: separate name in 2 lines if its too big
            canvas.drawText(
                    name,
                    (xRoot * FullscreenActivity.scaleFactor + 10 * FullscreenActivity.scaleFactor),
                    (yRoot * FullscreenActivity.scaleFactor + 60 * FullscreenActivity.scaleFactor),
                    thePaint);

            thePaint.setTextSize(40 * FullscreenActivity.scaleFactor);
            if (bonus1 != null) {
                canvas.drawText(
                        bonus1,
                        (xRoot * FullscreenActivity.scaleFactor + 10 * FullscreenActivity.scaleFactor),
                        (yRoot * FullscreenActivity.scaleFactor + 130 * FullscreenActivity.scaleFactor),
                        thePaint);
            }

            if (bonus2 != null) {
                canvas.drawText(
                        bonus2,
                        (xRoot * FullscreenActivity.scaleFactor + 10 * FullscreenActivity.scaleFactor),
                        (yRoot * FullscreenActivity.scaleFactor + 185 * FullscreenActivity.scaleFactor),
                        thePaint);
            }

            if (bonus3 != null) {
                canvas.drawText(
                        bonus3,
                        (xRoot * FullscreenActivity.scaleFactor + 10 * FullscreenActivity.scaleFactor),
                        (yRoot * FullscreenActivity.scaleFactor + 235 * FullscreenActivity.scaleFactor),
                        thePaint);
            }

            if (!GameEngine.playing.techs[techNumber]) { //check if the tech is researched
                thePaint.setColor(Color.YELLOW);
                canvas.drawText(
                        Integer.toString(foodPrice),
                        (xRoot * FullscreenActivity.scaleFactor + 260 * FullscreenActivity.scaleFactor),
                        (yRoot * FullscreenActivity.scaleFactor + 45 * FullscreenActivity.scaleFactor),
                        thePaint);
                thePaint.setColor(Color.argb(255, 204, 102, 0));
                canvas.drawText(
                        Integer.toString(ironPrice),
                        (xRoot * FullscreenActivity.scaleFactor + 310 * FullscreenActivity.scaleFactor),
                        (yRoot * FullscreenActivity.scaleFactor + 45 * FullscreenActivity.scaleFactor),
                        thePaint);
                thePaint.setColor(Color.GRAY);
                canvas.drawText(
                        Integer.toString(oilPrice),
                        (xRoot * FullscreenActivity.scaleFactor + 260 * FullscreenActivity.scaleFactor),
                        (yRoot * FullscreenActivity.scaleFactor + 85 * FullscreenActivity.scaleFactor),
                        thePaint);
                thePaint.setColor(Color.argb(255, 90, 90, 255));
                canvas.drawText(
                        Integer.toString(researchPrice),
                        (xRoot * FullscreenActivity.scaleFactor + 310 * FullscreenActivity.scaleFactor),
                        (yRoot * FullscreenActivity.scaleFactor + 85 * FullscreenActivity.scaleFactor),
                        thePaint);
            } else {
                if (GameEngine.playing == GameEngine.green) {
                    thePaint.setColor(Color.GREEN);
                }
                if (GameEngine.playing == GameEngine.red) {
                    thePaint.setColor(Color.RED);
                }
                canvas.drawRect((xRoot + 256) * FullscreenActivity.scaleFactor, (yRoot + 6) * FullscreenActivity.scaleFactor, (xRoot + 344) * FullscreenActivity.scaleFactor, (yRoot + 87) * FullscreenActivity.scaleFactor, thePaint);
            }
        }

        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                children[i].draw(canvas);
            }
        }
    }



}
*/
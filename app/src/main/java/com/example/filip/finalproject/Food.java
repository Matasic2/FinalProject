package com.example.filip.finalproject;

import android.content.Context;

public class Food extends Resources {
    //x and y are coordinates of resource, collectorX and collectorY are coordinates of it's collector
    public Food(Context context, int x, int y, int collectorX, int collectorY) {
        super (context,x,y,collectorX,collectorY,"food");
    }
}

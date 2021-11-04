package com.charicha.gameframework.classicsnake;

/**
 * Created by Charicha on 12/27/2017.
 */

public class Stain {

    public static final int TYPE_1 = 0;
    public static final int TYPE_2 = 1;
    public static final int TYPE_3 = 2;
    public int type;
    public int x, y;

    public Stain(int x, int y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

}

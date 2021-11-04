package com.charicha.gameframework.impl;

import android.graphics.Bitmap;

import com.charicha.gameframework.framework.Pixmap;

/**
 * Created by Charicha on 12/24/2017.
 */

public class AndroidPixmap implements Pixmap{

    Bitmap mBitmap;
    PixmapConfig mPixmapConfig;

    public AndroidPixmap(Bitmap bitmap, PixmapConfig pixmapConfig){
        this.mBitmap = bitmap;
        this.mPixmapConfig = pixmapConfig;
    }

    @Override
    public PixmapConfig getConfig() {
        return mPixmapConfig;
    }

    @Override
    public int getWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return mBitmap.getHeight();
    }
}

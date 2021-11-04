package com.charicha.gameframework.impl;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.charicha.gameframework.framework.Graphics;
import com.charicha.gameframework.framework.Pixmap;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Charicha on 12/24/2017.
 */

public class AndroidGraphics implements Graphics {

    AssetManager assetManager;
    Bitmap mFrameBuffer;
    Canvas mCanvas;
    Paint mPaint;
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();
    int mTextCenterOffsetY;

    public AndroidGraphics(Context context, Bitmap frameBuffer){
        this.assetManager = context.getAssets();
        this.mFrameBuffer = frameBuffer;
        this.mCanvas = new Canvas(frameBuffer);
        this.mPaint = new Paint();
    }

    @Override
    public Pixmap newPixmap(String fileName, Pixmap.PixmapConfig pixmapConfig) {
        Bitmap.Config config;
        if(pixmapConfig == Pixmap.PixmapConfig.ARGB4444){
            config = Bitmap.Config.ARGB_4444;
        } else if(pixmapConfig == Pixmap.PixmapConfig.RGB565){
            config = Bitmap.Config.RGB_565;
        } else {
            config = Bitmap.Config.ARGB_8888;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;

        Bitmap bitmap;
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            if(bitmap == null){
                throw new RuntimeException("Couldn't load the bitmap from asssets, " + fileName + "!");
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
            throw new RuntimeException("Couldn't load the bitmap from asssets, " + fileName + "!");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch(IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
        if(bitmap.getConfig() == Bitmap.Config.ARGB_4444){
            pixmapConfig = Pixmap.PixmapConfig.ARGB4444;
        } else if(bitmap.getConfig() == Bitmap.Config.RGB_565){
            pixmapConfig = Pixmap.PixmapConfig.RGB565;
        } else {
            pixmapConfig = Pixmap.PixmapConfig.ARGB8888;
        }
        return new AndroidPixmap(bitmap, pixmapConfig);
    }

    @Override
    public void clear(int color) {
        mCanvas.drawRGB((color & 0xff0000) >> 16, (color & 0x00ff00) >> 8, (color & 0x0000ff));
    }

    @Override
    public void drawPixel(int x, int y, int color){
        mPaint.setColor(Color.rgb( (color & 0xff0000) >> 16, (color & 0x00ff00) >> 8, color & 0x0000ff ));
        mCanvas.drawPoint(x, y, mPaint);
    }

    @Override
    public void setPaintStyle(Paint.Style style){
        mPaint.setStyle(style);
    }

    @Override
    public void setStrokeWidth(int strokeWidth){
        mPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, int color) {
        mPaint.setColor(Color.rgb( (color & 0xff0000) >> 16, (color & 0x00ff00) >> 8, color & 0x0000ff ));
        mCanvas.drawLine(x1, y1, x2, y2, mPaint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        mPaint.setColor(Color.rgb( (color & 0xff0000) >> 16, (color & 0x00ff00) >> 8, color & 0x0000ff ));
        mCanvas.drawRect(x, y, x + width - 1, y + height - 1, mPaint);
    }

    @Override
    public void drawCircle(int x, int y, int radius, int color){
        mPaint.setColor(Color.rgb( (color & 0xff0000) >> 16, (color & 0x00ff00) >> 8, color & 0x0000ff ));
        mCanvas.drawCircle(x, y, radius, mPaint);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        mCanvas.drawBitmap( ((AndroidPixmap)pixmap).mBitmap, x, y, null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x1, int y1, int width1, int height1, int x2, int y2, int width2, int height2) {
        srcRect.top = y1;
        srcRect.bottom = y1 + height1 - 1;
        srcRect.left = x1;
        srcRect.right = x1 + width1 - 1;

        dstRect.top = y2;
        dstRect.bottom = y2 + height2 - 1;
        dstRect.left = x2;
        dstRect.right = x2 + width2 - 1;

        mCanvas.drawBitmap(((AndroidPixmap)pixmap).mBitmap, srcRect, dstRect, null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x1, int y1, int x2, int y2, int width2, int height2) {
        srcRect.top = y1;
        srcRect.bottom = y1 + height2 - 1;
        srcRect.left = x1;
        srcRect.right = x1 + width2 - 1;

        dstRect.top = y2;
        dstRect.bottom = y2 + height2 - 1;
        dstRect.left = x2;
        dstRect.right = x2 + width2 - 1;

        mCanvas.drawBitmap(((AndroidPixmap)pixmap).mBitmap, srcRect, dstRect, null);
    }

    public int getWidth(){
        return mFrameBuffer.getWidth();
    }

    public int getHeight(){
        return mFrameBuffer.getHeight();
    }

    @Override
    public void setFontSize(int size) {
        mPaint.setTextSize(size);
    }

    @Override
    public void setFont(Typeface typeface) {
        mPaint.setTypeface(typeface);
    }

    @Override
    public void setTextAlign(Paint.Align align) {
        mPaint.setTextAlign(align);
    }

    @Override
    public void drawText(String text, int x, int y, int color){
        mPaint.setColor(Color.rgb( (color & 0xff0000) >> 16, (color & 0x00ff00) >> 8, color & 0x0000ff ));
        mPaint.getTextBounds(text, 0, text.length(), srcRect);
        mTextCenterOffsetY = (srcRect.bottom - srcRect.top)/2;
        mCanvas.drawText(text, x, y + mTextCenterOffsetY, mPaint);
    }
}

package com.charicha.gameframework.framework;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * Created by Charicha on 12/24/2017.
 */

public interface Graphics {

    public Pixmap newPixmap(String fileName, Pixmap.PixmapConfig pixmapConfig);

    public void clear(int color);

    public void drawPixel(int x, int y, int color);

    public void setPaintStyle(Paint.Style style);

    public void setStrokeWidth(int strokeWidth);

    public void drawLine(int x1, int y1, int x2, int y2, int color);

    public void drawRect(int x, int y, int width, int height, int color);

    public void drawCircle(int x, int y, int radius, int color);

    public void drawPixmap(Pixmap pixmap, int x, int y);

    public void drawPixmap(Pixmap pixmap, int x1, int y1, int width1, int height1, int x2, int y2, int width2, int height2);

    public void drawPixmap(Pixmap pixmap, int x1, int y1, int x2, int y2, int width2, int height2);

    public int getWidth();

    public int getHeight();

    public void setFontSize(int size);

    public void setFont(Typeface typeface);

    public void setTextAlign(Paint.Align aligh);

    public void drawText(String text, int x, int y, int color);
}

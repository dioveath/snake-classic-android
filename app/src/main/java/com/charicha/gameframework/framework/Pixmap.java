package com.charicha.gameframework.framework;


/**
 * Created by Charicha on 12/24/2017.
 */

public interface Pixmap {

    public static enum PixmapConfig {
        ARGB8888, ARGB4444, RGB565
    };

    public PixmapConfig getConfig();

    public int getWidth();

    public int getHeight();

}

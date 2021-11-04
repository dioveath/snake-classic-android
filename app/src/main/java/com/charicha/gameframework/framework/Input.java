package com.charicha.gameframework.framework;

import java.util.List;

/**
 * Created by Charicha on 12/22/2017.
 */

public interface Input {

    public static class TouchEvent {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_DRAGGED = 1;
        public static final int TOUCH_UP = 2;

        public int touchType;
        public int x;
        public int y;
        public int pointer;
    }

    public static class KeyEvent {
        public static final int KEY_DOWN = 0;
        public static final int KEY_UP = 1;

        public int type;
        public int keyCode;
        public char keyChar;
    }

    public boolean isKeyPressed(int keyCode);

    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);

    public float getAccelX();

    public float getAccelY();

    public float getAccelZ();

    public List<TouchEvent> getTouchEvents();

    public List<KeyEvent> getKeyEvents();

}

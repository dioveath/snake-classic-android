package com.charicha.gameframework.impl;

import android.content.Context;
import android.view.View;

import com.charicha.gameframework.framework.Input;

import java.util.List;

/**
 * Created by Charicha on 12/24/2017.
 */

public class AndroidInput implements Input{

    AccelerometerHandler accelerometerHandler;
    TouchHandler touchHandler;
    KeyboardHandler keyboardHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY){
        accelerometerHandler = new AccelerometerHandler(context);
        touchHandler = (MultiTouchHandler) new MultiTouchHandler(view, scaleX, scaleY);
        keyboardHandler = new KeyboardHandler(view);
    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        return keyboardHandler.isKeyPressed(keyCode);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public float getAccelX() {
        return accelerometerHandler.getAccelX();
    }

    @Override
    public float getAccelY() {
        return accelerometerHandler.getAccelY();
    }

    @Override
    public float getAccelZ() {
        return accelerometerHandler.getAccelZ();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }

    @Override
    public List<KeyEvent> getKeyEvents() {
        return keyboardHandler.getKeyEvents();
    }
}

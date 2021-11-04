package com.charicha.gameframework.impl;

import android.view.View;

import com.charicha.gameframework.framework.Input;

import java.util.List;

/**
 * Created by Charicha on 12/22/2017.
 */

public interface TouchHandler extends View.OnTouchListener{

    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);

    public List<Input.TouchEvent> getTouchEvents();

}

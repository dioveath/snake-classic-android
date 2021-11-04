package com.charicha.gameframework.impl;

import android.view.MotionEvent;
import android.view.View;

import com.charicha.gameframework.framework.Input.TouchEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charicha on 12/22/2017.
 */

public class SingleTouchHandler implements TouchHandler {

    Pool<TouchEvent> mTouchEventPool;
    List<TouchEvent> mTouchEventsBuffer;
    List<TouchEvent> mTouchEvents;
    int touchX;
    int touchY;
    float scaleX;
    float scaleY;
    boolean isTouched;

    public SingleTouchHandler(View view, float scaleX, float scaleY){
        view.setOnTouchListener(this);
        Pool.PoolObjectFactory<TouchEvent> factory = new Pool.PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };
        mTouchEventPool = new Pool<TouchEvent>(factory, 100);
        mTouchEventsBuffer = new ArrayList<TouchEvent>();
        mTouchEvents = new ArrayList<TouchEvent>();
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean isTouchDown(int pointer) {
        synchronized(this){
            if(pointer == 0)
                return isTouched;
            else
                return false;
        }
    }


    @Override
    public int getTouchX(int pointer) {
        synchronized(this){
            return touchX;
        }
    }

    @Override
    public int getTouchY(int pointer) {
        synchronized(this){
            return touchY;
        }
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized (this){
            int len = mTouchEvents.size();
            for(int i = 0; i < len; i++){
                mTouchEventPool.free(mTouchEvents.get(i));
            }
            mTouchEvents.clear();
            mTouchEvents.addAll(mTouchEventsBuffer);
            mTouchEventsBuffer.clear();
            return mTouchEvents;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        synchronized (this){
            TouchEvent touchEvent = mTouchEventPool.newObject();
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    touchEvent.touchType = TouchEvent.TOUCH_DOWN;
                    isTouched = true;
                    break;
                case MotionEvent.ACTION_UP:
                    touchEvent.touchType = TouchEvent.TOUCH_UP;
                    isTouched = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchEvent.touchType = TouchEvent.TOUCH_DRAGGED;
                    isTouched = true;
                    break;
            }
            touchEvent.x = this.touchX =  (int) (motionEvent.getX() * scaleX);
            touchEvent.y = this.touchY = (int) (motionEvent.getY() * scaleY);
            mTouchEventsBuffer.add(touchEvent);
        }
        return false;
    }
}

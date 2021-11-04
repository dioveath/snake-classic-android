package com.charicha.gameframework.impl;

import android.view.MotionEvent;
import android.view.View;

import com.charicha.gameframework.framework.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charicha on 12/24/2017.
 */

public class MultiTouchHandler implements TouchHandler {

    private static final int MAX_TOUCHPOINTS = 10;

    boolean[] isTouched = new boolean[MAX_TOUCHPOINTS];
    int[] touchX = new int[MAX_TOUCHPOINTS];
    int[] touchY = new int[MAX_TOUCHPOINTS];
    int[] id = new int[MAX_TOUCHPOINTS];
    float scaleX;
    float scaleY;
    Pool<Input.TouchEvent> mTouchEventPool;
    List<Input.TouchEvent> mTouchEventsBuffer;
    List<Input.TouchEvent> mTouchEvents;

    public MultiTouchHandler(View view, float scaleX, float scaleY){
        view.setOnTouchListener(this);
        Pool.PoolObjectFactory<Input.TouchEvent> factory = new Pool.PoolObjectFactory<Input.TouchEvent>(){
            @Override
            public Input.TouchEvent createObject() {
                return new Input.TouchEvent();
            }
        };
        mTouchEventPool = new Pool(factory, 100);
        mTouchEvents = new ArrayList<Input.TouchEvent>();
        mTouchEventsBuffer = new ArrayList<Input.TouchEvent>();
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        synchronized(this){
            int maskedAction = motionEvent.getAction() & MotionEvent.ACTION_MASK;
            int pointerIndex = (motionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            int pointerCount = motionEvent.getPointerCount();

            Input.TouchEvent touchEvent;
            for(int i = 0; i < MAX_TOUCHPOINTS; i++){
                if(i >= pointerCount){
                    isTouched[i] = false;
                    id[i] = -1;
                    continue;
                }
                int pointerId = motionEvent.getPointerId(i);
                switch(maskedAction){
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        touchEvent = mTouchEventPool.newObject();
                        isTouched[i] = true;
                        touchEvent.touchType = Input.TouchEvent.TOUCH_DOWN;
                        touchEvent.x = touchX[i] = (int) (motionEvent.getX(i) * scaleX);
                        touchEvent.y = touchY[i] = (int) (motionEvent.getY(i) * scaleY);
                        touchEvent.pointer = id[i] = motionEvent.getPointerId(i);
                        mTouchEventsBuffer.add(touchEvent);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_OUTSIDE:
                    case MotionEvent.ACTION_CANCEL:
                        touchEvent = mTouchEventPool.newObject();
                        isTouched[i] = false;
                        touchEvent.touchType = Input.TouchEvent.TOUCH_UP;
                        touchEvent.x = touchX[i] = (int) (motionEvent.getX(i) * scaleX);
                        touchEvent.y = touchY[i] = (int) (motionEvent.getY(i) * scaleY);
                        touchEvent.pointer = id[i] = motionEvent.getPointerId(i);
                        mTouchEventsBuffer.add(touchEvent);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchEvent = mTouchEventPool.newObject();
                        isTouched[i] = true;
                        touchEvent.touchType = Input.TouchEvent.TOUCH_DRAGGED;
                        touchEvent.x = touchX[i] = (int) (motionEvent.getX(i) * scaleX);
                        touchEvent.y = touchY[i] = (int) (motionEvent.getY(i) * scaleY);
                        touchEvent.pointer = id[i] = motionEvent.getPointerId(i);
                        mTouchEventsBuffer.add(touchEvent);
                        break;
                }
                return true;
            }

            return false;
        }
    }

    public boolean isTouchDown(int pointerId){
        synchronized(this){
            int index = getIndex(pointerId);
            if(index < 0 || index >= MAX_TOUCHPOINTS)
                return false;
            else
                return true;
        }
    }

    public int getTouchX(int pointerId){
        synchronized(this){
            int index = getIndex(pointerId);
            if(index < 0 || index >= MAX_TOUCHPOINTS)
                return 0;
            else
                return touchX[index];
        }
    }

    public int getTouchY(int pointerId){
        synchronized(this){
            int index = getIndex(pointerId);
            if(index < 0 || index >= MAX_TOUCHPOINTS)
                return 0;
            else
                return touchY[index];
        }
    }

    private int getIndex(int pointerId){
        for(int i = 0; i < MAX_TOUCHPOINTS; i++){
            if(id[i] == pointerId)
                return i;
        }
        return -1;
    }

    public List<Input.TouchEvent> getTouchEvents(){
        synchronized(this){
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
}

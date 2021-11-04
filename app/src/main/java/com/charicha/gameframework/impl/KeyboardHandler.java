package com.charicha.gameframework.impl;

import android.view.KeyEvent;
import android.view.View;

import com.charicha.gameframework.framework.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charicha on 12/24/2017.
 */

public class KeyboardHandler implements View.OnKeyListener {

    boolean[] pressedKeys = new boolean[128];
    Pool<Input.KeyEvent> mKeyEventPool;
    List<Input.KeyEvent> mKeyEventsBuffer = new ArrayList<Input.KeyEvent>();
    List<Input.KeyEvent> mKeyEvents = new ArrayList<Input.KeyEvent>();

    public KeyboardHandler(View view){
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        Pool.PoolObjectFactory<Input.KeyEvent> factory = new Pool.PoolObjectFactory<Input.KeyEvent>(){
            @Override
            public Input.KeyEvent createObject() {
                return new Input.KeyEvent();
            }
        };
        mKeyEventPool = new Pool(factory, 100);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(keyEvent.getAction() == KeyEvent.ACTION_MULTIPLE)
            return false;

        synchronized(this){
            Input.KeyEvent keyEvent1 = mKeyEventPool.newObject();
            keyEvent1.keyCode = keyEvent.getKeyCode();
            keyEvent1.keyChar = (char) keyEvent.getUnicodeChar();
            if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                keyEvent1.type = Input.KeyEvent.KEY_DOWN;
                if(keyEvent1.keyCode > 0 && keyEvent1.keyCode < 127){
                    pressedKeys[keyEvent1.keyCode] = true;
                }
            }
            if(keyEvent.getAction() == KeyEvent.ACTION_UP){
                keyEvent1.type = Input.KeyEvent.KEY_UP;
                if(keyEvent1.keyCode > 0 && keyEvent1.keyCode < 127){
                    pressedKeys[keyEvent1.keyCode] = false;
                }
            }
            mKeyEventsBuffer.add(keyEvent1);
//            if(keyEvent.getKeyCode() < 0 || keyEvent.getKeyCode() > 127){
//                pressedKeys[keyEvent.getKeyCode()] = true;
//            }
//            Input.KeyEvent keyEvent1 = mKeyEventPool.newObject();
//            keyEvent1.keyCode = keyEvent.getKeyCode();
//            keyEvent1.keyChar = (char) keyEvent.getUnicodeChar();
//            mKeyEventsBuffer.add(keyEvent1);
            return false;
        }
    }

    public List<Input.KeyEvent> getKeyEvents(){
        synchronized (this){
            int len = mKeyEvents.size();
            for(int i = 0; i < len; i++){
                mKeyEventPool.free(mKeyEvents.get(i));
            }
            mKeyEvents.clear();
            mKeyEvents.addAll(mKeyEventsBuffer);
            mKeyEventsBuffer.clear();
            return mKeyEvents;
        }
    }

    public boolean isKeyPressed(int keyCode){
        if(keyCode < 0 || keyCode > 127)
            return false;
        return pressedKeys[keyCode];
    }
}

package com.charicha.gameframework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;


/**
 * Created by Charicha on 12/25/2017.
 */

public class AndroidFastRenderView extends SurfaceView implements Runnable {

    AndroidGame mGame;
    Bitmap mFrameBuffer;
    Thread mRenderThread = null;
    SurfaceHolder mSurfaceHolder;
    volatile boolean running = false;

    public AndroidFastRenderView(AndroidGame androidGame, Bitmap frameBuffer){
        super(androidGame);
        this.mGame = androidGame;
        this.mFrameBuffer = frameBuffer;
        this.mSurfaceHolder = getHolder();
    }

    public void resume(){
        running = true;
        mRenderThread = new Thread(this);
        mRenderThread.start();

    }

    @Override
    public void run() {
        synchronized(this){
            Canvas canvas;
            double previousTime = System.nanoTime();
            while(running){
                if(!mSurfaceHolder.getSurface().isValid())
                    continue;
                float deltaTime = (float) (System.nanoTime() - previousTime)/1000000000;
                previousTime = System.nanoTime();

                mGame.getCurrentScreen().update(deltaTime);
                mGame.getCurrentScreen().render(deltaTime);

                canvas = mSurfaceHolder.lockCanvas();
                canvas.drawBitmap(mFrameBuffer, null, canvas.getClipBounds(), null);
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }

        }
    }

    public void pause(){
        running = false;
        while(true){
            try {
                mRenderThread.join();
                return;
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return super.onCreateInputConnection(outAttrs);
    }
}

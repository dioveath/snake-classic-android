package com.charicha.gameframework.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.charicha.gameframework.framework.Input;
import com.charicha.gameframework.impl.AndroidInput;

import java.util.List;

/**
 * Created by Charicha on 12/24/2017.
 */

public class MainActivity extends Activity {

    FastRenderView fastRenderView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        fastRenderView = new FastRenderView(this);
        setContentView(fastRenderView);
    }

    @Override
    public void onResume(){
        super.onResume();
        fastRenderView.resume();
    }

    @Override
    public void onPause(){
        fastRenderView.pause();
        super.onPause();
    }

    class FastRenderView extends SurfaceView implements Runnable {

        SurfaceHolder surfaceHolder;
        Thread mRenderThread;
        volatile boolean running = false;
        AndroidInput androidInput;
        List<Input.TouchEvent> touchEvents;
        float touchX;
        float touchY;
        int targetX = 400;
        int targetY = 400;
        Paint mPaint;

        public FastRenderView(Context context){
            super(context);
            androidInput = new AndroidInput(context, this, 1,1 );
            surfaceHolder = getHolder();
            mPaint = new Paint();
            Typeface font = Typeface.createFromAsset(getAssets(), "retganon.ttf");
            mPaint.setTypeface(font);
        }

        @Override
        public void run(){
            while(running){
                synchronized (this){
                    Canvas canvas;
                    if(!surfaceHolder.getSurface().isValid())
                        continue;
                    canvas = surfaceHolder.lockCanvas();
                     renderGame(canvas);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    updateGame();
                }
            }
        }

        public void renderGame(Canvas canvas){
            canvas.drawRGB(255, 15, 159);
            mPaint.setColor(Color.YELLOW);
            if(touchX != -1 && touchY != -1)
            canvas.drawCircle(touchX, touchY, 100, mPaint);
            touchX -= androidInput.getAccelX() * 3;
            touchY += androidInput.getAccelY() * 3;
            if(touchX >= canvas.getWidth() - 100){
                touchX = canvas.getWidth() - 100;
            }
            if(touchX <= 100){
                touchX = 100;
            }
            if(touchY >= canvas.getHeight() - 100){
                touchY = canvas.getHeight() - 100;
            }
            if(touchY <= 100){
                touchY = 100;
            }

            mPaint.setColor(Color.GREEN);
            canvas.drawCircle(targetX, targetY, 100, mPaint);
            mPaint.setColor(Color.CYAN);
            mPaint.setTextSize(80);
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("You puny humans are no match with me!", canvas.getWidth()/2, canvas.getHeight()/2, mPaint);
            canvas.drawText("Ha Ha Ha Ha Ha Ha!", canvas.getWidth()/2, canvas.getHeight()/2 + 80, mPaint);
        }

        public void updateGame(){
            touchEvents = androidInput.getTouchEvents();
            int len = touchEvents.size();
            for(int i = 0; i < len; i++){
                Input.TouchEvent touchEvent = touchEvents.get(i);
                if(touchEvent.touchType == Input.TouchEvent.TOUCH_DRAGGED){
                    targetX = touchEvent.x;
                    targetY = touchEvent.y;
                    break;
                }
            }
        }

        public void resume(){
            running = true;
            mRenderThread = new Thread(this);
            mRenderThread.start();
        }

        public void pause(){
            running = false;
            while(true){
                try {
                    mRenderThread.join();
                    return;
                } catch(InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        }

    }

}

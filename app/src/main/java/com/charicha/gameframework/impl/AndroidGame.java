package com.charicha.gameframework.impl;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.charicha.gameframework.framework.Audio;
import com.charicha.gameframework.framework.FileIO;
import com.charicha.gameframework.framework.Game;
import com.charicha.gameframework.framework.Graphics;
import com.charicha.gameframework.framework.Input;
import com.charicha.gameframework.framework.Screen;

/**
 * Created by Charicha on 12/25/2017.
 */

public abstract class AndroidGame extends Activity implements Game {

    public AndroidFastRenderView mRenderView;
    Graphics mGraphics;
    Audio mAudio;
    FileIO mFileIO;
    Input mInput;
    Screen mCurrentScreen;

    @Override
    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);

        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 1280 : 720;
        int frameBufferHeight = isLandscape ? 720 : 1280;

        Bitmap bitmap = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Bitmap.Config.RGB_565);

        float scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();

        mRenderView = new AndroidFastRenderView(this, bitmap);
        mGraphics = new AndroidGraphics(this, bitmap);
        mAudio = new AndroidAudio(this);
        mFileIO = new AndroidFileIO(this);
        mInput = new AndroidInput(this, mRenderView, scaleX, scaleY);
        mCurrentScreen = getStartScreen();
        setContentView(mRenderView);
    }

    @Override
    public void onResume(){
        super.onResume();
        mCurrentScreen.resume();
        mRenderView.resume();
    }

    @Override
    public void onPause(){
        super.onPause();
        mRenderView.pause();
        mCurrentScreen.pause();

        if (isFinishing()) {
            mCurrentScreen.dispose();
        }
    }


    @Override
    public void changeCurrentScreen(Screen screen){
        if(screen == null)
            throw new RuntimeException("Couldn't change the current screen to null");
        mCurrentScreen.pause();
        mCurrentScreen.dispose();
        screen.resume();
        screen.update(0);
        mCurrentScreen = screen;
    }

    @Override
    public Screen getCurrentScreen(){
        return mCurrentScreen;
    }

    @Override
    public FileIO getFileIO(){
        return mFileIO;
    }

    @Override
    public Graphics getGraphics(){
        return mGraphics;
    }

    @Override
    public Audio getAudio(){
        return mAudio;
    }

    @Override
    public Input getInput(){
        return mInput;
    }




}

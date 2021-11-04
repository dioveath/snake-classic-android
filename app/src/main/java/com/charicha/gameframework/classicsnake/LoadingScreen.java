package com.charicha.gameframework.classicsnake;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.charicha.gameframework.framework.Game;
import com.charicha.gameframework.framework.Graphics;
import com.charicha.gameframework.framework.Screen;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Charicha on 12/25/2017.
 */

public class LoadingScreen extends Screen {

    Graphics mGraphics;
    float loadingSpeed = 500;
    float loadedDataCount = 0;
    int mWidth = 0;
    int mHeight = 0;
    Thread mLoadAssetThread = null;

    public LoadingScreen(Game game) {
        super(game);
        mGraphics = game.getGraphics();
        mWidth = mGraphics.getWidth();
        mHeight = mGraphics.getHeight();
    }

    @Override
    public void update(float deltaTime) {
        loadedDataCount += loadingSpeed * deltaTime;
        if(loadedDataCount >= mWidth) {
            while(true){
                try {
                    mLoadAssetThread.join();
                    break;
                } catch(InterruptedException ie){
                    ie.printStackTrace();
                }
            }
            mGame.changeCurrentScreen(new MainMenuScreen(mGame));
        }
    }

    @Override
    public void render(float deltaTime) {
        mGraphics.clear(0x111111);
        mGraphics.drawText("Loading...", 200, mHeight - 100, 0xeeee44);
        mGraphics.drawRect(0, mHeight - 50, (int) loadedDataCount, 10, 0xeeeeee);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        mLoadAssetThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Assets.clickSound = mGame.getAudio().newSound("soundEnabled.wav");
                Assets.retganonFont = Typeface.createFromAsset(((Activity)mGame).getAssets(), "retganon.ttf");
                Assets.dudeBulletsFont = Typeface.createFromAsset(((Activity)mGame).getAssets(), "PIZZADUDEBULLETS.ttf");
                Assets.flowerFont = Typeface.createFromAsset(((Activity)mGame).getAssets(), "Kalocsai_Flowers.ttf");
                Settings.loadSettings(mGame.getFileIO());
                mGraphics.setFont(Assets.retganonFont);
                mGraphics.setFontSize(80);
                mGraphics.setTextAlign(Paint.Align.CENTER);
            }
        });
        mLoadAssetThread.start();
    }

    @Override
    public void dispose() {

    }
}

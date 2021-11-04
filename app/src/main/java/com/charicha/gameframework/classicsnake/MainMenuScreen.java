package com.charicha.gameframework.classicsnake;

import android.app.Activity;
import android.graphics.Paint;

import com.charicha.gameframework.framework.Game;
import com.charicha.gameframework.framework.Graphics;
import com.charicha.gameframework.framework.Input;
import com.charicha.gameframework.framework.Screen;

import java.util.List;

/**
 * Created by Charicha on 12/25/2017.
 */

public class MainMenuScreen extends Screen {

    Graphics mGraphics;
    int mWidth;
    int mHeight;
    boolean mExitPrompt = false;

    List<Input.TouchEvent> touchEvents;

    public MainMenuScreen(Game game) {
        super(game);
        this.mGraphics = mGame.getGraphics();
        this.mWidth = mGraphics.getWidth();
        this.mHeight = mGraphics.getHeight();
    }

    @Override
    public void update(float deltaTime) {
        touchEvents = mGame.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent curTouchEvent = touchEvents.get(i);

            if (curTouchEvent.touchType == Input.TouchEvent.TOUCH_UP) {
                if(mExitPrompt){
                        if (MainMenuScreen.pointInRect(curTouchEvent.x, curTouchEvent.y, mWidth/2 - 300, mHeight/2 + 20, 300, 100)) {
                            ((Activity)mGame).finish();
                            break;
                        }
                        if (MainMenuScreen.pointInRect(curTouchEvent.x, curTouchEvent.y, mWidth/2 , mHeight/2  + 20, 300, 100)) {
                            mExitPrompt = false;
                            break;
                        }
                        continue;
                }
                if (pointInRect(curTouchEvent.x, curTouchEvent.y, mWidth / 2 - 200, mHeight / 2 - 200, 400, 100)) {
                    if (Settings.soundEnabled) {
                        Assets.clickSound.play(1);
                    }
                    mGame.changeCurrentScreen(new PlayScreen(mGame));
                }
                if (pointInRect(curTouchEvent.x, curTouchEvent.y, mWidth / 2 - 200, mHeight / 2 - 50, 400, 100)) {
                    if (Settings.soundEnabled) {
                        Assets.clickSound.play(1);
                    }
                    mGame.changeCurrentScreen(new HelpScreen(mGame));
                }
                if (pointInRect(curTouchEvent.x, curTouchEvent.y, mWidth / 2 - 200, mHeight / 2 + 100, 400, 100)) {
                    if (Settings.soundEnabled) {
                        Assets.clickSound.play(1);
                    }
                    mGame.changeCurrentScreen(new HighScoresScreen(mGame));
                }
                if(pointInRect(curTouchEvent.x, curTouchEvent.y, mWidth / 2 - 200, mHeight / 2 + 250, 400, 100)){
                    if(Settings.soundEnabled)
                        Assets.clickSound.play(1);
                    mGame.changeCurrentScreen(new CreditsScreen(mGame));
                }
                if(pointInRect(curTouchEvent.x, curTouchEvent.y, 0, mHeight - 128, 128, 128)){
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled)
                        Assets.clickSound.play(1);
                }
            }
        }
    }

    @Override
    public void render(float deltaTime) {
        mGraphics.clear(0xffffff);

        mGraphics.setFont(Assets.flowerFont);
        mGraphics.setFontSize(300);
        mGraphics.setTextAlign(Paint.Align.CENTER);
        mGraphics.drawText("F", mWidth/2, 200, 0xff4563);

        mGraphics.setFont(Assets.retganonFont);
        mGraphics.setFontSize(60);
        mGraphics.drawRect(mWidth / 2 - 200, mHeight / 2 - 200, 400, 100, 0x45e488);
        mGraphics.drawText("Play Game", mWidth/2, mHeight/2 - 150, 0xffff33);
        mGraphics.drawRect(mWidth / 2 - 200, mHeight / 2 - 50, 400, 100, 0x45e488);
        mGraphics.drawText("Help", mWidth/2, mHeight/2,0xffff33);
        mGraphics.drawRect(mWidth / 2 - 200, mHeight / 2 + 100, 400, 100, 0x45e488);
        mGraphics.drawText("HighScores", mWidth/2, mHeight/2 + 150, 0xffff33);
        mGraphics.drawRect(mWidth / 2 - 200, mHeight / 2 + 250, 400, 100, 0x45e488);
        mGraphics.drawText("Credits", mWidth/2, mHeight/2 + 300, 0xffff33);
        if(Settings.soundEnabled){
            mGraphics.drawRect(0, mHeight - 127, 128, 128, 0x6666ff);
        } else {
            mGraphics.drawRect(0, mHeight - 127, 128, 128, 0xff6666);
        }
        if (mExitPrompt) {
            mGraphics.drawRect(mWidth/2 - 300, mHeight/2 - 200, 600, 400, 0x888888);
            mGraphics.drawText("Are you sure you", mWidth/2, mHeight/2 - 140, 0xeeee33);
            mGraphics.drawText("wanna quit?", mWidth/2, mHeight/2 - 80, 0xeeee33);
            mGraphics.drawRect(mWidth/2 - 300, mHeight/2 + 20, 300, 100, 0x45e488);
            mGraphics.drawText("Yes", mWidth/2 - 150, mHeight/2 + 70, 0xffff33);
            mGraphics.drawRect(mWidth/2 , mHeight/2  + 20, 300, 100, 0xe4e45488);
            mGraphics.drawText("No!", mWidth/2 + 150, mHeight/2 + 70, 0xffff33);
        }
    }

    @Override
    public void pause() {
        Settings.saveSettings(mGame.getFileIO());
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {

    }

    public static boolean pointInRect(int x, int y, int x1, int y1, int width, int height) {
        if ((x >= x1 && x <= (x1 + width)) && (y >= y1 && y <= y1 + height))
            return true;
        return false;
    }
}

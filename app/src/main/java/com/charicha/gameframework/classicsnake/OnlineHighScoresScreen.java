package com.charicha.gameframework.classicsnake;

import android.graphics.Paint;
import android.widget.ProgressBar;

import com.charicha.gameframework.framework.Game;
import com.charicha.gameframework.framework.Graphics;
import com.charicha.gameframework.framework.Input;
import com.charicha.gameframework.framework.Screen;

import java.util.List;

/**
 * Created by Charicha on 1/6/2018.
 */

public class OnlineHighScoresScreen extends Screen {

    Graphics mGraphics;
    int mWidth, mHeight;

    Input mInput;
    List<Input.TouchEvent> touchEvents;

    public OnlineHighScoresScreen(Game game) {
        super(game);
        mGraphics = game.getGraphics();
        mWidth = mGraphics.getWidth();
        mHeight = mGraphics.getHeight();

        mInput = game.getInput();
    }

    @Override
    public void update(float deltaTime) {
        touchEvents = mInput.getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++){
            Input.TouchEvent curTouchEvent = touchEvents.get(i);
            if(curTouchEvent.touchType == Input.TouchEvent.TOUCH_UP){
                if(MainMenuScreen.pointInRect(curTouchEvent.x, curTouchEvent.y, 0, mHeight - 128, 128, 128)){
                    if(Settings.soundEnabled)
                        Assets.clickSound.play(1);
                    mGame.changeCurrentScreen(new HighScoresScreen(mGame));
                    break;
                }
            }
        }
    }

    @Override
    public void render(float deltaTime) {
        mGraphics.clear(0xffffff);
        mGraphics.drawRect(0, mHeight - 128, 128, 128, 0x6666ff);
        mGraphics.setFontSize(80);
        mGraphics.setTextAlign(Paint.Align.CENTER);
        mGraphics.drawText("Online HighScores", mWidth/2, 200, 0x143789);
        mGraphics.setFontSize(60);
        for(int i = 0; i < 5; i++){
            if(i == 0){
                mGraphics.setTextAlign(Paint.Align.LEFT);
                mGraphics.drawText("SN ", 100, 400, 0x143789);
                mGraphics.setTextAlign(Paint.Align.CENTER);
                mGraphics.drawText("Name", mWidth/2, 400,0x143789);
                mGraphics.setTextAlign(Paint.Align.RIGHT);
                mGraphics.drawText("Score"
                        , mWidth - 100, 400, 0x145789);
            }
            int y = 500 + i * 100;
            mGraphics.setTextAlign(Paint.Align.LEFT);
            mGraphics.drawText((i + 1) + ". ", 100, y, 0x143789);
            mGraphics.setTextAlign(Paint.Align.CENTER);
            mGraphics.drawText(Settings.onlinePlayerNames[i], mWidth/2, y,0x143789);
            mGraphics.setTextAlign(Paint.Align.RIGHT);
            mGraphics.drawText(" " + Settings.onlineHighScores[i], mWidth - 100, y, 0x145789);
        }
        mGraphics.setTextAlign(Paint.Align.CENTER);
        mGraphics.drawText(Settings.onlineQueryUserMessage, mWidth/2, mHeight - 200, 0x145789);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        Settings.queryOnlineScore();
    }

    @Override
    public void dispose() {

    }
}

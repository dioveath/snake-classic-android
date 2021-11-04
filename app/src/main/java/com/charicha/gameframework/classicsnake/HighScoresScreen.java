package com.charicha.gameframework.classicsnake;

import android.graphics.Paint;

import com.charicha.gameframework.framework.Game;
import com.charicha.gameframework.framework.Graphics;
import com.charicha.gameframework.framework.Input;
import com.charicha.gameframework.framework.Screen;

import java.util.List;

/**
 * Created by Charicha on 12/25/2017.
 */

public class HighScoresScreen extends Screen {

    List<Input.TouchEvent> touchEvents;

    Graphics mGraphics;
    Input mInput;
    int mWidth = 0;
    int mHeight = 0;

    public HighScoresScreen(Game game) {
        super(game);
        mGraphics = game.getGraphics();
        mInput = game.getInput();
        mWidth = mGraphics.getWidth();
        mHeight = mGraphics.getHeight();
        mHeight = mGraphics.getHeight();
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
                    mGame.changeCurrentScreen(new MainMenuScreen(mGame));
                    break;
                }
                if(MainMenuScreen.pointInRect(curTouchEvent.x, curTouchEvent.y, mWidth - 384, mHeight - 128, 384, 128)){
                    if(Settings.soundEnabled)
                        Assets.clickSound.play(1);
                    mGame.changeCurrentScreen(new OnlineHighScoresScreen(mGame));
                    break;
                }
            }
        }
    }

    @Override
    public void render(float deltaTime) {
        mGraphics.clear(0xffffff);
        mGraphics.setFontSize(80);
        mGraphics.setTextAlign(Paint.Align.CENTER);
        mGraphics.drawText("Local HighScores", mWidth/2, 200, 0x143789);
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
            mGraphics.drawText(Settings.localPlayerNames[i], mWidth/2, y,0x143789);
            mGraphics.setTextAlign(Paint.Align.RIGHT);
            mGraphics.drawText(" " + Settings.localHighScores[i], mWidth - 100, y, 0x145789);
        }

        mGraphics.drawRect(0, mHeight - 128, 128, 128, 0x6666ff);
        mGraphics.drawRect(mWidth - 384, mHeight - 128, 384, 128, 0x6666ff);
        mGraphics.drawText("Online Highscores>", mWidth, mHeight-64, 0x472612);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {

    }

    public void drawText(String text, int x, int y){
        int len = text.length();
        for(int i = 0; i < len; i++){
            char character = text.charAt(i);

            if(character == ' '){
                x += 20;
                continue;
            }

            int srcX = 0;
            int srcWidth = 0;
            if(character == '.'){
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }
            mGraphics.drawPixmap(Assets.highScoreFont, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }
}

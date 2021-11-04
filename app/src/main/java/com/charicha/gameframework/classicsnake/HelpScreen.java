package com.charicha.gameframework.classicsnake;

import com.charicha.gameframework.framework.Game;
import com.charicha.gameframework.framework.Graphics;
import com.charicha.gameframework.framework.Input;
import com.charicha.gameframework.framework.Screen;

import java.util.List;

/**
 * Created by Charicha on 12/25/2017.
 */

public class HelpScreen extends Screen {

    Graphics mGraphics;
    List<Input.TouchEvent> touchEvents;
    int mWidth = 0;
    int mHeight = 0;

    public HelpScreen(Game game){
        super(game);
        mGraphics = game.getGraphics();
        mWidth = mGraphics.getWidth();
        mHeight = mGraphics.getHeight();
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render(float deltaTime) {
        mGraphics.clear(0xffffff);
        mGraphics.drawText("I won't help YOUUU!", mWidth/2, mHeight/2, 0x978613);
        mGraphics.drawText("%^&&@*!(^@*&^)(*&", mWidth/2, mHeight/2 + 100, 0x143789);
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
}

package com.charicha.gameframework.classicsnake;

import com.charicha.gameframework.framework.Game;
import com.charicha.gameframework.framework.Graphics;
import com.charicha.gameframework.framework.Screen;

/**
 * Created by Charicha on 12/28/2017.
 */

public class CreditsScreen extends Screen {

    Graphics mGraphics;
    int mWidth = 0;
    int mHeight = 0;

    float mTextPositionY;
    int mTextPositionSpeed = 100;

    public CreditsScreen(Game game) {
        super(game);
        mGraphics = game.getGraphics();
        mWidth = mGraphics.getWidth();
        mHeight = mGraphics.getHeight();
        mTextPositionY = mHeight;
    }

    @Override
    public void update(float deltaTime) {
        if(mTextPositionY > 400)
            mTextPositionY -= mTextPositionSpeed * deltaTime;
    }

    @Override
    public void render(float deltaTime) {
        mGraphics.clear(0xffffff);
        mGraphics.setFontSize(80);
        mGraphics.drawText("Credits", mWidth/2, 100, 0x143789);
        mGraphics.setFontSize(60);
        mGraphics.drawText("Developed by", mWidth/2, (int) mTextPositionY, 0x343789);
        mGraphics.drawText("Charicha Studio", mWidth/2, (int) mTextPositionY + 70, 0x143789);
        mGraphics.drawText("Programmer", mWidth/2, (int) mTextPositionY + 190, 0x143789);
        mGraphics.drawText("Saroj Rai", mWidth/2, (int) mTextPositionY + 260, 0x143789);
        mGraphics.drawText("Art & Graphics", mWidth/2, (int) mTextPositionY + 380, 0x143789);
        mGraphics.drawText("Saroj Rai", mWidth/2, (int) mTextPositionY + 450, 0x143789);
        mGraphics.drawText("Sound & Audio", mWidth/2, (int) mTextPositionY + 570, 0x143789);
        mGraphics.drawText("Saroj Rai", mWidth/2, (int) mTextPositionY + 640, 0x143789);
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

package com.charicha.gameframework.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.charicha.gameframework.framework.Game;
import com.charicha.gameframework.framework.Graphics;
import com.charicha.gameframework.framework.Pixmap;
import com.charicha.gameframework.framework.Screen;
import com.charicha.gameframework.impl.AndroidFastRenderView;
import com.charicha.gameframework.impl.AndroidGame;

/**
 * Created by Charicha on 12/25/2017.
 */

public class FrameworkTestActivity extends AndroidGame{

    @Override
    public Screen getStartScreen(){
        return new TestScreen(this);
    }

    public class TestScreen extends Screen {

        Graphics mGraphics;

        public TestScreen(Game game){
            super(game);
            this.mGraphics = game.getGraphics();
        }

        @Override
        public void update(float deltaTime) {

        }

        @Override
        public void render(float deltaTime) {
            mGraphics.clear(0xffff00);
            mGraphics.drawLine(0, 0, mGraphics.getWidth(), mGraphics.getHeight(), Color.BLUE);
            mGraphics.drawRect(100, 200, 100, 200, Color.argb( 180, 20, 155, 40));
            mGraphics.drawPixmap(mGraphics.newPixmap("zeddex.png", Pixmap.PixmapConfig.ARGB4444), 0, 0, 320, 200, 100, 100, 100, 100);
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

}

package com.charicha.gameframework.framework;

/**
 * Created by Charicha on 12/25/2017.
 */

public abstract class Screen {

    protected final Game mGame;

    public Screen(Game game){
        this.mGame = game;
    }

    public abstract void update(float deltaTime);

    public abstract void render(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();

}

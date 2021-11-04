package com.charicha.gameframework.framework;

/**
 * Created by Charicha on 12/22/2017.
 */

public interface Music {

    public void play();

    public void pause();

    public void stop();

    public boolean isPlaying();

    public boolean isStopped();

    public boolean isLooping();

    public void dispose();

}

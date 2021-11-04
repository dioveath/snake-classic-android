package com.charicha.gameframework.framework;

/**
 * Created by Charicha on 12/25/2017.
 */

public interface Game {

    public Screen getStartScreen();

    public void changeCurrentScreen(Screen screen);

    public Screen getCurrentScreen();

    public Graphics getGraphics();

    public Audio getAudio();

    public FileIO getFileIO();

    public Input getInput();

}

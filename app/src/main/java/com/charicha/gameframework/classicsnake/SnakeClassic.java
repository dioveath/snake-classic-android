package com.charicha.gameframework.classicsnake;

import com.charicha.gameframework.framework.Screen;
import com.charicha.gameframework.impl.AndroidGame;

/**
 * Created by Charicha on 12/25/2017.
 */

public class SnakeClassic extends AndroidGame {

    @Override
    public Screen getStartScreen(){
        return new LoadingScreen(this);
    }

    @Override
    public void onBackPressed(){
        if(getCurrentScreen() instanceof MainMenuScreen){
            ((MainMenuScreen) getCurrentScreen()).mExitPrompt = !(((MainMenuScreen) getCurrentScreen()).mExitPrompt);
        }
        if(getCurrentScreen() instanceof HelpScreen || getCurrentScreen() instanceof  HighScoresScreen || getCurrentScreen() instanceof CreditsScreen){
            changeCurrentScreen(new MainMenuScreen(this));
        }
        if(getCurrentScreen() instanceof PlayScreen){
            if(((PlayScreen) getCurrentScreen()).mCurrentState != PlayScreen.GameState.GAMEOVER)
                ((PlayScreen) getCurrentScreen()).mBackPrompt = !(((PlayScreen) getCurrentScreen()).mBackPrompt);
        }
        if(getCurrentScreen() instanceof OnlineHighScoresScreen){
            changeCurrentScreen(new HighScoresScreen(this));
        }
    }

}

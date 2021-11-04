package com.charicha.gameframework.classicsnake;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.charicha.gameframework.framework.Game;
import com.charicha.gameframework.framework.Graphics;
import com.charicha.gameframework.framework.Input;
import com.charicha.gameframework.framework.Screen;
import com.charicha.gameframework.impl.AndroidGame;
import com.charicha.gameframework.impl.AndroidInput;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import static com.charicha.gameframework.classicsnake.World.WORLD_HEIGHT;
import static com.charicha.gameframework.classicsnake.World.WORLD_WIDTH;

/**
 * Created by Charicha on 12/25/2017.
 */

public class PlayScreen extends Screen {

    public static enum GameState{
        READY, PLAY, PAUSE, GAMEOVER, BACKPROMPT;
    };

    GameState mCurrentState;
    Graphics mGraphics;
    int mWidth = 0;
    int mHeight = 0;

    World mWorld;
    Snake mWorldSnake;
    int mCellHeight = 0;
    int mCellWidth = 0;

    List<Input.TouchEvent> mTouchEvents;
    AndroidInput mInput;

    boolean mBackPrompt = false;

    StringBuilder keyboardBufferStringBuilder = new StringBuilder();
    int keyboardBufferLength = 0;
    int maxPlayerNameLength = 10;
    InputMethodManager inputMethodManager = (InputMethodManager)((Context)mGame).getSystemService(Context.INPUT_METHOD_SERVICE);
    boolean firstPromptOnlineHighScore = true;

    public PlayScreen(Game game){
        super(game);
        mGraphics = game.getGraphics();
        mCurrentState = GameState.READY;
        mWorld = new World();

        mWidth = mGraphics.getWidth();
        mHeight = mGraphics.getHeight();
        mCellWidth = (mWidth/WORLD_WIDTH);
        mCellHeight = ((mHeight - 150)/WORLD_HEIGHT) + 1;

        mWorldSnake = mWorld.mSnake;

        mInput = (AndroidInput) game.getInput();

        keyboardBufferLength = keyboardBufferStringBuilder.length();
    }

    @Override
    public void update(float deltaTime) {
        if(mBackPrompt){
            updateBackPrompt();
            return;
        }
        switch (mCurrentState) {
            case READY:
                    updateReady();
                    break;
                case PLAY:
                    updatePlay(deltaTime);
                    break;
                case PAUSE:
                    updatePause();
                    break;
            case GAMEOVER:
                updateGameOver();
                break;
            }
    }

    @Override
    public void render(float deltaTime) {
        if(mBackPrompt){
            renderBackPrompt();
            return;
        }
        switch(mCurrentState){
            case READY:
                renderReady();
                break;
            case PLAY:
                renderPlay();
                break;
            case PAUSE:
                renderPause();
                break;
            case GAMEOVER:
                renderGameOver();
                break;
        }
    }

    public void updateReady(){
        mTouchEvents = mInput.getTouchEvents();
        int len = mTouchEvents.size();
        for(int i = 0; i < len; i++){
            Input.TouchEvent curTouchEvent = mTouchEvents.get(i);
            if(curTouchEvent.touchType == Input.TouchEvent.TOUCH_UP){
                mCurrentState = GameState.PLAY;
                return;
            }
        }
    }

    public void renderReady(){
        renderPlay();
        mGraphics.setFontSize(60);
        mGraphics.drawText("READY!", mWidth/2, mHeight/2, 0x449088);
        mGraphics.drawText("TOUCH THE SCREEN", mWidth/2, mHeight/2 + 100, 0x449088);
        mGraphics.drawText("TO START !", mWidth/2, mHeight/2 + 200, 0x449088);
    }

    final int TAIL_PRECISION = 20;

    class VirtualStick {
        int cx, cy;
        int x, y;
        int mTouchpointer;
        boolean isActive = false;
    }

    VirtualStick mStick = new VirtualStick();
    int[] prevxs = new int[TAIL_PRECISION];
    int[] prevys = new int[TAIL_PRECISION];
    int recordedPointCount = 0;

    public void updatePlay(float deltaTime){
        mTouchEvents = mInput.getTouchEvents();
        int len = mTouchEvents.size();
        for(int i = 0; i < len; i++){
            Input.TouchEvent curTouchEvent = mTouchEvents.get(i);

            //Virtual JoyStick
            if(curTouchEvent.touchType == Input.TouchEvent.TOUCH_DOWN){
                if(!mStick.isActive){
                    mStick.x = mStick.cx = curTouchEvent.x;
                    mStick.y = mStick.cy = curTouchEvent.y;
                    mStick.mTouchpointer = curTouchEvent.pointer;
                    mStick.isActive = true;
                    recordedPointCount = 0;
                }
            }
            if(curTouchEvent.touchType == Input.TouchEvent.TOUCH_DRAGGED){
                if(mStick.isActive && mStick.mTouchpointer == curTouchEvent.pointer){
                    if(recordedPointCount == TAIL_PRECISION){
                        for(int j = 0; j < TAIL_PRECISION - 1; j++){
                            prevxs[j] = prevxs[j + 1];
                            prevys[j] = prevys[j + 1];
                        }
                        recordedPointCount--;
                    }
                   mStick.x = prevxs[recordedPointCount] = curTouchEvent.x;
                   mStick.y = prevys[recordedPointCount] = curTouchEvent.y;
                   if(recordedPointCount < TAIL_PRECISION)
                       recordedPointCount++;
                }
            }
            if(curTouchEvent.touchType == Input.TouchEvent.TOUCH_UP){
                if(mStick.isActive && mStick.mTouchpointer == curTouchEvent.pointer){
                    mStick.mTouchpointer = -1;
                    mStick.isActive = false;
                }
            }
        }

        //PROCESSING VIRTUAL STICK
        if(mStick.isActive){
            int dx = mStick.x - mStick.cx;
            int dy = mStick.y - mStick.cy;

            double dist = Math.sqrt(dx * dx + dy * dy);
            if(dist > 50){
                mWorld.mTurboMode = false;
                if(Math.abs(dx) > Math.abs(dy)){
                    if(dx > 0){
                        //right
                        if(mWorldSnake.direction == mWorldSnake.UP) mWorldSnake.turnRight();
                        if(mWorldSnake.direction == mWorldSnake.DOWN) mWorldSnake.turnLeft();
                    } else {
                        //left
                        if(mWorldSnake.direction == mWorldSnake.UP) mWorldSnake.turnLeft();
                        if(mWorldSnake.direction == mWorldSnake.DOWN) mWorldSnake.turnRight();
                    }
                } else {
                    if (dy > 0) {
                        //down
                        if(mWorldSnake.direction == mWorldSnake.LEFT) mWorldSnake.turnLeft();
                        if(mWorldSnake.direction == mWorldSnake.RIGHT) mWorldSnake.turnRight();
                    } else {
                        //up
                        if(mWorldSnake.direction == mWorldSnake.LEFT) mWorldSnake.turnRight();
                        if(mWorldSnake.direction == mWorldSnake.RIGHT) mWorldSnake.turnLeft();
                    }

                }
            } else {
                mWorld.mTurboMode = true;
            }
        } else {
            mWorld.mTurboMode = false;//END OF PROCESSING VIRTUAL STICK
        }

        mWorld.update(deltaTime);
        if(mWorld.isGameOver){
            mCurrentState = GameState.GAMEOVER;
        }
    }

    public void renderPlay(){
        mGraphics.clear(0xeeeeee);
        //Drawing World Coordinate
//        for(int i = 0; i <= WORLD_WIDTH; i++){
//            for(int j = 0; j <= WORLD_HEIGHT; j++){
//                mGraphics.drawLine(i * mCellWidth, (j) * mCellHeight, mWidth, (j) * mCellHeight, 0x000000);
//                mGraphics.drawLine((i) * mCellWidth, 0, (i) * mCellWidth, mHeight - 140, 0x000000);
//            }
//        }
        mGraphics.drawRect(mWorldSnake.mHead.x * mCellWidth, mWorldSnake.mHead.y * mCellHeight, mCellWidth , mCellHeight, 0xee7777);
        for(int i = 1; i < mWorldSnake.mParts.size(); i++){
            SnakePart cPart = mWorldSnake.mParts.get(i);
            mGraphics.drawRect(cPart.x * mCellWidth, cPart.y * mCellHeight, mCellWidth , mCellHeight, 0x662222);
        }
        mGraphics.drawRect(mWorld.mStain.x * mCellWidth, mWorld.mStain.y * mCellHeight, mCellWidth, mCellHeight, 0x33dd66);

        mGraphics.setStrokeWidth(5);
        mGraphics.drawLine(0, WORLD_HEIGHT * mCellHeight, mWidth, WORLD_HEIGHT * mCellHeight, 0x000000);
        mGraphics.setFontSize(60);
        mGraphics.drawText("" + mWorld.mCurrentScore, mWidth/2, 30, 0xaaaa33);
        mGraphics.drawText(mWorld.commentry, mWidth/2, mHeight - 70, 0xaaaa33);

        //Rendering Virtual JoyStick
        mGraphics.setStrokeWidth((int) (new Random().nextFloat() * 40));
        for(int i = 0; i < recordedPointCount - 1 ; i++){
            mGraphics.drawLine(prevxs[i], prevys[i], prevxs[i + 1], prevys[i + 1], 0x194964);
            mGraphics.drawCircle(prevxs[i], prevys[i], (int)(new Random().nextFloat() * 30), 0x194964);
        }
        if(!mStick.isActive && recordedPointCount >= 1){
            for(int j = 0; j < recordedPointCount - 1; j++){
                prevxs[j] = prevxs[j + 1];
                prevys[j] = prevys[j + 1];
            }
            recordedPointCount--;
        }
    }

    public void updatePause(){
    }

    public void renderPause(){

    }

    public void updateGameOver(){
        mTouchEvents = mInput.getTouchEvents();
        int len = mTouchEvents.size();
        for(int i = 0; i < len; i++){
            Input.TouchEvent curTouchEvent = mTouchEvents.get(i);
            if(curTouchEvent.touchType == Input.TouchEvent.TOUCH_UP && !Settings.waitingOnlineResponse){
                int posY = mHeight/2 + 200;
                if(MainMenuScreen.pointInRect(curTouchEvent.x, curTouchEvent.y, mWidth/2 - 300, posY + 20, 300, 100)){
                    //YES
                    if(firstPromptOnlineHighScore){
                        if(keyboardBufferStringBuilder.length() == 0) {
                            Settings.onlineInsertUserMessage = "You nameless fellow!";
                            return;
                        }
                        Settings.insertOnlineScore(keyboardBufferStringBuilder.toString(), mWorld.mCurrentScore);
                        firstPromptOnlineHighScore = false;
                        return;
                    }
                    if(!Settings.onlineError){
                        Settings.addLocalScore(keyboardBufferStringBuilder.toString(), mWorld.mCurrentScore);
                        mGame.changeCurrentScreen(new MainMenuScreen(mGame));
                        return;
                    } else {
                        Settings.insertOnlineScore(keyboardBufferStringBuilder.toString(), mWorld.mCurrentScore);
                        return;
                    }
                }
                if(MainMenuScreen.pointInRect(curTouchEvent.x, curTouchEvent.y, mWidth/2 , posY  + 20, 300, 100)){
                    //NO
                    if(keyboardBufferStringBuilder.length() == 0){
                        keyboardBufferStringBuilder.append("Charicha");
                    }
                    Settings.addLocalScore(keyboardBufferStringBuilder.toString(), mWorld.mCurrentScore);
                    mGame.changeCurrentScreen(new MainMenuScreen(mGame));
                }
                if(MainMenuScreen.pointInRect(curTouchEvent.x, curTouchEvent.y, mWidth/2 - 200, posY - 480, 400, 80)){
                    enableKeyboardAndRegisterListener();
                }
            }
        }

    }


    public void enableKeyboardAndRegisterListener(){
        // TODO: 1/6/2018 Make this method need a parameter a StringBuilder. And also create new class for registering listener
        // which will take stringBuilder to append all the keyevents
        inputMethodManager.toggleSoftInputFromWindow(((AndroidGame)mGame).mRenderView.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        ((AndroidGame)mGame).mRenderView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_UP && keyboardBufferLength <= maxPlayerNameLength){
                    if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                        //FINSIHED TYPING NAME
                        inputMethodManager.hideSoftInputFromInputMethod(((AndroidGame)mGame).mRenderView.getWindowToken(), 0);
                        ((AndroidGame)mGame).mRenderView.setOnKeyListener(null);
                    } else if(keyEvent.getKeyCode() == 67){
                        if(keyboardBufferLength >= 1){
                            keyboardBufferStringBuilder.delete(keyboardBufferLength - 1, keyboardBufferLength);
                            keyboardBufferLength--;
                        }
                    } else if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK){
//                        if(keyboardBufferLength >= 1){
//                            keyboardBufferStringBuilder.delete(keyboardBufferLength - 1, keyboardBufferLength);
//                            keyboardBufferLength--;
//                        }
                        inputMethodManager.hideSoftInputFromInputMethod(((AndroidGame)mGame).mRenderView.getWindowToken(), 0);
                        ((AndroidGame)mGame).mRenderView.setOnKeyListener(null);
                    } else {
                        char c = (char) keyEvent.getUnicodeChar();
                        keyboardBufferStringBuilder.append(c);
                        keyboardBufferLength++;
                    }
                } else if(keyEvent.getAction() == KeyEvent.ACTION_UP){
                    if(keyEvent.getKeyCode() == 67){
                        //BACKSPACE
                        if(keyboardBufferLength >= 1){
                            keyboardBufferStringBuilder.delete(keyboardBufferLength - 1, keyboardBufferLength);
                            keyboardBufferLength--;
                        }
                    }
                }
                return true;
            }
        });
    }

    String leftAction = "Yes";
    String rightAction = "No";
    public void renderGameOver(){
        renderPlay();
        int posY = mHeight/2 + 200;
        mGraphics.setFontSize(60);
        mGraphics.drawRect(mWidth/2 - 300, mHeight/2 - 500, 600, 1000, 0x888888);
        mGraphics.drawText("Do you want to submit", mWidth/2, posY - 140, 0xeeee33);
        mGraphics.drawText("your score online?", mWidth/2, posY - 80, 0xeeee33);
        mGraphics.drawRect(mWidth/2 - 300, posY + 20, 300, 100, 0x45e488);
        if(!firstPromptOnlineHighScore && !Settings.waitingOnlineResponse){
            if(Settings.onlineError){
                leftAction = "Retry";
                rightAction = "Cancel";
            } else if(!Settings.onlineError){
                leftAction = "MainMenu";
                rightAction = "MainMenu";
            }
        }
        mGraphics.drawText(leftAction, mWidth/2 - 150, posY + 70, 0xffff33);
        mGraphics.drawRect(mWidth/2 , posY  + 20, 300, 100, 0xe4e45488);
        mGraphics.drawText(rightAction, mWidth/2 + 150, posY + 70, 0xffff33);
        mGraphics.setFontSize(60);
        mGraphics.drawText("Player Name: ", mWidth/2, posY - 540, 0xeeee33);
        mGraphics.drawRect(mWidth/2 - 200, posY - 480, 400, 80, 0xffffff);
        mGraphics.drawText(keyboardBufferStringBuilder.toString() + "|", mWidth/2, posY - 440, 0xeeee33);
        mGraphics.drawText("Game Over!", mWidth/2, posY - 340, 0xeeee33);
        mGraphics.drawText("Your Score: " + mWorld.mCurrentScore, mWidth/2, posY - 240, 0xeeee33);

        mGraphics.drawText(Settings.onlineInsertUserMessage, mWidth/2, mHeight - 200, 0xeeee33);
    }

    public void updateBackPrompt(){
        mTouchEvents = mInput.getTouchEvents();
        int len = mTouchEvents.size();
        for(int i = 0; i< len; i++){
            Input.TouchEvent curTouchEvent = mTouchEvents.get(i);
            if(curTouchEvent.touchType == Input.TouchEvent.TOUCH_UP) {
                if (MainMenuScreen.pointInRect(curTouchEvent.x, curTouchEvent.y, mWidth/2 - 300, mHeight/2 + 20, 300, 100)) {
                    mGame.changeCurrentScreen(new MainMenuScreen(mGame));
                    break;
                }
                if (MainMenuScreen.pointInRect(curTouchEvent.x, curTouchEvent.y, mWidth/2 , mHeight/2  + 20, 300, 100)) {
                    mBackPrompt = false;
                    break;
                }
            }
        }
    }

    public void renderBackPrompt(){
        mGraphics.setFontSize(60);
        mGraphics.drawRect(mWidth/2 - 300, mHeight/2 - 200, 600, 400, 0x888888);
        mGraphics.drawText("Are you sure to", mWidth/2, mHeight/2 - 140, 0xeeee33);
        mGraphics.drawText("go back?", mWidth/2, mHeight/2 - 80, 0xeeee33);
        mGraphics.drawRect(mWidth/2 - 300, mHeight/2 + 20, 300, 100, 0x45e488);
        mGraphics.drawText("Yes", mWidth/2 - 150, mHeight/2 + 70, 0xffff33);
        mGraphics.drawRect(mWidth/2 , mHeight/2  + 20, 300, 100, 0xe4e45488);
        mGraphics.drawText("No!", mWidth/2 + 150, mHeight/2 + 70, 0xffff33);
    }

    @Override
    public void pause() {
        inputMethodManager.hideSoftInputFromInputMethod(((AndroidGame)mGame).mRenderView.getWindowToken(), 0);
        ((AndroidGame)mGame).mRenderView.setOnKeyListener(null);
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        Settings.onlineInsertUserMessage = "";
    }
}

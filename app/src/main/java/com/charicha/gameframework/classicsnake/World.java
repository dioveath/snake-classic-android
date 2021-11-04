package com.charicha.gameframework.classicsnake;

import java.util.Random;

/**
 * Created by Charicha on 12/27/2017.
 */

public class World {

    public static final int WORLD_WIDTH = 15;
    public static final int WORLD_HEIGHT = 22;
    public static final float INITIAL_TICK = 0.4f;
    public static final float DECREMENT_TICK = 0.02f;

    Snake mSnake;
    Stain mStain;
    int mCurrentScore;
    boolean isGameOver = false;
    boolean[][] mWorldCells = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
    Random random = new Random();

    float mAccumulatedTime = 0;
    float mTickTime = INITIAL_TICK;


    int[] bonuses = {3, 2, 1};
    float comboTimer = 0;
    String commentry = "";
    boolean mTurboMode = false;

    public World(){
        mSnake = new Snake(WORLD_WIDTH/2, WORLD_HEIGHT/2);
        mStain = new Stain(0, 0, Stain.TYPE_1);
        placeStain();
    }

    private void placeStain(){
        updateWorldCells();

        int stainX = random.nextInt(WORLD_WIDTH);
        int stainY = random.nextInt(WORLD_HEIGHT);
        while(true){
            if (mWorldCells[stainX][stainY] == false)
                break;
            stainX++;
            if(stainX >= WORLD_WIDTH){
                stainX = 0;
                stainY++;
                if(stainY >= WORLD_HEIGHT){
                    stainY = 0;
                }
            }
        }
        mStain = new Stain(stainX, stainY, random.nextInt(3));
    }

    public void update(float deltaTime){
        if(isGameOver)
            return;

        comboTimer += deltaTime;


        mAccumulatedTime += deltaTime;
        commentry = "mTurboMode: " + mTurboMode;
        if(mAccumulatedTime >= (mTurboMode ? 0.1f : mTickTime)){
            mAccumulatedTime -= mTickTime;


            mSnake.advance();
            if(mSnake.checkIfBitten()){
                isGameOver = true;
                return;
            }

            if(mSnake.mHead.x == mStain.x && mSnake.mHead.y == mStain.y){
                mSnake.eat();
                mCurrentScore += 10;

                if(mSnake.mParts.size() == WORLD_WIDTH * WORLD_HEIGHT) {
                    isGameOver = true;
                    return;
                }
                placeStain();

                if(mCurrentScore % 50 == 0 && mTickTime - DECREMENT_TICK > 0.1){
                    mTickTime -= DECREMENT_TICK;
                }
            }

        }
    }

    private void updateWorldCells(){
        for(int i = 0; i < WORLD_WIDTH; i++){
            for(int j = 0; j < WORLD_HEIGHT; j++){
                mWorldCells[i][j] = false;
            }
        }
        for(int i = 0; i < mSnake.mParts.size(); i++){
            SnakePart curPart = mSnake.mParts.get(i);
            mWorldCells[curPart.x][curPart.y] = true;
        }
    }


}

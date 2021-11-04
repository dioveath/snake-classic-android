package com.charicha.gameframework.classicsnake;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charicha on 12/27/2017.
 */

public class Snake {
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    List<SnakePart> mParts = new ArrayList<>();
    SnakePart mHead;
    int direction;

    //FOR DIRECTION ANAMOLIES
    boolean mCanChangeDirection = true;

    public Snake(int x, int y){
        direction = UP;
        mParts.add(new SnakePart(x, y));
        mParts.add(new SnakePart(x, y + 1));
        mParts.add(new SnakePart(x, y + 2));
        mHead = mParts.get(0);
    }

    public void advance(){
        int len = mParts.size();
        for(int i = len - 1; i > 0; i--){
            SnakePart curSnakePart = mParts.get(i);
            SnakePart nextSnakePart = mParts.get(i - 1);
            curSnakePart.x = nextSnakePart.x;
            curSnakePart.y = nextSnakePart.y;
        }
        if(direction == UP)
            mHead.y--;
        if(direction == RIGHT)
            mHead.x++;
        if(direction == DOWN)
            mHead.y++;
        if(direction == LEFT)
            mHead.x--;

        if(mHead.x > World.WORLD_WIDTH - 1)
            mHead.x = 0;
        if(mHead.x < 0)
            mHead.x = World.WORLD_WIDTH - 1;
        if(mHead.y > World.WORLD_HEIGHT - 1)
            mHead.y = 0;
        if(mHead.y < 0)
            mHead.y = World.WORLD_HEIGHT - 1;

        mCanChangeDirection = true;
    }

    public void turnLeft(){
        if(mCanChangeDirection){
            if(direction == UP){
                direction = LEFT;
                return;
            }
            direction--;
            mCanChangeDirection = false;
        }
    }

    public void turnRight(){
        if(mCanChangeDirection){
            if(direction == LEFT){
                direction = UP;
                return;
            }
            direction++;
            mCanChangeDirection = false;
        }
    }

    public void eat(){
        SnakePart tail = mParts.get(mParts.size() - 1);
        mParts.add(new SnakePart(tail.x, tail.y));
    }

    public boolean checkIfBitten(){
        SnakePart head = mHead;
        for(int i = 1; i < mParts.size(); i++){
            SnakePart curPart = mParts.get(i);
            if(head.x == curPart.x && head.y == curPart.y)
                return true;
        }
        return false;
    }
}

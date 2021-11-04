package com.charicha.gameframework.impl;

import android.content.res.AssetFileDescriptor;
import android.media.SoundPool;

import com.charicha.gameframework.framework.Sound;

/**
 * Created by Charicha on 12/22/2017.
 */

public class AndroidSound implements Sound {

    SoundPool mSoundPool;
    int mSoundID;

    public AndroidSound(SoundPool soundPool, int soundId){
        this.mSoundPool = soundPool;
        this.mSoundID = soundId;
    }

    @Override
    public void play(float volume) {
        mSoundPool.play(mSoundID, volume, volume, 0, 0, 1);
    }

    @Override
    public void dispose() {
        mSoundPool.unload(mSoundID);
    }
}

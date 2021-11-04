package com.charicha.gameframework.impl;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.provider.MediaStore;

import com.charicha.gameframework.framework.Audio;
import com.charicha.gameframework.framework.Music;
import com.charicha.gameframework.framework.Sound;

import java.io.IOException;

/**
 * Created by Charicha on 12/22/2017.
 */

public class AndroidAudio implements Audio {

    AssetManager mAssetManager;
    SoundPool mSoundPool;

    public AndroidAudio(Activity activity){
//        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, AudioManager.FLAG_SHOW_UI);
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.mAssetManager = activity.getAssets();
        this.mSoundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public Music newMusic(String fileName) {
        try {
            return new AndroidMusic(mAssetManager.openFd(fileName));
        } catch(IOException ioe){
            ioe.printStackTrace();
            throw new RuntimeException("Couldn't load music '" + fileName + "'.");
        }
    }

    @Override
    public Sound newSound(String fileName) {
        try {
            int soundId = mSoundPool.load(mAssetManager.openFd(fileName), 0);
            return new AndroidSound(mSoundPool, soundId);
        } catch(IOException ioe){
            ioe.printStackTrace();
            throw new RuntimeException("Couldn't load sound '" + fileName + "'.");
        }
    }

}

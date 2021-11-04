package com.charicha.gameframework.impl;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.charicha.gameframework.framework.Music;

import java.io.IOException;

/**
 * Created by Charicha on 12/22/2017.
 */

public class AndroidMusic implements Music, MediaPlayer.OnCompletionListener {

    MediaPlayer mMediaPlayer;
    boolean isPrepared = false;

    public AndroidMusic(AssetFileDescriptor assetFileDescriptor){
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mMediaPlayer.setOnCompletionListener(this);
            isPrepared = true;
            mMediaPlayer.prepare();
        } catch(IOException ioe){
            ioe.printStackTrace();
            throw new RuntimeException("Couldn't load music!");
        }
    }

    @Override
    public void play(){
        if(mMediaPlayer.isPlaying())
            return;
        try {
            synchronized (this) {
                if (!isPrepared)
                    mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
            throw new RuntimeException("Couldn't run music!");
        }
    }

    @Override
    public void pause(){
        if(mMediaPlayer.isPlaying())
            mMediaPlayer.pause();
    }

    @Override
    public void stop(){
        mMediaPlayer.stop();
        synchronized (this){
            isPrepared = false;
        }
    }

    @Override
    public void dispose(){
        if(mMediaPlayer.isPlaying())
            mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        synchronized (this){
            isPrepared = false;
        }
    }

    @Override
    public boolean isPlaying(){
        return mMediaPlayer.isPlaying();
    }

    @Override
    public boolean isLooping(){
        return mMediaPlayer.isLooping();
    }

    @Override
    public boolean isStopped(){
        return !isPrepared;
    }
}

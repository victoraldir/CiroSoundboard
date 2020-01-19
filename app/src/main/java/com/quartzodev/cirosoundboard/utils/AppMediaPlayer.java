package com.quartzodev.cirosoundboard.utils;


import android.content.Context;
import android.media.MediaPlayer;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by victoraldir on 14/12/2017.
 */

public class AppMediaPlayer implements LifecycleObserver, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mPlayer;
    private Context mContext;
    private FancyButton mFancyButton;
    private String lastResourceName;

    public static AppMediaPlayer newInstance(Context appContext, LifecycleOwner lifecycleOwner){
        return new AppMediaPlayer(appContext,lifecycleOwner);
    }


    public AppMediaPlayer(Context appContext, LifecycleOwner lifecycleOwner){

        mContext = appContext;
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);

        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause(){
        if(mPlayer.isPlaying())
            mPlayer.stop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume(){

    }

    public void setButton(FancyButton fancyButton){
        if(mPlayer.isPlaying() && mFancyButton != null){
            toggleButtonPlaying(false);
        }

        mFancyButton = fancyButton;
    }

    public void playAudio(String resourceName){

        try {

            if(lastResourceName != null && lastResourceName.equals(resourceName) && mPlayer.isPlaying()){
                lastResourceName = null;
                toggleButtonPlaying(false);
                mPlayer.reset();
                return;
            }

            lastResourceName = resourceName;

            mPlayer.reset();
            mPlayer.setDataSource(mContext, UriUtils.getResourceUri(resourceName,mContext));
            mPlayer.prepareAsync();

            toggleButtonPlaying(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playRandomAudio(String resourceName){

        try {

            mPlayer.reset();
            mPlayer.setDataSource(mContext, UriUtils.getResourceUri(resourceName,mContext));
            mPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toggleButtonPlaying(boolean flag){
        if(mFancyButton != null){

            if(flag){
                mFancyButton.setIconResource("\uf04c");
            }else{
                mFancyButton.setIconResource("\uf04b");
            }

        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        toggleButtonPlaying(false);
    }
}

package com.quartzodev.cirosoundboard.data.source;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.quartzodev.cirosoundboard.data.Audio;
import com.quartzodev.cirosoundboard.data.source.local.audio.AudioDataSource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by victoraldir on 20/12/2017.
 */

public class AudioRepository implements AudioDataSource {

    private volatile static AudioRepository INSTANCE = null;

    private AudioDataSource mAudioDataSource;

    private AudioRepository(AudioDataSource audioDataSource){
        mAudioDataSource = audioDataSource;
    }

    public static AudioRepository getInstance(AudioDataSource audioDataSource){
        if(INSTANCE == null)
            INSTANCE = new AudioRepository(audioDataSource);

        return INSTANCE;
    }

    @Override
    public LiveData<List<Audio>> getAudiosBySectionId(@NonNull final Long sectionId) {
        checkNotNull(sectionId);

        return mAudioDataSource.getAudiosBySectionId(sectionId);
    }

    @Override
    public void getRandomAudio(@NonNull GetObjectCallback<Audio> callback) {
        mAudioDataSource.getRandomAudio(callback);
    }

    @Override
    public void updateAudio(@NonNull Audio audio) {
        mAudioDataSource.updateAudio(audio);
    }


    @Override
    public LiveData<List<Audio>> loadFavorite() {
        return mAudioDataSource.loadFavorite();
    }

}

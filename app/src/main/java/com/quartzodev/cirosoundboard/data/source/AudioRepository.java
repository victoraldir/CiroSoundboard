package com.quartzodev.cirosoundboard.data.source;

import android.support.annotation.NonNull;


import com.quartzodev.cirosoundboard.data.Audio;
import com.quartzodev.cirosoundboard.data.Section;
import com.quartzodev.cirosoundboard.data.source.local.audio.AudioDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by victoraldir on 20/12/2017.
 */

public class AudioRepository implements AudioDataSource {

    private volatile static AudioRepository INSTANCE = null;

    private AudioDataSource mAudioDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<Long, List<Audio>> mCachedAudio;

    private AudioRepository(AudioDataSource audioDataSource){
        mAudioDataSource = audioDataSource;
    }

    public static AudioRepository getInstance(AudioDataSource audioDataSource){
        if(INSTANCE == null)
            INSTANCE = new AudioRepository(audioDataSource);

        return INSTANCE;
    }

    @Override
    public void getAudiosBySectionId(@NonNull final Long sectionId, @NonNull final GenericDataSource.LoadListCallback<Audio> callback) {
        checkNotNull(callback);
        checkNotNull(sectionId);

        if(mCachedAudio != null && mCachedAudio.containsKey(sectionId)){
            callback.onListLoaded(mCachedAudio.get(sectionId));
        }

        mAudioDataSource.getAudiosBySectionId(sectionId, new GenericDataSource.LoadListCallback<Audio>() {
            @Override
            public void onListLoaded(List<Audio> list) {

                refreshCacheAudio(sectionId, list);

                callback.onListLoaded(list);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void getRandomAudio(@NonNull GetObjectCallback<Audio> callback) {
        mAudioDataSource.getRandomAudio(callback);
    }

    @Override
    public void updateFavoriteFlag(@NonNull Long sectionId, @NonNull boolean flagFavorite) {
        mAudioDataSource.updateFavoriteFlag(sectionId, flagFavorite);
    }

    private void refreshCacheAudio(Long sectionId ,List<Audio> audioList) {
        if (mCachedAudio == null) {
            mCachedAudio = new LinkedHashMap<>();
        }
        mCachedAudio.clear();

        mCachedAudio.put(sectionId, audioList);
    }
}

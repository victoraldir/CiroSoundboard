package com.quartzodev.cirosoundboard.data.source.local.audio;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.quartzodev.cirosoundboard.data.Audio;
import com.quartzodev.cirosoundboard.data.Section;
import com.quartzodev.cirosoundboard.data.source.GenericDataSource;
import com.quartzodev.cirosoundboard.utils.AppExecutors;

import java.util.List;

/**
 * Created by victoraldir on 20/12/2017.
 */

public class AudioDataSourceImpl implements AudioDataSource {

    private static volatile AudioDataSourceImpl INSTANCE;

    private AudioDao mAudioDao;

    private AppExecutors mAppExecutors;

    private AudioDataSourceImpl(@NonNull AppExecutors appExecutors,
                                @NonNull AudioDao audioDao){
        mAppExecutors = appExecutors;
        mAudioDao = audioDao;
    }

    public static AudioDataSourceImpl getInstance(@NonNull AppExecutors appExecutors,
                                                  @NonNull AudioDao audioDao){
        if(INSTANCE == null) {
            synchronized (AudioDataSourceImpl.class) {
                INSTANCE = new AudioDataSourceImpl(appExecutors, audioDao);
            }
        }

        return INSTANCE;
    }

    @Override
    public LiveData<List<Audio>> getAudiosBySectionId(@NonNull final Long sectionId) {

        return mAudioDao.getAudiosBySectionId(sectionId);

//        Runnable getAudiosTask = new Runnable() {
//
//            @Override
//            public void run() {
//                final List<Audio> audioList = mAudioDao.getAudiosBySectionId(sectionId);
//
//                mAppExecutors.mainThread().execute(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        if(audioList.isEmpty()){
//                            callback.onDataNotAvailable();
//                        }else {
//                            callback.onListLoaded(audioList);
//                        };
//                    }
//                });
//            }
//
//
//        };
//
//        mAppExecutors.diskIO().execute(getAudiosTask);
    }

    @Override
    public void getRandomAudio(@NonNull final GetObjectCallback<Audio> callback) {
        Runnable getAudioTask = new Runnable() {

            @Override
            public void run() {
                final Audio audio = mAudioDao.getRandomAudio();

                mAppExecutors.mainThread().execute(new Runnable() {

                    @Override
                    public void run() {
                        if(audio != null){
                            callback.onObjectLoaded(audio);
                        }else {
                            callback.onDataNotAvailable();
                        };
                    }
                });
            }


        };

        mAppExecutors.diskIO().execute(getAudioTask);
    }

    @Override
    public void updateFavoriteFlag(@NonNull final Long sectionId, @NonNull final boolean flagFavorite) {
        Runnable getAudioTask = new Runnable() {

            @Override
            public void run() {
                mAudioDao.updateFavoriteFlag(sectionId,flagFavorite);
            }
        };

        mAppExecutors.diskIO().execute(getAudioTask);
    }

    @Override
    public LiveData<List<Audio>> loadFavorite() {
        return mAudioDao.listFavorites();
    }

}

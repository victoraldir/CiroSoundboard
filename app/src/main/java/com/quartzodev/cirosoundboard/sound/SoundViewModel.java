package com.quartzodev.cirosoundboard.sound;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.quartzodev.cirosoundboard.data.Audio;
import com.quartzodev.cirosoundboard.data.Section;
import com.quartzodev.cirosoundboard.data.source.AudioRepository;
import com.quartzodev.cirosoundboard.data.source.GenericDataSource;
import com.quartzodev.cirosoundboard.data.source.SectionRepository;

import java.util.List;

/**
 * Created by victoraldir on 17/12/2017.
 */

public class SoundViewModel extends ViewModel {

    private static final int AD_TRIGGER_COUNT = 5;

    private int mSwipeCounter;

    private LiveData<List<Audio>> mFavoriteList;

    private final AudioRepository mAudioRepository;
    private final SectionRepository mSectionRepository;

    public SoundViewModel(AudioRepository audioRepository,
                          SectionRepository sectionRepository) {

        mAudioRepository = audioRepository;
        mSectionRepository = sectionRepository;
        mFavoriteList = mAudioRepository.loadFavorite();
    }


    public LiveData<List<Audio>> loadAudios(Long sectionId){
        return mAudioRepository.getAudiosBySectionId(sectionId);
    }

    public void updateAudio(Audio audio){
        mAudioRepository.updateAudio(audio);
    }

    public void loadRandomAudio(GenericDataSource.GetObjectCallback<Audio> callback) {
        mAudioRepository.getRandomAudio(callback);
    }

    public LiveData<List<Audio>> getFavoriteList() {
        return mFavoriteList;
    }

    public void loadSections(GenericDataSource.LoadListCallback<Section> callback) {
        mSectionRepository.getSections(callback);
    }

    public boolean isShowInterstitialAd(){
        if(mSwipeCounter == AD_TRIGGER_COUNT){
            mSwipeCounter = 0;
            return true;
        }else{
            mSwipeCounter++;
        }
        return false;
    }

}

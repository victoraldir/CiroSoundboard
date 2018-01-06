package com.quartzodev.cirosoundboard.sound;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

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
}

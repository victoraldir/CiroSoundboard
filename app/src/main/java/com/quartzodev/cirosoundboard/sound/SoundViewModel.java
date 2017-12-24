package com.quartzodev.cirosoundboard.sound;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.quartzodev.cirosoundboard.data.Audio;
import com.quartzodev.cirosoundboard.data.Section;
import com.quartzodev.cirosoundboard.data.source.AudioRepository;
import com.quartzodev.cirosoundboard.data.source.GenericDataSource;
import com.quartzodev.cirosoundboard.data.source.SectionRepository;

/**
 * Created by victoraldir on 17/12/2017.
 */

public class SoundViewModel extends ViewModel {

    private final AudioRepository mAudioRepository;
    private final SectionRepository mSectionRepository;

    public SoundViewModel(AudioRepository audioRepository,
                          SectionRepository sectionRepository) {

        mAudioRepository = audioRepository;
        mSectionRepository = sectionRepository;
    }


    public void loadAudios(Long sectionId, GenericDataSource.LoadListCallback<Audio> callback){
        mAudioRepository.getAudiosBySectionId(sectionId, callback);
    }


    public void loadRandomAudio(GenericDataSource.GetObjectCallback<Audio> callback) {
        mAudioRepository.getRandomAudio(callback);
    }


    public void start() {}


    public void loadSections(GenericDataSource.LoadListCallback<Section> callback) {
        mSectionRepository.getSections(callback);
    }
}

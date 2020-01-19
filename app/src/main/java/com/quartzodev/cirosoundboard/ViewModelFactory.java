package com.quartzodev.cirosoundboard;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.quartzodev.cirosoundboard.data.source.AudioRepository;
import com.quartzodev.cirosoundboard.data.source.SectionRepository;
import com.quartzodev.cirosoundboard.data.source.local.CiroSoundBoardDatabase;
import com.quartzodev.cirosoundboard.data.source.local.audio.AudioDataSourceImpl;
import com.quartzodev.cirosoundboard.data.source.local.section.SectionDataSourceImpl;
import com.quartzodev.cirosoundboard.sound.SoundViewModel;
import com.quartzodev.cirosoundboard.utils.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by victoraldir on 20/12/2017.
 */

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final AudioRepository mAudioRepository;
    private final SectionRepository mSectionRepository;

    private ViewModelFactory(AudioRepository audioRepository,
                             SectionRepository sectionRepository) {
        mAudioRepository = audioRepository;
        mSectionRepository = sectionRepository;
    }

    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    checkNotNull(application);

                    CiroSoundBoardDatabase database = CiroSoundBoardDatabase.getInstance(application);

                    AppExecutors appExecutors = new AppExecutors();

                    INSTANCE = new ViewModelFactory(AudioRepository.getInstance(

                            AudioDataSourceImpl.getInstance(appExecutors,
                                    database.audioDao())),

                            SectionRepository.getInstance(SectionDataSourceImpl.getInstance(appExecutors,
                                    database.sectionDao())));
                }
            }
        }
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(SoundViewModel.class)) {
            return (T) new SoundViewModel(mAudioRepository,mSectionRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

}

package com.quartzodev.cirosoundboard.data.source.local.audio;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.quartzodev.cirosoundboard.data.Audio;
import com.quartzodev.cirosoundboard.data.Section;
import com.quartzodev.cirosoundboard.data.source.GenericDataSource;

import java.util.List;

/**
 * Created by victoraldir on 20/12/2017.
 */

public interface AudioDataSource extends GenericDataSource {

    LiveData<List<Audio>> getAudiosBySectionId(@NonNull Long sectionId);

    void getRandomAudio(@NonNull GenericDataSource.GetObjectCallback<Audio> callback);

    void updateAudio(@NonNull Audio audio);

    LiveData<List<Audio>> loadFavorite();
}

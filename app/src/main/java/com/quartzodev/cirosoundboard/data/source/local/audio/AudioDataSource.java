package com.quartzodev.cirosoundboard.data.source.local.audio;

import android.support.annotation.NonNull;

import com.quartzodev.cirosoundboard.data.Audio;
import com.quartzodev.cirosoundboard.data.Section;
import com.quartzodev.cirosoundboard.data.source.GenericDataSource;

/**
 * Created by victoraldir on 20/12/2017.
 */

public interface AudioDataSource extends GenericDataSource {

    void getAudiosBySectionId(@NonNull Long sectionId, @NonNull GenericDataSource.LoadListCallback<Audio> callback);

    void getRandomAudio(@NonNull GenericDataSource.GetObjectCallback<Audio> callback);

    void updateFavoriteFlag(@NonNull Long sectionId, @NonNull boolean flagFavorite);
}

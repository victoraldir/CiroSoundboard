package com.quartzodev.cirosoundboard.data.source.local.audio;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.quartzodev.cirosoundboard.data.Audio;

import java.util.List;

/**
 * Created by victoraldir on 16/12/2017.
 */

@Dao
public interface AudioDao {

    @Query("SELECT * FROM Audio WHERE audio.section_id = :sectionId")
    LiveData<List<Audio>> getAudiosBySectionId(Long sectionId);

    @Query("SELECT * FROM Audio ORDER BY RANDOM() LIMIT 1")
    Audio getRandomAudio();

    @Update
    void updateAudio(Audio audio);

    @Query("SELECT * FROM Audio WHERE flag_favorite = 1")
    LiveData<List<Audio>> listFavorites();
}

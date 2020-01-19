package com.quartzodev.cirosoundboard.data.source.local.section;


import androidx.room.Dao;
import androidx.room.Query;

import com.quartzodev.cirosoundboard.data.Section;

import java.util.List;

/**
 * Created by victoraldir on 16/12/2017.
 */

@Dao
public interface SectionDao {

    @Query("SELECT * FROM Section")
    List<Section> getSection();

}

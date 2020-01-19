package com.quartzodev.cirosoundboard.data.source.local.section;

import androidx.annotation.NonNull;

import com.quartzodev.cirosoundboard.data.Section;
import com.quartzodev.cirosoundboard.data.source.GenericDataSource;

/**
 * Created by victoraldir on 20/12/2017.
 */

public interface SectionDataSource extends GenericDataSource {

    void getSections(@NonNull GenericDataSource.LoadListCallback<Section> callback);

}

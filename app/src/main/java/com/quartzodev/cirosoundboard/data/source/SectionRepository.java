package com.quartzodev.cirosoundboard.data.source;


import androidx.annotation.NonNull;

import com.quartzodev.cirosoundboard.data.Section;
import com.quartzodev.cirosoundboard.data.source.local.section.SectionDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by victoraldir on 20/12/2017.
 */

public class SectionRepository implements SectionDataSource {

    private volatile static SectionRepository INSTANCE = null;

    private SectionDataSource mSectionDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<Long, Section> mCachedSection;

    private SectionRepository(SectionDataSource sectionDataSource){
        mSectionDataSource = sectionDataSource;
    }

    public static SectionRepository getInstance(SectionDataSource sectionDataSource){
        if(INSTANCE == null)
            INSTANCE = new SectionRepository(sectionDataSource);

        return INSTANCE;
    }

    @Override
    public void getSections(@NonNull final GenericDataSource.LoadListCallback<Section> callback) {
        checkNotNull(callback);

        if (mCachedSection != null) {
            callback.onListLoaded(new ArrayList<>(mCachedSection.values()));
            return;
        }

        mSectionDataSource.getSections(new GenericDataSource.LoadListCallback<Section>() {
            @Override
            public void onListLoaded(List<Section> list) {

                refreshCacheSection(list);

                callback.onListLoaded(list);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCacheSection(List<Section> sectionList) {
        if (mCachedSection == null) {
            mCachedSection = new LinkedHashMap<>();
        }
        mCachedSection.clear();
        for (Section section : sectionList) {
            mCachedSection.put(section.getId(), section);
        }
    }
}

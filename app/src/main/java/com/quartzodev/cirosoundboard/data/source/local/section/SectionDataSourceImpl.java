package com.quartzodev.cirosoundboard.data.source.local.section;

import android.support.annotation.NonNull;

import com.quartzodev.cirosoundboard.data.Audio;
import com.quartzodev.cirosoundboard.data.Section;
import com.quartzodev.cirosoundboard.utils.AppExecutors;

import java.util.List;

/**
 * Created by victoraldir on 20/12/2017.
 */

public class SectionDataSourceImpl implements SectionDataSource {

    private static volatile SectionDataSourceImpl INSTANCE;

    private SectionDao mSectionDao;

    private AppExecutors mAppExecutors;

    private SectionDataSourceImpl(@NonNull AppExecutors appExecutors,
                                @NonNull SectionDao sectionDao){
        mAppExecutors = appExecutors;
        mSectionDao = sectionDao;
    }

    public static SectionDataSourceImpl getInstance(@NonNull AppExecutors appExecutors,
                                                  @NonNull SectionDao sectionDao){
        if(INSTANCE == null) {
            synchronized (SectionDataSourceImpl.class) {
                INSTANCE = new SectionDataSourceImpl(appExecutors, sectionDao);
            }
        }

        return INSTANCE;
    }

    @Override
    public void getSections(@NonNull final LoadListCallback<Section> callback) {
        Runnable getAudiosTask = new Runnable() {

            @Override
            public void run() {
                final List<Section> sectionList = mSectionDao.getSection();

                mAppExecutors.mainThread().execute(new Runnable() {

                    @Override
                    public void run() {
                        if(sectionList.isEmpty()){
                            callback.onDataNotAvailable();
                        }else {
                            callback.onListLoaded(sectionList);
                        };
                    }
                });
            }


        };

        mAppExecutors.diskIO().execute(getAudiosTask);
    }
}

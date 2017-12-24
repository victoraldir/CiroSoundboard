package com.quartzodev.cirosoundboard.data.source;


import java.util.List;

/**
 * Created by victoraldir on 20/12/2017.
 */

public interface GenericDataSource{

    interface LoadListCallback<T> {

        void onListLoaded(List<T> list);

        void onDataNotAvailable();
    }

    interface GetObjectCallback<T> {

        void onObjectLoaded(T object);

        void onDataNotAvailable();
    }

}

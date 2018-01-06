package com.quartzodev.cirosoundboard.utils;

import android.content.Context;
import android.net.Uri;

/**
 * Created by victoraldir on 24/12/2017.
 */

public class UriUtils {

    /**
     *
     * @param resourceName (Without extension)
     * @return
     */

    public static Uri getResourceUri(String resourceName, Context context){

        return Uri.parse("android.resource://" + context.getPackageName() + "/" +
                context.getResources().getIdentifier(resourceName,
                        "raw", context.getPackageName()));
    }
}

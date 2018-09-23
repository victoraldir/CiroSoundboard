package com.quartzodev.cirosoundboard.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.core.content.FileProvider;

/**
 * Created by victoraldir on 25/12/2017.
 */

public class FileUtils {

    private static final String MP3_EXTENSION = ".mp3";
    private static final int BUFFER_SIZE = 2; // 2 MEGA

    public static Uri getExistingFile(Context context, String fileName){

        File songPath = new File(context.getExternalCacheDir(), "songs");
        File file = new File(songPath, fileName.concat(MP3_EXTENSION));

        if(file.exists()){

            Uri contentUri;

            if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
                contentUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file);
            } else {
                contentUri = Uri.fromFile(file);
            }

            return contentUri;
        }

        return null;
    }

    public static Uri writeStreamToFile(Context context, String fileName, String audioPath){

        InputStream inputStream = null;

        try {
            File songsPath = new File(context.getExternalCacheDir(), "songs");

            if(!songsPath.exists()) songsPath.mkdir();

            File file = new File(songsPath, fileName.concat(MP3_EXTENSION));

            inputStream = context.getContentResolver().openInputStream(UriUtils.getResourceUri(audioPath,context));

            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[BUFFER_SIZE * 1024]; // or other buffer size
                int read;

                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();

                Uri contentUri = null;

                if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
                    contentUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file);
                } else {
                    contentUri = Uri.fromFile(file);
                }

                return contentUri;
            } finally {
                output.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

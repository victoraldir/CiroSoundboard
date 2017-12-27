package com.quartzodev.cirosoundboard.utils;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by victoraldir on 25/12/2017.
 */

public class FileUtils {

    private static final String MP3_EXTENSION = ".mp3";
    private static final int BUFFER_SIZE = 2; // 2 MEGA

    public static File getExistingFile(Context context, String fileName){

        File file = new File(context.getExternalCacheDir(), fileName.concat(MP3_EXTENSION));

        if(file.exists()){
            return file;
        }

        return null;
    }

    public static File writeStreamToFile(Context context, String fileName, InputStream input){

        try {
            File file = new File(context.getExternalCacheDir(), fileName.concat(MP3_EXTENSION));
            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[BUFFER_SIZE * 1024]; // or other buffer size
                int read;

                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();

                return file;
            } finally {
                output.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

package com.rhpark.welcomehome;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;

import com.rhpark.welcomehome.data.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by rhpark on 2015. 9. 2..
 */
public class Utils {

    public static final String saveHomeImg(Context context, Bitmap bitmap) {
        File file = new File(getUserStorageDir(context.getApplicationContext()), Constants.HOME_IMG);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return file.getAbsolutePath();
        }
    }

    /**
     * // path to /data/data/yourapp/app_data/userInfo
     *
     * @param context
     * @return
     */
    public static File getUserStorageDir(Context context){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("userInfo", Context.MODE_PRIVATE);
        return directory;
    }
}

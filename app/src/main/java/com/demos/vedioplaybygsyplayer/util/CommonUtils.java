package com.demos.vedioplaybygsyplayer.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by shuyu on 2016/11/11.
 */

public class CommonUtils {

    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();

    public static final String NAME = "xlht-manager";

    public static void setViewHeight(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (null == layoutParams)
            return;
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void saveBitmap(Bitmap bitmap) throws FileNotFoundException {
        if (bitmap != null) {
            File file = new File(getPath(), "capture" + System.currentTimeMillis() + ".jpg");
            OutputStream outputStream;
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            bitmap.recycle();
        }
    }

    public static String getAppPath(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(SD_PATH);
        sb.append(File.separator);
        sb.append(name);
        sb.append(File.separator);
        return sb.toString();
    }

    public static String getPath() {
        String path = getAppPath(NAME);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


}

package com.demos.vedioplaybygsyplayer.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

/**
 * 获取缓存大小并清理缓存
 */

public class DataCleanManagerUtils {

    /*
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     */

    /**
     * 获取缓存值
     */
    public static String getTotalCacheSize(Context context) {

        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        File captureFile = new File(CommonUtils.getPath());
        if (captureFile.exists())
            cacheSize += getFolderSize(captureFile);
        return getFormatSize(cacheSize);
    }

    /**
     * 清除所有缓存
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        File captureFile = new File(CommonUtils.getPath());
        if (captureFile.exists())
            deleteDir(captureFile);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
            //TODO 有网页清理时注意排错
            // 原作者：下面两句清理webview网页缓存.但是每次执行都报false,我用的是魅蓝5.1的系统，后来发现且/data/data/应用package目录下找不到database文///件夹 不知道是不是个别手机的问题，
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        }
    }

    /**
     * 删除某个文件
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null && children.length > 0) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        }
        if (dir != null) {
            return dir.delete();
        } else {
            return false;
        }
    }

    /**
     * 获取文件
     */
    private static long getFolderSize(File file) {
        long size = 0;
        if (file != null) {
            File[] fileList = file.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (File file1 : fileList) {
                    // 如果下面还有文件
                    if (file1.isDirectory()) {
                        size += getFolderSize(file1);
                    } else {
                        size += file1.length();
                    }
                }
            }
        }
        return size;
    }

    /**
     * 格式化单位
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        double megaByte = kiloByte / 1024;
        double gigaByte = megaByte / 1024;
        if (kiloByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(size));
            return result2 + "B";
        }
        if (megaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(kiloByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = BigDecimal.valueOf(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}

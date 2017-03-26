package com.idotools.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wuxiaojun on 16-10-8.
 */
public class FileUtils {

    /***
     * 读取数据
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readFile(Context context, String fileName) {
        String content = null;
        FileInputStream fileInputStream = null;
        try {
            if (!TextUtils.isEmpty(fileName)) {
                fileInputStream = context.openFileInput(fileName);
                if (fileInputStream != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    while ((len = fileInputStream.read(bytes)) != -1) {
                        bos.write(bytes, 0, len);
                    }
                    bos.close();
                    content = new String(bos.toByteArray());
                }
            }
        } catch (IOException e) {
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

    /***
     * 保存数据
     *
     * @param context
     * @param fileName
     * @param content
     */
    public static void saveFile(Context context, String fileName, String content) {
        FileOutputStream fileOutputStream = null;
        try {
            if (!TextUtils.isEmpty(fileName)) {
                fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                ByteArrayInputStream bis = new ByteArrayInputStream(content.getBytes());
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = bis.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, len);
                }
                bis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     * 读取assetmanager下的文件
     * @param mContext
     * @return
     */
    public static String getAssetsFile(Context mContext){
        String content = null;
        try {
            AssetManager am = mContext.getAssets();
            InputStream open = am.open("area.json");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = open.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            bos.close();
            content = new String(bos.toByteArray());
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

}

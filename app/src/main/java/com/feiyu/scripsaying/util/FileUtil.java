package com.feiyu.scripsaying.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by YueDong on 2017/1/1.
 */
public class FileUtil {
    /**
     * 图片基础路径
     * @return
     */
    public static File getPicBaseFile(String filePath) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  //文件可用
            File f = new File(Environment.getExternalStorageDirectory(), filePath);
            if(!f.exists()) f.mkdirs();
            return f;
        }else{
            return null;
        }
    }

    //获取文件名
    public static String getFileName(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date cruDate = Calendar.getInstance().getTime();
        String strDate = sdf.format(cruDate);
        String fileName =strDate+".jpg";
        return fileName;
    }
    //保存图片
    public static String saveBitmap(Bitmap bitmap, String fileName, File baseFile) {
        FileOutputStream bos = null;
        File imgFile = new File(baseFile, "/" + fileName);
        try {
            bos = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.flush();
                bos.close();
                return imgFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static final String TAG = "file";

    /**
     * 读取asset目录下文件
     * @param context
     * @param file
     * @param code
     * @return
     */
    public static String readFile(Context context, String file, String code) {
        AssetManager am = context.getAssets();
        int len;
        byte[] buf;
        String result = "";
        try {
            InputStream in = am.open(file);
            len = in.available();
            buf = new byte[len];
            in.read(buf, 0, len);
            result = new String(buf, code);
            in.close();
        } catch (Exception e) {
            Log.e(TAG, "readFile Exception==" + e.getMessage());
        }
        return result;
    }

    private static String fileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + "Camera";

    /**
     * 获取路径
     * @param fileName
     * @return
     */
    public static String getFilePath(String fileName) {
        String fileSrc = fileDir + File.separator + fileName;
        return fileSrc;
    }

    /**
     * 创建文件
     * @param fileName
     */
    public static void createFile(String fileName) {
        File src = new File(fileDir);
        if (!src.exists()) {
            try {
                src.mkdirs();
                File file = new File(src, fileName);
                file.createNewFile();
            } catch (IOException e) {
                Log.i("hdm", "createFile  IOException");
            }
        }
    }

    /**
     * 删除文件
     * @param fileName
     */
    public static void deleteFile(String fileName) {
        File file = new File(getFilePath(fileName));
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 写数据到文件中
     * @param datas
     * @param fileName
     */
    public static void writeToFile(byte[] datas, String fileName) {
        FileOutputStream stream;
        createFile(fileName);
        try {
            Log.i(TAG, "write  begin");
            stream = new FileOutputStream(new File(getFilePath(fileName)));
            stream.write(datas);
            Log.i(TAG, "write  succcess");
            stream.flush();
            stream.close();
        } catch (FileNotFoundException e) {
            Log.i(TAG, "write  FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i(TAG, "write  FileNotFoundException");
            e.printStackTrace();
        }
    }

    /**
     * 保存路径
     * @param bitmap
     * @param fileName
     */
    public static void saveFilePath(Bitmap bitmap, String fileName) {
        OutputStream stream = null;
        try {
            createFile(fileName);
            stream = new FileOutputStream(new File(getFilePath(fileName)));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            Log.i(TAG, "saveFilePath  success");
        } catch (FileNotFoundException e) {
            Log.i(TAG, "saveFilePath  IOException");
        } catch (IOException e) {
            Log.i(TAG, "saveFilePath  IOException");
        }
    }

    /**
     * 获取路径
     * @param filePath
     * @return
     */
    public static File[] getFiles(String filePath) {
        File[] files = null;
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            files = new File[]{file};
        }
        return files;
    }
}

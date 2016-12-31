package com.feiyu.scripsaying.util;

import android.util.Log;

/**
 * Created by YueDong on 2016/9/23.
 */
public class LogUtil {
    public static boolean isDebug=true;
    public static void d(String tag, String msg){
        if(isDebug){
            Log.d(tag, msg);
        }
    }
    public static void e(String tag, String msg){
        if(isDebug){
            Log.e(tag, msg);
        }
    }
    public static void d(Object object, String msg){
        if(isDebug){
            Log.d(object.getClass().getSimpleName(), msg);
        }
    }
    public static void e(Object object, String msg){
        if(isDebug){
            Log.e(object.getClass().getSimpleName(), msg);
        }
    }
}

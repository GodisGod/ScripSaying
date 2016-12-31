package com.feiyu.scripsaying.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Bob on 15/8/21.
 */
public class ScripContext {

    private static ScripContext mScripContext;
    public Context mContext;
    private SharedPreferences mPreferences;

    public static ScripContext getInstance() {

        if (mScripContext == null) {
            mScripContext = new ScripContext();
        }
        return mScripContext;
    }

    private ScripContext() {
    }

    private ScripContext(Context context) {
        mContext = context;
        mScripContext = this;
        //http初始化 用于登录、注册使用
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }


    public static void init(Context context) {
        mScripContext = new ScripContext(context);
    }

    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }

}

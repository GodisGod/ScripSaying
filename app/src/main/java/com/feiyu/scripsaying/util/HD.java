package com.feiyu.scripsaying.util;

import android.util.Log;
import android.widget.Toast;

import com.feiyu.scripsaying.APP;


/**
 * Created by 鸿达 on 2016/12/12.
 */
public class HD {
    private static final boolean debug = true;
    private static final String TAG = "LHD";

    public static void TOS(String str) {
        if (debug) {
            Toast.makeText(APP.getContext(), str, Toast.LENGTH_SHORT).show();
        }
    }

    public static void LOG(String str) {
        if (debug) {
            Log.i(TAG, str);
        }
    }
    public static void TLOG(String str) {
        if (debug) {
            Log.i(TAG, str);
            Toast.makeText(APP.getContext(), str, Toast.LENGTH_SHORT).show();
        }
    }

}

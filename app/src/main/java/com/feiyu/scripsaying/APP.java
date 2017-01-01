package com.feiyu.scripsaying;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.WindowManager;

import com.feiyu.scripsaying.listener.ScripConnectStatusListener;
import com.feiyu.scripsaying.listener.ScripConversationBehaviorListener;
import com.feiyu.scripsaying.listener.ScripConversationListBehaviorListener;
import com.feiyu.scripsaying.listener.ScripReceiveMessageListener;
import com.feiyu.scripsaying.listener.ScripSendMessageListener;
import com.feiyu.scripsaying.provider.UserInfoProvider;
import com.feiyu.scripsaying.util.ScripContext;

import cn.bmob.v3.Bmob;
import io.rong.imkit.RongIM;

/**
 * Created by 李鸿达 on 2016/12/26.
 */

public class App extends Application {

    private static Context mContext;
    private static App sInstance;
    public static int SCREEN_WIDTH;
    private ScripSendMessageListener listener;
    private ScripReceiveMessageListener receiveMessageListener;
    private ScripConnectStatusListener connectStatusListener;
    private ScripConversationListBehaviorListener conversationListBehaviorListener;
    private ScripConversationBehaviorListener conversationBehaviorListener;
    private UserInfoProvider userInfoProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        initSdk();
        initListener();
    }
    private void init() {
        mContext = getApplicationContext();
        sInstance = this;
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point screen = new Point();
        SCREEN_WIDTH = Math.min(screen.x, screen.y);
    }

    private void initSdk() {
        //Bmob初始化
        Bmob.initialize(this,"76202b4f9b7fad037aff78434b2ceb9b");
        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
            RongIM.getInstance().setSendMessageListener(listener);
//            RongIM.setOnReceiveMessageListener(receiveMessageListener);
            RongIM.setConnectionStatusListener(connectStatusListener);
            RongIM.setConversationListBehaviorListener(conversationListBehaviorListener);
            RongIM.setConversationBehaviorListener(conversationBehaviorListener);
            initUserProvider();
            Log.i("LHD", " RongIM.init(this);");
            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
                ScripContext.init(this);
            }
        }

    }

    private void initListener() {
        listener = new ScripSendMessageListener();
        receiveMessageListener = new ScripReceiveMessageListener();
        connectStatusListener = new ScripConnectStatusListener();
        conversationListBehaviorListener = new ScripConversationListBehaviorListener();
        conversationBehaviorListener = new ScripConversationBehaviorListener();
    }

    private void initUserProvider() {
        //todo 保存用户信息到Bmob
        userInfoProvider = new UserInfoProvider();
        /**
         * 设置用户信息的提供者，供 RongIM 调用获取用户名称和头像信息。
         *
         *  UserInfoProvider 用户信息提供者。
         *  isCacheUserInfo  设置是否由 IMKit 来缓存用户信息。<br>
         *                         如果 App 提供的 UserInfoProvider
         *                         每次都需要通过网络请求用户数据，而不是将用户数据缓存到本地内存，会影响用户信息的加载速度；<br>
         *                         此时最好将本参数设置为 true，由 IMKit 将用户信息缓存到本地内存中。
         * @see UserInfoProvider
         */
        RongIM.setUserInfoProvider(userInfoProvider, true);
    }

    public static Context getContext() {
        if (mContext == null) {
            throw new NullPointerException("App Context is Null");
        }
        return mContext;
    }

    public static App getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("App sInstance is Null");
        }
        return sInstance;
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

}

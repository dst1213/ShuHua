package com.xy.shuhua.ui;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import com.xy.shuhua.common_background.Account;
import io.rong.imkit.RongIM;

/**
 * Created by xiaoyu on 2016/3/19.
 */
public class CustomApplication extends Application {
    private static CustomApplication instance;
    private Account account;

    public static boolean android_show_filter = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
        }
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static CustomApplication getInstance() {
        return instance;
    }

    public Account getAccount() {
        if (account == null) {
            account = Account.loadAccount();
        }
        return account;
    }
}

package com.reward.help.merchant;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.text.TextUtils;


import com.reward.help.merchant.chat.DemoHelper;
import com.reward.help.merchant.chat.db.DbOpenHelper;
import com.reward.help.merchant.chat.db.TopUser;
import com.reward.help.merchant.utils.CrashHandler;
import com.reward.help.merchant.utils.SpUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 更新一下,加了个注释
 * <p>
 * Created by wuxiaojun on 2017/1/4.
 */

public class App extends Application{

    private static App mApp;
    private static String APP_CLIENT_KEY = null;
    private static String APP_CLIENT_COOKIE = null;
    public static String currentUserNick = "";

    public static String getAppClientKey() {
        if (TextUtils.isEmpty(APP_CLIENT_KEY)) {
            APP_CLIENT_KEY = (String) SpUtils.get(SpUtils.SP_KEY,"String");
        }
        return APP_CLIENT_KEY;
    }

    public static void setAppClientKey(String appClientKey) {
        APP_CLIENT_KEY = appClientKey;
        SpUtils.put(SpUtils.SP_KEY,appClientKey);
    }

    public static String getAppClientCookie() {
        if (TextUtils.isEmpty(APP_CLIENT_COOKIE)) {
            APP_CLIENT_COOKIE = (String) SpUtils.get(SpUtils.SP_COOKIE,"String");
        }
        return APP_CLIENT_COOKIE;
    }

    public static void setAppClientCookie(String appClientCookie) {
        APP_CLIENT_COOKIE = appClientCookie;
        SpUtils.put(SpUtils.SP_COOKIE,appClientCookie);
    }

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
//        FreelineCore.init(this);
        mApp = this;

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(mApp.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

//        EMOptions options = new EMOptions();
//        // 默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(false);
//        //初始化
//        EMClient.getInstance().init(mApp, options);
//        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(true);
//        EaseUI.getInstance().init(this,options);
        DemoHelper.getInstance().init(mApp);


    }

    public static App getApplication(){
        return mApp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }


    public Map<String,TopUser> getTopUserList(){
        return DemoHelper.getInstance().getTopUserList();
    }

    public void setTopUserList(Map<String,TopUser> contactList){
        DemoHelper.getInstance().setTopUserList(contactList);
    }
}

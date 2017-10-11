package com.help.reward;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;

import com.help.reward.bean.Response.LoginResponse;
import com.help.reward.chat.DemoHelper;
import com.help.reward.chat.db.TopUser;
import com.help.reward.utils.Constant;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.smssdk.SMSSDK;

/**
 * 更新一下,加了个注释
 * <p>
 * Created by wuxiaojun on 2017/1/4.
 */

public class App extends Application {

    private static App mApp;
    public static String APP_CLIENT_KEY = null; // key
    public static String APP_USER_ID = null; // user_id
    public static String APP_CLIENT_COOKIE = null; // cookie
    public static LoginResponse mLoginReponse; // 会员信息
    public static String GETUI_CLIENT_ID = null; // 个推id

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
//        FreelineCore.init(this);
        mApp = this;

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

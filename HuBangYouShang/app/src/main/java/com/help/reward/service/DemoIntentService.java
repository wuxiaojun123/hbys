package com.help.reward.service;

import android.content.Context;
import android.text.TextUtils;

import com.idotools.utils.LogUtils;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wuxiaojun on 17-8-29.
 */

public class DemoIntentService extends GTIntentService {

    public DemoIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        if (msg != null) {
            byte[] payload = msg.getPayload();
            String taskid = msg.getTaskId();
            String messageid = msg.getMessageId();
            // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
            boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
            //System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));
            if (payload != null) {
                String data = new String(payload);
                LogUtils.e("GetuiSdkDemo receiver payload : " + data);
                if (!TextUtils.isEmpty(data)) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String test = jsonObject.getString("test");
                        if (!TextUtils.isEmpty(test)) {
                            //自定义通知栏消息

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        LogUtils.e("cid是：" + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
}

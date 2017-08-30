package com.help.reward.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.help.reward.App;
import com.help.reward.bean.Response.LoginResponse;
import com.help.reward.bean.UserBean;
import com.help.reward.rxbus.RxBus;
import com.help.reward.utils.JsonUtils;
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
                        boolean hasKey = jsonObject.has("update_member");
                        if (hasKey) {
                            sendUpdateMember(jsonObject);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                RxBus.getDefault().post("loginSuccess");
            }
        }
    };

    /**
     * 发送更新会员信息的数据
     *
     * @param jsonObject
     * @throws JSONException
     */
    private void sendUpdateMember(JSONObject jsonObject) throws JSONException {
        String update_member = jsonObject.getString("update_member");
        UserBean userBean = (UserBean) JsonUtils.toObject(update_member, UserBean.class);
        // 发送消息给我的界面，更新数据
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.username = userBean.username;
        loginResponse.userid = userBean.userid;
        loginResponse.available_predeposit = userBean.available_predeposit;
        loginResponse.avator = userBean.avator;
        loginResponse.complained = userBean.complained;
        loginResponse.discount_level = userBean.discount_level;
        loginResponse.easemobId = userBean.easemobId;
        loginResponse.complaint = userBean.complaint;
        loginResponse.key = userBean.key;
        loginResponse.new_message = userBean.new_message;
        loginResponse.general_voucher = userBean.general_voucher;
        loginResponse.level_name = userBean.level_name;
        loginResponse.point = userBean.point;
        loginResponse.voucher = userBean.voucher;
        loginResponse.people_help = userBean.people_help;

        App.mLoginReponse = loginResponse;

        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        App.GETUI_CLIENT_ID = clientid;
        LogUtils.e("cid是：" + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
}

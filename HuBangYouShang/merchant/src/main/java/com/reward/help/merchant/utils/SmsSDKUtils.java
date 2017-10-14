package com.reward.help.merchant.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.idotools.utils.JudgeNetWork;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.view.MyProcessDialog;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by wuxiaojun on 2017/5/2.
 */

public class SmsSDKUtils {

    public Context mContext;
    private EventHandler eh;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MyProcessDialog.closeDialog();
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                LogUtils.e("data：" + data);
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    LogUtils.e("提交验证码成功");
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功  开始计时
                    LogUtils.e("获取验证码成功");
                    if(mOnSMSSDKListener != null){
                        mOnSMSSDKListener.onSMSSDKSendSuccessListener();
                    }

                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                    LogUtils.e("返回支持发送验证码的国家列表");
                }
            } else {
                LogUtils.e("注册接口中：data" + data);
                try {
                    if(data instanceof String){
                        JSONObject jsonObject = new JSONObject((String) data);
                        String detail = jsonObject.getString("detail");
                        if(detail != null){
                            ToastUtils.show(mContext, detail);
                        }else{
                            ToastUtils.show(mContext, "发送验证码失败!");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.show(mContext, "发送验证码失败!");
                }
            }
        }
    };
    //datajava.lang.Throwable: {"status":477,"detail":"当前手机号发送短信的数量超过限额"}

    public SmsSDKUtils(Context context) {
        this.mContext = context;
        initSms();
    }

    public void initSms() {
        SMSSDK.initSDK(mContext, Constant.SMS_APPKEY, Constant.SMS_APPSECRET);
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    public void sendCode(String mobile) {
        if (ValidateUtil.isMobile(mobile)) {
            if (JudgeNetWork.isNetAvailable(mContext)) {
                MyProcessDialog.showDialog(mContext, "请稍等...", true, false);
                SMSSDK.getVerificationCode("86", mobile, new OnSendMessageHandler() {
                    @Override
                    public boolean onSendMessage(String s, String s1) {
                        LogUtils.e("OnSendMessageHandler中s=" + s + "==s1=" + s1);
                        return false;
                    }
                });
                // 验证 验证码是否正确
//                            SMSSDK.submitVerificationCode(); // s：86 s1:手机好 s2：验证码
            } else {
                ToastUtils.show(mContext, "请检查网络设置");
            }
        } else {
            ToastUtils.show(mContext, "手机号格式不正确");
        }
    }

    private OnSMSSDKListener mOnSMSSDKListener;

    public void setOnSMSSDKListener(OnSMSSDKListener onSMSSDKListener) {
        this.mOnSMSSDKListener = onSMSSDKListener;
    }

    public interface OnSMSSDKListener {
        void onSMSSDKSendSuccessListener();
    }

    public void destroySms() {
        SMSSDK.unregisterEventHandler(eh); //注册短信回调
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

}

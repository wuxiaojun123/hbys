package com.help.reward.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.RegisterResponse;
import com.help.reward.bean.Response.VerificationCodeResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.Constant;
import com.help.reward.utils.CountDownTimeUtils;
import com.help.reward.utils.ValidateUtil;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.JudgeNetWork;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 2017/1/8.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.et_phone_number)
    EditText et_phone_number; // 手机号
    @BindView(R.id.tv_code)
    TextView tv_code; // 计时器
    @BindView(R.id.et_code)
    EditText et_code; // 验证码

    @BindView(R.id.et_pwd)
    EditText et_pwd; // 密码
    @BindView(R.id.et_pwd_custom)
    EditText et_pwd_custom; // 确认密码
    @BindView(R.id.et_invitation_code)
    EditText et_invitation_code; // 邀请code
    @BindView(R.id.cb_agreement)
    CheckBox cb_agreement; // 复选框

    private CountDownTimeUtils mTimer;
//    public String verificationCode; // 请求到的code
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
                    tv_code.setClickable(false);
                    mTimer.start();

                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                    LogUtils.e("返回支持发送验证码的国家列表");
                }
            } else {
                ((Throwable) data).printStackTrace();
                LogUtils.e("注册接口中：data"+data);
                ToastUtils.show(mContext,"发送验证码失败!请稍后重试");
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        initTimer();
        sms();
    }


    private void sms() {
        SMSSDK.initSDK(this, Constant.SMS_APPKEY, Constant.SMS_APPSECRET);
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

    private void initTimer() {
        mTimer = new CountDownTimeUtils(CountDownTimeUtils.millisInFuture, CountDownTimeUtils.countDownInterval);
        mTimer.setOnCountDownTimeListener(new CountDownTimeUtils.OnCountDownTimeListener() {
            @Override
            public void onTick(long millisUntilFinished) { // 计时开始
                tv_code.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() { // 计时结束
                tv_code.setClickable(true);
                tv_code.setText(R.string.string_recapture);
            }
        });
    }

    private void initView() {
        tv_title.setText(R.string.string_register_title);
        tv_title_right.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_code, R.id.btn_register})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(RegisterActivity.this);

                break;
            case R.id.tv_code: // 点击验证code
                String registerUserName = et_phone_number.getText().toString().trim();
                if (!TextUtils.isEmpty(registerUserName)) {
                    if (ValidateUtil.isMobile(registerUserName)) {
                        if(JudgeNetWork.isNetAvailable(mContext)){
                            MyProcessDialog.showDialog(mContext,"请稍等...",true,false);
                            SMSSDK.getVerificationCode("86", registerUserName, new OnSendMessageHandler() {
                                @Override
                                public boolean onSendMessage(String s, String s1) {
                                    LogUtils.e("OnSendMessageHandler中s=" + s + "==s1=" + s1);
                                    return false;
                                }
                            });
                            // 验证 验证码是否正确
//                            SMSSDK.submitVerificationCode(); // s：86 s1:手机好 s2：验证码
                        }else{
                            ToastUtils.show(mContext,"请检查网络设置");
                        }
                    } else {
                        ToastUtils.show(mContext, "手机号格式不正确");
                    }
                } else {
                    ToastUtils.show(mContext, "请输入手机号");
                }

                break;
            case R.id.btn_register: // 注册
                register();

                break;
        }
    }

    /***
     * 点击注册
     */
    private void register() {
        String registerUserName = et_phone_number.getText().toString().trim();
        String registerPwd = et_pwd.getText().toString().trim();
        String registerPwdAgain = et_pwd_custom.getText().toString().trim();
        String registerCode = et_code.getText().toString().trim();
        String registerInvitationCode = et_invitation_code.getText().toString().trim(); // 邀请code

        if (!TextUtils.isEmpty(registerUserName)) {
            if (ValidateUtil.isMobile(registerUserName)) {
                if (!TextUtils.isEmpty(registerCode)) {
                    if (!TextUtils.isEmpty(registerPwd)) {
                        if (!TextUtils.isEmpty(registerPwdAgain)) {
                            if (registerPwd.equals(registerPwdAgain)) {
                                if (cb_agreement.isChecked()) {
                                    requestRegister(registerUserName, registerPwd, registerCode, registerInvitationCode);
                                } else {
                                    ToastUtils.show(mContext, "请选中用户说明");
                                }
                            } else {
                                ToastUtils.show(mContext, "密码不一致，请重新输入");
                            }
                        } else {
                            ToastUtils.show(mContext, "请再次输入密码");
                        }
                    } else {
                        ToastUtils.show(mContext, "请输入密码");
                    }
                } else {
                    ToastUtils.show(mContext, "请输入验证码");
                }
            } else {
                ToastUtils.show(mContext, "手机号格式不正确");
            }
        } else {
            ToastUtils.show(mContext, "请输入手机号");
        }
    }

    private void requestRegister(String phone, String registerPwd, String registerCode, String registerInvitationCode) {
        MyProcessDialog.showDialog(mContext);
        PersonalNetwork
                .getLoginApi()
                .getRegisterBean(phone, registerCode, registerPwd, Constant.PLATFORM_CLIENT)
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<RegisterResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        if (e instanceof UnknownHostException) {
                            ToastUtils.show(mContext, "请求到错误服务器");
                        } else if (e instanceof SocketTimeoutException) {
                            ToastUtils.show(mContext, "请求超时");
                        }
                    }

                    @Override
                    public void onNext(RegisterResponse res) {
                        MyProcessDialog.closeDialog();
                        if (res.code == 200) { // 获取验证code成功
                            App.APP_CLIENT_KEY = res.data.key;

                            finish();
                            ActivitySlideAnim.slideOutAnim(RegisterActivity.this);
                            LogUtils.e("注册成功。。。" + res.data.username);
                        } else {
                            ToastUtils.show(mContext, res.msg);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        SMSSDK.unregisterEventHandler(eh); //注册短信回调
        super.onDestroy();
    }
}

package com.help.reward.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.chat.DemoHelper;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.Constant;
import com.help.reward.utils.SharedPreferenceConstant;
import com.help.reward.view.AlertDialog;
import com.help.reward.view.MyProcessDialog;
import com.hyphenate.EMCallBack;
import com.idotools.utils.DataCleanManager;
import com.idotools.utils.LogUtils;
import com.idotools.utils.SharedPreferencesHelper;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 设置
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;
    @BindView(R.id.btn_switch)
    ToggleButton btn_switch; // 消息设置


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        SharedPreferencesHelper spHelp = SharedPreferencesHelper.getInstance(mContext);
        boolean msgFlag = spHelp.getBoolean(SharedPreferenceConstant.KEY_MESSAGE_SETTING, false);
        btn_switch.setChecked(msgFlag);
    }

    private void initView() {
        tv_title.setText(R.string.string_setting_title);
        tv_title_right.setVisibility(View.GONE);
    }


    @OnClick({R.id.iv_title_back, R.id.btn_switch, R.id.tv_clean_cache,
            R.id.tv_call, R.id.tv_help_center, R.id.tv_feedback,
            R.id.tv_about, R.id.tv_logout})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(SettingActivity.this);

                break;
            case R.id.btn_switch: // 消息设置
                SharedPreferencesHelper spHelp = SharedPreferencesHelper.getInstance(mContext);
                LogUtils.e("当前消息设置是否:" + btn_switch.isChecked());
                spHelp.putBoolean(SharedPreferenceConstant.KEY_MESSAGE_SETTING, btn_switch.isChecked());

                break;
            case R.id.tv_clean_cache: // 清除缓存
                cleanCache();


                break;
            case R.id.tv_call: // 客服中心
                call();

                break;

            case R.id.tv_help_center:
                startActivity(new Intent(SettingActivity.this, HelpCenterActivity.class));
                ActivitySlideAnim.slideInAnim(SettingActivity.this);

                break;
            case R.id.tv_feedback:
                startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));
                ActivitySlideAnim.slideInAnim(SettingActivity.this);

                break;
            case R.id.tv_about:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                ActivitySlideAnim.slideInAnim(SettingActivity.this);

                break;
            case R.id.tv_logout: // 登出
                if (App.APP_CLIENT_KEY != null) {
                    logoutDialog();
                }

                break;
        }
    }

    private void cleanCache() {
        new AlertDialog(SettingActivity.this)
                .builder()
                .setTitle(R.string.string_system_prompt)
                .setMsg("是否清除所有图片缓存")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataCleanManager.cleanInternalCache(mContext);
                        DataCleanManager.cleanCustomCache(Constant.ROOT);
                        DataCleanManager.cleanFiles(mContext);
                        DataCleanManager.cleanExternalCache(mContext);
                        // 清除商品搜索记录
                        SharedPreferencesHelper.getInstance(mContext).putString(SharedPreferenceConstant.KEY_SEARCH_SHOP_HISTORY, null);

                        ToastUtils.show(mContext, "清除成功");
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void call() {
        new AlertDialog(SettingActivity.this)
                .builder()
                .setTitle(R.string.string_system_prompt)
                .setMsg(Constant.TEL_PHONE)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.TEL_PHONE));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void logoutDialog() {
        new AlertDialog(SettingActivity.this)
                .builder()
                .setTitle(R.string.string_system_prompt)
                .setMsg("确认退出吗?")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void logout() {
        PersonalNetwork
                .getLoginApi()
                .getLogoutResponse("android", App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<String>>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BaseResponse<String> response) {

                        if (response.code == 200) {
                            if (response.data != null) { // 返回地址id  response.data.address_id
                                logoutHuanxin();
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void logoutHuanxin() {
        DemoHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        MyProcessDialog.closeDialog();
                        // show login screen
                        ToastUtils.show(mContext, "退出成功");
                        App.APP_CLIENT_KEY = null;
                        App.APP_CLIENT_COOKIE = null;
                        App.mLoginReponse = null;
                        App.APP_USER_ID = null;
                        // 应该清除个人信息页面的信息
                        RxBus.getDefault().post("logout");

                        // 清除当前activity
                        finish();
                        ActivitySlideAnim.slideOutAnim(SettingActivity.this);
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        ToastUtils.show(mContext, "unbind devicetokens failed");
                    }
                });
            }
        });

    }

}

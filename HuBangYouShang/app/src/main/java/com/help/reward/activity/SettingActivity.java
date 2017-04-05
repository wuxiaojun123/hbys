package com.help.reward.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.help.reward.R;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.Constant;
import com.help.reward.utils.SharedPreferenceConstant;
import com.help.reward.view.AlertDialog;
import com.idotools.utils.DataCleanManager;
import com.idotools.utils.LogUtils;
import com.idotools.utils.SharedPreferencesHelper;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
                logout();

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

                        ToastUtils.show(mContext,"清除成功");
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
                .setMsg("025-58840881")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:025-58840881"));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void logout(){
    }

}

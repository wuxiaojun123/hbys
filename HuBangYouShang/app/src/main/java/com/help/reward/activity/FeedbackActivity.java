package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.RegisterResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.ValidateUtil;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 意见反馈
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.et_mobile)
    EditText et_mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        tv_title.setText(R.string.string_feedback_title);
        tv_title_right.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(FeedbackActivity.this);

                break;
            case R.id.btn_commit:
                commit();

                break;
        }
    }

    private void commit() {
        String contentStr = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(contentStr)) {
            ToastUtils.show(mContext, "请输入您的宝贵意见");
            return;
        }
        String mobileStr = et_mobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobileStr)) {
            ToastUtils.show(mContext, "请输入您的联系方式");
            return;
        }
        if(!ValidateUtil.isMobile(mobileStr)){
            ToastUtils.show(mContext, "手机号格式不正确");
            return;
        }
        MyProcessDialog.showDialog(mContext,"正在提交");
        PersonalNetwork.getResponseApi().getFeedbackResponse(App.APP_CLIENT_KEY, contentStr, mobileStr)
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<BaseResponse>() {
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
                    public void onNext(BaseResponse res) {
                        MyProcessDialog.closeDialog();
                        if (res.code == 200) { // 获取验证code成功
                            ToastUtils.show(mContext,"反馈成功");
                            finish();
                            ActivitySlideAnim.slideOutAnim(FeedbackActivity.this);
                        } else {
                            ToastUtils.show(mContext, res.msg);
                        }
                    }
                });
    }

}

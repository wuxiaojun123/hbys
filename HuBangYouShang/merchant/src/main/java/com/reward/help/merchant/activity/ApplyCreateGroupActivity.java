package com.reward.help.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.coupon.CouponPointsConstant;
import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.App;
import com.reward.help.merchant.R;
import com.reward.help.merchant.bean.Response.BaseResponse;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.network.CouponPointsNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApplyCreateGroupActivity extends BaseActivity {
//public class ApplyCreateGroupActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_appley_content)
    EditText mEtContent;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvTitle.setText(getText(R.string.apply_new_group));

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                ApplyCreateGroupActivity.this.finish();
            }
        });
    }

    @OnClick({R.id.iv_title_back,R.id.btn_creat_group_commit})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_back:
                hideSoftKeyboard();
                finish();
                break;
            case R.id.btn_creat_group_commit:
                if (TextUtils.isEmpty(mEtContent.getText().toString().trim())){
                    ToastUtils.show(mContext,"请输入申请内容");
                    return;
                }
                commit(mEtContent.getText().toString().trim());
                break;
        }
    }

    private void commit(String content) {
        MyProcessDialog.showDialog(ApplyCreateGroupActivity.this);
        subscribe = CouponPointsNetwork.getCouponListApi().apply(App.getAppClientKey(),content)
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
                    public void onNext(BaseResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            ToastUtils.show(mContext,"提交成功，请耐心等待审核！");
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }
}

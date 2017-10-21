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
import com.reward.help.merchant.bean.CreateGroupInfoResponse;
import com.reward.help.merchant.bean.Response.BaseResponse;
import com.reward.help.merchant.bean.Response.GroupProgressResponse;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.network.CouponPointsNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.utils.ActivitySlideAnim;
import com.reward.help.merchant.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 申请建群
 */
public class ApplyCreateGroupActivity extends BaseActivity {
    //public class ApplyCreateGroupActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_appley_content)
    EditText mEtContent;
    @BindView(R.id.tv_failed)
    TextView tv_failed;

    private boolean examineFailed; // 审核失败为true
    private String id; // 建群的id

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
        initView();

        examineFailed = getIntent().getBooleanExtra("examine_failed", false);
        if (examineFailed) {
            id = getIntent().getStringExtra("id");
            tv_failed.setVisibility(View.VISIBLE);
            mTvTitle.setText("申请建群");
            // 请求数据
            initData();
        }
    }

    private void initData() {
        CouponPointsNetwork.getCouponListApi().getApplyProgressInfo(id, App.getAppClientKey())
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<CreateGroupInfoResponse>() {
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
                    public void onNext(CreateGroupInfoResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                CreateGroupInfoResponse.CreateGroupInfoBean bean = response.data.info;
                                bindView(bean);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /***
     * 绑定view
     * @param bean
     */
    private void bindView(CreateGroupInfoResponse.CreateGroupInfoBean bean) {
        mEtContent.setText(bean.content);
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

    @OnClick({R.id.iv_title_back, R.id.btn_creat_group_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                hideSoftKeyboard();
                finish();
                break;
            case R.id.btn_creat_group_commit:
                String content = mEtContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.show(mContext, "请输入申请内容");
                    return;
                }
                if (examineFailed) {
                    modifyCommit(content);
                } else {
                    commit(content);
                }
                break;
        }
    }

    /***
     * 修改提交
     * @param content
     */
    private void modifyCommit(String content) {
        MyProcessDialog.showDialog(ApplyCreateGroupActivity.this);
        CouponPointsNetwork
                .getCouponListApi()
                .commitModifyApplyProgress(id, content, App.getAppClientKey())
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
                            ToastUtils.show(mContext, "提交成功，请耐心等待审核！");
                            setResult(1);
                            finish();
                            ActivitySlideAnim.slideOutAnim(ApplyCreateGroupActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void commit(String content) {
        MyProcessDialog.showDialog(ApplyCreateGroupActivity.this);
        subscribe = CouponPointsNetwork.getCouponListApi().apply(App.getAppClientKey(), content)
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
                            ToastUtils.show(mContext, "提交成功，请耐心等待审核！");
                            finish();
                            ActivitySlideAnim.slideOutAnim(ApplyCreateGroupActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }
}

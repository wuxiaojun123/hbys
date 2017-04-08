package com.reward.help.merchant.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.App;
import com.reward.help.merchant.R;
import com.reward.help.merchant.bean.Response.BaseResponse;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.network.PersonalNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 意见反馈
 *
 * Created by wuxiaojun on 2017/1/8.
 */

public class FeedbackActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_feekback_content)
    EditText mContent;

    @BindView(R.id.et_fackback_phone)
    EditText mPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        tv_title.setText("意见反馈");
    }

    @OnClick({R.id.iv_title_back,R.id.btn_feekback_commit})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_title_back:
                finish();
                //ActivitySlideAnim.slideOutAnim(FeedbackActivity.this);

                break;
            case R.id.btn_feekback_commit:

                String content = mContent.getText().toString();
                String phone  = mPhone.getText().toString();

                if (TextUtils.isEmpty(content)) {
                    ToastUtils.show(FeedbackActivity.this,"请输入内容");
                    return;
                }

                if (TextUtils.isEmpty(content)) {
                    ToastUtils.show(FeedbackActivity.this,"请输入联系方式");
                    return;
                }

                feekbackRequest(content,phone);
                break;
        }
    }

    private void feekbackRequest(String content, String phone) {
        MyProcessDialog.showDialog(FeedbackActivity.this);
        subscribe = PersonalNetwork.getResponseApi().getFeedbackResponse(App.getAppClientKey(),content,phone)
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
                            ToastUtils.show(mContext, "提交成功");
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }


}

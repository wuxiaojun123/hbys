package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.StringUtils;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 发布评论
 * Created by MXY on 2017/2/19.
 */

public class HelpRewardCommentActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;


    @BindView(R.id.et_content)
    EditText et_content;


    String post_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helprewardcomment);
        ButterKnife.bind(this);
        post_id = getIntent().getExtras().getString("post_id");

        initView();
    }

    private void initView() {
        tvTitle.setText("评论");
        tvTitleRight.setText("提交");
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_right})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                subComplained();
                break;
        }
    }


    private void subComplained() {
        String content = et_content.getText().toString().trim();
        if (!StringUtils.checkStr(content) || content.length() < 10) {
            ToastUtils.show(mContext, "请输入评论内容,不少于10字");
            return;
        }
        subComplainedData(content);
    }

    protected Subscription subscribe;

    private void subComplainedData(String content) {
        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
                .getSubRewardCommentBean(App.APP_CLIENT_KEY, "comment", post_id, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            ToastUtils.show(mContext, "评论成功");
                            setResult(RESULT_OK);
                            finish();

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }


    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }

}

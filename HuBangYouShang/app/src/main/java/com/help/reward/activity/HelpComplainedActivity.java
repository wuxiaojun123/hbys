package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ChooseCameraPopuUtils;
import com.help.reward.utils.DealChoosePicUtils;
import com.help.reward.utils.DialogUtil;
import com.help.reward.utils.StringUtils;
import com.help.reward.utils.UploadImageUtils;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 发布投诉
 * Created by MXY on 2017/2/19.
 */

public class HelpComplainedActivity extends BaseActivity implements DealChoosePicUtils.DealChoosePicListener, UploadImageUtils.OnUploadImageListener {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;

    @BindView(R.id.tv_post_title)
    TextView tv_post_title;
    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.et_content)
    EditText et_content;


    @BindView(R.id.iv_release_addphoto)
    ImageView ivReleaseAddphoto;
    DealChoosePicUtils dealChoosePicUtils;
    UploadImageUtils uploadImagUtils;

    String post_id;
    String type;
    String comment_id;
    String post_title;
    String u_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_complained);
        ButterKnife.bind(this);
        post_id = getIntent().getExtras().getString("post_id");
        type = getIntent().getExtras().getString("type");
        comment_id = getIntent().getExtras().getString("comment_id");
        post_title = getIntent().getExtras().getString("post_title");
        u_name = getIntent().getExtras().getString("u_name");

        initView();
    }

    private void initView() {
        tvTitle.setText("投诉");
        tvTitleRight.setText("提交");
        tv_post_title.setText(post_title);
        tv_name.setText("投诉："+u_name);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_right, R.id.iv_release_addphoto})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                subComplained();
                break;
            case R.id.iv_release_addphoto:
                ChooseCameraPopuUtils.showPopupWindow(this, v);
                if (dealChoosePicUtils == null) {
                    dealChoosePicUtils = new DealChoosePicUtils(this);
                    dealChoosePicUtils.setDealChoosePicListener(this);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (dealChoosePicUtils != null)
            dealChoosePicUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finishDeal(String path, int type) {
        // TODO Auto-generated method stub
        ToastUtils.show(this, path);
        if (uploadImagUtils == null) {
            uploadImagUtils = new UploadImageUtils(mContext);
            uploadImagUtils.setOnUploadImageListener(this);
        }
//        uploadImagUtils.upImage(path,"seek_help");
    }

    private void subComplained() {
        String content = et_content.getText().toString().trim();
        if (!StringUtils.checkStr(content)||content.length()<100) {
            ToastUtils.show(mContext, "请输入投诉内容,不少于100字");
            return;
        }
        subComplainedData(content);
    }

    protected Subscription subscribe;

    private void subComplainedData(String  content) {
        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
                .getComplainSHPostBean(App.APP_CLIENT_KEY, "complain_shpost", post_id, comment_id,type, content)
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
                            DialogUtil.showConfirmCancleDialog(HelpComplainedActivity.this, "系统提示", "您的投诉内容已提交\n待被投诉人申诉", "", "确定", new DialogUtil.OnDialogUtilClickListener() {
                                @Override
                                public void onClick(boolean isLeft) {
                                  finish();
                                }
                            });

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

    @Override
    public void onLoadError() {
        //图片上传失败
    }

    @Override
    public void onLoadSucced(String default_dir, String file_name) {
        //图片上传成功
        Log.e("onLoadSucced", default_dir + "===" + file_name);
    }
}

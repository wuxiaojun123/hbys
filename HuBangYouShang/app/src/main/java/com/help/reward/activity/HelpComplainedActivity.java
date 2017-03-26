package com.help.reward.activity;

import android.content.Intent;
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
import com.help.reward.utils.ChooseCameraPopuUtils;
import com.help.reward.utils.DialogUtil;
import com.help.reward.utils.GlideUtils;
import com.help.reward.utils.StringUtils;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

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

public class HelpComplainedActivity extends BaseActivity {

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

    String post_id;
    String type;
    String comment_id;
    String post_title;
    String u_name;
    ChooseCameraPopuUtils chooseCameraPopuUtils;


    @BindView(R.id.iv_photo1)
    ImageView iv_photo1;
    @BindView(R.id.iv_delete1)
    ImageView iv_delete1;
    @BindView(R.id.iv_photo2)
    ImageView iv_photo2;
    @BindView(R.id.iv_delete2)
    ImageView iv_delete2;
    @BindView(R.id.iv_photo3)
    ImageView iv_photo3;
    @BindView(R.id.iv_delete3)
    ImageView iv_delete3;
    @BindView(R.id.iv_photo4)
    ImageView iv_photo4;
    @BindView(R.id.iv_delete4)
    ImageView iv_delete4;
    List<String> photoUrl = new ArrayList<>();

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
        tv_name.setText("投诉：" + u_name);
        chooseCameraPopuUtils = new ChooseCameraPopuUtils(this, "p_complaint");
        chooseCameraPopuUtils.setOnUploadImageListener(new ChooseCameraPopuUtils.OnUploadImageListener() {
            @Override
            public void onLoadError() {

            }

            @Override
            public void onLoadSucced(String url) {
                photoUrl.add(url);
                showPhoto();
            }
        });
    }

    void showPhoto() {
        iv_photo1.setVisibility(View.GONE);
        iv_delete1.setVisibility(View.GONE);
        iv_photo2.setVisibility(View.GONE);
        iv_delete2.setVisibility(View.GONE);
        iv_photo3.setVisibility(View.GONE);
        iv_delete3.setVisibility(View.GONE);
        iv_photo4.setVisibility(View.GONE);
        iv_delete4.setVisibility(View.GONE);
        ivReleaseAddphoto.setVisibility(View.VISIBLE);
        switch (photoUrl.size()) {
            case 4:
                ivReleaseAddphoto.setVisibility(View.GONE);
                iv_photo4.setVisibility(View.VISIBLE);
                iv_delete4.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(photoUrl.get(3),iv_photo4);
            case 3:
                iv_photo3.setVisibility(View.VISIBLE);
                iv_delete3.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(photoUrl.get(2),iv_photo3);
            case 2:
                iv_photo2.setVisibility(View.VISIBLE);
                iv_delete2.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(photoUrl.get(1),iv_photo2);
            case 1:
                iv_photo1.setVisibility(View.VISIBLE);
                iv_delete1.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(photoUrl.get(0),iv_photo1);
                break;
        }
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_right, R.id.iv_release_addphoto,R.id.iv_delete1,R.id.iv_delete2,R.id.iv_delete3,R.id.iv_delete4})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                subComplained();
                break;
            case R.id.iv_release_addphoto:
                chooseCameraPopuUtils.showPopupWindow();
                break;
            case R.id.iv_delete1:
                photoUrl.remove(0);
                showPhoto();
                break;
            case R.id.iv_delete2:
                photoUrl.remove(1);
                showPhoto();
                break;
            case R.id.iv_delete3:
                photoUrl.remove(2);
                showPhoto();
                break;
            case R.id.iv_delete4:
                photoUrl.remove(3);
                showPhoto();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (chooseCameraPopuUtils != null)
            chooseCameraPopuUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subComplained() {
        String content = et_content.getText().toString().trim();
        if (!StringUtils.checkStr(content) || content.length() < 100) {
            ToastUtils.show(mContext, "请输入投诉内容,不少于100字");
            return;
        }
        subComplainedData(content);
    }

    protected Subscription subscribe;

    private void subComplainedData(String content) {
        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
                .getComplainSHPostBean(App.APP_CLIENT_KEY, "complain_shpost", post_id, comment_id, type, content,(String[]) photoUrl.toArray(new String[photoUrl.size()]))
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

}

package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.HelpComplainedDetailResponse;
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
 * 发布申诉
 * Created by MXY on 2017/2/19.
 */

public class HelpComplainedDetailActivity extends BaseActivity {

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
    @BindView(R.id.root_layout)
    LinearLayout root_layout;

    @BindView(R.id.tv_content_title)
    TextView tv_content_title;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.appel_layout)
    LinearLayout appel_layout;
    @BindView(R.id.tv_apple_title)
    TextView tv_apple_title;
    @BindView(R.id.tv_appel)
    TextView tv_appel;
    @BindView(R.id.complainant_explain_layout)
    LinearLayout complainant_explain_layout;
    @BindView(R.id.tv_complainant_explain_title)
    TextView tv_complainant_explain_title;
    @BindView(R.id.tv_complainant_explain)
    TextView tv_complainant_explain;
    @BindView(R.id.respondent_explain_layout)
    LinearLayout respondent_explain_layout;
    @BindView(R.id.tv_respondent_explain_title)
    TextView tv_respondent_explain_title;
    @BindView(R.id.tv_respondent_explain)
    TextView tv_respondent_explain;


    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.ed_layout)
    LinearLayout ed_layout;
    @BindView(R.id.et_content_title)
    TextView et_content_title;

    @BindView(R.id.iv_release_addphoto)
    ImageView ivReleaseAddphoto;
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
    String complaint_id;
    ChooseCameraPopuUtils chooseCameraPopuUtils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_complaineddetail);
        ButterKnife.bind(this);
        complaint_id = getIntent().getExtras().getString("complaint_id");
        initView();
        MyProcessDialog.showDialog(mContext);
        requestData();
    }

    private void initView() {
        tvTitle.setText("申诉");
        tvTitleRight.setText("提交");
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
        if ("您可以提交申诉如下：".equals(et_content_title.getText().toString())) {
            if (!StringUtils.checkStr(content) || content.length() < 100) {
                ToastUtils.show(mContext, "请输入申诉内容,不少于100字");
                return;
            }
            subComplainedData(content, "appeal");
        } else {
            subComplainedData(content, "explain");
        }
    }

    protected Subscription subscribe;

    private void subComplainedData(String content, final String op) {
        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
                .getSubComplainedBean(App.APP_CLIENT_KEY, op, complaint_id, content,(String[]) photoUrl.toArray(new String[photoUrl.size()]))
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
                            et_content.setText("");
                            if ("appeal".equalsIgnoreCase(op)) {
                                DialogUtil.showConfirmCancleDialog(HelpComplainedDetailActivity.this, "系统提示", "您的申诉内容已提交，一天后将在投票版块公示进行投票。此外您还可以在一天内再提交一次申诉补充说明。", "等待公示", "提交补充说明", new DialogUtil.OnDialogUtilClickListener() {
                                    @Override
                                    public void onClick(boolean isLeft) {

                                        if (isLeft) {
                                            MyProcessDialog.showDialog(mContext);
                                            requestData();
                                        } else {
                                            finish();
                                        }
                                    }
                                });
                            } else {
                                ToastUtils.show(mContext, "提交成功");
                                MyProcessDialog.showDialog(mContext);
                                requestData();
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }


    private void requestData() {
        subscribe = HelpNetwork
                .getHelpApi()
                .getComplainedDetailBean(App.APP_CLIENT_KEY, "info", complaint_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpComplainedDetailResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                    }

                    @Override
                    public void onNext(HelpComplainedDetailResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            root_layout.setVisibility(View.VISIBLE);
                            bindData(response);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void bindData(HelpComplainedDetailResponse response) {
        if (App.APP_USER_ID.equals(response.data.complainant_id)) {
            tv_content_title.setText("您的投诉如下：");
            tv_apple_title.setText("对方的申诉内容如下：");
            tv_complainant_explain_title.setText("您的解释如下：");
            tv_respondent_explain_title.setText("对方的解释如下");

            if (!StringUtils.checkStr(response.data.appeal) || StringUtils.checkStr(response.data.complainant_explain)) {
                //如果对面没有申诉 那就不让填写信息
                ed_layout.setVisibility(View.GONE);
                tvTitleRight.setVisibility(View.GONE);
            } else {
                ed_layout.setVisibility(View.VISIBLE);
                tvTitleRight.setVisibility(View.VISIBLE);
            }
            et_content_title.setText("您可以提交最后一次说明：");
            et_content.setHint("请输入您的说明");


        } else {
            tv_content_title.setText("您被投诉如下：");
            tv_apple_title.setText("您的申诉内容如下：");
            tv_complainant_explain_title.setText("对方的解释如下：");
            tv_respondent_explain_title.setText("您的解释如下");

            if (!StringUtils.checkStr(response.data.appeal)) {
                //如果您没有申诉 那就不让填写信息
                et_content_title.setText("您可以提交申诉如下：");
                et_content.setHint("请输入您的申诉内容不少于100字");
                ed_layout.setVisibility(View.VISIBLE);
                tvTitleRight.setVisibility(View.VISIBLE);
            } else {
                if (!StringUtils.checkStr(response.data.respondent_explain)) {
                    //如果您没有申诉 那就不让填写信息
                    et_content_title.setText("您可以提交最后一次说明：");
                    et_content.setHint("请输入您的说明");
                    ed_layout.setVisibility(View.VISIBLE);
                    tvTitleRight.setVisibility(View.VISIBLE);
                } else {
                    tvTitleRight.setVisibility(View.GONE);
                    ed_layout.setVisibility(View.GONE);
                }
            }

        }

        tv_post_title.setText(response.data.post_title);
        tv_name.setText(response.data.complainant_name + "投诉");

        tv_content.setText(response.data.content);
        if (StringUtils.checkStr(response.data.appeal)) {
            appel_layout.setVisibility(View.VISIBLE);
            tv_appel.setText(response.data.appeal);
        } else {
            appel_layout.setVisibility(View.GONE);
        }

        if (StringUtils.checkStr(response.data.complainant_explain)) {
            complainant_explain_layout.setVisibility(View.VISIBLE);
            tv_complainant_explain.setText(response.data.complainant_explain);
        } else {
            complainant_explain_layout.setVisibility(View.GONE);
        }
        if (StringUtils.checkStr(response.data.respondent_explain)) {
            respondent_explain_layout.setVisibility(View.VISIBLE);
            tv_respondent_explain.setText(response.data.respondent_explain);
        } else {
            respondent_explain_layout.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }

}

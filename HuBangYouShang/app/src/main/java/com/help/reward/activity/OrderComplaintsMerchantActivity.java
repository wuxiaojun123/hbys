package com.help.reward.activity;

import android.content.Intent;
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
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.ChooseCameraPopuUtils;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.AlertDialog;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 投诉商家
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class OrderComplaintsMerchantActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.tv_store_name)
    TextView tv_store_name; // 商家名称
    @BindView(R.id.et_content)
    EditText et_content; // 输入内容

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

    @BindView(R.id.tv_photonum)
    TextView tv_photonum;

    List<String> photoUrl = new ArrayList<>();
    List<String> file_names = new ArrayList<>();
    ChooseCameraPopuUtils chooseCameraPopuUtils;
    private String order_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complaints_merchant);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        chooseCameraPopuUtils = new ChooseCameraPopuUtils(this, "p_complaint");
        chooseCameraPopuUtils.setOnUploadImageListener(new ChooseCameraPopuUtils.OnUploadImageListener() {
            @Override
            public void onLoadError() {
                ToastUtils.show(mContext, "获取图片错误");
            }

            @Override
            public void onLoadSucced(String file_name, String url) {

                photoUrl.add(url);
                file_names.add(file_name);
                showPhoto();
            }
        });
    }

    private void initView() {
        tv_title.setText(R.string.string_order_complaints_merchant_title);
        tv_title_right.setVisibility(View.GONE);
        String seller_name = getIntent().getStringExtra("seller_name");
        tv_store_name.setText(seller_name);
        order_id = getIntent().getStringExtra("order_id");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (chooseCameraPopuUtils != null)
            chooseCameraPopuUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.iv_title_back, R.id.iv_release_addphoto, R.id.btn_commit,
            R.id.iv_delete1, R.id.iv_delete2, R.id.iv_delete3, R.id.iv_delete4})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(OrderComplaintsMerchantActivity.this);

                break;
            case R.id.iv_release_addphoto:
                chooseCameraPopuUtils.showPopupWindow();

                break;
            case R.id.iv_delete1:
                photoUrl.remove(0);
                file_names.remove(0);
                showPhoto();
                break;
            case R.id.iv_delete2:
                photoUrl.remove(1);
                file_names.remove(1);
                showPhoto();
                break;
            case R.id.iv_delete3:
                photoUrl.remove(2);
                file_names.remove(2);
                showPhoto();
                break;
            case R.id.iv_delete4:
                photoUrl.remove(3);
                file_names.remove(3);
                showPhoto();
                break;
            case R.id.btn_commit:
                commit();

                break;
        }
    }

    private void commit() {
        String contentStr = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(contentStr)) {
            ToastUtils.show(mContext, "请输入投诉内容");
            return;
        }
        if (contentStr.length() < 50) {
            ToastUtils.show(mContext, "您输入的内容太少了");
            return;
        }
        String pic1 = null;
        String pic2 = null;
        String pic3 = null;
        String pic4 = null;
        if (!file_names.isEmpty()) {
            for (int i = 0; i < file_names.size(); i++) {
                if (i == 0) {
                    pic1 = file_names.get(0);
                } else if (i == 1) {
                    pic2 = file_names.get(1);
                } else if (i == 2) {
                    pic3 = file_names.get(2);
                } else if (i == 3) {
                    pic4 = file_names.get(3);
                }
            }
        }
        MyProcessDialog.showDialog(mContext, "正在提交...");
        PersonalNetwork
                .getResponseApi()
                .getComplaintResponse(order_id, contentStr, pic1, pic2, pic3, pic4, App.APP_CLIENT_KEY)
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
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) { // 显示数据
                                showMyDialog(response.msg);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /***
     * 评价成功
     *
     * @param msg
     */
    private void showMyDialog(String msg) {
        new AlertDialog(OrderComplaintsMerchantActivity.this).builder()
                .setTitle(R.string.exit_title).setMsg(msg)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        ActivitySlideAnim.slideOutAnim(OrderComplaintsMerchantActivity.this);
                    }
                }).show();
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
                GlideUtils.loadImage(photoUrl.get(3), iv_photo4);
            case 3:
                iv_photo3.setVisibility(View.VISIBLE);
                iv_delete3.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(photoUrl.get(2), iv_photo3);
            case 2:
                iv_photo2.setVisibility(View.VISIBLE);
                iv_delete2.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(photoUrl.get(1), iv_photo2);
            case 1:
                iv_photo1.setVisibility(View.VISIBLE);
                iv_delete1.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(photoUrl.get(0), iv_photo1);
                break;
        }
        tv_photonum.setText("还可上传（" + (4 - photoUrl.size()) + "）张");
    }

}

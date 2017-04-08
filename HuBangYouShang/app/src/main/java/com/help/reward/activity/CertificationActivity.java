package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.CertificationResponse;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.help.reward.utils.ValidateUtil;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 实名认证
 * Created by wuxiaojun on 2017/1/9.
 */

public class CertificationActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.tv_certification_failed)
    TextView tv_certification_failed; // 审核失败
    @BindView(R.id.tv_certification_success)
    TextView tv_certification_success; // 审核成功
    @BindView(R.id.tv_certification_audit)
    TextView tv_certification_audit; // 审核中

    @BindView(R.id.et_name)
    EditText et_name; // 真实姓名
    @BindView(R.id.et_card)
    EditText et_card; // 身份证号
    @BindView(R.id.iv_photo)
    ImageView iv_photo; // 用户身份证照片
    @BindView(R.id.iv_upload)
    ImageView iv_upload; // 点击选择相片
    @BindView(R.id.tv_upload)
    TextView tv_upload; // 点击上传
    @BindView(R.id.btn_commit)
    Button btn_commit; // 提交
    @BindView(R.id.ll_info)
    LinearLayout ll_info; // 信息
    private String identity_img; // 图片


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);
        ButterKnife.bind(this);
        initView();
        initNet();
    }

    private void initNet() {
        PersonalNetwork
                .getResponseApi()
                .getCertificationStateResponse(App.APP_CLIENT_KEY) // 真实姓名，身份证号，图片路径
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<CertificationResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(CertificationResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                judgeState(response.data);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /***
     * 根据状态来显示不同的view
     * // 0未认证 1通过 2未通过 3-审核中'
     *
     * @param bean
     */
    private void judgeState(CertificationResponse.CertificationBean bean) {
        if (bean.certification != null) {
            if ("0".equals(bean.certification)) { // 未认证，正常显示

            } else if ("1".equals(bean.certification)) {
                tv_certification_success.setVisibility(View.VISIBLE);
                btn_commit.setVisibility(View.GONE);
                et_name.setText(bean.member_truename);
                et_name.setClickable(false);
                et_card.setText(bean.ID_card);
                et_card.setClickable(false);
                iv_photo.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(bean.identity_img, iv_photo);
                iv_upload.setVisibility(View.GONE);

            } else if ("2".equals(bean.certification)) { // 审核未通过
                tv_certification_failed.setVisibility(View.VISIBLE);

            } else if ("3".equals(bean.certification)) {
                ll_info.setVisibility(View.GONE);
                tv_certification_audit.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initView() {
        tv_title.setText(R.string.string_certification_title);
        tv_title_right.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back, R.id.iv_upload, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(CertificationActivity.this);

                break;
            case R.id.iv_upload: // 点击上传


                break;
            case R.id.btn_commit: // 点击提交
                commit();

                break;
        }
    }

    private void commit() {
        String trueName = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(trueName)) {
            ToastUtils.show(mContext, "请填写您的真实姓名");
            return;
        }
        String cardStr = et_card.getText().toString().trim();
        if (TextUtils.isEmpty(cardStr)) {
            ToastUtils.show(mContext, "请输入您的身份证号");
            return;
        }
        if (!ValidateUtil.isShenFenZheng(cardStr)) {
            ToastUtils.show(mContext, "请检查您的身份证号");
            return;
        }
        if (TextUtils.isEmpty(identity_img)) {
            ToastUtils.show(mContext, "请上传您的手持身份证照片");
            return;
        }
        PersonalNetwork
                .getResponseApi()
                .getCertificationPostResponse(trueName, cardStr, identity_img, App.APP_CLIENT_KEY) // 真实姓名，身份证号，图片路径
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<String>>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BaseResponse<String> response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                // 设置用户属性
                                LogUtils.e("修改个人信息成功。。。" + response.data);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }


}

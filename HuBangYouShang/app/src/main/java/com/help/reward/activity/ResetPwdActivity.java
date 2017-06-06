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
import com.help.reward.utils.ValidateUtil;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 重置密码
 * <p>
 * Created by wuxiaojun on 2017/1/15.
 */

public class ResetPwdActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.et_comfirm_pwd)
    EditText et_comfirm_pwd;

    private String phone;
    private int flag; // 标记   1:找回密码--重置密码   2:登录密码--重置密码  4:支付密码

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        ButterKnife.bind(this);
        phone = getIntent().getStringExtra("phone");
        flag = getIntent().getIntExtra("flag", 0);
        initView();
    }

    private void initView() {
        tv_title.setText(R.string.string_reset_pwd_title);
        tv_title_right.setVisibility(View.GONE);
    }


    @OnClick({R.id.iv_title_back, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(ResetPwdActivity.this);

                break;
            case R.id.btn_commit: // 提交
                String pwd = et_pwd.getText().toString().trim();
                String pwd1 = et_comfirm_pwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    if (!TextUtils.isEmpty(pwd1)) {
                        if (pwd.equals(pwd1)) {
                            judge(pwd, pwd1);
                        } else {
                            ToastUtils.show(mContext, "请检查您的确认密码");
                        }
                    } else {
                        ToastUtils.show(mContext, "请输入确认密码");
                    }
                } else {
                    ToastUtils.show(mContext, "请输入密码");
                }


                break;
        }
    }

    private void judge(String pwd, String pwd1) {
        if (flag == 1) { // 找回密码
            commit(pwd, pwd1);
        } else if (flag == 2) { // 登录密码
            commit2(pwd, pwd1);
        }else if(flag == 3){

        }else if(flag == 4){
            commit4(pwd,pwd1);
        }
    }

    private void commit(String pwd, String pwd1) {
        PersonalNetwork.getLoginApi()
                .getResetPasswordBean(pwd, pwd1, phone)
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
                                setResult(1);
                                finish();
                                ActivitySlideAnim.slideInAnim(ResetPwdActivity.this);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void commit2(String pwd, String pwd1) {
        PersonalNetwork.getResponseApi()
                .getModifyPwdStep5(App.APP_CLIENT_KEY, pwd, pwd1)
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
                                setResult(1);
                                finish();
                                ActivitySlideAnim.slideInAnim(ResetPwdActivity.this);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void commit3(String pwd, String pwd1) {
        PersonalNetwork.getResponseApi()
                .getModifyPwdStep5(App.APP_CLIENT_KEY, pwd, pwd1)
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
                                setResult(1);
                                finish();
                                ActivitySlideAnim.slideInAnim(ResetPwdActivity.this);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void commit4(String pwd, String pwd1) {
        PersonalNetwork.getResponseApi()
                .getModifyPayStep5(App.APP_CLIENT_KEY, pwd, pwd1)
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
                                setResult(1);
                                finish();
                                ActivitySlideAnim.slideInAnim(ResetPwdActivity.this);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}

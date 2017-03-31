package com.help.reward.activity;

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
import com.help.reward.bean.Response.GeneralExchangeVolumeResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 帮赏分兑换通用卷
 * <p>
 * Created by wuxiaojun on 2017/2/13.
 */

public class GeneralExchangeVolumeActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.et_exchange_value)
    EditText et_exchange_value; // 兑换数值
    @BindView(R.id.tv_discount_percentage)
    TextView tv_discount_percentage; // 优惠百分比
    @BindView(R.id.tv_user_balance)
    TextView tv_user_balance; // 可使用帮赏分

    private Subscription subscribe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_exchange_volume);
        ButterKnife.bind(this);
        initView();
        initNetwork();
    }

    private void initView() {
        tv_title.setText(R.string.string_bangshangfen_duihuan_tongyongjuan_title);
        tv_title_right.setVisibility(View.GONE);
    }

    /***
     * ?act=member_points&op=member_points_detail
     */
    private void initNetwork() {
        PersonalNetwork
                .getResponseApi()
                .getGeneralExchangeVolumeResponse(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<GeneralExchangeVolumeResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(GeneralExchangeVolumeResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                tv_discount_percentage.setText(response.data.discount_level);
                                tv_user_balance.setText(response.data.member_points);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    @OnClick({R.id.iv_title_back, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                back();

                break;
            case R.id.btn_commit:
                String numStr = et_exchange_value.getText().toString().trim();
                if (!TextUtils.isEmpty(numStr)) {
                    String bangshangFenStr = tv_user_balance.getText().toString().trim();
                    int bangshangFen = Integer.parseInt(bangshangFenStr);
                    int num = Integer.parseInt(numStr);
                    if (num <= bangshangFen) {
                        commit(num);
                    } else {
                        ToastUtils.show(mContext, "不能大于您的帮赏分");
                    }
                } else {
                    ToastUtils.show(mContext, "请输入帮赏分");
                }

                break;
        }
    }

    private void back() {
        finish();
        ActivitySlideAnim.slideOutAnim(GeneralExchangeVolumeActivity.this);
    }


    private void commit(int num) {
        subscribe = PersonalNetwork
                .getResponseApi()
                .getCommitGeneralExchangeVolumeResponse(App.APP_CLIENT_KEY, num + "")
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
                                ToastUtils.show(mContext, response.data.toString());
                                // 回到原来页面，并且更改帮赏分和余额的数目
                                back();
                            }
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

package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.BalanceExchangeResponse;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.UpdateLoginDataRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 余额兑换帮赏分
 * <p>
 * Created by wuxiaojun on 2017/2/13.
 */

public class BalanceExchangeHelpScoreActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.et_recharge_account)
    EditText et_recharge_account; // 充值金额
    @BindView(R.id.tv_balance)
    TextView tv_balance; // 可使用余额
    @BindView(R.id.btn_go_to_recharge)
    Button btn_go_to_recharge;

    private Subscription subscribe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_exchange_help_score);
        ButterKnife.bind(this);
        initView();
        initNetwork();
    }

    private void initView() {
        tv_title.setText(R.string.string_yue_duihuan_bangshangfen_title);
        tv_title_right.setVisibility(View.GONE);
        btn_go_to_recharge.setText("确认兑换");
    }

    /***
     * ?act=member_points&op=member_points_detail
     */
    private void initNetwork() {
        PersonalNetwork
                .getResponseApi()
                .getBalanceExchangeResponse(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BalanceExchangeResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BalanceExchangeResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                tv_balance.setText(response.data.available_total_num+"");
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    @OnClick({R.id.iv_title_back, R.id.btn_go_to_recharge})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                back();

                break;
            case R.id.btn_go_to_recharge:// 去充值
                // 判断充值余额最低1元
                String numStr = et_recharge_account.getText().toString().trim();
                if(!TextUtils.isEmpty(numStr)){
                    int num = Integer.parseInt(numStr);
                    if(num >= 1){
                        commit(num);
                    }else{
                        ToastUtils.show(mContext,"最少充值1元");
                    }
                }else {
                    ToastUtils.show(mContext,"请输入金额");
                }

                break;
        }
    }

    private void back() {
        finish();
        ActivitySlideAnim.slideOutAnim(BalanceExchangeHelpScoreActivity.this);
    }

    private void commit(int num) {
        subscribe = PersonalNetwork
                .getResponseApi()
                .getBalanceExchangeStringResponse(App.APP_CLIENT_KEY, num + "")
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
                                RxBus.getDefault().post(new UpdateLoginDataRxbusType(true));
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
        if(subscribe != null && !subscribe.isUnsubscribed()){
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }
}

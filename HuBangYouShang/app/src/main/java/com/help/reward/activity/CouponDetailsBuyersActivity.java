package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.CouponDetailsResponse;
import com.help.reward.bean.Response.CouponTradingResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 优惠劵详情----买家
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class CouponDetailsBuyersActivity extends BaseActivity implements View.OnClickListener {
    // identity：’owner’(下架/确认交易)/’buyer’(我要交易)
    public static final String IDENTITY_BUYER = "buyer";
    public static final String IDENTITY_OWNER = "owner";

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.iv_image)
    ImageView iv_image; // 图片
    @BindView(R.id.tv_name)
    TextView tv_name; // 商家名称
    @BindView(R.id.tv_attribute)
    TextView tv_attribute; // 描述
    @BindView(R.id.tv_management)
    TextView tv_management; // 主要经营
    @BindView(R.id.tv_address)
    TextView tv_address; // 所在地
    @BindView(R.id.tv_praise)
    TextView tv_praise; // 好评
    @BindView(R.id.tv_complaint)
    TextView tv_complaint; // 投诉
    @BindView(R.id.tv_constact)
    TextView tv_constact; // 联系方式
    @BindView(R.id.tv_quota)
    TextView tv_quota; // 额度
    @BindView(R.id.tv_effective_time)
    TextView tv_effective_time; // 有效期

    @BindView(R.id.tv_transaction_price)
    TextView tv_transaction_price; // 交易价格
    @BindView(R.id.btn_transaction)
    Button btn_transaction; // 交易

    private String voucher_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details_buyers);
        ButterKnife.bind(this);

        voucher_id = getIntent().getStringExtra("voucher_id");
        LogUtils.e("当前的voucher_id=" + voucher_id);
        initView();
        initData();
    }

    private void initView() {
        tv_title.setText("优惠劵详情");
        tv_title_right.setVisibility(View.GONE);
    }

    private void initData() {
        if (TextUtils.isEmpty(voucher_id)) {
            return;
        }
        PersonalNetwork
                .getResponseApi()
                .getCouponDetailsResponse(App.APP_CLIENT_KEY, voucher_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<CouponDetailsResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(CouponDetailsResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                bindView(response.data);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }


    private void bindView(CouponDetailsResponse response) {
        if (IDENTITY_BUYER.equals(response.identity)) {
            btn_transaction.setTag(IDENTITY_BUYER);
        } else if (IDENTITY_OWNER.equals(response.identity)) {

        }
        tv_name.setText(response.store_info.store_name);
    }

    @OnClick({R.id.iv_title_back, R.id.btn_transaction})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(CouponDetailsBuyersActivity.this);

                break;
            case R.id.btn_transaction:
                String tag = (String) btn_transaction.getTag();
                if (tag != null) {
                    if (tag.equals(IDENTITY_BUYER)) { // 我要交易

                    }
                }

                break;
        }
    }


}

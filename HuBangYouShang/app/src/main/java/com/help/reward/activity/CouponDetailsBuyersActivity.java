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
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.CouponDetailsResponse;
import com.help.reward.bean.Response.CouponTradingResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.idotools.utils.DateUtil;
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
    public static final String IDENTITY_OWNER = "owner"; // 确认交易
    public static final String IDENTITY_OWNER1 = "owner1"; // 确认交易
    public static final String IDENTITY_OWNER2 = "owner2"; // 优惠劵下架

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
    TextView tv_attribute; // 描述,服务，物流
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

    @BindView(R.id.et_transaction_price)
    TextView et_transaction_price; // 交易价格
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
        if(TextUtils.isEmpty(App.APP_CLIENT_KEY)){
            return;
        }
        if (TextUtils.isEmpty(voucher_id)) {
            return;
        }
        PersonalNetwork
                .getResponseApi()
                .getCouponDetailsResponse(App.APP_CLIENT_KEY, voucher_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<CouponDetailsResponse>() {
                    /*@Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }*/

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
        try {
            voucher_id = response.voucher_info.voucher_id;
            if (IDENTITY_BUYER.equals(response.identity)) { // 买家的优惠劵详情
                btn_transaction.setTag(IDENTITY_BUYER);
                et_transaction_price.setText(response.voucher_info.voucher_owner_setting);
                et_transaction_price.setEnabled(false);

            } else if (IDENTITY_OWNER.equals(response.identity)) { // 卖家的优惠劵信息
                LogUtils.e("当前的voucher_state=" + response.voucher_info.voucher_state);
                if ("1".equals(response.voucher_info.voucher_state)) { // 确认交易
                    btn_transaction.setText("确认交易");
                    btn_transaction.setTag(IDENTITY_OWNER1);

                } else if ("5".equals(response.voucher_info.voucher_state)) { // 显示优惠劵下架
                    btn_transaction.setText("优惠劵下架");
                    btn_transaction.setTag(IDENTITY_OWNER2);

                    et_transaction_price.setText(response.voucher_info.voucher_owner_setting);
                    et_transaction_price.setEnabled(false);
                } else {
                    btn_transaction.setVisibility(View.INVISIBLE);

                    et_transaction_price.setText(response.voucher_info.voucher_owner_setting);
                    et_transaction_price.setEnabled(false);
                }
            }
            GlideUtils.loadImage(response.store_info.store_avatar, iv_image);
            tv_name.setText(response.store_info.store_name);
            tv_attribute.setText("描述：" + response.store_info.store_desccredit + "\t\t服务：" +
                    response.store_info.store_servicecredit + "\t\t物流：" + response.store_info.store_deliverycredit); // 描述
            tv_management.setText("主要经营：\t" + response.store_info.store_zy); // 经营
            tv_address.setText(response.store_info.area_info + response.store_info.store_address); // 所在地
            tv_praise.setText(response.store_info.storepraise_rate);
            tv_complaint.setText(response.store_info.store_complaint);
            tv_constact.setText(response.store_info.store_phone);
            tv_quota.setText("满" +response.voucher_info.voucher_limit + "减去" + response.voucher_info.voucher_price);
            String time = DateUtil.getDateToString(response.voucher_info.voucher_start_date) + "-" +
                    DateUtil.getDateToString(response.voucher_info.voucher_end_date);
            tv_effective_time.setText(time);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    if (tag.equals(IDENTITY_BUYER)) { // 买家--我要交易
                        transaction();
                    } else if (tag.equals(IDENTITY_OWNER1)) { // 卖家--确认交易
                        comFirmTransaction();
                    } else if (tag.equals(IDENTITY_OWNER2)) { // 卖家--优惠劵下架
                        withdrawTransaction();
                    }
                }

                break;
        }
    }

    /***
     * 我要交易
     */
    private void transaction() {
        String transactionPrice = et_transaction_price.getText().toString().trim();
        if (TextUtils.isEmpty(transactionPrice)) {
            ToastUtils.show(mContext, "请输入交易价格");
            return;
        }
        int price = Integer.parseInt(transactionPrice);
        if (price <= 0) {
            ToastUtils.show(mContext, "交易价格必须大于0");
            return;
        }
        PersonalNetwork
                .getResponseApi()
                .getCouponPutOnSaleResponse(App.APP_CLIENT_KEY, voucher_id, price)
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
                            LogUtils.e("返回数据是：" + response.data);
                            if (response.data != null) {
                                ToastUtils.show(mContext, response.data);
                                finish();
                                ActivitySlideAnim.slideOutAnim(CouponDetailsBuyersActivity.this);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /***
     * 确认交易
     */
    private void comFirmTransaction() {
        String transactionPrice = et_transaction_price.getText().toString().trim();
        if (TextUtils.isEmpty(transactionPrice)) {
            ToastUtils.show(mContext, "请输入交易价格");
            return;
        }
        int price = Integer.parseInt(transactionPrice);
        if (price <= 0) {
            ToastUtils.show(mContext, "交易价格必须大于0");
            return;
        }
        PersonalNetwork
                .getResponseApi()
                .getCouponBuyResponse(App.APP_CLIENT_KEY, voucher_id, price)
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
                                ToastUtils.show(mContext, response.data);
                                finish();
                                ActivitySlideAnim.slideOutAnim(CouponDetailsBuyersActivity.this);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /****
     * 优惠劵下架
     */
    private void withdrawTransaction() {
        PersonalNetwork
                .getResponseApi()
                .getCouponWithdrawResponse(App.APP_CLIENT_KEY, voucher_id)
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
                                ToastUtils.show(mContext, response.data);
                                finish();
                                ActivitySlideAnim.slideOutAnim(CouponDetailsBuyersActivity.this);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }


}

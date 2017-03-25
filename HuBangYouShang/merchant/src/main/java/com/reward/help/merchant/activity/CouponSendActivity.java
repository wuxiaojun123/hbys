package com.reward.help.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.coupon.CouponPointsConstant;
import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.App;
import com.reward.help.merchant.R;
import com.reward.help.merchant.bean.CouponListBean;
import com.reward.help.merchant.bean.Response.BaseResponse;
import com.reward.help.merchant.bean.Response.CouponListResponse;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.network.CouponPointsNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.utils.DateUtils;
import com.reward.help.merchant.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fanjunqing on 25/03/2017.
 */

public class CouponSendActivity extends BaseActivity implements View.OnClickListener{

    public static final String SEND_EXTRA = "CouponSendActivity";

    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_send_coupon_store)
    TextView mTvStore;
    @BindView(R.id.tv_send_coupon_date)
    TextView mTvDate;

    @BindView(R.id.tv_send_coupon_discout)
    TextView mDiscount;
    @BindView(R.id.tv_send_coupon_limit)
    TextView mLimit;

    @BindView(R.id.tv_send_coupon_left_count)
    TextView mLeftCount;

    @BindView(R.id.et_send_coupon_count)
    EditText mEtSendCount;

    @BindView(R.id.tv_send_coupon_groupnum)
    TextView mTvGroupNum;
    private CouponListBean coupon;

    private Intent intent;
    String greeting;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_coupons_send);
        ButterKnife.bind(this);
        intent = getIntent();
        coupon = (CouponListBean) intent.getSerializableExtra(SEND_EXTRA);
        initView();
    }

    private void initView() {
        mTvTitle.setText(getText(R.string.send_coupon));

        if (coupon != null) {
            mTvStore.setText(coupon.getVoucher_t_storename());
            mTvDate.setText(DateUtils.strDateToStr(coupon.getVoucher_t_start_date()) + "-" + DateUtils.strDateToStr(coupon.getVoucher_t_end_date()));
            mDiscount.setText(String.format(getString(R.string.format_discount_money),coupon.getVoucher_t_price()));
            mLimit.setText(String.format(getString(R.string.format_limit_money),coupon.getVoucher_t_limit()));

            mLeftCount.setText(getString(R.string.format_left_count) + coupon.getVoucher_t_total());

            greeting = String.format(getString(R.string.format_greeting),new String[]{coupon.getVoucher_t_price(),coupon.getVoucher_t_limit()});
        }
    }

    @OnClick({R.id.iv_title_back,R.id.btn_send_coupon})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_back:
                hideSoftKeyboard();
                this.finish();
                break;

            case R.id.btn_send_coupon:
                sendCouponRequest();
                break;
        }

    }

    private void sendCouponRequest() {

        if (coupon == null) {
            ToastUtils.show(this,"暂无可使用的优惠券");
            return;
        }
        String num = mEtSendCount.getText().toString();
        if (TextUtils.isEmpty(num)) {
            ToastUtils.show(this,"请输入发放优惠券的个数");
            return;
        }
        try {
            if (Integer.parseInt(num) > Integer.parseInt(coupon.getVoucher_t_total())) {
                ToastUtils.show(this, "请重新输入发放优惠券的个数");
                return;
            }
        }catch (Exception e){
            return;
        }

        MyProcessDialog.showDialog(CouponSendActivity.this);
        subscribe = CouponPointsNetwork.getCouponListApi().sendCoupon(App.getAppClientKey(),coupon.getVoucher_t_id(),num)
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        if (e instanceof UnknownHostException) {
                            ToastUtils.show(mContext, "请求到错误服务器");
                        } else if (e instanceof SocketTimeoutException) {
                            ToastUtils.show(mContext, "请求超时");
                        }
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            intent = new Intent();
                            intent.putExtra(CouponPointsConstant.EXTRA_COUPON_POINTS_RECEIVER_ID,response.data.toString());
                            intent.putExtra(CouponPointsConstant.EXTRA_GREETING,greeting);
                            setResult(RESULT_OK,intent);
                            CouponSendActivity.this.finish();
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }
}

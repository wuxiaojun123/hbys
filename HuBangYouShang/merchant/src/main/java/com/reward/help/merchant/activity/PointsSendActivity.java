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
import com.reward.help.merchant.bean.Response.BaseResponse;
import com.reward.help.merchant.bean.Response.QueryMyPointsResponse;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.network.CouponPointsNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PointsSendActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_send_points_my_points)
    TextView mTvMyPoints;

    @BindView(R.id.et_send_points_num)
    EditText mEtSendNum;
    @BindView(R.id.et_send_points_pre_num)
    EditText mEtSendPePoint;
    @BindView(R.id.tv_send_points_groupnum)
    TextView mTvGroupNum;
    @BindView(R.id.tv_send_points_result)
    TextView mTvPointResult;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_points_send);
        ButterKnife.bind(this);
        initView();

        initData();
    }

    private void initData() {
        getPointRequest();
    }

    private void initView() {
        int groupNum = getIntent().getIntExtra("num",0);
        mTvGroupNum.setText(String.format(getString(R.string.group_num),groupNum+""));
    }


    @OnClick({R.id.iv_title_back,R.id.btn_send_points})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_title_back:
                hideSoftKeyboard();
                PointsSendActivity.this.finish();
                break;

            case R.id.btn_send_points:
                if (TextUtils.isEmpty(mEtSendNum.getText())) {
                    ToastUtils.show(PointsSendActivity.this,getString(R.string.send_points_num_hint));
                    return;
                }

                if (TextUtils.isEmpty(mEtSendPePoint.getText())) {
                    ToastUtils.show(PointsSendActivity.this,getString(R.string.send_points_per_num_hint));
                    return;
                }
                sendPointsRequest();
                break;
        }
    }


    private void getPointRequest() {
        subscribe = CouponPointsNetwork.getCouponListApi().getPoints(App.getAppClientKey())
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<QueryMyPointsResponse>() {
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
                    public void onNext(QueryMyPointsResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            String points = response.data.points;
                            if(!TextUtils.isEmpty(points)) {
                                mTvMyPoints.setText(points);
                            }
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }


    private void sendPointsRequest() {
        String people_limit = mEtSendNum.getText().toString();
        String num_limit = mEtSendPePoint.getText().toString();

        MyProcessDialog.showDialog(PointsSendActivity.this);
        subscribe = CouponPointsNetwork.getCouponListApi().sendPoints(App.getAppClientKey(),people_limit,num_limit)
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
                            Intent intent = new Intent();
                            intent.putExtra(CouponPointsConstant.EXTRA_COUPON_POINTS_RECEIVER_ID,response.data.toString());
                            setResult(RESULT_OK,intent);
                            PointsSendActivity.this.finish();
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }
}

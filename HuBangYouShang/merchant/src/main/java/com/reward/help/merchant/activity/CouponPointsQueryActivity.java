package com.reward.help.merchant.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.idotools.utils.LogUtils;
import com.idotools.utils.MetricsUtils;
import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.App;
import com.reward.help.merchant.R;
import com.reward.help.merchant.adapter.QueryCouponAdapter;
import com.reward.help.merchant.adapter.QueryPointsAdapter;
import com.reward.help.merchant.bean.CouponLogBean;
import com.reward.help.merchant.bean.PointsLogBean;
import com.reward.help.merchant.bean.Response.QueryCouponResponse;
import com.reward.help.merchant.bean.Response.QueryPointsResponse;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.network.CouponPointsNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.utils.GlideUtils;
import com.reward.help.merchant.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CouponPointsQueryActivity extends BaseActivity {

    public static final String Extra = "CouponPointsQueryActivity_Extra";
    public static final String Extra_GiveId = "CouponPointsQueryActivity_Extra_GiveId";

    public static final int Extra_Coupon = 0;
    public static final int Extra_Points = Extra_Coupon + 1;


    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;


    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_query_coupon_store_pic)
    ImageView mIvStorePic;
    @BindView(R.id.tv_query_coupon_store)
    TextView mTvStore;
    @BindView(R.id.tv_query_coupon_num)
    TextView mTvNum;

    private String giveId;
    private int type;
    private Intent intent;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private QueryCouponAdapter couponAdapter;

    private QueryPointsAdapter pointsAdapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_coupon_points_query);
        ButterKnife.bind(this);
        intent = getIntent();
        giveId = intent.getStringExtra(Extra_GiveId);
        type = intent.getIntExtra(Extra,0);
        initView();

        initData();
    }

    private void initData() {
        if (Extra_Points == type) {
            queryPointsRequest();
        } else {
            queryCouponRequest();
        }
    }

    private void initView() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        if (Extra_Points == type) {
            mTvTitle.setText(getString(R.string.get_points_title));
            pointsAdapter = new QueryPointsAdapter(this);
            mLRecyclerViewAdapter = new LRecyclerViewAdapter(pointsAdapter);
        } else {
            mTvTitle.setText(getString(R.string.get_coupon_title));
            couponAdapter = new QueryCouponAdapter(this);
            mLRecyclerViewAdapter = new LRecyclerViewAdapter(couponAdapter);
        }
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(true);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        lRecyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                //super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildPosition(view) != 0) {
                    outRect.top = MetricsUtils.dipToPx(1);
                }
            }
        });

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponPointsQueryActivity.this.finish();
            }
        });

    }



    private void queryPointsRequest() {
        MyProcessDialog.showDialog(CouponPointsQueryActivity.this);
        subscribe = CouponPointsNetwork.getCouponListApi().queryPoints(App.getAppClientKey(),giveId)
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<QueryPointsResponse>() {
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
                    public void onNext(QueryPointsResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            LogUtils.e(response.data.toString());
                            pointsAdapter.setDataList(response.data.receive_list);
                            showPointsInfo(response.data.give_info);
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void showCouponInfo(CouponLogBean.VoucherInfo voucher_info) {
        if (voucher_info != null) {
            mTvStore.setText(voucher_info.seller_name);
            String num = "";
            try {
                String showNum = voucher_info.num_given +"/" +voucher_info.num;
                num = String.format(getString(R.string.format_query_coupon_get_num),showNum);
            }catch (Exception e){}
            mTvNum.setText(num);
        }
    }


    private void showPointsInfo(PointsLogBean.VoucherInfo voucher_info) {
        if (voucher_info != null) {
            mTvStore.setText(voucher_info.seller_name);
            try {
                String peoplelimit = voucher_info.people_received + "/" + voucher_info.people_limit;
                String numlimit = voucher_info.num_given + "/"+ voucher_info.num_limit;
                String[] showNum = new String[]{peoplelimit,numlimit};
                String num = String.format(getString(R.string.format_query_points_get_num),showNum);
                mTvNum.setText(num);

                GlideUtils.loadBoundImage(voucher_info.member_avatar,mIvStorePic);


            }catch (Exception e){}
        }
    }


    private void queryCouponRequest() {
        MyProcessDialog.showDialog(CouponPointsQueryActivity.this);
        subscribe = CouponPointsNetwork.getCouponListApi().queryCoupon(App.getAppClientKey(),giveId)
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<QueryCouponResponse>() {
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
                    public void onNext(QueryCouponResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            couponAdapter.setDataList(response.data.receive_log_list);
                            showCouponInfo(response.data.voucher_info);
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }
}

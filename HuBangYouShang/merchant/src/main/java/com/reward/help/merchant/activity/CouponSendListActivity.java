package com.reward.help.merchant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.idotools.utils.LogUtils;
import com.idotools.utils.MetricsUtils;
import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.App;
import com.reward.help.merchant.R;
import com.reward.help.merchant.adapter.CouponListAdapter;
import com.reward.help.merchant.bean.CouponListBean;
import com.reward.help.merchant.bean.Response.CouponListResponse;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.chat.ui.ChatFragment;
import com.reward.help.merchant.network.CouponPointsNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.rxbus.RxBus;
import com.reward.help.merchant.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponSendListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;


    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;

    private CouponListAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private List<CouponListBean> mList = null;

    private List<CouponListBean> mCheckedList = null;

    private static int requestCode;

    private Intent intent;
    private int num; // 群人数，如果是0表示对当前某个用户单独发放
    private String groupId;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_coupon_send);
        ButterKnife.bind(this);
        intent = getIntent();
        num = intent.getIntExtra("num", 0);
        groupId = intent.getStringExtra("groupId");
        initView();
        initData();

    }

    private void initData() {
        getCouponListRequest();
    }

    private void initView() {
        mTvTitle.setText(getText(R.string.send_coupon));
        mTvRight.setText(getText(R.string.next));
        mCheckedList = new ArrayList<CouponListBean>();
        lRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CouponListAdapter(this);
        adapter.setDataList(mList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
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
                    outRect.top = MetricsUtils.dipToPx(10);
                }
            }
        });

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCheckedList.clear();
                mCheckedList.add(mList.get(position));
                adapter.setmCheckList(mCheckedList);
            }
        });
    }


    private void getCouponListRequest() {
        MyProcessDialog.showDialog(CouponSendListActivity.this);
        subscribe = CouponPointsNetwork.getCouponListApi().getCouponList(App.getAppClientKey())
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<CouponListResponse>() {
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
                    public void onNext(CouponListResponse couponListResponse) {
                        MyProcessDialog.closeDialog();
                        if (couponListResponse.code == 200) {
                            mList = couponListResponse.data.voucher_list;
                            adapter.setDataList(mList);
                            //finish();
                            //ActivitySlideAnim.slideOutAnim(LoginActivity.this);
                        } else {
                            ToastUtils.show(mContext, couponListResponse.msg);
                        }
                    }
                });
    }

    @OnClick({R.id.iv_title_back, R.id.tv_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                CouponSendListActivity.this.finish();
                break;
            case R.id.tv_right:
                if (mCheckedList.size() > 0) {
                    intent.setClass(this, CouponSendActivity.class)
                            .putExtra(CouponSendActivity.SEND_EXTRA, mCheckedList.get(0))
                            .putExtra("num", num)
                            .putExtra("groupId",groupId);
                    startActivityForResult(intent, requestCode);
                } else {
                    ToastUtils.show(this, "请选择要发送的优惠券");
                }
                //TODO
                break;
        }

    }

    public static void startActivityForResult(Fragment fragment, Intent intent, int reCode) {
        fragment.startActivityForResult(intent, reCode);
        requestCode = reCode;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            CouponSendListActivity.this.finish();
        }
    }
}

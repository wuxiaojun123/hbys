package com.reward.help.merchant.activity;

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
import com.idotools.utils.MetricsUtils;
import com.reward.help.merchant.R;
import com.reward.help.merchant.adapter.CouponListAdapter;
import com.reward.help.merchant.bean.CouponListBean;
import com.reward.help.merchant.chat.ui.BaseActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fanjunqing on 21/03/2017.
 */

public class CouponSendListActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_coupon_send);
        ButterKnife.bind(this);

        initData();

        initView();
    }

    private void initData() {
        mList = new ArrayList<CouponListBean>();
        for(int i = 0;i < 20;i++) {
            CouponListBean couponListBean = new CouponListBean();
            mList.add(couponListBean);
        }
    }

    private void initView() {
        mTvTitle.setText(getText(R.string.send_coupon));
        mTvRight.setText(getText(R.string.next));

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
    }
}

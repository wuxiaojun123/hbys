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
import com.base.recyclerview.OnItemClickListener;
import com.base.recyclerview.OnRefreshListener;
import com.idotools.utils.MetricsUtils;
import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.App;
import com.reward.help.merchant.R;
import com.reward.help.merchant.adapter.GroupProgressAdapter;
import com.reward.help.merchant.bean.Response.BaseResponse;
import com.reward.help.merchant.bean.Response.GroupProgressResponse;
import com.reward.help.merchant.chat.ui.BaseActivity;
import com.reward.help.merchant.network.CouponPointsNetwork;
import com.reward.help.merchant.network.base.BaseSubscriber;
import com.reward.help.merchant.view.MyProcessDialog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 申请进度查看
 */
public class QueryApplyGroupActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;

    GroupProgressAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_query_apply_group);
        ButterKnife.bind(this);
        mTvTitle.setText(getText(R.string.contact_apply_tip));

        initView();

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                QueryApplyGroupActivity.this.finish();
            }
        });
        initData(false);
    }

    private void initView() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupProgressAdapter(this);
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
                    outRect.top = MetricsUtils.dipToPx(1);
                }
            }
        });

        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(true);
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GroupProgressResponse.GroupProgrossInfoBean bean = adapter.getDataList().get(position);

                Intent mIntent = new Intent(mContext, ApplyCreateGroupActivity.class);
                mIntent.putExtra("examine_failed", true);
                mIntent.putExtra("id", bean.id);
                startActivityForResult(mIntent, 1);
            }
        });
    }

    private void initData(final boolean isRefresh) {
        if (!isRefresh)
            MyProcessDialog.showDialog(QueryApplyGroupActivity.this);
        subscribe = CouponPointsNetwork.getCouponListApi().queryApplyProgress(App.getAppClientKey())
                .subscribeOn(Schedulers.io()) // 请求放在io线程中
                .observeOn(AndroidSchedulers.mainThread()) // 请求结果放在主线程中
                .subscribe(new BaseSubscriber<GroupProgressResponse>() {
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
                    public void onNext(GroupProgressResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                adapter.setDataList(response.data.list);
                            }
                            if (isRefresh) {
                                lRecyclerview.refreshComplete(1);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            initData(true);
        }
    }

}

package com.help.reward.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.widget.LinearLayout;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.activity.MyOrderActivity;
import com.help.reward.adapter.MyOrderAdapter;
import com.help.reward.bean.MyOrderListBean;
import com.help.reward.bean.Response.MyOrderResponse;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.BooleanRxbusType;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 2017/2/8.
 */

public class MyOrderAllFragment extends BaseFragment {

	private int										numSize		= 15;

	private int										currentPage	= 1;

	@BindView(R.id.id_recycler_view) LRecyclerView	lRecyclerview;

	@BindView(R.id.ll_empty) LinearLayout			ll_empty;

	private String									state_type;			// 订单状态

	private MyOrderAdapter							mOrderAdapter;

	@Override protected int getLayoutId() {
		return R.layout.fragment_my_order_all;
	}

	@Override protected void init() {
		initRecyclerView();
		Bundle bundle = getArguments();
		if (bundle != null) {
			state_type = bundle.getString(MyOrderActivity.STATE_TYPE);
		}
		((SimpleItemAnimator) lRecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);

		initNetwork();
		initRxbus();
	}

	private void initRxbus() {
		RxBus.getDefault().toObservable(BooleanRxbusType.class).subscribe(new Action1<BooleanRxbusType>() {

			@Override public void call(BooleanRxbusType booleanRxbusType) {
				if (booleanRxbusType.isRefresh) {
					// 刷新数据
					initNetwork();
				}
			}
		});
	}

	private void initNetwork() {
		if (App.APP_CLIENT_KEY == null) {
			ToastUtils.show(mContext, R.string.string_please_login);
			return;
		}
		PersonalNetwork.getResponseApi().getMyOrderResponse("member_order", "order_list", currentPage + "", state_type, App.APP_CLIENT_KEY).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<MyOrderResponse>() {

					@Override public void onError(Throwable e) {
						e.printStackTrace();
						lRecyclerview.refreshComplete(numSize);
						ToastUtils.show(mContext, R.string.string_error);
					}

					@Override public void onNext(MyOrderResponse response) {
						lRecyclerview.refreshComplete(numSize);
						if (response.code == 200) {
							if (response.data != null) {
								if (response.data.order_group_list != null) {
									List<MyOrderListBean.OrderList> list = new ArrayList<MyOrderListBean.OrderList>();
									List<MyOrderListBean> order_group_list = response.data.order_group_list;
									for (MyOrderListBean orderListBean : order_group_list) {
										List<MyOrderListBean.OrderList> order_list = orderListBean.order_list;
										for (MyOrderListBean.OrderList bean : order_list) {
											list.add(bean);
										}
									}
									// LogUtils.e("返回数据集合是：" + list.size());
									if (currentPage == 1) {
										mOrderAdapter.setDataList(list);
									} else {
										mOrderAdapter.addAll(list);
									}
								}
							}
							if (!response.hasmore) { // 是否有更多数据
								lRecyclerview.setNoMore(true);
							} else {
								currentPage += 1;
							}
						} else {
							ToastUtils.show(mContext, response.msg);
						}
					}
				});
	}

	private void initRecyclerView() {
		lRecyclerview.setEmptyView(ll_empty);
		lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
		mOrderAdapter = new MyOrderAdapter(mContext);
		LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mOrderAdapter);
		lRecyclerview.setAdapter(mLRecyclerViewAdapter);
		initRefreshListener();
		initLoadMoreListener();
	}

	private void initLoadMoreListener() {
		lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override public void onLoadMore() { // 请求更多
				initNetwork();
			}
		});
	}

	private void initRefreshListener() {
		lRecyclerview.setOnRefreshListener(new OnRefreshListener() {

			@Override public void onRefresh() { // 刷新
				currentPage = 1;
				initNetwork();
			}
		});
	}

}

package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.ExpandShopcartAdapter;
import com.help.reward.bean.MyOrderListBean;
import com.help.reward.bean.MyOrderShopBean;
import com.help.reward.bean.Response.MyOrderResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ADBrian on 26/03/2017.
 */

public class ShopcartActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.id_recycler_view)
    ExpandableListView lRecyclerview;

    private ExpandShopcartAdapter mAdapter;

    private List<MyOrderShopBean> mSelected = new ArrayList<MyOrderShopBean>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcart);
        ButterKnife.bind(this);

        initview();

        //initNetwork();

        load();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void expandChild(){
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            lRecyclerview.expandGroup(i);
        }
    }

    private void initview() {

        tv_title.setText("购物车");
        tv_title_right.setVisibility(View.GONE);

        mAdapter = new ExpandShopcartAdapter(mSelected,this,lRecyclerview);
        lRecyclerview.setAdapter(mAdapter);

        lRecyclerview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        /*
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ShopcartAdapter(mContext);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        lRecyclerview.setLoadMoreEnabled(false);*/
    }

    private void load() {
        PersonalNetwork
                .getResponseApi()
                .getMyOrderResponse("member_order", "order_list", 0 + "", 1+"", App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<MyOrderResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(MyOrderResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (response.data.order_group_list != null) {
                                    List<MyOrderListBean.OrderList> list = new ArrayList<MyOrderListBean.OrderList>();
                                    List<MyOrderListBean> order_group_list = response.data.order_group_list;
                                    for (MyOrderListBean orderListBean:order_group_list){
                                        List<MyOrderListBean.OrderList> order_list = orderListBean.order_list;
                                        for (MyOrderListBean.OrderList bean:order_list){
                                            list.add(bean);
                                        }
                                    }
                                    mAdapter.setDataList(list);
                                    //expandChild();
                                }
                            }

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }


    private void initNetwork() {

        ShopcartNetwork.getShopcartCookieApi().getShopcartList(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }
}

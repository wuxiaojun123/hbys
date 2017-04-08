package com.help.reward.fragment;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.GoodEvaluateAdapter;
import com.help.reward.bean.GoodEvaluateBean;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.GoodEvaluateResponse;
import com.help.reward.bean.Response.HelpSeekInfoResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.ShopMallNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MXY on 2017/2/26.
 */

public class GoodRetedFragment extends BaseFragment implements View.OnClickListener{


    @BindView(R.id.lv_shopinfo_rated)
    LRecyclerView lRecyclerview;

    private int curpage = 1;
    private int numSize = 15;

    private Collection<GoodEvaluateBean> mDatas = new ArrayList<>();
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private GoodEvaluateAdapter adapter;

    private String goodsId = "233";
    private String type = null;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goodinfo_rated;
    }

    @Override
    protected void init() {
        initView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            //goodsId = bundle.getString("goods_id");
            if (!TextUtils.isEmpty(goodsId)) {
                initNetwork();
            }
        }
    }

    private void initView() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new GoodEvaluateAdapter(getActivity());
        adapter.setDataList(mDatas);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(true);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());

        initLoadMoreListener();
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                curpage++;
                initNetwork();
            }
        });
    }

    private void initNetwork() {
        MyProcessDialog.showDialog(getActivity());
        subscribe = ShopMallNetwork
                .getShopMallMainApi()
                .getGoodDetailsEvaluateResponse(goodsId, type, curpage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<GoodEvaluateResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        lRecyclerview.refreshComplete(numSize);
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(GoodEvaluateResponse response) {
                        MyProcessDialog.closeDialog();
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            if (curpage == 1) {
                                adapter.setDataList(response.data.goods_eval_list);
                            } else {
                                adapter.addAll(response.data.goods_eval_list);
                            }

                            if (!response.hasmore) {
                                lRecyclerview.setLoadMoreEnabled(false);
                            } else {
                                lRecyclerview.setLoadMoreEnabled(true);
                            }

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    @OnClick({R.id.tv_evaluate_all,R.id.tv_evaluate_good,R.id.tv_evaluate_middle,R.id.tv_evaluate_bad})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_evaluate_all:
                type = null;
                curpage = 1;
                initNetwork();
                break;
            case R.id.tv_evaluate_good:
                type = "1";
                curpage = 1;
                initNetwork();
                break;
            case R.id.tv_evaluate_middle:
                type = "2";
                curpage = 1;
                initNetwork();
                break;
            case R.id.tv_evaluate_bad:
                type = "3";
                curpage = 1;
                initNetwork();
                break;
        }
    }
}

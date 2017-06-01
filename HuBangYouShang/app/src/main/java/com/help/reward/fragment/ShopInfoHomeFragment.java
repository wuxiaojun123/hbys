package com.help.reward.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.activity.GoodInfoActivity;
import com.help.reward.adapter.StoreGoodsAdapter;
import com.help.reward.adapter.viewholder.BannerImageStoreDetailHolderView;
import com.help.reward.bean.Response.StoreDetailHomeResponse;
import com.help.reward.bean.ShopMallHotBean;
import com.help.reward.bean.StotrDetailBean;
import com.help.reward.network.StoreDetailNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MXY on 2017/2/26.
 */

public class ShopInfoHomeFragment extends BaseFragment {

    private View contentView;
    LRecyclerView lRecyclerview;
    private int numSize = 15;
    private StoreGoodsAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    List<ShopMallHotBean> mDatas = new ArrayList<>();
    String store_id;
    TextView tv_goods_price1, tv_goods_price2, tv_goods_price3, tv_goods_price4, tv_goods_price5;
    TextView tv_goods_jingle1, tv_goods_jingle2, tv_goods_jingle3, tv_goods_jingle4, tv_goods_jingle5;
    TextView tv_goods_name1, tv_goods_name2, tv_goods_name3, tv_goods_name4, tv_goods_name5;
    ImageView iv_goods1, iv_goods2, iv_goods3, iv_goods4, iv_goods5;
    LinearLayout layout_goods1, layout_goods2, layout_goods3, layout_goods4, layout_goods5;
    LinearLayout layout_goodsLine1, layout_goodsLine2;
    ConvenientBanner banner_shopinfo_home;
    TextView tv_footview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_shopinfo_home, null);
        }
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("store_id")) {
            store_id = bundle.getString("store_id");
        }
        lRecyclerview = (LRecyclerView) contentView.findViewById(R.id.id_stickynavlayout_innerscrollview);
        initData();
        return contentView;
    }

    private void initData() {
        GridLayoutManager mgr = new GridLayoutManager(mContext, 2);
        lRecyclerview.setLayoutManager(mgr);
        adapter = new StoreGoodsAdapter(mContext);
        adapter.setDataList(mDatas);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        initHeader();
        initFootView();
        initItemClickListener();
        requestData();
    }

    private void initItemClickListener() {
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, GoodInfoActivity.class);
                intent.putExtra("goods_id", adapter.getDataList().get(position).goods_id);
                intent.putExtra("store_id", adapter.getDataList().get(position).store_id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(getActivity());
            }

        });
    }


    private void initHeader() {
        View view = getActivity().getLayoutInflater().inflate(
                R.layout.header_fragment_shopinfo_home, null);
        tv_goods_price1 = (TextView) view.findViewById(R.id.tv_goods_price1);
        tv_goods_price2 = (TextView) view.findViewById(R.id.tv_goods_price2);
        tv_goods_price3 = (TextView) view.findViewById(R.id.tv_goods_price3);
        tv_goods_price4 = (TextView) view.findViewById(R.id.tv_goods_price4);
        tv_goods_price5 = (TextView) view.findViewById(R.id.tv_goods_price5);

        tv_goods_jingle1 = (TextView) view.findViewById(R.id.tv_goods_jingle1);
        tv_goods_jingle2 = (TextView) view.findViewById(R.id.tv_goods_jingle2);
        tv_goods_jingle3 = (TextView) view.findViewById(R.id.tv_goods_jingle3);
        tv_goods_jingle4 = (TextView) view.findViewById(R.id.tv_goods_jingle4);
        tv_goods_jingle5 = (TextView) view.findViewById(R.id.tv_goods_jingle5);

        tv_goods_name1 = (TextView) view.findViewById(R.id.tv_goods_name1);
        tv_goods_name2 = (TextView) view.findViewById(R.id.tv_goods_name2);
        tv_goods_name3 = (TextView) view.findViewById(R.id.tv_goods_name3);
        tv_goods_name4 = (TextView) view.findViewById(R.id.tv_goods_name4);
        tv_goods_name5 = (TextView) view.findViewById(R.id.tv_goods_name5);

        iv_goods1 = (ImageView) view.findViewById(R.id.iv_goods1);
        iv_goods2 = (ImageView) view.findViewById(R.id.iv_goods2);
        iv_goods3 = (ImageView) view.findViewById(R.id.iv_goods3);
        iv_goods4 = (ImageView) view.findViewById(R.id.iv_goods4);
        iv_goods5 = (ImageView) view.findViewById(R.id.iv_goods5);

        layout_goods1 = (LinearLayout) view.findViewById(R.id.layout_goods1);
        layout_goods2 = (LinearLayout) view.findViewById(R.id.layout_goods2);
        layout_goods3 = (LinearLayout) view.findViewById(R.id.layout_goods3);
        layout_goods4 = (LinearLayout) view.findViewById(R.id.layout_goods4);
        layout_goods5 = (LinearLayout) view.findViewById(R.id.layout_goods5);

        layout_goodsLine1 = (LinearLayout) view.findViewById(R.id.layout_goodsLine1);
        layout_goodsLine2 = (LinearLayout) view.findViewById(R.id.layout_goodsLine2);

        banner_shopinfo_home = (ConvenientBanner) view.findViewById(R.id.banner_shopinfo_home);

        mLRecyclerViewAdapter.addHeaderView(view);
    }

    void initFootView() {
        View view = getActivity().getLayoutInflater().inflate(
                R.layout.adapter_footview, null);
        tv_footview = (TextView) view.findViewById(R.id.tv_footview);
        tv_footview.setText("查看店铺全部商品");
        tv_footview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show(mContext, "查看全部商品");
            }
        });
        mLRecyclerViewAdapter.addFooterView(view);
    }

    private void requestData() {

        subscribe = StoreDetailNetwork
                .getStroeApi()
                .getStoreDetailHomeResponse(App.APP_CLIENT_KEY, "store_info", store_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<StoreDetailHomeResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(StoreDetailHomeResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (onShopInfoLoadSucced != null) {
                                    onShopInfoLoadSucced.onLoad(response.data.store_info);
                                }
                                bindData(response);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }


    void bindData(StoreDetailHomeResponse response) {
        adapter.setDataList(response.data.rec_goods_list);
        switch (response.data.sale_goods_list.size()) {
            case 5:
                layout_goods5.setVisibility(View.VISIBLE);
                ShopMallHotBean item5 = response.data.sale_goods_list.get(4);
                setOnGoodsClick(layout_goods5, item5.goods_id, item5.store_id);
                tv_goods_price5.setText("￥" + item5.goods_price);
                tv_goods_name5.setText(item5.goods_name);
                tv_goods_jingle5.setText(item5.goods_jingle);
                GlideUtils.loadImage(item5.goods_image_url, iv_goods5);
            case 4:
                layout_goods4.setVisibility(View.VISIBLE);
                ShopMallHotBean item4 = response.data.sale_goods_list.get(3);
                setOnGoodsClick(layout_goods4, item4.goods_id, item4.store_id);
                tv_goods_price4.setText("￥" + item4.goods_price);
                tv_goods_name4.setText(item4.goods_name);
                tv_goods_jingle4.setText(item4.goods_jingle);
                GlideUtils.loadImage(item4.goods_image_url, iv_goods4);
            case 3:
                layout_goods3.setVisibility(View.VISIBLE);
                layout_goodsLine2.setVisibility(View.VISIBLE);
                ShopMallHotBean item3 = response.data.sale_goods_list.get(2);
                setOnGoodsClick(layout_goods3, item3.goods_id, item3.store_id);
                tv_goods_price3.setText("￥" + item3.goods_price);
                tv_goods_name3.setText(item3.goods_name);
                tv_goods_jingle3.setText(item3.goods_jingle);
                GlideUtils.loadImage(item3.goods_image_url, iv_goods3);
            case 2:
                layout_goods2.setVisibility(View.VISIBLE);
                ShopMallHotBean item2 = response.data.sale_goods_list.get(1);
                setOnGoodsClick(layout_goods2, item2.goods_id, item2.store_id);
                tv_goods_price2.setText("￥" + item2.goods_price);
                tv_goods_name2.setText(item2.goods_name);
                tv_goods_jingle2.setText(item2.goods_jingle);
                GlideUtils.loadImage(item2.goods_image_url, iv_goods2);
            case 1:
                layout_goods1.setVisibility(View.VISIBLE);
                layout_goodsLine1.setVisibility(View.VISIBLE);
                ShopMallHotBean item1 = response.data.sale_goods_list.get(0);
                setOnGoodsClick(layout_goods1, item1.goods_id, item1.store_id);
                tv_goods_price1.setText("￥" + item1.goods_price);
                tv_goods_name1.setText(item1.goods_name);
                tv_goods_jingle1.setText(item1.goods_jingle);
                GlideUtils.loadImage(item1.goods_image_url, iv_goods1);
                break;
            case 0:

                break;
        }
        banner_shopinfo_home.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerImageStoreDetailHolderView();
            }
        }, response.data.store_info.store_slide)
                .setPageIndicator(new int[]{R.mipmap.img_ic_page_indicator, R.mipmap.img_ic_page_indicator_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(3000);
        banner_shopinfo_home.setFocusable(true);
        banner_shopinfo_home.setFocusableInTouchMode(true);
        banner_shopinfo_home.requestFocus();
        if (response.data.store_info.store_slide.size() == 0) {
            banner_shopinfo_home.setVisibility(View.GONE);
        }
    }

    private void setOnGoodsClick(View view, final String goods_id, final String store_id) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GoodInfoActivity.class);
                intent.putExtra("goods_id", goods_id);
                intent.putExtra("store_id", store_id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(getActivity());
            }
        });
    }


    public interface OnShopInfoLoadSucced {
        void onLoad(StotrDetailBean store_info);
    }

    OnShopInfoLoadSucced onShopInfoLoadSucced;

    public void setOnShopInfoLoadSucced(OnShopInfoLoadSucced onShopInfoLoadSucced) {
        this.onShopInfoLoadSucced = onShopInfoLoadSucced;
    }
}

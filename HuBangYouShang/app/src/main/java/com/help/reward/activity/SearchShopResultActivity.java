package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.base.recyclerview.OnLoadMoreListener;
import com.help.reward.R;
import com.help.reward.adapter.StoreGoodsAdapter;
import com.help.reward.bean.PinPaiBean;
import com.help.reward.bean.Response.StoreDetailAllResponse;
import com.help.reward.bean.ShopMallHotBean;
import com.help.reward.network.ShopMallNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.GoodsTypeSearchPop;
import com.help.reward.view.MyProcessDialog;
import com.help.reward.view.SearchEditTextView;
import com.help.reward.view.SearchGoodsFenleiPop;
import com.help.reward.view.SearchGoodsZonghePop;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 搜索商品页面结果展示页面
 * Created by wuxiaojun on 17-3-31.
 */

public class SearchShopResultActivity extends BaseActivity {

    @BindView(R.id.id_drawerlayout)
    DrawerLayout mDrawerlayout;
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.iv_search_type)
    TextView iv_search_type;
    @BindView(R.id.et_search)
    SearchEditTextView et_search;
    @BindView(R.id.tv_text)
    TextView tv_text;
    @BindView(R.id.layout_alltitle)
    LinearLayout layout_alltitle;

    @BindView(R.id.tv_zonghe)
    TextView tv_zonghe;
    @BindView(R.id.tv_salenum)
    TextView tv_salenum;
    @BindView(R.id.tv_xinyong)
    TextView tv_xinyong;
    @BindView(R.id.tv_shaixuan)
    TextView tv_shaixuan;
    @BindView(R.id.iv_style)
    ImageView iv_style;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    private StoreGoodsAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    List<ShopMallHotBean> mDatas = new ArrayList<>();
    String store_id;
    int curpage = 1;

    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    String keyword;
    String searchType = "goods";
    String key;//salenum 销量 clicknum 人气 price 价格order desc降序asc升序；默认降序
    String price_from, price_to;
    String b_id;// 品牌id
    List<String> service = new ArrayList<>();//freight 包邮 COD 货到付款 refund 急速退款 protection 消费者保障quality 正品保障 sevenDay 7天无理由退货
    String order;
    String type = "gird";
    String zongheType = "zonghe";

    List<PinPaiBean> pinpaiList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop_result);
        ButterKnife.bind(this);
        keyword = getIntent().getExtras().getString("keyword");
        for (int i = 0; i < 12; i++) {
            PinPaiBean p = new PinPaiBean();
            p.b_id = i + "";
            p.b_name = "品牌" + i;
            pinpaiList.add(p);
        }
        initView();
        initData();
    }

    void initView() {
        iv_title_back.setVisibility(View.VISIBLE);
        tv_text.setVisibility(View.GONE);
        et_search.setHint("搜索关键字相关商品");
    }

    private void initData() {
        linearLayoutManager = new LinearLayoutManager(mContext);
        gridLayoutManager = new GridLayoutManager(mContext, 2);
        lRecyclerview.setLayoutManager(gridLayoutManager);
        adapter = new StoreGoodsAdapter(mContext);
        adapter.setDataList(mDatas);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        initLoadMoreListener();
        initItemClickListener();
        requestData();
    }

    @OnClick({R.id.iv_title_back, R.id.iv_search_type, R.id.ll_zonghe,
            R.id.tv_salenum, R.id.tv_xinyong, R.id.ll_shaixuan, R.id.ll_style,
            R.id.tv_ok})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(SearchShopResultActivity.this);

                break;
            case R.id.iv_search_type:
                new GoodsTypeSearchPop().showPopupWindow(this, iv_search_type).setOnTypeChooseListener(new GoodsTypeSearchPop.OnTypeChooseListener() {
                    @Override
                    public void onType(String type) {
                        searchType = type;
                        if ("goods".equals(type)) {
                            et_search.setHint("搜索关键字相关商品");
                            et_search.setText("商品");
                        } else {
                            et_search.setHint("搜索关键字相关店铺");
                            et_search.setText("店铺");
                        }
                    }
                });
                break;
            case R.id.ll_zonghe:
                new SearchGoodsZonghePop().showPopupWindow(this, layout_alltitle, zongheType).setOnTypeChooseListener(new SearchGoodsZonghePop.OnTypeChooseListener() {
                    @Override
                    public void onType(String type) {
                        if (zongheType.equals(type)) {
                            return;
                        }
                        zongheType = type;
                        if ("pricedesc".equals(type)) {
                            key = "price";
                            order = "desc";
                        } else if ("pricedasc".equals(type)) {
                            key = "price";
                            order = "asc";
                        } else if ("clicknum".equals(type)) {
                            key = "clicknum ";
                            order = "";
                        } else {
                            key = "";
                            order = "";
                        }
                        requestData();
                    }
                });
                break;
            case R.id.tv_salenum:
                if ("salenum".equals(key)) {
                    order = "asc";
                } else {
                    key = "salenum";
                    order = "desc";
                }
                requestData();
                break;
            case R.id.tv_xinyong:
                if ("salenum".equals(key)) {
                    order = "asc";
                } else {
                    key = "salenum";
                    order = "desc";
                }
                requestData();
                break;
            case R.id.ll_shaixuan:
                mDrawerlayout.openDrawer(Gravity.RIGHT);
                /*new SearchGoodsFenleiPop().showPopupWindow(this, tv_shaixuan, b_id, price_from, price_to, service, pinpaiList)
                        .setOnTypeChooseListener(new SearchGoodsFenleiPop.OnTypeChooseListener() {
                            @Override
                            public void onType(String b_id, String price_from, String price_to, List<String> service) {
                                SearchShopResultActivity.this.b_id = b_id;
                                SearchShopResultActivity.this.price_from = price_from;
                                SearchShopResultActivity.this.price_to = price_to;
                                SearchShopResultActivity.this.service = service;
                            }
                });*/
//                requestData();

                break;
            case R.id.ll_style:
                if ("list".equals(type)) {
                    type = "gird";
                    iv_style.setImageResource(R.mipmap.list_piece);
                    setGridAdapter();
                } else {
                    type = "list";
                    iv_style.setImageResource(R.mipmap.list_grid);
                    setListAdapter();
                }
                break;
            case R.id.tv_ok: // 点击筛选完成
                mDrawerlayout.closeDrawer(Gravity.RIGHT);

                break;

        }
    }


    private void setGridAdapter() {
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        lRecyclerview.setLayoutManager(gridLayoutManager);
        adapter.setType("grid");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        lRecyclerview.getLayoutManager().scrollToPosition(position);
        initLoadMoreListener();
        initItemClickListener();
    }

    private void setListAdapter() {
        int position = gridLayoutManager.findFirstVisibleItemPosition();
        lRecyclerview.setLayoutManager(linearLayoutManager);
        adapter.setType("list");
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        lRecyclerview.getLayoutManager().scrollToPosition(position);
        initLoadMoreListener();
        initItemClickListener();
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                curpage++;
                requestData();
            }
        });
    }

    private void initItemClickListener() {
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, GoodInfoActivity.class);
                intent.putExtra("goods_id", adapter.getDataList().get(position).goods_id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(SearchShopResultActivity.this);
            }

        });
    }

    Subscription subscribe;

    private void requestData() {

        subscribe = ShopMallNetwork
                .getShopMallMainApi()
                .getSearchGoodsResponse(key, order, price_from, price_to, b_id, (String[]) service.toArray(new String[service.size()]), keyword, curpage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<StoreDetailAllResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(15);
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                        if (curpage != 1)
                            curpage--;
                    }

                    @Override
                    public void onNext(StoreDetailAllResponse response) {
                        lRecyclerview.refreshComplete(15);
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (curpage == 1) {
                                    mDatas = response.data.goods_list;
                                    adapter.setDataList(response.data.goods_list);
                                    if (adapter.getDataList().size() == 0) {
                                    }
                                } else {
                                    mDatas.addAll(response.data.goods_list);
                                    adapter.addAll(response.data.goods_list);
                                }
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

    @Override
    public void onBackPressed() {
        if(mDrawerlayout.isDrawerOpen(Gravity.RIGHT)){
            mDrawerlayout.closeDrawer(Gravity.RIGHT);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
        ActivitySlideAnim.slideOutAnim(this);
    }
}

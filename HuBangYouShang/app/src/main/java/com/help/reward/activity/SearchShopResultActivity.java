package com.help.reward.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.base.recyclerview.OnLoadMoreListener;
import com.help.reward.R;
import com.help.reward.adapter.GoodsSearchBrandAdapter;
import com.help.reward.adapter.SearchStoreAdapter;
import com.help.reward.adapter.StoreGoodsAdapter;
import com.help.reward.bean.BrandBean;
import com.help.reward.bean.Response.BrandResponse;
import com.help.reward.bean.Response.SearchStoreResponse;
import com.help.reward.bean.Response.StoreDetailAllResponse;
import com.help.reward.bean.SearchStoreInfoBean;
import com.help.reward.bean.ShopMallHotBean;
import com.help.reward.network.ShopMallNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.StringUtils;
import com.help.reward.view.GoodsTypeSearchPop;
import com.help.reward.view.MyGridView;
import com.help.reward.view.MyProcessDialog;
import com.help.reward.view.SearchEditTextView;
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
    @BindView(R.id.tv_shaixuan)
    TextView tv_shaixuan;
    @BindView(R.id.iv_style)
    ImageView iv_style;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    @BindView(R.id.id_store_recycler_view)
    LRecyclerView lStoreRecyclerview;


    @BindView(R.id.tv_reset)
    TextView tv_reset;
    @BindView(R.id.tv_ok)
    TextView tv_ok;
    @BindView(R.id.myGridView)
    MyGridView myGridView;


    @BindView(R.id.tv_freight)
    TextView tv_freight;
    @BindView(R.id.tv_cod)
    TextView tv_cod;
    @BindView(R.id.tv_refund)
    TextView tv_refund;
    @BindView(R.id.tv_protection)
    TextView tv_protection;
    @BindView(R.id.tv_quality)
    TextView tv_quality;
    @BindView(R.id.tv_sevenDay)
    TextView tv_sevenDay;

    @BindView(R.id.et_priceform)
    EditText et_priceform;
    @BindView(R.id.et_priceto)
    EditText et_priceto;

    GoodsSearchBrandAdapter brandAdapter;


    private StoreGoodsAdapter adapter;
    private SearchStoreAdapter storeAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private LRecyclerViewAdapter storeLRecyclerViewAdapter = null;
    List<ShopMallHotBean> mDatas = new ArrayList<>();
    String store_id;
    int curpage = 1;

    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    String keyword;
    String searchType = "goods";
    String key;//salenum 销量 clicknum 人气 price 价格order desc降序asc升序；默认降序
    String price_from, price_to;
    String b_id = "";// 品牌id
    List<String> service = new ArrayList<>();//freight 包邮 COD 货到付款 refund 急速退款 protection 消费者保障quality 正品保障 sevenDay 7天无理由退货
    String order;
    String type = "gird";
    String zongheType = "zonghe";
    List<BrandBean> brandList = new ArrayList<>();
    List<SearchStoreInfoBean> storeList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop_result);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            keyword = bundle.getString("keyword");
            searchType = bundle.getString("searchType", "goods"); // 店铺 store
        }

        initView();
        initData();
    }

    void initView() {
        iv_title_back.setVisibility(View.VISIBLE);
        tv_text.setVisibility(View.GONE);
        et_search.setHint("搜索关键字相关商品");
        et_search.setText(keyword);
        et_search.setOnKeyListener(new SearchEditTextView.onKeyListener() {
            @Override
            public void onKey() {
                if (!StringUtils.checkStr(et_search.getText().toString().trim())) {
                    return;
                }
                keyword = et_search.getText().toString().trim();
                if ("商品".equals(iv_search_type.getText().toString())) {
                    key = "";
                    order = "";
                    zongheType = "zonghe";
                    requestData(true);
                } else {
                    //搜索店铺
                    requestStoreData(true);

                }
            }
        });
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= brandList.size()) {
                    return;
                }
                if (b_id.equals(brandList.get(i).brand_id)) {
                    b_id = "";
                    brandAdapter.setB_id(b_id);
                } else {
                    b_id = brandList.get(i).brand_id;
                    brandAdapter.setB_id(b_id);
                }
            }
        });
    }

    private void initData() {
        brandAdapter = new GoodsSearchBrandAdapter(this);
        brandAdapter.setDatas(brandList);
        myGridView.setAdapter(brandAdapter);


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


        storeAdapter = new SearchStoreAdapter(this);
        storeAdapter.setDataList(storeList);
        lStoreRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        storeLRecyclerViewAdapter = new LRecyclerViewAdapter(storeAdapter);
        lStoreRecyclerview.setAdapter(storeLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lStoreRecyclerview.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        lStoreRecyclerview.setLoadMoreEnabled(false);
        lStoreRecyclerview.setItemAnimator(new DefaultItemAnimator());
        initLoadMoreListener();
        initItemClickListener();
        initBrandData();
        if (!"goods".equals(searchType)) {
            et_search.setHint("搜索关键字相关店铺");
            iv_search_type.setText("店铺");
            layout_alltitle.setVisibility(View.GONE);
            lRecyclerview.setVisibility(View.GONE);
            lStoreRecyclerview.setVisibility(View.VISIBLE);
            requestStoreData(true);
        }else{
            requestData(true);
        }




    }

    @OnClick({R.id.iv_title_back, R.id.iv_search_type, R.id.ll_zonghe,
            R.id.tv_salenum, R.id.ll_shaixuan, R.id.ll_style,
            R.id.tv_ok, R.id.tv_reset, R.id.iv_showAll, R.id.tv_freight, R.id.tv_cod, R.id.tv_refund, R.id.tv_protection, R.id.tv_quality, R.id.tv_sevenDay})
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
                            iv_search_type.setText("商品");
                            layout_alltitle.setVisibility(View.VISIBLE);
                            mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //打开手势滑动
                        } else {
                            et_search.setHint("搜索关键字相关店铺");
                            iv_search_type.setText("店铺");
                            layout_alltitle.setVisibility(View.GONE);
                            mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
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
                        } else if ("priceasc".equals(type)) {
                            key = "price";
                            order = "asc";
                        } else if ("clicknum".equals(type)) {
                            key = "clicknum";
                            order = "desc";
                        } else {
                            key = "";
                            order = "";
                        }
                        requestData(true);
                    }
                });
                break;
            case R.id.tv_salenum:
                key = "salenum";
                if ("asc".equals(order)) {
                    order = "desc";
                } else {
                    order = "asc";
                }
                requestData(true);
                break;
            case R.id.ll_shaixuan:
                if (brandList == null || brandList.size() == 0) {
                    initBrandData();
                }
                mDrawerlayout.openDrawer(Gravity.RIGHT);

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
                String from = et_priceform.getText().toString().trim();
                String to = et_priceto.getText().toString().trim();
                if (StringUtils.checkStr(from) || StringUtils.checkStr(to)) {
                    if (!StringUtils.checkStr(from)) {
                        ToastUtils.show(this, "请输入最低价格");
                        return;
                    }
                    if (!StringUtils.checkStr(to)) {
                        ToastUtils.show(this, "请输入最高价格");
                        return;
                    }
                    if (Integer.parseInt(to) <= Integer.parseInt(from)) {
                        ToastUtils.show(this, "最高价格需大于最低价格");
                        return;
                    }
                    price_from = from;
                    price_to = to;
                }
                mDrawerlayout.closeDrawer(Gravity.RIGHT);
                if (!StringUtils.checkStr(et_search.getText().toString().trim())) {
                    return;
                }
                keyword = et_search.getText().toString().trim();
                requestData(true);
                break;
            case R.id.tv_reset:
                b_id = "";
                price_from = "";
                price_to = "";
                service.clear();
                brandAdapter.setB_id(b_id);
                et_priceform.setText("");
                et_priceto.setText("");
                setSelected(tv_freight, service.contains("freight"));
                setSelected(tv_cod, service.contains("COD"));
                setSelected(tv_refund, service.contains("refund"));
                setSelected(tv_protection, service.contains("protection"));
                setSelected(tv_quality, service.contains("quality"));
                setSelected(tv_sevenDay, service.contains("sevenDay"));
                break;
            case R.id.iv_showAll:
                brandAdapter.setShowAll();
                break;
            case R.id.tv_freight:
                clickService(tv_freight, "freight");
                break;
            case R.id.tv_cod:
                clickService(tv_cod, "COD");
                break;
            case R.id.tv_refund:
                clickService(tv_refund, "refund");
                break;
            case R.id.tv_protection:
                clickService(tv_protection, "protection");
                break;
            case R.id.tv_quality:
                clickService(tv_quality, "quality");
                break;
            case R.id.tv_sevenDay:
                clickService(tv_sevenDay, "sevenDay");
                break;
        }
    }

    private void clickService(TextView textView, String type) {
        if (service.contains(type)) {
            service.remove(type);
        } else {
            service.add(type);
        }
        setSelected(textView, service.contains(type));
    }

    private void setSelected(TextView textView, boolean isSelected) {
        if (isSelected) {
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setBackgroundResource(R.drawable.fa372d_bg);
        } else {
            textView.setTextColor(Color.parseColor("#3a4a6b"));
            textView.setBackgroundResource(R.drawable.dcdcdc_f8f8f9_bg);
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
                requestData(false);
            }
        });
        lStoreRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                curpage++;
                //搜索店铺
                requestStoreData(false);
            }
        });
    }

    private void initItemClickListener() {
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, GoodInfoActivity.class);
                intent.putExtra("goods_id", adapter.getDataList().get(position).goods_id);
                intent.putExtra("store_id", adapter.getDataList().get(position).store_id);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(SearchShopResultActivity.this);
            }

        });

    }

    Subscription subscribe;

    private void requestData(boolean isFirst) {
        if (isFirst) {
            curpage = 1;
            MyProcessDialog.showDialog(this);
        }

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
                                    lStoreRecyclerview.setVisibility(View.GONE);
                                    lRecyclerview.setVisibility(View.VISIBLE);
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

    private void requestStoreData(boolean isFirst) {
        if (isFirst) {
            curpage = 1;
            MyProcessDialog.showDialog(this);
        }

        subscribe = ShopMallNetwork
                .getShopMallMainApi()
                .getSearchStoreResponse(keyword, curpage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<SearchStoreResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lStoreRecyclerview.refreshComplete(15);
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                        if (curpage != 1)
                            curpage--;
                    }

                    @Override
                    public void onNext(SearchStoreResponse response) {
                        lStoreRecyclerview.refreshComplete(15);
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (curpage == 1) {
                                    lRecyclerview.setVisibility(View.GONE);
                                    lStoreRecyclerview.setVisibility(View.VISIBLE);
                                    storeList = response.data.list;
                                    storeAdapter.setDataList(response.data.list);
                                    if (adapter.getDataList().size() == 0) {
                                    }
                                } else {
                                    storeList.addAll(response.data.list);
                                    storeAdapter.addAll(response.data.list);
                                }
                            }
                            if (!response.hasmore) {
                                lStoreRecyclerview.setLoadMoreEnabled(false);
                            } else {
                                lStoreRecyclerview.setLoadMoreEnabled(true);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }

    /**
     * 品牌
     */
    private void initBrandData() {

        subscribe = ShopMallNetwork
                .getShopMallMainApi()
                .getBrandBeanResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BrandResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(BrandResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                brandList = response.data.brand_list;
                                brandAdapter.setDatas(brandList);
                            }
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (mDrawerlayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerlayout.closeDrawer(Gravity.RIGHT);
        } else {
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

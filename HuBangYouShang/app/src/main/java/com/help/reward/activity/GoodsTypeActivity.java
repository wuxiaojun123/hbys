package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.help.reward.R;
import com.help.reward.adapter.GoodsSecondTypeAdapter;
import com.help.reward.adapter.GoodsTypeAdapter;
import com.help.reward.bean.GoodsTypeBean;
import com.help.reward.bean.Response.GoodsSecondTypeResponse;
import com.help.reward.bean.Response.GoodsTypeResponse;
import com.help.reward.network.ShopMallNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.GoodsTypeSearchPop;
import com.help.reward.view.MyProcessDialog;
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
 * 商品分类列表
 */

public class GoodsTypeActivity extends BaseActivity {
    protected Subscription subscribe;

    @BindView(R.id.lv_types)
    LRecyclerView lRecyclerview;
    @BindView(R.id.lv_secondtype)
    ListView lv_secondtype;

    @BindView(R.id.et_shop_search)
    EditText et_shop_search;
    @BindView(R.id.tv_title_left)
    TextView tv_title_left;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    private GoodsTypeAdapter adapter;
    public List<GoodsTypeBean> mDatas = new ArrayList<>();
    LRecyclerViewAdapter mLRecyclerViewAdapter;
    GoodsSecondTypeAdapter goodsSecondTypeAdapter;
    String searchType = "goods";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodstype);
        ButterKnife.bind(this);
        initView();
        initRecycler();
        MyProcessDialog.showDialog(mContext);
        requestData();
    }

    void initView() {
        et_shop_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        if ("goods".equals(searchType)) {

                        } else {

                        }
                        ToastUtils.show(mContext, et_shop_search.getText().toString());
                        return true;
                    }
                }
                return false;
            }
        });
        et_shop_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
                if (!TextUtils.isEmpty(text)) {
                    iv_clear.setVisibility(View.VISIBLE);
                } else {
                    iv_clear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GoodsTypeAdapter(this);
        adapter.setDataList(mDatas);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        initItemClickListener();
        goodsSecondTypeAdapter = new GoodsSecondTypeAdapter(this);
        lv_secondtype.setAdapter(goodsSecondTypeAdapter);
    }

    private void initItemClickListener() {
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!adapter.getDataList().get(position).isSelect) {
                    for (int i = 0; i < adapter.getDataList().size(); i++) {
                        adapter.getDataList().get(i).isSelect = false;
                    }
                    adapter.getDataList().get(position).isSelect = true;
                    adapter.notifyDataSetChanged();
                    requestSecondData(adapter.getDataList().get(position).gc_id);
                }

            }

        });
    }


    @OnClick({R.id.tv_title_right, R.id.tv_title_left, R.id.iv_clear})
    void click(View v) {
        switch (v.getId()) {
            case R.id.tv_title_right:
                finish();
                break;
            case R.id.tv_title_left:
                new GoodsTypeSearchPop().showPopupWindow(this, tv_title_left).setOnTypeChooseListener(new GoodsTypeSearchPop.OnTypeChooseListener() {
                    @Override
                    public void onType(String type) {
                        searchType = type;
                        if("goods".equals(type)) {
                            et_shop_search.setHint("搜索关键字相关商品");
                            tv_title_left.setText("商品");
                        }else{
                            et_shop_search.setHint("搜索关键字相关店铺");
                            tv_title_left.setText("店铺");
                        }
                    }
                });
                break;
            case R.id.iv_clear:
                et_shop_search.setText("");
                break;

        }
    }

    private void requestData() {
        subscribe = ShopMallNetwork
                .getShopMallMainApi()
                .getGoodClassResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<GoodsTypeResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(GoodsTypeResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                response.data.class_list.get(0).isSelect = true;
                                adapter.setDataList(response.data.class_list);
                                requestSecondData(response.data.class_list.get(0).gc_id);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void requestSecondData(String gc_id) {
        MyProcessDialog.showDialog(this);
        subscribe = ShopMallNetwork
                .getShopMallMainApi()
                .getGoodSecondClassResponse(gc_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<GoodsSecondTypeResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(GoodsSecondTypeResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                goodsSecondTypeAdapter.setmDatas(response.data.class_list);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
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

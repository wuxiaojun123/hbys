package com.help.reward.activity;

import android.content.Intent;
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
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.CartInfoBean;
import com.help.reward.bean.Response.MyOrderResponse;
import com.help.reward.bean.Response.ShopCartResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ADBrian on 26/03/2017.
 */

public class ShopcartActivity extends BaseActivity implements ExpandShopcartAdapter.ShopCartOperateListener,View.OnClickListener {


    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.id_recycler_view)
    ExpandableListView lRecyclerview;

    @BindView(R.id.iv_all_check)
    ImageView mIvAll;
    @BindView(R.id.tv_total)
    TextView mTvtotal;

    private ExpandShopcartAdapter mAdapter;

    private List<CartInfoBean.GoodInfoBean> mSelected = new ArrayList<CartInfoBean.GoodInfoBean>();
    private List<CartInfoBean> cart_list = new ArrayList<CartInfoBean>();

    boolean isAll = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcart);
        ButterKnife.bind(this);

        initview();

        //initNetwork();

        initNetwork();
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

        mAdapter = new ExpandShopcartAdapter(mSelected,this,cart_list,lRecyclerview,this);
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


    private void initNetwork() {
        MyProcessDialog.showDialog(mContext);
        ShopcartNetwork.getShopcartCookieApi().getShopcartList(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ShopCartResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(ShopCartResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                if (response.data.cart_list != null) {
                                    cart_list.addAll(response.data.cart_list);
                                    mAdapter.notifyDataSetChanged();
                                    //expandChild();
                                }
                            }

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }

    /**
     *
     * @param goodInfo 删除和编辑数量必须传入
     * @param action 操作
     * @param num 编辑数量必须传入
     */
    @Override
    public void operate(CartInfoBean.GoodInfoBean goodInfo, int action,int num) {

        if (action == ExpandShopcartAdapter.SHOPCART_DELETED) {
            if (goodInfo != null) {
                deleteRequest(goodInfo);
            }


        } else if (action == ExpandShopcartAdapter.SHOPCART_SELECTED) {
            if(judgeIsAll()){
                isAll = true;
                mIvAll.setImageResource(R.mipmap.img_address_checkbox_checked);
            } else {
                isAll = false;
                mIvAll.setImageResource(R.mipmap.img_address_checkbox);
            }
        } else if (action == ExpandShopcartAdapter.SHOPCART_NUM_EDIT) {
            editNumRequest(goodInfo,num);
        }
        calculate();
    }

    private void editNumRequest(final CartInfoBean.GoodInfoBean goodInfo, final int num) {
        MyProcessDialog.showDialog(mContext);
        ShopcartNetwork.getShopcartCookieApi().getShopcartEdit(App.APP_CLIENT_KEY,goodInfo.cart_id,num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            goodInfo.goods_num = num +"";
                            calculate();
                            mAdapter.notifyDataSetChanged();

                            //if (extend_order_goods.size() > 1){
                            //    mDataList.get(groupPosition).goods.remove(childPosition);
                            //} else {
                            //    mDataList.remove(groupPosition);
                            //}

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void deleteRequest(final CartInfoBean.GoodInfoBean good_info) {
        MyProcessDialog.showDialog(mContext);
        ShopcartNetwork.getShopcartCookieApi().getShopcartDelete(App.APP_CLIENT_KEY,good_info.cart_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            for (int i = 0; i< cart_list.size();i++) {
                                CartInfoBean cartInfo = cart_list.get(i);
                                if (cartInfo.goods!= null) {
                                    if (cartInfo.goods.contains(good_info)) {
                                        if (cartInfo.goods.size() > 1) {
                                            cartInfo.goods.remove(good_info);
                                        } else {
                                            cart_list.remove(cartInfo);
                                        }
                                    }
                                }
                            }

                            if (mSelected.contains(good_info)) {
                                mSelected.remove(good_info);
                            }
                            calculate();
                            mAdapter.notifyDataSetChanged();

                            //if (extend_order_goods.size() > 1){
                            //    mDataList.get(groupPosition).goods.remove(childPosition);
                            //} else {
                            //    mDataList.remove(groupPosition);
                            //}

                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private boolean judgeIsAll() {
        if (cart_list.isEmpty()){
            return false;
        }

        if (mSelected.isEmpty()) {
            return false;
        }

        for (CartInfoBean cartInfo:cart_list) {
            if (cartInfo.goods!= null) {
                for (CartInfoBean.GoodInfoBean goodBean:
                     cartInfo.goods) {
                    if (!mSelected.contains(goodBean)) {
                        return  false;
                    }
                }
            }
        }
        return true;
    }

    private  void  addAll(){
        mSelected.clear();
        for (CartInfoBean cartInfo:cart_list) {
            if (cartInfo.goods!= null) {
               mSelected.addAll(cartInfo.goods);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public static String formatPrice2BlankToBlank ( String str ){
        DecimalFormat df = new DecimalFormat ( "###,##0.00" ) ;
        return df.format ( Double.parseDouble( str ) ) ;
    }

    private void calculate() {
        String result = "0";
        if (!mSelected.isEmpty()) {
            for (CartInfoBean.GoodInfoBean goodInfo:
            mSelected) {
                try {
                    result = new BigDecimal(result).add(new BigDecimal(goodInfo.goods_price).multiply(new BigDecimal(goodInfo.goods_num))).toString();
                }catch (Exception e){
                    e.printStackTrace();
                    ToastUtils.show(ShopcartActivity.this,"error num");
                }
            }
        }
        mTvtotal.setText("合计： ¥ "+formatPrice2BlankToBlank(result));
    }

    @OnClick({R.id.ll_select_all,R.id.commit,R.id.iv_title_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_select_all:

                if (isAll) {
                    isAll = false;
                    mIvAll.setImageResource(R.mipmap.img_address_checkbox);
                    mSelected.clear();
                }else {
                    isAll = true;
                    mIvAll.setImageResource(R.mipmap.img_address_checkbox_checked);
                    addAll();
                }
                calculate();
                mAdapter.notifyDataSetChanged();

            break;
            case R.id.commit:
                
                if (!mSelected.isEmpty()) {
                    StringBuffer sb = new StringBuffer();
                    for (int i=0;i < mSelected.size();i++) {
                        CartInfoBean.GoodInfoBean goodInfoBean = mSelected.get(i);
                        sb.append(goodInfoBean.cart_id+"|"+goodInfoBean.goods_num);
                        sb.append(",");
                    }

                    Intent intent = new Intent(ShopcartActivity.this, ConfirmOrderActivity.class);
                    intent.putExtra("cart_id", sb.toString().substring(0,sb.length()-1));
                    intent.putExtra("if_cart","1");
                    startActivity(intent);
                }
                //startActivity(new Intent(ShopcartActivity.this,ConfirmOrderActivity.class));

                break;
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(ShopcartActivity.this);

                break;
        }
    }
}

package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.PropertyAdapter;
import com.help.reward.bean.GoodSpecBean;
import com.help.reward.bean.GoodsInfoBean;
import com.help.reward.bean.PropertyBean;
import com.help.reward.bean.PropertyValueBean;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.LogUtils;
import com.idotools.utils.MobileScreenUtils;
import com.idotools.utils.ToastUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ADBrian on 15/04/2017.
 */

public class GoodPropertyActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_good_pic)
    ImageView mIvPic;

    @BindView(R.id.tv_good_price)
    TextView mTvPrice;

    @BindView(R.id.tv_good_num)
    TextView mTvNum;

    @BindView(R.id.tv_good_pro)
    TextView mTvProtip;

    @BindView(R.id.lv_property)
    ListView mList;

    @BindView(R.id.ib_dialog_number_add)
    ImageButton mNumAdd;

    @BindView(R.id.ib_dialog_number_des)
    ImageButton mNumDes;

    @BindView(R.id.et_dialog_number_show)
    EditText mNumShow;

    private PropertyBean propertyBean;
    private int numShow = 1;
    private int Max_num = 99;
    private String goodsId;
    private PropertyAdapter propertyAdapter; // 属性适配器
    private ArrayList<GoodSpecBean> spec_all_goods; // 商品规格

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int paddingHeight = MobileScreenUtils.getNavigationBarHeight(mContext);
        //窗口对齐屏幕宽度
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, paddingHeight);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
        setFinishOnTouchOutside(true);
        setContentView(R.layout.activity_good_property);
        ButterKnife.bind(this);

        propertyBean = (PropertyBean) getIntent().getSerializableExtra("goods_property");
        spec_all_goods = getIntent().getParcelableArrayListExtra("spec_all_goods");
        goodsId = propertyBean.getGoods_id();
        initView();
    }

    @Override
    protected void setStatusBar() {
        // 不需要状态栏变色
    }

    private void initView() {
        propertyAdapter = new PropertyAdapter(GoodPropertyActivity.this);
        mList.setAdapter(propertyAdapter);
        if (propertyBean != null) {
            bindview();
            mTvProtip.setText(propertyBean.getTip());
            propertyAdapter.setList(propertyBean.getPropertyList());
        }
        initEvent();
        setButtonState(numShow, mNumAdd, mNumDes);
        mNumShow.setText(numShow + "");

        propertyAdapter.setPropertyOnClickListener(new PropertyAdapter.PropertyOnClickListener() {
            @Override
            public void onPropertyClickListener() {
                selectedSpec();
            }
        });

        setSelectProMap();
    }

    private void initEvent() {
        mNumAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newNum = numShow + 1;
                setButtonState(newNum, mNumAdd, mNumDes);
                mNumShow.setText(numShow + "");
                mNumShow.setSelection(Integer.toString(numShow).length());
            }
        });

        mNumDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newNum = numShow - 1;
                setButtonState(newNum, mNumAdd, mNumDes);
                mNumShow.setText(numShow + "");
                mNumShow.setSelection(Integer.toString(numShow).length());

            }
        });

        mNumShow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int newNum = -1;
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    newNum = Integer.parseInt(s.toString().trim());
                    if (newNum > Max_num) {
                        mNumShow.setText(Max_num + "");
                    }
                } else {
                    newNum = 1;
                }
                setButtonState(newNum, mNumAdd, mNumDes);
            }
        });
    }

    private void setSelectProMap() {
        HashMap<Integer, GoodsInfoBean.PropertyValueInfo> selectProMap = new HashMap<>();
        if (spec_all_goods != null) {
            int size = spec_all_goods.size();
            for (int j = 0; j < size; j++) {
                GoodSpecBean goodSpecBean = spec_all_goods.get(j);
                if (propertyBean.getGoods_id().equals(goodSpecBean.goods_id)) {// 商品id相等
                    String specStr = goodSpecBean.spec_str; // 这是商品规格
                    // 根据商品规格 一般是111|222这种格式，然后遍历属性集合
                    List<PropertyValueBean> mList = propertyBean.getPropertyList();
                    for (int i = 0; i < mList.size(); i++) {
                        List<GoodsInfoBean.PropertyValueInfo> propertyValueInfoList = mList.get(i).getPropertyChildList();
                        for (int z = 0; z < propertyValueInfoList.size(); z++) {
                            String specValueId = propertyValueInfoList.get(z).spec_value_id + "";
                            if (specStr.contains(specValueId)) {
//                                LogUtils.e("选择的属性值是" + specValueId + "--" + propertyValueInfoList.get(z).spec_value_name);
                                selectProMap.put(mList.get(i).getProperty_parent_id(), propertyValueInfoList.get(z));
                            }
                        }
                    }
                    break;
                }
            }
        }
        propertyAdapter.setSelectProMap(selectProMap);
    }

    private void selectedSpec() {
        HashMap<Integer, GoodsInfoBean.PropertyValueInfo> map = propertyAdapter.getSelectProMap();
        if (map != null) {
            StringBuilder sb;
            if (propertyBean != null && propertyBean.getPropertyList() != null) {
                sb = new StringBuilder();
                List<PropertyValueBean> mList = propertyBean.getPropertyList();
                for (int i = 0; i < mList.size(); i++) {
                    int parentId = mList.get(i).getProperty_parent_id();
//                    if (parentId != 0) {
                    GoodsInfoBean.PropertyValueInfo info = map.get(parentId);
                    if (info != null) {
                        sb.append(info.spec_value_id + "|");
                    }
//                    }
                }
                if (sb != null) {
                    String specValue = sb.toString();
                    specValue = specValue.substring(0, specValue.length() - 1);
//                    LogUtils.e("选择的规格是:" + specValue);
                    // 根据规格查询对应的商品id
                    if (spec_all_goods != null) {
                        int size = spec_all_goods.size();
                        for (int j = 0; j < size; j++) {
                            GoodSpecBean goodSpecBean = spec_all_goods.get(j);
                            if (specValue.equals(goodSpecBean.spec_str)) {
                                goodsId = goodSpecBean.goods_id;
                                propertyBean.setGoods_id(goodSpecBean.goods_id);
                                propertyBean.setGoods_name(goodSpecBean.goods_name);
                                propertyBean.setGoods_num(goodSpecBean.goods_storage);
                                propertyBean.setGoods_price(goodSpecBean.goods_price);
                                propertyBean.setGoods_pic(goodSpecBean.spec_image);
                                propertyBean.goods_img = goodSpecBean.goods_image;
                                propertyBean.goods_marketprice = goodSpecBean.goods_marketprice;
                                propertyBean.goods_salenum = goodSpecBean.goods_salenum;
                                bindview();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void bindview() {
        GlideUtils.loadImage(propertyBean.getGoods_pic(), mIvPic);
        mTvNum.setText("库存" + propertyBean.getGoods_num());
        mTvPrice.setText("¥ " + propertyBean.getGoods_price());
    }

    private void setButtonState(int num, ImageButton mIbNumAdd, ImageButton mIbNumDes) {
        if (num <= 1) {
            numShow = 1;
            mIbNumAdd.setEnabled(true);
            mIbNumDes.setEnabled(false);
        } else {
            if (num >= Max_num) {
                numShow = Max_num;
                mIbNumAdd.setEnabled(false);
                mIbNumDes.setEnabled(true);
            } else {
                numShow = num;
                mIbNumAdd.setEnabled(true);
                mIbNumDes.setEnabled(true);
            }
        }
    }

    @OnClick({R.id.btn_close, R.id.tv_goodinfo_shopcart_add, R.id.tv_goodinfo_buy})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                this.finish();
                break;
            case R.id.tv_goodinfo_shopcart_add:
                addToShopcart();
                break;
            case R.id.tv_goodinfo_buy:
                Intent intent = new Intent(GoodPropertyActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("cart_id", goodsId + "|" + numShow);
                intent.putExtra("if_cart", "0");
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(GoodPropertyActivity.this);
                finish();

                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        propertyBean.setSelectNum(numShow + "");
        RxBus.getDefault().post(propertyBean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void addToShopcart() {
        if (App.APP_CLIENT_KEY == null) {
            ToastUtils.show(mContext, R.string.string_please_login);
            return;
        }
        // 得判断当前数量是否大于库存量
        if (numShow <= 0) {
            ToastUtils.show(mContext, "商品数量必须大于0");
            return;
        }
        String goods_num = propertyBean.getGoods_num();
        int goodsNum = Integer.parseInt(goods_num);
        if (numShow > goodsNum) {
            ToastUtils.show(mContext, "商品数量不能大于库存量");
            return;
        }
//        goodsId = propertyBean.getGoods_id();
        if (!TextUtils.isEmpty(goodsId)) {
            MyProcessDialog.showDialog(mContext);
            ShopcartNetwork.getShopcartCookieApi().getShopcartAdd(App.APP_CLIENT_KEY, goodsId, numShow + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse>() {

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
                        public void onNext(BaseResponse baseResponse) {
                            MyProcessDialog.closeDialog();
                            if (baseResponse.code == 200) {
                                ToastUtils.show(mContext, "加入购物车成功");
                                finish();
                            } else {
                                ToastUtils.show(mContext, baseResponse.msg);
                            }
                        }
                    });
        }
    }


}

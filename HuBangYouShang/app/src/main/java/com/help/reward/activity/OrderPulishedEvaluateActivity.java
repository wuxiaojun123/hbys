package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.OrderPulishedEvaluateAdapter;
import com.help.reward.bean.OrderInfoBean;
import com.help.reward.bean.OrderPulishedEvaluateBean;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.OrderInfoResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.BooleanRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.ChooseCameraPopuUtils;
import com.help.reward.utils.GlideUtils;
import com.help.reward.utils.JsonUtils;
import com.help.reward.view.AlertDialog;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 发表评价
 * <p>
 * item_order_pulished_evaluate
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class OrderPulishedEvaluateActivity extends BaseActivity implements View.OnClickListener, OrderPulishedEvaluateAdapter.OnAddPhotoListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;
    @BindView(R.id.id_logistics_rating_bar)
    RatingBar id_logistics_rating_bar; // 物流服务
    @BindView(R.id.id_service_rating_bar)
    RatingBar id_service_rating_bar; // 服务态度

    private OrderPulishedEvaluateAdapter mAdapter;
    private String[] goods_id;
    private String[] goods_img;
    private String[] goods_name;
    private String[] rec_id;
    private List<OrderPulishedEvaluateBean> mList = new ArrayList<>();
    ChooseCameraPopuUtils chooseCameraPopuUtils;
    private int currentPosition; // 点击的当前添加评论照片的item
    private String order_id; // 订单id
    private String store_desccredit; // 描述服务
    private String store_deliverycredit; // 物流服务
    private String store_servicecredit; // 态度服务

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pulished_evaluate);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        id_logistics_rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // 物流服务
                store_deliverycredit = rating + "";
            }
        });
        id_service_rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // 服务态度
                store_servicecredit = rating + "";
            }
        });
    }

    private void initData() {
        Intent mIntent = getIntent();
        order_id = mIntent.getStringExtra("order_id");
        goods_id = mIntent.getStringArrayExtra("goods_id");
        goods_img = mIntent.getStringArrayExtra("goods_img");
        goods_name = mIntent.getStringArrayExtra("goods_name");
        rec_id = mIntent.getStringArrayExtra("rec_id");
        if (goods_id != null) {
            int length = goods_id.length;
            LogUtils.e("评价页面的商品个数是：" + length);
            for (int i = 0; i < length; i++) {
                OrderPulishedEvaluateBean bean = new OrderPulishedEvaluateBean();
                bean.goods_id = goods_id[i];
                bean.goods_img = goods_img[i];
                bean.goods_name = goods_name[i];
                bean.rec_id = rec_id[i];
                bean.evaluate_images = new ArrayList<>();
                bean.file_names = new ArrayList<>();
                mList.add(bean);
            }
        }
        mAdapter.setDataList(mList);
    }

    private void initView() {
        tv_title.setText(R.string.string_order_evaluate_title);
        tv_title_right.setVisibility(View.GONE);
        initRecyclerView();
        chooseCameraPopuUtils = new ChooseCameraPopuUtils(this, "evaluate");
        chooseCameraPopuUtils.setOnUploadImageListener(new ChooseCameraPopuUtils.OnUploadImageListener() {
            @Override
            public void onLoadError() {
                ToastUtils.show(mContext, "获取图片失败");
            }

            @Override
            public void onLoadSucced(String file_name, String url) {
                mAdapter.setPhotoImageView(currentPosition, file_name, url);
            }
        });
        mAdapter.setOnAddPhotoListener(this);
    }


    private void initRecyclerView() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new OrderPulishedEvaluateAdapter(mContext);
        LRecyclerViewAdapter mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setPullRefreshEnabled(false);
    }

    @OnClick({R.id.iv_title_back, R.id.id_commit_evaluate})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(OrderPulishedEvaluateActivity.this);

                break;
            case R.id.id_commit_evaluate: // 评价提交
                commit();

                break;
        }
    }

    /***
     * 评价提交
     * 1:内容和评分都写了
     * 2:内容写了，评分没写
     * 3:内容没写，评分写了
     * 4:内容和评分都没写
     */
    private void commit() {
        if (TextUtils.isEmpty(order_id)) {
            ToastUtils.show(mContext, "没有这个订单");
            return;
        }
        String evaluate = null;
//        boolean flag = true;
        float ratingAverage = 0;
        int ratingAcount = 0;
        Map<String, OrderPulishedEvaluateBean> evalueateMap = new HashMap<>();
        if (mList != null) {
            for (OrderPulishedEvaluateBean bean : mList) {
                if (bean.comment != null) {
                    if (bean.score == null) {
                        ToastUtils.show(mContext, "请给" + bean.goods_name + "商品评分");
                        return;
                    }
                }
                if (bean.score != null) {
                    if (bean.comment == null) {
                        ToastUtils.show(mContext, "请填写" + bean.goods_name + "商品的购买体会");
                        return;
                    }
                }
                if (bean.score != null && bean.comment != null) {
                    evalueateMap.put(bean.rec_id, bean);
                    ratingAverage += Float.parseFloat(bean.score);
                    ratingAcount++;
                }
            }
        }
        if (evalueateMap == null) {
            ToastUtils.show(mContext, "至少评价一个商品");
            return;
        }
        if (TextUtils.isEmpty(store_deliverycredit)) {
            ToastUtils.show(mContext, "请评价物流服务");
            return;
        }
        if (TextUtils.isEmpty(store_servicecredit)) {
            ToastUtils.show(mContext, "请评价服务态度");
            return;
        }
        if (ratingAverage != 0) {
            store_desccredit = (ratingAverage / ratingAcount) + "";
        }
        // Map<String, OrderPulishedEvaluateBean>
        Map<String, Object> map = new HashMap<>();
        map.put("goods", evalueateMap);
        map.put("store_desccredit", store_desccredit); // 描述=每个商品的描述取平均值
        map.put("store_deliverycredit", store_deliverycredit); // 物流
        map.put("store_servicecredit", store_servicecredit); // 服务

        evaluate = JsonUtils.toJsonFromMap(map);
        LogUtils.e("转成json的格式是：" + evaluate);
        MyProcessDialog.showDialog(mContext, "正在提交...");
        PersonalNetwork
                .getResponseApi()
                .getEvaluateResponse(order_id, evaluate, App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<String>>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BaseResponse<String> response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            LogUtils.e("返回的数据是:" + response.data);
                            if (response.data != null) { // 显示数据
                                showMyDialog(response.msg);
                                // 发送更新数据
                                RxBus.getDefault().post(new BooleanRxbusType(true));
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /***
     * 评价成功
     *
     * @param msg
     */
    private void showMyDialog(String msg) {
        new AlertDialog(OrderPulishedEvaluateActivity.this).builder()
                .setTitle(R.string.exit_title).setMsg(msg)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        ActivitySlideAnim.slideOutAnim(OrderPulishedEvaluateActivity.this);
                    }
                }).show();
    }

    @Override
    public void onItemAddPhotoListener(int position) {
        this.currentPosition = position;
        chooseCameraPopuUtils.showPopupWindow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (chooseCameraPopuUtils != null)
            chooseCameraPopuUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}

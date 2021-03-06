package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.AdInfoBean;
import com.help.reward.bean.Response.AdInfoResponse;
import com.help.reward.bean.Response.AddSellerGroupResponse;
import com.help.reward.bean.Response.WatchAdGetScroeResponse;
import com.help.reward.chat.Constant;
import com.help.reward.chat.ui.ChatActivity;
import com.help.reward.network.IntegrationNetwork;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.CountDownTimeUtils;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.AlertDialog;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.LogUtils;
import com.idotools.utils.MetricsUtils;
import com.idotools.utils.ToastUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 看广告
 * Created by wuxiaojun on 2017/1/5.
 */

public class WatchAdActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.tv_ad_title)
    TextView tv_ad_title; // 标题
    @BindView(R.id.tv_ad_name)
    TextView tv_ad_name; // 副标题
    @BindView(R.id.tv_score)
    TextView tv_score; // 赏分
    @BindView(R.id.iv_img_ad)
    ImageView iv_img_ad; // 图片
    @BindView(R.id.tv_point_of_praise)
    TextView tv_point_of_praise; // 看完点赞
    @BindView(R.id.tv_buy)
    TextView tv_buy; // 加群购买
    @BindView(R.id.tv_time)
    TextView tv_time; // 还需观看多少S

    private CountDownTimeUtils mTimer;
    private String ad_id; // 广告id
    private String ad_type; // 广告类型
    private String type; // 看完点赞 null  加群购买 join
    private int ad_type_flag; // 看完点赞 1  加群购买 2
    private String seller_member_id; // 卖家对应的member_id
    private String avaliable_groupid; // 商家群id

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_ad);
        ButterKnife.bind(this);
        initView();
        ad_id = getIntent().getStringExtra("ad_id");
        LogUtils.e("广告id是：" + ad_id);
        ad_type = getIntent().getStringExtra("ad_type");
        if (!TextUtils.isEmpty(ad_type)) {
            if ("看完点赞".equals(ad_type)) {
                tv_point_of_praise.setVisibility(View.VISIBLE);
                tv_buy.setVisibility(View.GONE);
                ad_type_flag = 1;
                type = null;
            } else {
                tv_buy.setVisibility(View.VISIBLE);
                tv_point_of_praise.setVisibility(View.GONE);
                ad_type_flag = 2;
                type = "join";
            }
        }
        if (!TextUtils.isEmpty(ad_id)) { // 请求服务器
            initNet();
        }

    }

    private void initView() {
        tv_title.setText(R.string.string_watch_ad);
        tv_title_right.setVisibility(View.GONE);
        // 固定图片的宽和高
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_img_ad.getLayoutParams();
        params.width = MetricsUtils.dipToPx(710);
        params.height = MetricsUtils.dipToPx(950);
        iv_img_ad.setLayoutParams(params);
    }

    /***
     * act=advertisement&op=info
     */
    private void initNet() {
        if (App.APP_CLIENT_KEY == null) {
            ToastUtils.show(mContext, "需登录");
        }
        IntegrationNetwork
                .getIntegrationApi()
                .getAdInfoResponse("advertisement", "info", ad_id, type, App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AdInfoResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(AdInfoResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                AdInfoBean bean = response.data.info;
                                ad_id = bean.id;
                                tv_ad_title.setText(bean.name);
                                tv_ad_name.setText(bean.user_name);
                                tv_score.setText(bean.per_credit + "帮赏分");
                                GlideUtils.loadImage(bean.url, iv_img_ad);
                                if (ad_type_flag == 1) {
                                    tv_point_of_praise.setText("看完点赞(" + bean.click_num + "/" + bean.times + ")");
                                } else {
                                    tv_buy.setText("加群购买(" + bean.click_num + "/" + bean.times + ")");
                                    seller_member_id = response.data.info.seller_member_id;
//                                    if (response.data.isInGroup) {
                                    avaliable_groupid = response.data.avaliable_groupid;
//                                    }
                                }
                                LogUtils.e("是否看过该广告" + response.data.hasWathced + "----" + response.data.isInGroup + "----" + response.data.avaliable_groupid);
                                if (response.data.hasWathced) { // 已看过该广告
                                    tv_time.setText("已看过");
                                    tv_point_of_praise.setText("已看过");
                                    tv_buy.setText("已看过");

                                } else if (bean.times.equals(bean.click_num)) { // 已结束
                                    tv_time.setText("已结束");
                                    tv_point_of_praise.setText("已结束");
                                    tv_buy.setText("已结束");

                                } else {
                                    initTimer(); // 开始计时
                                    mTimer.start();
                                }
                                tv_point_of_praise.setClickable(false);
                                tv_buy.setClickable(false);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }

    @OnClick({R.id.iv_title_back, R.id.tv_point_of_praise, R.id.tv_buy})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(WatchAdActivity.this);

                break;
            case R.id.tv_point_of_praise:
                // 需要登录才能点击，看完点赞,只有观看完成才可以点击,如果点击过了，就不应该再次点击
                if (App.APP_USER_ID != null) {
                    getScroe();
                } else {
                    ToastUtils.show(mContext, "请先登录");
                }

                break;
            case R.id.tv_buy:
                // 加群购买
                if (App.APP_USER_ID != null) {
                    getScroe();
                } else {
                    ToastUtils.show(mContext, "请先登录");
                }

                break;
        }
    }

    /**
     * 加群购买
     */
    private void addSellerGroup() {
        if (!TextUtils.isEmpty(avaliable_groupid)) {
            Intent intent = new Intent(WatchAdActivity.this, ChatActivity.class);
            intent.putExtra(Constant.EXTRA_USER_ID, avaliable_groupid);
            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
            startActivity(intent);
            ActivitySlideAnim.slideInAnim(WatchAdActivity.this);
        } else {
            new AlertDialog(WatchAdActivity.this)
                    .builder()
                    .setTitle(R.string.string_system_prompt)
                    .setMsg("该商家尚未创建商家群或者群已满").setNegativeButton("确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        }
        /*LogUtils.e("seller_member_id是：" + seller_member_id);
        if (TextUtils.isEmpty(seller_member_id)) {
            return;
        }
        ShopcartNetwork
                .getShopcartCookieApi()
                .getAddSellerGroup(App.APP_CLIENT_KEY, seller_member_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AddSellerGroupResponse>() {

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
                    public void onNext(AddSellerGroupResponse res) {
                        MyProcessDialog.closeDialog();
                        if (res.code == 200) { //
                            LogUtils.e("结果是：" + res.data.groupid);
                            if (res.data != null && !TextUtils.isEmpty(res.data.groupid)) {
                                Intent intent = new Intent(WatchAdActivity.this, ChatActivity.class);
                                intent.putExtra(Constant.EXTRA_USER_ID, res.data.groupid);
                                intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                                startActivity(intent);
                                ActivitySlideAnim.slideInAnim(WatchAdActivity.this);
                            }
                        } else {
                            ToastUtils.show(mContext, res.msg);
                        }
                    }
                });*/
    }

    /***
     * 获取帮赏分
     */
    private void getScroe() {
        IntegrationNetwork
                .getIntegrationApi()
                .getWatchAdGetScroeResponse("advertisement", "watch", App.APP_CLIENT_KEY, ad_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<WatchAdGetScroeResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(WatchAdGetScroeResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                LogUtils.e("剩余帮赏分是：" + response.data.rest);
                                showDialogPointOfPraise(response.data.get, response.data.rest);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void showDialogPointOfPraise(String getScore, String rest) {
        String msg = null;
        if (TextUtils.isEmpty(rest) || "0".equals(rest)) {
            msg = "恭喜您获得" + getScore + "帮赏分";
        } else {
            msg = "恭喜您获得" + getScore + "帮赏分\n剩余" + rest + "帮赏分5天后到账";
        }
        new AlertDialog(WatchAdActivity.this)
                .builder()
                .setTitle(R.string.string_system_prompt)
                .setMsg(msg).setNegativeButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ad_type_flag == 2) {
                    addSellerGroup();
                }
            }
        }).show();
    }

    private void initTimer() {
        mTimer = new CountDownTimeUtils(5 * 1000, 1000);
        mTimer.setOnCountDownTimeListener(new CountDownTimeUtils.OnCountDownTimeListener() {
            @Override
            public void onTick(long millisUntilFinished) { // 计时开始
                long time = millisUntilFinished / 1000;
                tv_time.setText("还需观看" + time + "s");
            }

            @Override
            public void onFinish() { // 计时结束
                tv_time.setText("观看完成");
                tv_point_of_praise.setClickable(true);
                tv_buy.setClickable(true);

                tv_point_of_praise.setBackgroundResource(R.drawable.selector_watch_ad_buy_bg);
                tv_buy.setBackgroundResource(R.drawable.selector_watch_ad_buy_bg);

                tv_point_of_praise.setTextColor(getResources().getColorStateList(R.color.selector_watch_ad_buy_textcolor));
                tv_buy.setTextColor(getResources().getColorStateList(R.color.selector_watch_ad_buy_textcolor));

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.onDestroy();
    }
}

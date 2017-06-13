package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
    private int ad_type_flag; // 看完点赞 1  加群购买 2
    private boolean watchAdFinish; // 看完ad，点击获取帮赏分后，不能再点击了
    private String seller_member_id; // 卖家对应的member_id


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
            } else {
                tv_buy.setVisibility(View.VISIBLE);
                tv_point_of_praise.setVisibility(View.GONE);
                ad_type_flag = 2;
            }
        }
        if (!TextUtils.isEmpty(ad_id)) { // 请求服务器
            initNet();
        }
        if (App.APP_USER_ID == null) {
            ToastUtils.show(mContext, "需登录");
        }
    }

    private void initView() {
        tv_title.setText(R.string.string_watch_ad);
        tv_title_right.setVisibility(View.GONE);
    }

    /***
     * act=advertisement&op=info
     */
    private void initNet() {
        IntegrationNetwork
                .getIntegrationApi()
                .getAdInfoResponse("advertisement", "info", ad_id)
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
                                }
                                if (!response.data.hasWathced) {
                                    initTimer(); // 开始计时
                                    mTimer.start();
                                    tv_point_of_praise.setClickable(false);
                                    tv_buy.setClickable(false);

                                } else {
                                    tv_time.setText("已观看");
                                    tv_point_of_praise.setVisibility(View.GONE);
                                    tv_buy.setVisibility(View.GONE);
                                }

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
                    addSellerGroup();

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
        LogUtils.e("seller_member_id是：" + seller_member_id);
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
                        if (res.code == 200) { // 收藏成功
                            LogUtils.e("结果是：" + res.data.groupid);
                            if (res.data != null && !TextUtils.isEmpty(res.data.groupid)) {
                                Intent intent = new Intent(WatchAdActivity.this, ChatActivity.class);
                                intent.putExtra(Constant.EXTRA_USER_ID, res.data.groupid);
                                startActivity(intent);
                            }
                        } else {
                            ToastUtils.show(mContext, res.msg);
                        }
                    }
                });
    }

    /***
     * "data": {
     * "get": "1"
     * }
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
                                showDialogPointOfPraise(response.data.get);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
//                        watchAdFinish = true;
                    }
                });
    }

    private void showDialogPointOfPraise(String score) {
        String msg = null;
        if (ad_type_flag == 1) {
            msg = "恭喜您获得" + score + "帮赏分";
        } else {
            msg = "恭喜您获得" + score + "帮赏分\n剩余多少帮赏分多少天后到账";
        }
        new AlertDialog(WatchAdActivity.this)
                .builder()
                .setTitle(R.string.string_system_prompt)
                .setMsg(msg).setNegativeButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }

    private void initTimer() {
        mTimer = new CountDownTimeUtils(15 * 1000, 15);
        mTimer.setOnCountDownTimeListener(new CountDownTimeUtils.OnCountDownTimeListener() {
            @Override
            public void onTick(long millisUntilFinished) { // 计时开始
                tv_time.setText("还需观看" + millisUntilFinished / 1000 + "s");
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

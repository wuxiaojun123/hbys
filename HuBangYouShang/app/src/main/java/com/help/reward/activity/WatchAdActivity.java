package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.AdInfoBean;
import com.help.reward.bean.AdvertisementBean;
import com.help.reward.bean.Response.AdInfoResponse;
import com.help.reward.bean.Response.MyVoteResponse;
import com.help.reward.network.IntegrationNetwork;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.CountDownTimeUtils;
import com.help.reward.utils.GlideUtils;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

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
    @BindView(R.id.tv_time)
    TextView tv_time; // 还需观看多少S

    private CountDownTimeUtils mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_ad);
        ButterKnife.bind(this);
        initView();
        String ad_id = getIntent().getStringExtra("ad_id");
        if (!TextUtils.isEmpty(ad_id)) { // 请求服务器
            initNet(ad_id);
        }
    }

    private void initView() {
        tv_title.setText(R.string.string_watch_ad);
        tv_title_right.setVisibility(View.GONE);
    }

    /***
     * act=advertisement&op=info
     *
     * @param ad_id
     */
    private void initNet(String ad_id) {
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
                                tv_ad_title.setText(bean.name);
                                tv_ad_name.setText(bean.user_name);
                                tv_score.setText(bean.credit + "帮赏分");
                                GlideUtils.loadImage(bean.url, iv_img_ad);
                                // 开始计时
                                initTimer();
                                mTimer.start();
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }

    @OnClick({R.id.iv_title_back})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(WatchAdActivity.this);

                break;
        }
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

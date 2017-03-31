package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.HelpCenterAdapter;
import com.help.reward.bean.Response.HelpCenterResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * 帮助中心
 * item_help_center.xml
 * Created by wuxiaojun on 2017/1/9.
 */

public class HelpCenterActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.id_recycler_view)
    LRecyclerView lRecyclerview;

    private int numSize = 15;

    private HelpCenterAdapter myCollectionStoreAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        ButterKnife.bind(this);
        initView();
        initNetwork();
    }

    private void initView() {
        tv_title.setText(R.string.string_help_center_title);
        tv_title_right.setVisibility(View.GONE);

        lRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        myCollectionStoreAdapter = new HelpCenterAdapter(mContext);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(myCollectionStoreAdapter);
        lRecyclerview.setAdapter(adapter);
    }

    private void initNetwork() {
        PersonalNetwork
                .getResponseApi()
                .getHelpCenterResponse(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpCenterResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        lRecyclerview.refreshComplete(numSize);
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(HelpCenterResponse response) {
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            LogUtils.e("获取数据成功。。。" + response.data.article_list.size());
                            if (response.data != null) {
                                myCollectionStoreAdapter.addAll(response.data.article_list);
                            }
                            lRecyclerview.setPullRefreshEnabled(false);
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
        switch (id){
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(HelpCenterActivity.this);

                break;
        }
    }

}

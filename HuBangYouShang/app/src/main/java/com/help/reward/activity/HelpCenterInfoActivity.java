package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.HelpCenterAdapter;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.bean.Response.HelpCenterResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.Constant;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 帮助中心--详细信息
 * item_help_center.xml
 * Created by wuxiaojun on 2017/1/9.
 */

public class HelpCenterInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.id_webview)
    WebView id_webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center_info);
        ButterKnife.bind(this);
        initView();
        initNetwork();
    }

    private void initView() {
        tv_title.setText(R.string.string_help_center_title);
        tv_title_right.setVisibility(View.GONE);

    }

    private void initNetwork() {
        /*String article_id = getIntent().getStringExtra("article_id");
        if (TextUtils.isEmpty(article_id)) {
            return;
        }
        // act=article&op=article_content
        PersonalNetwork
                .getResponseApi()
                .getHelpCenterInfoResponse("article", "article_content", article_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<String>>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(BaseResponse<String> response) {
                        if (response.code == 200) {
                            tv_content.setText(response.data);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });*/
        String article_id = getIntent().getStringExtra("article_id");
        if (TextUtils.isEmpty(article_id)) {
            return;
        }
        id_webview.getSettings().setJavaScriptEnabled(true);
        id_webview.setWebChromeClient(new WebChromeClient());
        id_webview.setWebViewClient(new WebViewClient());

        id_webview.loadUrl(Constant.BASE_URL + "/mobile/index.php?act=article&op=article_content&article_id=" + article_id);
    }


    @OnClick({R.id.iv_title_back})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(HelpCenterInfoActivity.this);

                break;
        }
    }

}

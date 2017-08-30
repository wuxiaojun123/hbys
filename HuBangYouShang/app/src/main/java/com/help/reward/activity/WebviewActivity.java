package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.WebViewUrlResponse;
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
 * Created by wuxiaojun on 17-8-17.
 */

public class WebviewActivity extends BaseActivity {

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
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        id_webview.getSettings().setJavaScriptEnabled(true);
        id_webview.setWebChromeClient(new WebChromeClient());
        id_webview.setWebViewClient(new WebViewClient());

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        tv_title.setText(title);
        tv_title_right.setVisibility(View.GONE);
        String op = intent.getStringExtra("op");
        initNet(op);
    }

    private void initNet(String op) {
        LogUtils.e("开始请求....");
        // act=article&op=protocol  ?act=article&op=protocol
        PersonalNetwork
                .getLoginApi()
                .getWebviewUrlResponse("article", "protocol")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<WebViewUrlResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LogUtils.e("返回数据失败,,," + e.toString());
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(WebViewUrlResponse response) {
                        LogUtils.e("返回值是:" + response.code + "----" + response.msg);
                        if (response.code == 200) {
                            if (response.data != null) {
                                LogUtils.e("返回url是：" + response.data.url);
                                id_webview.loadUrl(Constant.BASE_URL + "/" + response.data.url);
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_title_back:
                finish();
                ActivitySlideAnim.slideOutAnim(WebviewActivity.this);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (id_webview != null) {
            id_webview.destroy();
        }
    }
}

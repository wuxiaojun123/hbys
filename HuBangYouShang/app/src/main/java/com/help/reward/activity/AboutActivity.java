package com.help.reward.activity;

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
 * 关于
 * <p>
 * Created by wuxiaojun on 2017/1/8.
 */

public class AboutActivity extends BaseActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        id_webview.setWebChromeClient(new WebChromeClient());
        id_webview.setWebViewClient(new WebViewClient());
        id_webview.getSettings().setJavaScriptEnabled(true);
        initView();
        initNet();
    }

    private void initView() {
        tv_title.setText(R.string.string_about_title);
        tv_title_right.setVisibility(View.GONE);
    }

    private void initNet() {
        if (App.APP_CLIENT_KEY == null) {
            return;
        }
        PersonalNetwork
                .getResponseApi()
                .getAboutOurResponse(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<WebViewUrlResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(WebViewUrlResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                id_webview.loadUrl(Constant.BASE_URL+"/" + response.data.url);
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
                ActivitySlideAnim.slideOutAnim(AboutActivity.this);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(id_webview != null){
            id_webview.destroy();
        }
    }

}

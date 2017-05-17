package com.help.reward.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.HelpSeekInfoCommentAdapter;
import com.help.reward.bean.HelpSeekCommentBean;
import com.help.reward.bean.HelpSeekInfoBean;
import com.help.reward.bean.Response.HelpSeekInfoResponse;
import com.help.reward.bean.Response.StringResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.HelpSeekInfoRefreshRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.help.reward.utils.StringUtils;
import com.help.reward.view.ChooseCommentTypePop;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.DateUtil;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 求助详情
 */

public class HelpSeekInfoActivity extends BaseActivity {
    protected Subscription subscribe;
    private Subscription rxSubscribe;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_favorites)
    ImageView iv_title_favorites;
    @BindView(R.id.iv_title_complaint)
    ImageView iv_title_complaint;

    @BindView(R.id.comment_layout)
    LinearLayout comment_layout;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.et_comment)
    EditText et_comment;
    @BindView(R.id.tv_comment)
    TextView tv_comment;

    @BindView(R.id.lv_helpseekinfo)
    LRecyclerView lRecyclerview;
    private int numSize = 15;
    private HelpSeekInfoCommentAdapter adapter;
    public List<HelpSeekCommentBean> mDatas = new ArrayList<>();
    String id;
    int curpage = 1;
    String commentType = "2";

    ImageView iv_helpinfo_headimg;
    TextView tv_helpinfo_uname;
    TextView tv_helpinfo_date;
    TextView tv_helpinfo_count;
    TextView tv_helpinfo_title;
    TextView tv_helpinfo_content;
    ImageView iv_image1, iv_image2, iv_image3, iv_image4;
    TextView tv_showAll;
    LRecyclerViewAdapter mLRecyclerViewAdapter;
    ChooseCommentTypePop chooseCommentTypePop;

    String has_commented, status;
    String from;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpseekinfo);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            id = bundle.getString("id");
            if (!StringUtils.checkStr(id)) {
                finish();
            }
            if(bundle.containsKey("from")){
                from = bundle.getString("from");
            }
        }
        initView();
        initRecycler();
        MyProcessDialog.showDialog(mContext);
        requestData();
    }

    private void initView() {
        tvTitle.setText("详情页");
        getRxBusData();
    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HelpSeekInfoCommentAdapter(this);
        adapter.setDataList(mDatas);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(mLRecyclerViewAdapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(true);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        lRecyclerview.setVisibility(View.GONE);
        initHeader();
        initRefreshListener();
        initLoadMoreListener();
    }

    private void initHeader() {
        View view = getLayoutInflater().inflate(
                R.layout.header_helpinfo, null);
        iv_helpinfo_headimg = (ImageView) view.findViewById(R.id.iv_helpinfo_headimg);
        tv_helpinfo_uname = (TextView) view.findViewById(R.id.tv_helpinfo_uname);
        tv_helpinfo_date = (TextView) view.findViewById(R.id.tv_helpinfo_date);
        tv_helpinfo_count = (TextView) view.findViewById(R.id.tv_helpinfo_count);
        tv_helpinfo_title = (TextView) view.findViewById(R.id.tv_helpinfo_title);
        tv_helpinfo_content = (TextView) view.findViewById(R.id.tv_helpinfo_content);
        iv_image1 = (ImageView) view.findViewById(R.id.iv_image1);
        iv_image2 = (ImageView) view.findViewById(R.id.iv_image2);
        iv_image3 = (ImageView) view.findViewById(R.id.iv_image3);
        iv_image4 = (ImageView) view.findViewById(R.id.iv_image4);

        tv_showAll = (TextView) view.findViewById(R.id.tv_showAll);
        tv_showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("全文".equals(tv_showAll.getText().toString())) {
                    tv_showAll.setText("收起");
                    tv_helpinfo_content.setMaxLines(100);
                } else {
                    tv_showAll.setText("全文");
                    tv_helpinfo_content.setMaxLines(5);
                }
            }
        });
        mLRecyclerViewAdapter.addHeaderView(view);

    }

    private void initRefreshListener() {
        lRecyclerview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() { // 如果集合中没有数据，则进行刷新，否则不刷新
                curpage = 1;
                requestData();
            }
        });
    }

    private void initLoadMoreListener() {
        lRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                curpage++;
                requestData();
            }
        });
    }
    @Override
    public void onBackPressed() {

        if("ReleaseHelpActivity".equals(from)){
            startActivity(new Intent(mContext, MyHelpActivity.class));
        }
        super.onBackPressed();
    }


    @OnClick({R.id.iv_title_back, R.id.tv_comment, R.id.tv_type, R.id.tv_send, R.id.iv_title_favorites, R.id.iv_title_complaint})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                onBackPressed();
                break;
            case R.id.tv_comment:
                //继续跟帖
                Intent intentDetail = new Intent(this, HelpSeekCommentDetailActivity.class);
                intentDetail.putExtra("id", has_commented);
                intentDetail.putExtra("type", commentType);
                intentDetail.putExtra("post_id", id);
                intentDetail.putExtra("status", status);
                startActivity(intentDetail);
                ActivitySlideAnim.slideInAnim(this);
                break;
            case R.id.tv_type:
                //公聊私聊
                if (chooseCommentTypePop == null) {
                    chooseCommentTypePop = new ChooseCommentTypePop();
                    chooseCommentTypePop.setOnTypeChooseListener(new ChooseCommentTypePop.OnTypeChooseListener() {
                        @Override
                        public void onType(String type) {
                            tv_type.setText(type);
                            if ("公聊".equals(type)) {
                                commentType = "2";
                            } else {
                                commentType = "1";
                            }
                        }
                    });
                }
                chooseCommentTypePop.showPopupWindow(this, tv_type);
                break;
            case R.id.tv_send:
                //发送跟帖
                String comment = et_comment.getText().toString().trim();
                if (StringUtils.checkStr(comment)) {
                    subComment(comment);
                } else {
                    ToastUtils.show(mContext, "请输入跟帖内容");
                }

                break;
            case R.id.iv_title_favorites:
                //收藏
                setFavorites();
                break;
            case R.id.iv_title_complaint:
                //投诉
                Intent intent = new Intent(mContext, HelpComplainedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("post_id", id);
                bundle.putString("type", "help");
                bundle.putString("comment_id", "");
                bundle.putString("post_title", tv_helpinfo_title.getText().toString());
                bundle.putString("u_name", tv_helpinfo_uname.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                ActivitySlideAnim.slideInAnim(this);
                break;

        }
    }

    private void requestData() {

        subscribe = HelpNetwork
                .getHelpApi()
                .getHelpSeekInfoBean(App.APP_CLIENT_KEY, "seek_help_detail", id, curpage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpSeekInfoResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        lRecyclerview.refreshComplete(numSize);
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(HelpSeekInfoResponse response) {
                        MyProcessDialog.closeDialog();
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            bindData(response);
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    //投票

    private void setFavorites() {
        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
                .getFavoritesAddBean(App.APP_CLIENT_KEY, "favorites_add", id, "help")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<StringResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(StringResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            ToastUtils.show(mContext, "收藏成功");
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }

    private void subComment(String content) {
        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
                .getSubCommentBean(App.APP_CLIENT_KEY, "comment", id, commentType, "", content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<StringResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(StringResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            ToastUtils.show(mContext, "评论成功");
                            requestData();
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }


    private void bindData(HelpSeekInfoResponse response) {
        lRecyclerview.setVisibility(View.VISIBLE);
        HelpSeekInfoBean info = response.data.info;
        GlideUtils.loadCircleImage(info.member_avatar, iv_helpinfo_headimg);
        tv_helpinfo_uname.setText(info.u_name);
        tv_helpinfo_date.setText(DateUtil.getDateToString(info.create_time * 1000));
        tv_helpinfo_count.setText("跟帖" + response.data.comment_num);
        tv_helpinfo_title.setText(info.title);
        tv_helpinfo_content.setText(info.content);
        if (info.img_url != null && info.img_url.size() > 0) {
            switch (info.img_url.size()) {
                case 4:
                    iv_image4.setVisibility(View.VISIBLE);
                    iv_image4.setOnClickListener(new onShowBigeImageCick(info.img_url.get(3)));
                    GlideUtils.loadImage(info.img_url.get(3), iv_image4);
                case 3:
                    iv_image3.setVisibility(View.VISIBLE);
                    iv_image3.setOnClickListener(new onShowBigeImageCick(info.img_url.get(2)));
                    GlideUtils.loadImage(info.img_url.get(2), iv_image3);
                case 2:
                    iv_image2.setVisibility(View.VISIBLE);
                    iv_image2.setOnClickListener(new onShowBigeImageCick(info.img_url.get(1)));
                    GlideUtils.loadImage(info.img_url.get(1), iv_image2);
                case 1:
                    iv_image1.setVisibility(View.VISIBLE);
                    iv_image1.setOnClickListener(new onShowBigeImageCick(info.img_url.get(0)));
                    GlideUtils.loadImage(info.img_url.get(0), iv_image1);
                    break;
            }
        }

        has_commented = response.data.has_commented;
        if(response.data.comment.size()>0){
            if(!"0".equals(response.data.comment.get(0).best_re)){
                info.status="结帖";
            }
        }
        status = info.status;
        commentType = response.data.commented_type;
        if ("结帖".equals(info.status)) {
            comment_layout.setVisibility(View.GONE);
            tv_comment.setVisibility(View.GONE);
        } else {
            if (!"0".equals(response.data.has_commented)&&!"False".equalsIgnoreCase(response.data.has_commented)) {
                comment_layout.setVisibility(View.GONE);
                tv_comment.setVisibility(View.VISIBLE);
            } else {
                comment_layout.setVisibility(View.VISIBLE);
                tv_comment.setVisibility(View.GONE);
            }
        }
        adapter.setHasmore(response.hasmore);
        if (response.hasmore) {
            lRecyclerview.setLoadMoreEnabled(true);
        } else {
            lRecyclerview.setLoadMoreEnabled(false);
        }
        adapter.setIsMyPost(info.u_id.equals(App.APP_USER_ID));
        if (!info.u_id.equals(App.APP_USER_ID)) {
            iv_title_favorites.setVisibility(View.VISIBLE);
            iv_title_complaint.setVisibility(View.VISIBLE);
        }else{
            comment_layout.setVisibility(View.GONE);
            tv_comment.setVisibility(View.GONE);
        }
        adapter.setStatus(info.status);
        if (curpage == 1) {
            adapter.setDataList(response.data.comment);
        } else {
            adapter.addAll(response.data.comment);
        }

    }


    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        if (rxSubscribe != null && !rxSubscribe.isUnsubscribed()) {
            rxSubscribe.unsubscribe();
        }
        super.onDestroy();
        ActivitySlideAnim.slideOutAnim(this);
    }

    private class onShowBigeImageCick implements View.OnClickListener {
        String url;

        private onShowBigeImageCick(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View view) {
            showBigImage(url);
        }
    }

    private void showBigImage(String url) {
        Intent intent = new Intent(this, ShowBigImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("imageUrl", url);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    /**
     * 获取rxbus传递过来的数据,刷新
     */
    private void getRxBusData() {
        rxSubscribe = RxBus.getDefault().toObservable(HelpSeekInfoRefreshRxbusType.class).subscribe(new Action1<HelpSeekInfoRefreshRxbusType>() {
            @Override
            public void call(HelpSeekInfoRefreshRxbusType type) {
                requestData();
            }
        });
    }
}

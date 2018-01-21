package com.help.reward.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnLoadMoreListener;
import com.base.recyclerview.OnRefreshListener;
import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.HelpRewardnfoCommentAdapter;
import com.help.reward.bean.HelpRewardCommentBean;
import com.help.reward.bean.HelpRewardInfoBean;
import com.help.reward.bean.Response.HelpRewardChatResponse;
import com.help.reward.bean.Response.HelpRewardCommentResponse;
import com.help.reward.bean.Response.HelpRewardInfoResponse;
import com.help.reward.bean.Response.StringResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.UpdateLoginDataRxbusType;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.DialogUtil;
import com.help.reward.utils.DisplayUtil;
import com.help.reward.utils.GlideUtils;
import com.help.reward.utils.IntentUtil;
import com.help.reward.utils.ScreenUtils;
import com.help.reward.utils.StringUtils;
import com.help.reward.view.HelpRewardInfoMorePop;
import com.help.reward.view.MyProcessDialog;
import com.hyphenate.easeui.ui.EaseShowBigImageActivity;
import com.idotools.utils.DateUtil;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 赞赏详情
 */

public class HelpRewardInfoActivity extends BaseActivity {
    protected Subscription subscribe;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_right)
    ImageView iv_title_right;

    @BindView(R.id.comment_layout)
    LinearLayout comment_layout;
    @BindView(R.id.tv_rewardcomment)
    TextView tv_rewardcomment;
    @BindView(R.id.tv_comment)
    TextView tv_comment;

    @BindView(R.id.lv_helprewardinfo)
    LRecyclerView lRecyclerview;

    private HelpRewardnfoCommentAdapter adapter;
    List<HelpRewardCommentBean> helpRewardCommentBeans = new ArrayList<>();
    List<HelpRewardCommentBean> helpRewardChatBeans = new ArrayList<>();
    List<HelpRewardCommentBean> mDatas = new ArrayList<>();
    LRecyclerViewAdapter mLRecyclerViewAdapter;


    ImageView iv_helpinfo_headimg;
    TextView tv_helpinfo_uname;
    TextView tv_helpinfo_date;
    TextView tv_helpinfo_count;
    TextView tv_helpinfo_title;
    TextView tv_helpinfo_content;
    ImageView iv_image1;
    ImageView iv_image2;
    ImageView iv_image3;
    ImageView iv_image4;
    TextView tv_showAll;
    ImageView iv_reward;
    TextView tv_reward_num;

    HorizontalScrollView hs_collect_1, hs_collect_2;

    LinearLayout collect_layout1, collect_layout2;

    String has_chatted;
    String id;
    int curpage = 1, curpage2 = 1;
    boolean hasmore, hasmore2;
    int numSize = 15;
    String u_name, content;
    String from;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helprewardinfo);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            if (!StringUtils.checkStr(id)) {
                finish();
            }
            if (bundle.containsKey("from")) {
                from = bundle.getString("from");
            }
        }

        initView();
        initRecycler();
        MyProcessDialog.showDialog(this);
        requestData();
    }

    private void initView() {
        tvTitle.setText("详情页");
    }

    private void initRecycler() {
        lRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HelpRewardnfoCommentAdapter(this);
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
                R.layout.header_rewardinfo, null);
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
        iv_reward = (ImageView) view.findViewById(R.id.iv_reward);
        tv_reward_num = (TextView) view.findViewById(R.id.tv_reward_num);

        iv_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showConfirmCancleDialog(HelpRewardInfoActivity.this, "系统提示", "将扣除您10个帮赏分,\n是否确认打赏？", new DialogUtil.OnDialogUtilClickListener() {
                    @Override
                    public void onClick(boolean isLeft) {
                        if (isLeft) {
                            setPoints();
                        }
                    }
                });
            }
        });
        hs_collect_1 = (HorizontalScrollView) view.findViewById(R.id.hs_collect_1);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) hs_collect_1.getLayoutParams();
        params.width = ScreenUtils.getScreenWidth(this);
        hs_collect_1.setLayoutParams(params);
        hs_collect_2 = (HorizontalScrollView) view.findViewById(R.id.hs_collect_2);

        collect_layout1 = (LinearLayout) view.findViewById(R.id.collect_layout1);
        collect_layout2 = (LinearLayout) view.findViewById(R.id.collect_layout2);
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

        if ("ReleaseRewardActivity".equals(from)) {
            startActivity(new Intent(mContext, MyRewardActivity.class));
        }
        super.onBackPressed();
    }

    @OnClick({R.id.iv_title_back, R.id.iv_title_right, R.id.tv_comment, R.id.tv_rewardcomment})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                onBackPressed();
                break;
            case R.id.iv_title_right:
                new HelpRewardInfoMorePop(id, u_name, content, "admiration").showPopupWindow(this, iv_title_right);

                break;
            case R.id.tv_comment:
                //继续跟帖
                if ("开始跟帖".equalsIgnoreCase(tv_comment.getText().toString())) {

                    DialogUtil.showConfirmCancleDialog(this, "系统提示", "您即将进入私聊模式,\n聊天内容仅您与发帖人可见", new DialogUtil.OnDialogUtilClickListener() {
                        @Override
                        public void onClick(boolean isLeft) {
                            if (isLeft) {
                                Intent intentDetail = new Intent(mContext, HelpRewardChatDetailActivity.class);
                                intentDetail.putExtra("id", has_chatted);
                                intentDetail.putExtra("post_id", id);
                                intentDetail.putExtra("hint", "跟帖");
                                startActivityForResult(intentDetail, 200);
                                ActivitySlideAnim.slideInAnim(HelpRewardInfoActivity.this);
                            }
                        }
                    });
                } else {
                    Intent intentDetail = new Intent(this, HelpRewardChatDetailActivity.class);
                    intentDetail.putExtra("id", has_chatted);
                    intentDetail.putExtra("post_id", id);
                    intentDetail.putExtra("hint", "继续跟帖");
                    startActivityForResult(intentDetail, 200);
                    ActivitySlideAnim.slideInAnim(this);
                }
                break;
            case R.id.tv_rewardcomment:
                // 写评论
                Intent intent = new Intent(mContext, HelpRewardCommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("post_id", id);
                intent.putExtras(bundle);
                startActivityForResult(intent, 100);
                ActivitySlideAnim.slideInAnim(this);
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                curpage = 1;
                requestData();
            }
        } else if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                curpage = 1;
                requestData();
            }
        }
    }

    private void requestData() {

        subscribe = HelpNetwork
                .getHelpApi()
                .getHelpRewardInfoBean(App.APP_CLIENT_KEY, "get_reward_detail", id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpRewardInfoResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        lRecyclerview.refreshComplete(numSize);
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(HelpRewardInfoResponse response) {
                        MyProcessDialog.closeDialog();
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            bindData(response);
                            requestChatData();
                            requestCommentData();
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void requestCommentData() {

        subscribe = HelpNetwork
                .getHelpApi()
                .getHelpRewardCommentBean(App.APP_CLIENT_KEY, "reward_comment", id, curpage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpRewardCommentResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        lRecyclerview.refreshComplete(numSize);
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        if (curpage != 1)
                            curpage--;
//                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(HelpRewardCommentResponse response) {
                        MyProcessDialog.closeDialog();
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            hasmore = response.hasmore;
                            bindComment(response.data.message_list, "comment");
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    private void requestChatData() {

        subscribe = HelpNetwork
                .getHelpApi()
                .getHelpRewardChatBean(App.APP_CLIENT_KEY, "reward_prvChat", id, curpage2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpRewardChatResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        lRecyclerview.refreshComplete(numSize);
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        if (curpage2 != 1)
                            curpage2--;
//                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(HelpRewardChatResponse response) {
                        MyProcessDialog.closeDialog();
                        lRecyclerview.refreshComplete(numSize);
                        if (response.code == 200) {
                            hasmore2 = response.hasmore;
                            bindComment(response.data.chat_list, "chat");
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    //赏
    private void setPoints() {
        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
                .getGiveRewardPoints10Bean(App.APP_CLIENT_KEY, "reward_post", id)
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
                            ToastUtils.show(mContext, "打赏成功");
                            curpage = 1;
                            requestData();
//                            RxBus.getDefault().post(new UpdateLoginDataRxbusType(true));
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }


    public void bindData(HelpRewardInfoResponse response) {
        lRecyclerview.setVisibility(View.VISIBLE);
        HelpRewardInfoBean info = response.data.info;
        GlideUtils.loadCircleImage(info.member_avatar, iv_helpinfo_headimg);
        IntentUtil.startPersonalHomePage(HelpRewardInfoActivity.this, info.u_id, iv_helpinfo_headimg);
        u_name = info.u_name;
        content = info.content;
        tv_helpinfo_uname.setText(info.u_name);
        tv_helpinfo_date.setText(DateUtil.getDateToString(info.create_time + ""));
        if(!TextUtils.isEmpty(info.admiration_all)){
            tv_helpinfo_count.setText("获赏" + info.admiration_all);
        }
        tv_helpinfo_title.setText(info.title);
        tv_helpinfo_content.setText(info.content);
        if(!TextUtils.isEmpty(info.admiration)){
            tv_reward_num.setText(info.admiration + "人赞赏");
        }
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
        if (!"False".equalsIgnoreCase(response.data.has_chatted)) {
            has_chatted = response.data.has_chatted;
        }
        adapter.setIsMyPost(info.u_id.equals(App.APP_USER_ID));
        if (!App.APP_USER_ID.equals(info.u_id)) {
            iv_title_right.setVisibility(View.VISIBLE);
            comment_layout.setVisibility(View.VISIBLE);
            if ("False".equalsIgnoreCase(response.data.has_chatted)) {
                tv_comment.setText("开始跟帖");
            } else {
                tv_comment.setText("继续跟帖");
            }
            if ("False".equalsIgnoreCase(response.data.could_comment)) {
                tv_rewardcomment.setVisibility(View.GONE);
            } else {
                tv_rewardcomment.setVisibility(View.VISIBLE);
            }

        } else {
            iv_title_right.setVisibility(View.GONE);
            comment_layout.setVisibility(View.GONE);
        }

        bindAdmirationView(info);

    }

    /**
     * 绑定评论和私聊
     */
    boolean chatFinsh, commentFinsh;

    private void bindComment(List<HelpRewardCommentBean> datas, String type) {

        if ("chat".equalsIgnoreCase(type)) {
            if (curpage == 1) {
                helpRewardChatBeans.clear();
            }
            helpRewardChatBeans.addAll(datas);
            chatFinsh = true;
            if (!hasmore2) {
                commentFinsh = true;
            }
        } else {
            if (curpage2 == 1) {
                helpRewardCommentBeans.clear();
            }
            helpRewardCommentBeans.addAll(datas);
            commentFinsh = true;
            if (!hasmore) {
                chatFinsh = true;
            }
        }
        if (chatFinsh && chatFinsh) {
            if (curpage == 1 && curpage2 == 1) {
                mDatas.clear();
                mDatas.addAll(helpRewardChatBeans);
                mDatas.addAll(helpRewardCommentBeans);
                adapter.setHelpRewardChatBeans(helpRewardChatBeans);
                adapter.setHelpRewardCommentBeans(helpRewardCommentBeans);
                adapter.setDataList(mDatas);
            } else {
                adapter.setHelpRewardChatBeans(helpRewardChatBeans);
                adapter.setHelpRewardCommentBeans(helpRewardCommentBeans);
                mDatas.addAll(helpRewardChatBeans);
                mDatas.addAll(helpRewardCommentBeans);
                adapter.setDataList(mDatas);
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
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
     * 绑定赞赏人头像
     *
     * @param info
     */
    private void bindAdmirationView(HelpRewardInfoBean info) {
        if (info.admiration_list.size() > 0) {
            hs_collect_1.setVisibility(View.VISIBLE);
            if (info.admiration_list.size() > 8) {
                hs_collect_2.setVisibility(View.VISIBLE);
            } else {
                hs_collect_2.setVisibility(View.GONE);
            }
        } else {
            hs_collect_1.setVisibility(View.GONE);
        }
        collect_layout1.removeAllViews();
        collect_layout2.removeAllViews();
        for (int i = 0; i < info.admiration_list.size(); i++) {

            int item_width = (ScreenUtils.getScreenWidth(mContext) - DisplayUtil.dipToPixel(mContext, 10 * 2 + 15 * 2
                    + 8 * 5)) / 8;
            ImageView circle_img = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(item_width, item_width);
            params.setMargins(DisplayUtil.dipToPixel(mContext, 5), 0, 0, 0);
            circle_img.setLayoutParams(params);
            GlideUtils.loadCircleImage(info.admiration_list.get(i).avatar, circle_img);
            IntentUtil.startPersonalHomePage(HelpRewardInfoActivity.this, info.admiration_list.get(i).id, circle_img);
            if (i < 8) {
                collect_layout1.addView(circle_img);
            } else {
                collect_layout2.addView(circle_img);
            }
        }
    }


}

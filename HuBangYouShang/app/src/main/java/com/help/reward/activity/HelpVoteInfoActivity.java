package com.help.reward.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.Response.HelpVoteInfoResponse;
import com.help.reward.bean.Response.SubVoteResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.help.reward.utils.StringUtils;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MXY on 2017/2/20.
 */

public class HelpVoteInfoActivity extends BaseActivity {
    protected Subscription subscribe;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_helpinfo_title)
    TextView tv_helpinfo_title;
    @BindView(R.id.tv_helpinfo_content)
    TextView tv_helpinfo_content;

    @BindView(R.id.iv_icon1)
    ImageView iv_icon1;
    @BindView(R.id.iv_icon2)
    ImageView iv_icon2;
    @BindView(R.id.tv_vote1)
    TextView tv_vote1;
    @BindView(R.id.tv_vote2)
    TextView tv_vote2;
    @BindView(R.id.tv_name1)
    TextView tv_name1;
    @BindView(R.id.tv_name2)
    TextView tv_name2;
    @BindView(R.id.layout_vote1)
    LinearLayout layout_vote1;
    @BindView(R.id.layout_vote2)
    LinearLayout layout_vote2;

    @BindView(R.id.checkbox_vote1)
    CheckBox checkbox_vote1;
    @BindView(R.id.checkbox_vote2)
    CheckBox checkbox_vote2;
    @BindView(R.id.tv_votename1)
    TextView tv_votename1;
    @BindView(R.id.tv_votename2)
    TextView tv_votename2;
    @BindView(R.id.tv_subvote)
    TextView tv_subvote;
    @BindView(R.id.layout_content)
    LinearLayout layout_content;

    String id;
    String complainant_id, respondent_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpvoteinfo);
        ButterKnife.bind(this);
        id = getIntent().getExtras().getString("id");
        if (!StringUtils.checkStr(id)) {
            finish();
        }
        initView();
        requestData();
    }

    private void initView() {
        tvTitle.setText("详情页");
        tvTitleRight.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_subvote, R.id.layout_vote1, R.id.layout_vote2})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.layout_vote1:
                checkbox_vote1.setChecked(true);
                checkbox_vote2.setChecked(false);
                break;
            case R.id.layout_vote2:
                checkbox_vote1.setChecked(false);
                checkbox_vote2.setChecked(true);
                break;
            case R.id.tv_subvote:
                if (!checkbox_vote1.isChecked() && !checkbox_vote2.isChecked()) {
                    ToastUtils.show(mContext, "请选择投票方");
                    return;
                }

                if (checkbox_vote1.isChecked()) {
                    subVote(complainant_id);
                } else {
                    subVote(respondent_id);
                }
                break;
        }
    }

    private void requestData() {
        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
                .getVoteInfoBean(App.APP_CLIENT_KEY, "vote_info", id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpVoteInfoResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                    }

                    @Override
                    public void onNext(HelpVoteInfoResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            if (response.data != null) {
                                layout_content.setVisibility(View.VISIBLE);
                                GlideUtils.loadCircleImage(response.data.complainant_avatar, iv_icon1);
                                GlideUtils.loadCircleImage(response.data.respondent_avatar, iv_icon2);
                                complainant_id = response.data.complainant_id;
                                respondent_id = response.data.respondent_id;
                                tv_vote1.setText(response.data.complainant_vote);
                                tv_vote2.setText(response.data.respondent_vote);
                                tv_name1.setText(response.data.complainant_name);
                                tv_name2.setText(response.data.respondent_name);
                                tv_helpinfo_title.setText(response.data.post_title);
                                tv_helpinfo_content.setText(response.data.content);
                                tv_votename1.setText(response.data.complainant_name);
                                tv_votename2.setText(response.data.respondent_name);
                                if (!"false".equalsIgnoreCase(response.data.has_voted)) {

                                    if ("vote_for_cpn".equalsIgnoreCase(response.data.has_voted)) {
                                        checkbox_vote1.setChecked(true);
                                        checkbox_vote2.setChecked(false);
                                    } else if ("vote_for_rpd".equalsIgnoreCase(response.data.has_voted)) {
                                        checkbox_vote1.setChecked(false);
                                        checkbox_vote2.setChecked(true);
                                    }

                                    tv_subvote.setText("已投票");
                                    tv_subvote.setBackgroundResource(R.drawable.vote_done);
                                    tv_subvote.setEnabled(false);
                                    layout_vote1.setEnabled(false);
                                    layout_vote2.setEnabled(false);
                                    tv_subvote.setTextColor(getResources().getColor(R.color.color_8a));
                                }
                            }
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    //投票

    private void subVote(String complaint_id) {
        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
                .getSubVoteBean(App.APP_CLIENT_KEY,id, complaint_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<SubVoteResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        MyProcessDialog.closeDialog();
                        e.printStackTrace();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(SubVoteResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            ToastUtils.show(mContext, "投票成功");
                            tv_subvote.setText("已投票");
                            tv_subvote.setBackgroundResource(R.drawable.vote_done);
                            tv_subvote.setEnabled(false);
                            layout_vote1.setEnabled(false);
                            layout_vote2.setEnabled(false);
                            tv_subvote.setTextColor(getResources().getColor(R.color.color_8a));
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
        ActivitySlideAnim.slideOutAnim(this);
    }

}

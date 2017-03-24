package com.help.reward.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.activity.HelpComplainedActivity;
import com.help.reward.activity.HelpRewardChatDetailActivity;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.HelpRewardCommentBean;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.idotools.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;


/**
 *
 */

public class HelpRewardnfoCommentAdapter extends BaseRecyclerAdapter<HelpRewardCommentBean> {
    Activity mActivity;
    boolean isMyPost = false;//我是不是发帖人
    List<HelpRewardCommentBean> helpRewardCommentBeans = new ArrayList<>();
    List<HelpRewardCommentBean> helpRewardChatBeans = new ArrayList<>();

    public HelpRewardnfoCommentAdapter(Activity context) {
        super(context);
        this.mActivity = context;
    }

    public void setHelpRewardChatBeans(List<HelpRewardCommentBean> helpRewardChatBeans) {
        this.helpRewardChatBeans = helpRewardChatBeans;
    }

    public void setHelpRewardCommentBeans(List<HelpRewardCommentBean> helpRewardCommentBeans) {
        this.helpRewardCommentBeans = helpRewardCommentBeans;
    }

    public void setIsMyPost(boolean isMyPost) {
        this.isMyPost = isMyPost;
    }


    @Override
    public int getItemCount() {
        if (mDataList.size() == 0) {
            return 1;
        }
        return mDataList.size();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_helprewardcomment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        if (mDataList.size() == 0) {
            holder.getView(R.id.tv_empty).setVisibility(View.VISIBLE);
            holder.getView(R.id.comment_layout).setVisibility(View.GONE);
            return;
        }
        holder.getView(R.id.tv_empty).setVisibility(View.GONE);
        holder.getView(R.id.comment_layout).setVisibility(View.VISIBLE);
        final HelpRewardCommentBean item = mDataList.get(position);

        LinearLayout title_layout = holder.getView(R.id.title_layout);
        title_layout.setVisibility(View.GONE);
        TextView tv_label = holder.getView(R.id.tv_label);
        if (position == 0 && helpRewardChatBeans.size() > 0) {
            if (App.APP_USER_ID.equalsIgnoreCase(item.u_id)) {
                tv_label.setText("我的跟帖");
            } else {
                tv_label.setText("跟帖提问");
            }
            title_layout.setVisibility(View.VISIBLE);
        } else if (position == 0 && helpRewardChatBeans.size() <= 0) {
            tv_label.setText("赞赏评论");
            title_layout.setVisibility(View.VISIBLE);
        } else if (position == helpRewardChatBeans.size() && helpRewardCommentBeans.size() > 0) {
            tv_label.setText("赞赏评论");
            title_layout.setVisibility(View.VISIBLE);
        }


        ImageView iv_helpinfo_headimg = holder.getView(R.id.iv_helpinfo_headimg);
        GlideUtils.loadCircleImage(item.member_avatar, iv_helpinfo_headimg);
        TextView tv_helpinfo_uname = holder.getView(R.id.tv_helpinfo_uname);
        tv_helpinfo_uname.setText(item.u_name);
        TextView tv_helpinfo_date = holder.getView(R.id.tv_helpinfo_date);
        tv_helpinfo_date.setText(DateUtil.getDateToString(item.create_time * 1000));


        TextView tv_helpinfo_content = holder.getView(R.id.tv_helpinfo_content);
        tv_helpinfo_content.setText(item.content);

        ImageView iv_reply = holder.getView(R.id.iv_reply);

        if (!"0".equalsIgnoreCase(item.has_read)) {
            iv_reply.setImageResource(R.mipmap.reply_selected);
        } else {
            iv_reply.setImageResource(R.mipmap.reply_normal);
        }
        iv_reply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //继续跟帖
                Intent intentDetail = new Intent(mActivity, HelpRewardChatDetailActivity.class);
                intentDetail.putExtra("id", item.id);
                intentDetail.putExtra("post_id", id);
                mActivity.startActivity(intentDetail);
                ActivitySlideAnim.slideInAnim(mActivity);

            }
        });
        ImageView iv_complain = holder.getView(R.id.iv_complain);
        iv_complain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //继续跟帖
                Intent intent = new Intent(mActivity, HelpComplainedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("post_id", item.post_id);
                bundle.putString("type", "admiration");
                bundle.putString("comment_id", item.id);
                bundle.putString("post_title", item.content);
                bundle.putString("u_name", item.u_name);
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
                ActivitySlideAnim.slideInAnim(mActivity);
            }
        });

        if (position >= helpRewardChatBeans.size()) {
            iv_reply.setVisibility(View.GONE);
            iv_complain.setVisibility(View.GONE);
        } else {
            iv_reply.setVisibility(View.VISIBLE);
            if (!isMyPost) {
                iv_complain.setVisibility(View.GONE);
            } else {
                iv_complain.setVisibility(View.VISIBLE);
            }
        }

    }

}
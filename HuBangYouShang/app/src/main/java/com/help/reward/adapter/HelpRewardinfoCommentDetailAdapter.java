package com.help.reward.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.HelpRewardCommentBean;
import com.help.reward.utils.GlideUtils;
import com.help.reward.utils.IntentUtil;
import com.idotools.utils.DateUtil;


/**
 * huoshang评论详情
 */

public class HelpRewardinfoCommentDetailAdapter extends BaseRecyclerAdapter<HelpRewardCommentBean> {
    Activity mActivity;

    public HelpRewardinfoCommentDetailAdapter(Activity context) {
        super(context);
        this.mActivity = context;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_helprewardcomment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {

        View left_layout = holder.getView(R.id.left_layout);
        if (position == 0) {
            left_layout.setVisibility(View.GONE);
        } else {
            left_layout.setVisibility(View.VISIBLE);
        }
         HelpRewardCommentBean item = mDataList.get(position);

        LinearLayout title_layout = holder.getView(R.id.title_layout);
        title_layout.setVisibility(View.GONE);

        ImageView iv_helpinfo_headimg = holder.getView(R.id.iv_helpinfo_headimg);
        GlideUtils.loadCircleImage(item.member_avatar, iv_helpinfo_headimg);
        IntentUtil.startPersonalHomePage(mActivity,item.u_id,iv_helpinfo_headimg);
        TextView tv_helpinfo_uname = holder.getView(R.id.tv_helpinfo_uname);
        tv_helpinfo_uname.setText(item.u_name);
        TextView tv_helpinfo_date = holder.getView(R.id.tv_helpinfo_date);
        tv_helpinfo_date.setText(DateUtil.getDateToString(item.create_time+""));


        TextView tv_helpinfo_content = holder.getView(R.id.tv_helpinfo_content);
        tv_helpinfo_content.setText(item.content);

        ImageView iv_reply = holder.getView(R.id.iv_reply);
        ImageView iv_complain = holder.getView(R.id.iv_complain);

        iv_reply.setVisibility(View.GONE);
        iv_complain.setVisibility(View.GONE);

    }
}
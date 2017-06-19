package com.help.reward.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.HelpSeekCommentBean;
import com.help.reward.utils.GlideUtils;
import com.idotools.utils.DateUtil;


/**
 * 求助评论详情
 */

public class HelpSeekInfoCommentDetailAdapter extends BaseRecyclerAdapter<HelpSeekCommentBean> {
    Activity mActivity;

    public HelpSeekInfoCommentDetailAdapter(Activity context) {
        super(context);
        this.mActivity = context;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_helpinfocomment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {

        View left_layout = holder.getView(R.id.left_layout);
        if (position == 0) {
            left_layout.setVisibility(View.GONE);
        } else {
            left_layout.setVisibility(View.VISIBLE);
        }
        final HelpSeekCommentBean item = mDataList.get(position);
        ImageView iv_helpinfo_private = holder.getView(R.id.iv_helpinfo_private);
        if ("1".equals(item.type)) {
            iv_helpinfo_private.setVisibility(View.VISIBLE);
        } else {
            iv_helpinfo_private.setVisibility(View.GONE);
        }
        LinearLayout title_layout = holder.getView(R.id.title_layout);
        title_layout.setVisibility(View.GONE);

        ImageView iv_helpinfo_headimg = holder.getView(R.id.iv_helpinfo_headimg);
        GlideUtils.loadCircleImage(item.member_avatar, iv_helpinfo_headimg);
        TextView tv_helpinfo_uname = holder.getView(R.id.tv_helpinfo_uname);
        tv_helpinfo_uname.setText(item.u_name);
        TextView tv_helpinfo_date = holder.getView(R.id.tv_helpinfo_date);
        tv_helpinfo_date.setText(DateUtil.getDateToString(item.create_time + ""));

        TextView tv_helpinfo_count = holder.getView(R.id.tv_helpinfo_count);
        tv_helpinfo_count.setVisibility(View.GONE);
        TextView tv_helpinfo_content = holder.getView(R.id.tv_helpinfo_content);
        tv_helpinfo_content.setText(item.content);
        LinearLayout bottom_layout = holder.getView(R.id.bottom_layout);
        bottom_layout.setVisibility(View.GONE);

    }
}
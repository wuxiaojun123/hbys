package com.reward.help.merchant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.base.recyclerview.OnItemClickListener;
import com.idotools.utils.DateUtil;
import com.reward.help.merchant.R;
import com.reward.help.merchant.activity.ApplyCreateGroupActivity;
import com.reward.help.merchant.adapter.viewholder.SuperViewHolder;
import com.reward.help.merchant.bean.Response.GroupProgressResponse;
import com.reward.help.merchant.utils.DateUtils;

/**
 * Created by ADBrian on 08/04/2017.
 */

public class GroupProgressAdapter extends BaseRecyclerAdapter<GroupProgressResponse.GroupProgrossInfoBean> {

    private Context mContext;

    public GroupProgressAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_group_progress;
    }

    @Override
    public void onBindItemHolder(final SuperViewHolder holder, final int position) {

        TextView mTvName = (TextView) holder.itemView.findViewById(R.id.name);
        TextView mTvResult = (TextView) holder.itemView.findViewById(R.id.tv_group_result);
        TextView mTvCreateDate = (TextView) holder.itemView.findViewById(R.id.tv_date);

        final GroupProgressResponse.GroupProgrossInfoBean groupProgrossInfoBean = mDataList.get(position);
        mTvName.setText(groupProgrossInfoBean.content);
        mTvResult.setText(groupProgrossInfoBean.status);
        mTvCreateDate.setText(DateUtil.getDateToString2(groupProgrossInfoBean.created));

        if ("审核失败".equals(mTvResult.getText().toString())) {
            mTvResult.setTextColor(mContext.getResources().getColor(R.color.actionsheet_red));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                }
            });
        } else {
            mTvResult.setTextColor(mContext.getResources().getColor(R.color.color_83b2f6));
        }
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

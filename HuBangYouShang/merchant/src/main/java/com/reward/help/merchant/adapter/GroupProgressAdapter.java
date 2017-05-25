package com.reward.help.merchant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.idotools.utils.DateUtil;
import com.reward.help.merchant.R;
import com.reward.help.merchant.adapter.viewholder.SuperViewHolder;
import com.reward.help.merchant.bean.Response.GroupProgressResponse;
import com.reward.help.merchant.utils.DateUtils;

/**
 * Created by ADBrian on 08/04/2017.
 */

public class GroupProgressAdapter extends BaseRecyclerAdapter<GroupProgressResponse.GroupProgrossInfoBean> {

    public GroupProgressAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_group_progress;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        TextView mTvName = (TextView) holder.itemView.findViewById(R.id.name);
        TextView mTvResult = (TextView) holder.itemView.findViewById(R.id.tv_group_result);
        TextView mTvCreateDate = (TextView) holder.itemView.findViewById(R.id.tv_date);

        GroupProgressResponse.GroupProgrossInfoBean groupProgrossInfoBean = mDataList.get(position);
        mTvName.setText(groupProgrossInfoBean.content);
        mTvResult.setText(groupProgrossInfoBean.status);

        long date = -1;

        try {
            date = Long.parseLong(groupProgrossInfoBean.created);

            if (date != -1) {
                mTvCreateDate.setText(DateUtil.getDay(date));
            }
        }catch (Exception e){

        }


        if ("申请失败".equals(mTvResult.getText().toString())) {
            mTvResult.setTextColor(mContext.getResources().getColor(R.color.actionsheet_red));
        } else {
            mTvResult.setTextColor(mContext.getResources().getColor(R.color.color_83b2f6));
        }
    }

}

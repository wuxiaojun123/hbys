package com.reward.help.merchant.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.utils.EaseUserUtils;
import com.reward.help.merchant.R;
import com.reward.help.merchant.adapter.BaseRecyclerAdapter;
import com.reward.help.merchant.adapter.viewholder.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADBrian on 04/04/2017.
 */

public class GroupMemberAdapter extends BaseRecyclerAdapter<String> {


    private List<String> mSelectList;

    public GroupMemberAdapter(Context context, ArrayList<String> selectlist) {
        super(context);
        this.mSelectList = selectlist;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_group_member;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final String name = mDataList.get(position);
        ImageView mIvSelect = (ImageView) holder.itemView.findViewById(R.id.iv_groupmember_select);
        ImageView mIvPhoto = (ImageView) holder.itemView.findViewById(R.id.iv_member_avatar);
        TextView mTvName = (TextView) holder.itemView.findViewById(R.id.tv_groupmember_name);

        EaseUserUtils.setUserNick(name, mTvName);
        EaseUserUtils.setUserAvatar(mContext, name, mIvPhoto);


        if (mSelectList.contains(name)) {
            mIvSelect.setImageResource(R.mipmap.coupon_xuan_a);
        } else {
            mIvSelect.setImageResource(R.mipmap.coupon_xuan_b);
        }

//        mIvSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mSelectList.contains(name)) {
//                    mSelectList.remove(name);
//                } else {
//                    mSelectList.add(name);
//                }
//                notifyDataSetChanged();
//            }
//        });
    }
    
}

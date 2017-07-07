package com.help.reward.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.activity.HelpCenterInfoActivity;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.HelpCenterBean;
import com.help.reward.bean.MyCollectionStoreBean;
import com.help.reward.minterface.OnItemDeleteListener;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.SwipeMenuView;

/**
 * 帮助中心
 * Created by wuxiaojun on 2017/2/26.
 */

public class HelpCenterAdapter extends BaseRecyclerAdapter {

    public HelpCenterAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_help_center;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        TextView tv_title = holder.getView(R.id.tv_title);


        final HelpCenterBean bean = (HelpCenterBean) mDataList.get(position);

        tv_title.setText(bean.article_title);
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, HelpCenterInfoActivity.class);
                mIntent.putExtra("article_id",bean.article_id);
                mContext.startActivity(mIntent);
                ActivitySlideAnim.slideInAnim((Activity) mContext);
            }
        });

    }



}

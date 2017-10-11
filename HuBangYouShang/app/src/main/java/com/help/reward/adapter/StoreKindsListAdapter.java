package com.help.reward.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.StoreKindsBean;


/**
 *
 */

public class StoreKindsListAdapter extends BaseRecyclerAdapter<StoreKindsBean> {
    Activity mActivity;

    public StoreKindsListAdapter(Activity context) {
        super(context);
        this.mActivity = context;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_storekinds;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {

        View bottomLine1 = holder.getView(R.id.bottomLine1);
        View bottomLine2 = holder.getView(R.id.bottomLine2);
        if (position == mDataList.size() - 1) {
            bottomLine1.setVisibility(View.GONE);
            bottomLine2.setVisibility(View.VISIBLE);
        } else {
            bottomLine1.setVisibility(View.VISIBLE);
            bottomLine2.setVisibility(View.GONE);
        }
        final StoreKindsBean item = mDataList.get(position);

        TextView tv_title = holder.getView(R.id.tv_title);
        tv_title.setText(item.name);

    }
}
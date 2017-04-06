package com.help.reward.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.GoodEvaluateBean;
import com.help.reward.utils.GlideUtils;

/**
 * Created by ADBrian on 06/04/2017.
 */

public class GoodEvaluateAdapter extends BaseRecyclerAdapter<GoodEvaluateBean> {

    public GoodEvaluateAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_good_evaluate;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ImageView mIvPhoto = (ImageView) holder.itemView.findViewById(R.id.iv_good_evaluate_photo);
        TextView mTvName = (TextView) holder.itemView.findViewById(R.id.tv_good_evaluate_name);
        TextView mTvDate = (TextView) holder.itemView.findViewById(R.id.tv_good_evaluate_date);
        TextView mTvProp = (TextView) holder.itemView.findViewById(R.id.tv_good_evaluate_prop);
        TextView mTvContent = (TextView) holder.itemView.findViewById(R.id.tv_evaluate_content);

        GoodEvaluateBean goodEvaluate = mDataList.get(position);
        GlideUtils.loadImage(goodEvaluate.member_avatar, mIvPhoto);
        mTvName.setText(goodEvaluate.geval_frommembername);
        mTvDate.setText(goodEvaluate.geval_addtime_date);
        mTvContent.setText(goodEvaluate.geval_content);
    }
}

package com.help.reward.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.activity.BaseActivity;
import com.help.reward.activity.MainActivity;
import com.help.reward.activity.WatchAdActivity;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.AdvertisementBean;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.GlideUtils;

/**
 * Created by wuxiaojun on 17-3-2.
 */

public class IntegrationWatchPraiseAdapter extends BaseRecyclerAdapter {

    public IntegrationWatchPraiseAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_ad;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TextView tv_title = holder.getView(R.id.tv_title);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_score = holder.getView(R.id.tv_score);
        ImageView iv_img_ad = holder.getView(R.id.iv_img_ad);
        LinearLayout ll_content = holder.getView(R.id.ll_content);

        final AdvertisementBean bean = (AdvertisementBean) mDataList.get(position);
        tv_title.setText(bean.name);
        tv_name.setText(bean.user_name);
        tv_score.setText(bean.credit+"帮赏分");
        GlideUtils.loadImage(bean.url,iv_img_ad);

        ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.putExtra("ad_id",bean.id);
                mIntent.putExtra("ad_type",bean.type);
                mIntent.setClassName(mContext.getPackageName(), WatchAdActivity.class.getName());
                mContext.startActivity(mIntent);
                ActivitySlideAnim.slideInAnim((BaseActivity)mContext);
            }
        });
    }

}

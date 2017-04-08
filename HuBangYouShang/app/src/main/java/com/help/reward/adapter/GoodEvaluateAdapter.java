package com.help.reward.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.GoodEvaluateBean;
import com.help.reward.utils.DisplayUtil;
import com.help.reward.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADBrian on 06/04/2017.
 */

public class GoodEvaluateAdapter extends BaseRecyclerAdapter<GoodEvaluateBean> {

    private static int LINE_NUMBERS = 3;
    private List<ImageView> lsImageView = new ArrayList<>();


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

        LinearLayout mLayout = (LinearLayout) holder.itemView.findViewById(R.id.ll_img_content);

        GoodEvaluateBean goodEvaluate = mDataList.get(position);
        GlideUtils.loadImage(goodEvaluate.member_avatar, mIvPhoto);
        mTvName.setText(goodEvaluate.geval_frommembername);
        mTvDate.setText(goodEvaluate.geval_addtime_date);
        mTvContent.setText(goodEvaluate.geval_content);

        String[] geval_image_240 = mDataList.get(position).geval_image_240;
        if (geval_image_240 != null && geval_image_240.length > 0) {
            int total = geval_image_240.length / LINE_NUMBERS;
            int MaxLength = geval_image_240.length > 6 ? 6 : geval_image_240.length;
            if (mLayout.getTag() == null) {
                int i = 0;
                if (total > 1) {
                    LinearLayout ll = new LinearLayout(mContext);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll.setLayoutParams(lp1);

                    for (; i < LINE_NUMBERS; i++) {
                        ImageView iv = new ImageView(mContext);
                        //iv.setImageResource(R.mipmap.img_default);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext,90),
                                DisplayUtil.dip2px(mContext,90));
                        lp.leftMargin = 10;
                        //iv.setLayoutParams(lp);
                        ll.addView(iv);
                        GlideUtils.loadImage(geval_image_240[i],iv);
                    }
                    mLayout.addView(ll);
                }

                LinearLayout ll2 = new LinearLayout(mContext);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                ll2.setLayoutParams(lp2);

                for (; i < MaxLength; i++) {
                    ImageView iv = new ImageView(mContext);
                    //iv.setImageResource(R.mipmap.img_default);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext,90),
                            DisplayUtil.dip2px(mContext,90));
                    lp.leftMargin = 10;
                    iv.setLayoutParams(lp);
                    ll2.addView(iv);
                    mLayout.addView(ll2);
                    GlideUtils.loadImage(geval_image_240[i],iv);
                }

                mLayout.setTag(goodEvaluate.geval_frommemberid);

            }
        }
    }
}

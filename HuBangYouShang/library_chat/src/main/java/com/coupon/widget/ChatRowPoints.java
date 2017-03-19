package com.coupon.widget;

import android.content.Context;
import android.widget.BaseAdapter;

import com.coupon.CouponPointsConstant;
import com.hbys.chatlibrary.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;


public class ChatRowPoints extends EaseChatRow {

    public ChatRowPoints(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }
    @Override
    protected void onInflateView() {
        if (message.getBooleanAttribute(CouponPointsConstant.MESSAGE_ATTR_IS_POINTS, false)) {
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    R.layout.em_row_received_points : R.layout.em_row_sent_points, this);
        }
    }

    @Override
    protected void onFindViewById() {

    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSetUpView() {

    }

    @Override
    protected void onBubbleClick() {

    }
}

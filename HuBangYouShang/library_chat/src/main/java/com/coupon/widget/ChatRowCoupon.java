package com.coupon.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coupon.CouponPointsConstant;
import com.hbys.chatlibrary.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;


public class ChatRowCoupon extends EaseChatRow {

    private TextView mTvGreeting;
    private TextView mTvSponsorName;
    private TextView mTvPacketType;

    public ChatRowCoupon(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }
    @Override
    protected void onInflateView() {
        if (message.getBooleanAttribute(CouponPointsConstant.MESSAGE_ATTR_IS_COUPON, false)) {
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    R.layout.em_row_received_coupon : R.layout.em_row_sent_coupon, this);
        }
    }

    @Override
    protected void onFindViewById() {
        mTvGreeting = (TextView) findViewById(R.id.tv_money_greeting);
        mTvSponsorName = (TextView) findViewById(R.id.tv_sponsor_name);
        mTvPacketType = (TextView) findViewById(R.id.tv_packet_type);
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSetUpView() {
        String sponsorName = message.getStringAttribute(CouponPointsConstant.EXTRA_SPONSOR_NAME, "");
        String greetings = message.getStringAttribute(CouponPointsConstant.EXTRA_GREETING, "");
        mTvGreeting.setText(greetings);

        if(TextUtils.isEmpty(sponsorName)){
            sponsorName = context.getString(R.string.get_coupon);
        }
        mTvSponsorName.setText(sponsorName);
        String packetType = message.getStringAttribute(CouponPointsConstant.MESSAGE_ATTR_CP_TYPE, "");
    //        if (!TextUtils.isEmpty(packetType) && TextUtils.equals(packetType, CouponPointsConstant.GROUP_RED_PACKET_TYPE_EXCLUSIVE)) {
    //            mTvPacketType.setVisibility(VISIBLE);
    //            mTvPacketType.setText(R.string.exclusive_red_packet);
    //        } else {
    //            mTvPacketType.setVisibility(GONE);
    //        }
        handleTextMessage();
    }

    @Override
    protected void onBubbleClick() {

    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    // 发送消息
                    break;
                case SUCCESS: // 发送成功
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL: // 发送失败
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS: // 发送中
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }
}

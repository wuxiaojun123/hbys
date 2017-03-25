package com.coupon;

import android.content.Context;
import android.content.Intent;

import com.hbys.chatlibrary.R;
import com.hyphenate.chat.EMMessage;

public class CouponPointsUtils {

    /**
     * 创建发送优惠券消息
     * @param context
     * @param data
     * @param toChatUsername
     * @return
     */
    public static EMMessage createCouponMessage(Context context, Intent data, String toChatUsername) {
        String couponId = data.getStringExtra(CouponPointsConstant.EXTRA_COUPON_POINTS_ID);
        String receiverId = data.getStringExtra(CouponPointsConstant.EXTRA_COUPON_POINTS_RECEIVER_ID);
        String greetings = data.getStringExtra(CouponPointsConstant.EXTRA_GREETING);

        //String redPacketType = data.getStringExtra(CouponPointsConstant.EXTRA_COUPON_POINTS_TYPE);
        EMMessage message = EMMessage.createTxtSendMessage("[" + context.getResources().getString(R.string.get_coupon) + "]", toChatUsername);
        message.setAttribute(CouponPointsConstant.MESSAGE_ATTR_IS_POINTS, true);
        //message.setAttribute(CouponPointsConstant.EXTRA_SPONSOR_NAME, context.getResources().getString(R.string.get_points));
        message.setAttribute(CouponPointsConstant.EXTRA_COUPON_POINTS_ID, couponId);
        message.setAttribute(CouponPointsConstant.EXTRA_COUPON_POINTS_RECEIVER_ID, receiverId);
        message.setAttribute(CouponPointsConstant.EXTRA_SPONSOR_NAME, context.getResources().getString(R.string.get_coupon));
        message.setAttribute(CouponPointsConstant.EXTRA_GREETING, greetings);

        message.setAttribute(CouponPointsConstant.MESSAGE_ATTR_IS_COUPON, true);
        return message;
    }

    /**
     * 创建发送帮赏分消息
     * @param context
     * @param data
     * @param toChatUsername
     * @return
     */
    public static EMMessage createPointsMessage(Context context, Intent data, String toChatUsername) {
        String pointsId = data.getStringExtra(CouponPointsConstant.EXTRA_COUPON_POINTS_ID);
        String receiverId = data.getStringExtra(CouponPointsConstant.EXTRA_COUPON_POINTS_RECEIVER_ID);
        //String redPacketType = data.getStringExtra(CouponPointsConstant.EXTRA_COUPON_POINTS_TYPE);
        EMMessage message = EMMessage.createTxtSendMessage("[" + context.getResources().getString(R.string.get_points) + "]", toChatUsername);
        message.setAttribute(CouponPointsConstant.MESSAGE_ATTR_IS_POINTS, true);
        //message.setAttribute(CouponPointsConstant.EXTRA_SPONSOR_NAME, context.getResources().getString(R.string.get_points));
        message.setAttribute(CouponPointsConstant.EXTRA_COUPON_POINTS_ID, pointsId);
        message.setAttribute(CouponPointsConstant.EXTRA_COUPON_POINTS_RECEIVER_ID, receiverId);
        return message;
    }
}

package com.reward.help.merchant.utils;

import android.os.Environment;

/**
 * Created by wuxiaojun on 2017/2/23.
 */

public class Constant {

    public static final String PLATFORM_CLIENT = "android"; // 平台是安卓

    // 210.72.13.135 http://www.hubangyoushang.com
    public static final String BASE_URL = "http://www.hubangyoushang.com/";
    // 测试账号
//    public static final String BASE_URL = "http://test.hubangyoushang.com/";
    // 电话号码，设置页面
    public static final String TEL_PHONE = "025-58840881";
    // sms短信
    public static final String SMS_APPKEY = "1bebf93388e20"; // sms短信验证
    public static final String SMS_APPSECRET = "007b9903829fa24053dd4386a2019e4b"; // sms短信验证
    // 登录
    public static final String URL_LOGIN = "mobile/index.php?act=login";

    public static final String URL_SELLER_LOGIN = "mobile/index.php?act=seller_login";

    // 注册获取验证码
    public static final String URL_GET_CODE = "mobile/index.php?act=connect&op=get_sms_captcha";


    public static final String URL_CHECK_CODE = "/mobile/index.php?act=member_account&op=modify_password_step3";

    public static final String URL_UPDATE_PASSWORD = "/mobile/index.php?act=member_account&op=modify_password_step5";

    public static final String URL_UPLOADIMAGE = "mobile/index.php?act=upload_file&op=upload_img";

    public static final String URL_GET_COUPON_LIST = "/mobile/index.php?act=seller_voucher&op=listVoucher";
    //发送优惠券
    public static final String URL_SEND_COUPON = "/mobile/index.php?act=seller_voucher&op=giveVoucher";

    public static final String URL_QUERY_COUPON = "/mobile/index.php?act=seller_voucher&op=logReceive";

    //发送营销积分
    public static final String URL_SEND_POINTS = "/mobile/index.php?act=seller_points&op=givePoints";

    public static final String URL_QUERY_POINTS = "/mobile/index.php?act=seller_points&op=logReceive";

    public static final String URL_GET_POINTS = "/mobile/index.php?act=seller_points&op=sellerPoints";


    public static final String URL_GET_STORE = "/mobile/index.php?act=seller_store&op=store_joinin_info";

    public static final String URL_GROUP_APPLY = "/mobile/index.php?act=seller_group&op=groupApply";

    public static final String URL_GROUP_PROGRESS = "/mobile/index.php?act=seller_group&op=groupApplyList";

    public static final String URL_GROUP_STORE = "mobile/index.php?act=store&op=intoStore";

    public static final String URL_PROFILE_COMMIT = "/mobile/index.php?act=seller_index&op=edit_seller_info";


    public static final String ROOT = Environment.getExternalStorageDirectory().getPath() + "/hbzsmerchant/";;
}

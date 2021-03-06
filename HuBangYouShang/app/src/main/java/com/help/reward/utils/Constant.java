package com.help.reward.utils;

import android.os.Environment;

/**
 * Created by wuxiaojun on 2017/2/23.
 */

public class Constant {

	public static final String	PLATFORM_CLIENT										= "android";																																																					// 平台是安卓

	public static final String	ROOT												= Environment.getExternalStorageDirectory().getPath() + "/hbzs/";

	public static final String	SMS_APPKEY											= "1bebf93388e20";																																																				// sms短信验证

	public static final String	SMS_APPSECRET										= "007b9903829fa24053dd4386a2019e4b";																																															// sms短信验证

	// public static final String SMS_APPKEY = "1cf4553965b4a"; // sms短信验证
	// public static final String SMS_APPSECRET =
	// "e128daef725bde4e63e0ee8def531a3c"; // sms短信验证

	// 210.72.13.135 http://jyb.youdoidodo.com http://192.168.3.2/ 12d1:107e
	public static String		BASE_URL											= "http://www.hubangyoushang.com";																																																// 正式域名
	// public static String BASE_URL = "http://test.hubangyoushang.com";

	// 微信登陆的id
	public static final String	WXCHAT_APP_ID										= "wx5a5e4c632b2ae894";

	public static final String	WXCHAT_APP_SECRET									= "18501139afdf58a23a8d05c56169728b";

	// QQ登陆的id
	public static final String	QQ_LOGIN_APP_ID										= "1106183304";

	public static final String	QQ_LOGIN_APP_KEY									= "wimYjPwDCPkKfENj";

	/**
	 * 支付宝支付业务：入参app_id
	 */
	public static final String	ALIPAY_APPID										= "2017050207078993";

	public static final String	ALIPAY_RSA_PRIVATE									= "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

	// 用户协议
	public static final String	URL_AGREEMENT										= "/mobile/index.php?act=article&op=protocol";

	// 电话号码，设置页面
	public static final String	TEL_PHONE											= "025-58840881";

	// 登录
	public static final String	URL_LOGIN											= "mobile/index.php?act=login";

	// 注册获取验证码
	public static final String	URL_GET_CODE										= "mobile/index.php?act=shop&op=index";

	// 注册
	public static final String	URL_REGISTER										= "mobile/index.php?act=connect&op=sms_register";

	// 退出登录
	public static final String	URL_LOGOUT											= "/mobile/index.php?act=logout";

	// 商城首页
	public static final String	URL_SHOP_MALL_MAIN									= "mobile/index.php?act=shop&op=index";

	// 商品详情信息
	public static final String	URL_SHOP_MALL_INFO									= "mobile/index.php";

	// 商品详情评价
	public static final String	URL_SHOP_MALL_INFO_EVALUATE							= "mobile/index.php";

	// 会员相关--余额明细 参数[post]：type=1/2 1-收入2-支出，不带参数为全部
	public static final String	URL_MEMBER_BALANCE									= "mobile/index.php";

	// 会员相关--帮赏分明细 参数[post]：type=1/2 1-收入2-支出，不带参数为全部
	public static final String	URL_MEMBER_ACCOUNT_HELP_REWARD						= "mobile/index.php";

	// 会员相关--通用卷明细 参数[post]：type=1/2 1-收入2-支出，不带参数为全部
	public static final String	URL_MEMBER_ACCOUNT_GENERAL_VOLUME					= "mobile/index.php";

	// 会员相关--我的优惠卷
	public static final String	URL_MEMBER_ACCOUNT_MY_COUPON						= "mobile/index.php?act=member_voucher&op=voucher_list";

	// 会员相关--优惠卷详情
	public static final String	URL_MEMBER_ACCOUNT_MY_COUPON_INFO					= "mobile/index.php?act=member_voucher&op=voucher_info";

	// 会员相关--优惠卷上架 参数[post]：price；voucher_id
	public static final String	URL_MEMBER_ACCOUNT_MY_COUPON_PUT_ON_SALE			= "mobile/index.php?act=member_voucher&op=put_on_sale";

	// 余额兑换帮赏分
	public static final String	URL_MEMBER_YUE_DUIHUAN_BANGSHANGFEN					= "mobile/index.php?act=member_points&op=deposit_exchange";

	// 余额兑换帮赏分--提交
	public static final String	URL_MEMBER_COMMIT_YUE_DUIHUAN_BANGSHANGFEN			= "mobile/index.php?act=member_points&op=deposit_exchange_post";

	// 帮赏分兑换通用卷
	public static final String	URL_MEMBER_BANGSHANGFEN_DUIHUAN_TONGYONGJUAN		= "mobile/index.php?act=member_points&op=exchange_general";

	// 帮赏分兑换通用卷--提交
	public static final String	URL_MEMBER_COMMIT_BANGSHANGFEN_DUIHUAN_TONGYONGJUAN	= "mobile/index.php?act=member_points&op=exchange_general_post";

	public static final String	URL_MESSAGE											= "mobile/index.php?act=member_message";

	public static final String	URL_HELP											= "mobile/index.php?act=index";

	public static final String	URL_AREA											= "mobile/index.php?act=area&op=post_area_list";

	public static final String	URL_BOARD											= "mobile/index.php?act=index&op=get_board";

	public static final String	URL_SUBREWARD										= "mobile/index.php?act=get_reward&op=release";

	// 发布求助帖
	public static final String	URL_SUBSEEKHELP										= "mobile/index.php?act=seek_help";

	public static final String	URL_GETREWARD										= "mobile/index.php?act=get_reward";

	public static final String	URL_UPLOADIMAGE										= "mobile/index.php?act=upload_file&op=upload_img";

	public static final String	URL_SUBVOTE											= "mobile/index.php?act=member_vote&op=vote";

	public static final String	URL_COMPLAIN										= "mobile/index.php?act=member_p_complain";

	public static final String	URL_FAVORITESADD									= "mobile/index.php?act=member_favorites_post";

	public static final String	URL_RECEIVE_COUPON									= "mobile/index.php?act=member_voucher&op=receiveVoucher";

	public static final String	URL_RECEIVE_POINTS									= "mobile/index.php?act=member_points&op=receivePoints";

	public static final String	URL_RECEIVE_COUPON_LOG								= "mobile/index.php?act=member_voucher&op=receiveVoucherLog";

	public static final String	URL_RECEIVE_POINTS_LOG								= "mobile/index.php?act=member_points&op=receivePointsLog";

	public static final String	URL_GROUP_STORE										= "mobile/index.php?act=store&op=intoStore";

	// 购物车
	public static final String	URL_SHOPCART_ADD									= "mobile/index.php?act=member_cart&op=cart_add";

	public static final String	URL_SHOPCART_QUERY									= "mobile/index.php?act=member_cart&op=cart_list";

	public static final String	URL_SHOPCART_DELETE									= "mobile/index.php?act=member_cart&op=cart_del";

	public static final String	URL_SHOPCART_EDIT									= "mobile/index.php?act=member_cart&op=cart_edit_quantity";

	public static final String	URL_BUY_STEP_ONE									= "mobile/index.php?act=member_buy&op=buy_step1";

	public static final String	URL_BUY_STEP_TWO									= "mobile/index.php?act=member_buy&op=buy_step2";

	public static final String	URL_STORE											= "mobile/index.php?act=store";

	public static final String	URL_STORE_ADD										= "mobile/index.php?act=member_favorites_store&op=favorites_add";

	public static final String	URL_GOODSCLASS										= "mobile/index.php?act=goods_class&op=index";

	public static final String	URL_SEARCHGOODS										= "mobile/index.php?act=goods&op=goods_list";

	public static final String	URL_SEARCHSTORE										= "mobile/index.php";

	public static final String	URL_PAYOKGOODS										= "mobile/index.php?act=member_buy&op=pay_ok";

	// 品牌列表
	public static final String	URL_BRANDLIST										= "mobile/index.php?act=brand&op=recommend_list";
}

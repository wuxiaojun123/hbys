package com.help.reward.bean.Response;

/**
 * Created by wuxiaojun on 2017/2/23.
 */

public class LoginResponse extends BaseResponse<LoginResponse> {

    public String username;
    public String userid;
    public String key;
    public String avator; // 头像
    public String point;
    public String level_name;
    public String people_help;
    public String complaint;
    public String complained;
    public String available_predeposit;
    public String discount_level;
    public String general_voucher;
    public String voucher;
    public String easemobId; // 环信登陆账号名称
    public boolean new_message; // 是否有新的消息，true有

}

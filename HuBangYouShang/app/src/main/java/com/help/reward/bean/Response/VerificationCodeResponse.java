package com.help.reward.bean.Response;

/**
 * 获取验证码
 * Created by wuxiaojun on 17-3-9.
 */

public class VerificationCodeResponse extends BaseResponse<VerificationCodeResponse.Code> {


    public class Code {
        public String sms_time;
    }

}

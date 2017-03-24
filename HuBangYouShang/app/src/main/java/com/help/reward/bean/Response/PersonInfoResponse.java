package com.help.reward.bean.Response;

import com.help.reward.bean.PersonInfoBean;

/**
 * * "member_id": "2",
 * "member_name": "liyanhong",
 * "member_avatar": "http://210.72.13.135/data/upload/shop/common/default_user_portrait.gif",
 * "member_sex": "1",
 * "invitation_code": "v7nawm12",
 * "member_provinceid": "1",
 * "member_cityid": "13",
 * "member_areaid": "21",
 * "member_business": "IT业",
 * "member_position": "程序员",
 * "description": "眼如丹凤，眉似卧蚕，滴溜溜两耳垂珠，明皎皎双睛点漆。唇方口正，髭须地阁轻盈；额阔顶平，皮肉天仓饱满。坐定时浑如虎相，走动时有若 ...",
 * "area_info": "北京 福建 海南"
 * Created by wuxiaojun on 2017/3/23.
 */

public class PersonInfoResponse extends BaseResponse<PersonInfoResponse> {

    public String member_id;
    public String member_name;
    public String member_avatar;
    public String member_sex;
    public String invitation_code;
    public String member_provinceid;
    public String member_cityid;
    public String member_areaid;
    public String member_business;
    public String member_position;
    public String description;
    public String area_info;

}

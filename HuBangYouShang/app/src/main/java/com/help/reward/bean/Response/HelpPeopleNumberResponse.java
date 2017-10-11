package com.help.reward.bean.Response;

import java.util.List;

/**
 * {
 * "id": "2",
 * "memberid": "4",//会员id
 * "membername": "phone_18810543074",//会员姓名
 * "points": "1",//帮助人数变化数 负数表示减少
 * "addtime": "1482735167",
 * "desc": "获赏",//说明
 * "postid": "1",//帖子id
 * "posttitle": "不要这么容易就想放弃",//帖子标题
 * "posttype": "2"//帖子类型1-求助2-获赏
 * }
 * <p>
 *     帮助人数
 * Created by wuxiaojun on 2017/9/6.
 */

public class HelpPeopleNumberResponse extends BaseResponse<HelpPeopleNumberResponse.HelpPeopleNumberRes> {

    public boolean hasmore;

    public class HelpPeopleNumberRes {
        public List<HelpPeopleNumberBean> list;
        public String helppeople; // 当前帮助人数
    }

    public class HelpPeopleNumberBean {

        public String id;
        public String memberid;
        public String membername;
        public String points;
        public String addtime;
        public String desc;
        public String postid;
        public String posttitle;
        public String posttype;

    }

}

package com.help.reward.bean;

/*{
    "code": 200,
    "msg": "操作成功",
    "hasmore": false,
    "page_total": 1,
    "data": {
        "goods_eval_list": [
            {
                "geval_scores": "2",
                "geval_content": "号！",
                "geval_content_again": "",
                "geval_addtime": "1490434711",
                "geval_frommemberid": "6",
                "geval_frommembername": "zhouying",
                "geval_state": "0",
                "geval_remark": null,
                "geval_explain": null,
                "geval_explain_again": "",
                "member_avatar": "http://jyb.youdoidodo.com/data/upload/shop/common/default_user_portrait.gif",
                "geval_addtime_date": "2017-03-25",
                "geval_image_240": [
                    "http://jyb.youdoidodo.com/data/upload/shop/common/default_goods_image_240.gif"
                ],
                "geval_image_1024": [
                    "http://jyb.youdoidodo.com/data/upload/shop/common/default_goods_image_240.gif"
                ],
                "geval_addtime_again_date": "1970-01-01",
                "geval_image_again_240": [],
                "geval_image_again_1024": []
            }
        ]
    }
}
 */
public class GoodEvaluateBean {
    public String geval_scores;
    public String geval_content;
    public String geval_frommemberid;
    public String geval_frommembername;
    public String member_avatar;
    public String geval_addtime_date;

    public String[] geval_image_240;
    public String[] geval_image_1024;

    /*
    public String geval_content_again;
    public String geval_addtime;
    public String geval_state;
    public String geval_remark;
    public String geval_explain;
    public String geval_explain_again;
    public String geval_addtime_again_date;
    public String[] geval_image_again_240;
    public String[] geval_image_again_1024;*/

}
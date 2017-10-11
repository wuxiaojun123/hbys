package com.help.reward.bean;

import java.util.List;

/**
 * 商品评价页的实体类，需要上传给服务器
 * Created by wuxiaojun on 2017/4/7.
 */

public class OrderPulishedEvaluateBean {

    public String goods_id; // 商品id
    public String rec_id; // 购买商品记录id
    public String goods_img;
    public String goods_name;
    public String score; // 分数
    public String comment; // 评论内容
    /*public String[] evaluate_images; // 评论的图片
    public String[] file_names;*/
    public List<String> evaluate_images;
    public List<String> file_names;

}

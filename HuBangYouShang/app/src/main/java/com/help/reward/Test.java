package com.help.reward;

import com.help.reward.bean.OrderPulishedEvaluateBean;
import com.help.reward.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuxiaojun on 2017/4/7.
 */

public class Test {

    public static void main(String[] args) {
        List<OrderPulishedEvaluateBean> list = new ArrayList<>();
        OrderPulishedEvaluateBean bean = new OrderPulishedEvaluateBean();
        bean.goods_id = "商品id1";
        bean.score = "2";
        bean.comment = "评价一";
        bean.evaluate_images = new ArrayList<>();
        bean.evaluate_images.addAll(getList());
        list.add(bean);

        String beanJson1 = JsonUtils.toJsonFromObject(bean);
        System.out.println("beanJson1转换之后的是：" + beanJson1);


        OrderPulishedEvaluateBean bean2 = new OrderPulishedEvaluateBean();
        bean2.goods_id = "商品id2";
        bean2.score = "3";
        bean2.comment = "评价3";
        bean2.evaluate_images = new ArrayList<>();
        bean2.evaluate_images.addAll(getList2());
        list.add(bean2);

        Map<String,OrderPulishedEvaluateBean> map2 = new HashMap<>();
        map2.put(bean2.goods_id,bean2);
        map2.put(bean.goods_id,bean);
        String map2Json = JsonUtils.toJsonFromMap(map2);
        System.out.println("map2Json转换之后的是：" + map2Json);

        Map<String,Map<String,OrderPulishedEvaluateBean>> map = new HashMap<>();
        map.put("goods",map2);
        String mapString = JsonUtils.toJsonFromMap(map);
        System.out.println("mapString转换之后的是：" + mapString);
    }

    public static List<String> getList() {
        List<String> list = new ArrayList<>(2);
        list.add("123.jpg");
        list.add("234.jpg");
        return list;
    }

    public static List<String> getList2() {
        List<String> list = new ArrayList<>(2);
        list.add("222222.jpg");
        list.add("333333.jpg");
        return list;
    }

}

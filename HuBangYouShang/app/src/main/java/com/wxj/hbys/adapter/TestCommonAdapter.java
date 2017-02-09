package com.wxj.hbys.adapter;

import android.content.Context;
import android.widget.TextView;

import java.util.List;

/**
 * 通用Adapter使用方法
 * Created by hello world on 2017/2/9.
 * 调用方法
        TestCommonAdapter adapter = new TestCommonAdapter(context,数据集,子布局id);
 *
 */

public class TestCommonAdapter extends CommonAdapter<String>{
    public TestCommonAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
//        获取view
//        TextView view = holder.getView(控件id);
//        通用设置方法，可在ViewHolder类中自行添加
//        holder.setText(控件id,显示内容)

    }
}

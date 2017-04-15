package com.help.reward.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.help.reward.R;
import com.help.reward.bean.GoodsInfoBean;
import com.help.reward.bean.PropertyValueBean;
import com.help.reward.view.GoodPropertyViewGroup;
import com.hyphenate.util.DensityUtil;
import android.view.ViewGroup.MarginLayoutParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ADBrian on 15/04/2017.
 */

public class PropertyAdapter extends BaseAdapter {


    private Context mContext;
    private List<PropertyValueBean> mList;

    //用于保存用户的属性集合
    private HashMap<Integer,GoodsInfoBean.PropertyValueInfo> selectProMap=new HashMap<Integer, GoodsInfoBean.PropertyValueInfo>();
    /**
     * 返回选中的属性
     * @return
     */
    public HashMap<Integer, GoodsInfoBean.PropertyValueInfo> getSelectProMap() {
        return selectProMap;
    }

    public void setSelectProMap(HashMap<Integer, GoodsInfoBean.PropertyValueInfo> selectProMap) {
        this.selectProMap = selectProMap;
    }

    public  void setList(List<PropertyValueBean> list){
        this.mList=list;
        notifyDataSetChanged();
    }


    public PropertyAdapter(Context context){
        super();
        this.mContext=context;
        //drawableNormal=mContext.getResources().getDrawable(R.drawable.tv_property_label);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList == null? 0 :mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            // 获取list_item布局文件的视图
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.item_good_property, null,true);
            holder = new ViewHolder();

            // 获取控件对象
            holder.tvPropName= (TextView) convertView.findViewById(R.id.tv_property_name);
            // 设置控件集到convertView
            holder.vgPropContents= (GoodPropertyViewGroup) convertView.findViewById(R.id.gpv_property);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        PropertyValueBean propertyValueBean = (PropertyValueBean) getItem(position);
        holder.tvPropName.setText(propertyValueBean.getProperty_parent_name());//规格名称
        //动态加载标签
        //判断布局中的子控件是否为0，如果不为0，就不添加了，防止ListView滚动时重复添加
        List<GoodsInfoBean.PropertyValueInfo> propertyChildList = propertyValueBean.getPropertyChildList();
        if (holder.vgPropContents.getChildCount() == 0 && propertyChildList != null) {
            TextView[] textViews = new TextView[propertyChildList.size()];
            //设置每个标签的文本和布局
            //TableRow tr=new TableRow(mContext);

            for (int i = 0; i < propertyChildList.size(); i++) {
                TextView textView = (TextView) View.inflate(mContext, R.layout.textview_property, null);
                textViews[i] = textView;
                textViews[i].setText(propertyChildList.get(i).spec_value_name);
                textViews[i].setTag(i);
                setNormalTextview(textViews[i]);
                MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int pxSpace = (int) DensityUtil.dip2px(mContext, 5);
                marginLayoutParams.setMargins(pxSpace, pxSpace, pxSpace, pxSpace);
                holder.vgPropContents.addView(textViews[i], marginLayoutParams);
                //textViews[i].setOnClickListener(new LableClickListener(propertyValueBean.getProperty_parent_id(),propertyChildList.get(i)));

            }

            for (int j = 0; j < textViews.length; j++) {
                textViews[j].setTag(textViews);
                textViews[j].setOnClickListener(new LableClickListener(propertyValueBean.getProperty_parent_id(), propertyChildList.get(j)));
            }
        }

        /**判断之前是否已选中标签*/
        if (selectProMap.get(propertyValueBean.getProperty_parent_id()) != null) {
            for (int h = 0; h < holder.vgPropContents.getChildCount(); h++) {
                TextView v = (TextView) holder.vgPropContents.getChildAt(h);
                if (selectProMap.get(propertyValueBean.getProperty_parent_id()).spec_value_name.equals(v.getText().toString())) {
                    setPressTextview(v);
                }
            }
        }
        return convertView;
    }



    private  void setNormalTextview(TextView mTv){
        mTv.setTextColor(mContext.getResources().getColor(R.color.color_79));
        mTv.setBackgroundResource(R.drawable.property_text_nor);
    }

    private  void setPressTextview(TextView mTv){
        mTv.setTextColor(mContext.getResources().getColor(R.color.color_while));
        mTv.setBackgroundResource(R.drawable.property_text_press);
    }
    /*定义item对象*/
    public class ViewHolder {

        TextView tvPropName;
        GoodPropertyViewGroup vgPropContents;
    }

    class LableClickListener implements View.OnClickListener {
        private int key;
        private GoodsInfoBean.PropertyValueInfo value;
        public LableClickListener(int property_parent_id, GoodsInfoBean.PropertyValueInfo propertyValueInfo) {
            this.key = property_parent_id;
            this.value = propertyValueInfo;
        }

        @Override
        public void onClick(View v) {
            TextView[] textViews=(TextView[])v.getTag();
            TextView tv=(TextView)v;
            for(int i=0;i<textViews.length;i++){
                //让点击的标签背景变成橙色，字体颜色变为白色
                if(tv.equals(textViews[i])){
                    setPressTextview(textViews[i]);
                    selectProMap.put(key, value);
                }else{
                    //其他标签背景变成白色，字体颜色为黑色
                    setNormalTextview(textViews[i]);
                }
            }

        }
    }
}

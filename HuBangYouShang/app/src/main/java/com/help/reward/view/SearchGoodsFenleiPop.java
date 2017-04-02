package com.help.reward.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.adapter.GoodsSearchPinPaiAdapter;
import com.help.reward.bean.PinPaiBean;
import com.help.reward.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/*
 *搜索商品分类 *
 */
public class SearchGoodsFenleiPop implements OnClickListener {
    TextView tv_reset, tv_ok;
    MyGridView myGridView;
    TextView tv_freight, tv_cod, tv_refund, tv_protection, tv_quality, tv_sevenDay;
    EditText et_priceform, et_priceto;
    List<PinPaiBean> mDatas = new ArrayList<>();
    List<String> service = new ArrayList<>();
    String b_id;
    String priceFrom, priceTo;
    GoodsSearchPinPaiAdapter mAdapter;
    ImageView iv_showAll;

    PopupWindow popupWindow;

    public SearchGoodsFenleiPop showPopupWindow(
            Activity activity, View root, String b_id, String priceFrom, String priceTo, List<String> service, List<PinPaiBean> mDatas) {

        this.b_id = b_id;
        this.service = service;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.mDatas = mDatas;
        View popupWindow_view = LayoutInflater.from(activity).inflate(
                R.layout.pop_searchgoodsfenlei, null);


        // 使用下面的构造方法
        popupWindow = new PopupWindow(popupWindow_view,
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        popupWindow.setWidth(ScreenUtils.getScreenWidth(activity) * 8 / 10);
        popupWindow.setAnimationStyle(R.style.goodsfeileiAnimation);

        tv_reset = (TextView) popupWindow_view.findViewById(R.id.tv_reset);
        tv_reset.setOnClickListener(this);
        tv_ok = (TextView) popupWindow_view.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(this);
        tv_freight = (TextView) popupWindow_view.findViewById(R.id.tv_freight);
        tv_freight.setOnClickListener(this);
        tv_cod = (TextView) popupWindow_view.findViewById(R.id.tv_cod);
        tv_cod.setOnClickListener(this);
        tv_refund = (TextView) popupWindow_view.findViewById(R.id.tv_refund);
        tv_refund.setOnClickListener(this);
        tv_protection = (TextView) popupWindow_view.findViewById(R.id.tv_protection);
        tv_protection.setOnClickListener(this);
        tv_quality = (TextView) popupWindow_view.findViewById(R.id.tv_quality);
        tv_quality.setOnClickListener(this);
        tv_sevenDay = (TextView) popupWindow_view.findViewById(R.id.tv_sevenDay);
        tv_sevenDay.setOnClickListener(this);
        et_priceform = (EditText) popupWindow_view.findViewById(R.id.et_priceform);
        et_priceto = (EditText) popupWindow_view.findViewById(R.id.et_priceto);
        iv_showAll = (ImageView) popupWindow_view.findViewById(R.id.iv_showAll);
        iv_showAll.setOnClickListener(this);
        myGridView = (MyGridView) popupWindow_view.findViewById(R.id.myGridView);
        mAdapter = new GoodsSearchPinPaiAdapter(activity);
        mAdapter.setDatas(mDatas);
        myGridView.setAdapter(mAdapter);
        popupWindow.showAtLocation(root, Gravity.NO_GRAVITY,0,0);
        return this;
    }

    private void initView() {
        mAdapter.setB_id(b_id);
        et_priceform.setText(priceFrom);
        et_priceto.setText(priceTo);
        if (service != null) {
            setSelected(tv_freight, service.contains("freight"));
            setSelected(tv_cod, service.contains("COD"));
            setSelected(tv_refund, service.contains("refund"));
            setSelected(tv_protection, service.contains("protection"));
            setSelected(tv_quality, service.contains("quality"));
            setSelected(tv_sevenDay, service.contains("sevenDay"));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_reset:
                b_id = "";
                priceFrom = "";
                priceTo = "";
                service.clear();
                initView();
                break;
            case R.id.tv_ok:
                dissPoup();
                if (onTypeChooseListener != null) {
                    onTypeChooseListener.onType(b_id, priceFrom, priceTo, service);
                }
                break;
            case R.id.iv_showAll:
                mAdapter.setShowAll();
                break;
            case R.id.tv_freight:
                clickService(tv_freight, "freight");
                break;
            case R.id.tv_cod:
                clickService(tv_cod, "COD");
                break;
            case R.id.tv_refund:
                clickService(tv_refund, "refund");
                break;
            case R.id.tv_protection:
                clickService(tv_protection, "protection");
                break;
            case R.id.tv_quality:
                clickService(tv_quality, "quality");
                break;
            case R.id.tv_sevenDay:
                clickService(tv_sevenDay, "sevenDay");
                break;
        }

    }

    private void clickService(TextView textView, String type) {
        if (service.contains(type)) {
            service.remove(type);
        } else {
            service.add(type);
        }
        setSelected(textView, service.contains(type));
    }

    private void setSelected(TextView textView, boolean isSelected) {
        if (isSelected) {
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setBackgroundResource(R.drawable.fa372d_bg);
        } else {
            textView.setTextColor(Color.parseColor("#3a4a6b"));
            textView.setBackgroundResource(R.drawable.dcdcdc_f8f8f9_bg);
        }
    }

    private void dissPoup() {
        if (null != popupWindow && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    OnTypeChooseListener onTypeChooseListener;

    public interface OnTypeChooseListener {
        void onType(String b_id, String priceFrom, String priceTo, List<String> service);
    }

    public void setOnTypeChooseListener(OnTypeChooseListener onTypeChooseListener) {
        this.onTypeChooseListener = onTypeChooseListener;
    }

}

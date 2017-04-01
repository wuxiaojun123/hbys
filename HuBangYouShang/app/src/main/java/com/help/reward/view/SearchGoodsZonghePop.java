package com.help.reward.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.help.reward.R;

/*
 *搜索商品综合
 */
public class SearchGoodsZonghePop {

    public SearchGoodsZonghePop showPopupWindow(
            Activity activity, View root, String type) {
        View popupWindow_view = LayoutInflater.from(activity).inflate(
                R.layout.pop_searchgoodszonghe, null);


        // 使用下面的构造方法
        PopupWindow popupWindow = new PopupWindow(popupWindow_view,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        popupWindow_view.findViewById(R.id.tv_zonghe).setOnClickListener(
                new ClickListener(activity, popupWindow));
        popupWindow_view.findViewById(R.id.tv_price1).setOnClickListener(
                new ClickListener(activity, popupWindow));
        popupWindow_view.findViewById(R.id.tv_price2).setOnClickListener(
                new ClickListener(activity, popupWindow));
        popupWindow_view.findViewById(R.id.tv_clicknum).setOnClickListener(
                new ClickListener(activity, popupWindow));
        if ("pricedesc".equals(type)) {
            popupWindow_view.findViewById(R.id.iv_price1).setVisibility(View.VISIBLE);
        } else if ("pricedasc".equals(type)) {
            popupWindow_view.findViewById(R.id.iv_price2).setVisibility(View.VISIBLE);
        } else if ("clicknum".equals(type)) {
            popupWindow_view.findViewById(R.id.iv_clicknum).setVisibility(View.VISIBLE);
        } else {
            popupWindow_view.findViewById(R.id.iv_zonghe).setVisibility(View.VISIBLE);
        }
        popupWindow.showAsDropDown(root);
        return this;
    }


    private class ClickListener implements OnClickListener {
        Activity mActivity;
        PopupWindow mPopupWindow;

        ClickListener(Activity activity,
                      PopupWindow poup) {
            mPopupWindow = poup;
            mActivity = activity;
        }

        @Override
        public void onClick(View view) {
            dissPoup(mPopupWindow);
            switch (view.getId()) {
                case R.id.tv_zonghe:
                    if (onTypeChooseListener != null) {
                        onTypeChooseListener.onType("zonghe");
                    }
                    break;
                case R.id.tv_price1:
                    if (onTypeChooseListener != null) {
                        onTypeChooseListener.onType("pricedesc");
                    }
                    break;
                case R.id.tv_price2:
                    if (onTypeChooseListener != null) {
                        onTypeChooseListener.onType("priceasc");
                    }
                    break;
                case R.id.tv_clicknum:
                    if (onTypeChooseListener != null) {
                        onTypeChooseListener.onType("clicknum");
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private void dissPoup(PopupWindow poup) {
        if (null != poup && poup.isShowing())
            poup.dismiss();
    }

    OnTypeChooseListener onTypeChooseListener;

    public interface OnTypeChooseListener {
        void onType(String type);
    }

    public void setOnTypeChooseListener(OnTypeChooseListener onTypeChooseListener) {
        this.onTypeChooseListener = onTypeChooseListener;
    }

}

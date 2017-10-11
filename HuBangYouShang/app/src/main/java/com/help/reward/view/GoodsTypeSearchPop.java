package com.help.reward.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.help.reward.R;
import com.help.reward.utils.DisplayUtil;

/*
 *分类左上角搜索menu
 */
public class GoodsTypeSearchPop {

    public GoodsTypeSearchPop showPopupWindow(
            Activity activity, View root) {
        View popupWindow_view = LayoutInflater.from(activity).inflate(
                R.layout.pop_goodstypesearch, null);


        // 使用下面的构造方法
        PopupWindow popupWindow = new PopupWindow(popupWindow_view,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setWidth(DisplayUtil.dip2px(activity, 90));
        popupWindow_view.findViewById(R.id.layout_goods).setOnClickListener(
                new ClickListener(activity, popupWindow));
        popupWindow_view.findViewById(R.id.layout_shop).setOnClickListener(
                new ClickListener(activity, popupWindow));

        popupWindow_view.setOnClickListener(new ClickListener(activity, popupWindow));
        popupWindow.showAsDropDown(root, 0,0 - DisplayUtil.dip2px(activity, 5));
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
                case R.id.layout_goods:
                    if (onTypeChooseListener != null) {
                        onTypeChooseListener.onType("goods");
                    }
                    break;
                case R.id.layout_shop:
                    if (onTypeChooseListener != null) {
                        onTypeChooseListener.onType("store");
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

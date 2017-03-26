package com.help.reward.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.base.recyclerview.OnItemClickListener;
import com.help.reward.R;
import com.help.reward.adapter.SearchHistoryAdapter;
import com.help.reward.utils.StringUtils;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * 搜索历史弹框
 */
public class SearchHistoryPop {
    List<String> mDatas = new ArrayList<>();
    Activity activity;
    String type;

    public SearchHistoryPop(Activity activity, String type) {
        this.activity = activity;
        this.type = type;
    }

    public void showPopupWindow(View root) {

        View popupWindow_view = LayoutInflater.from(activity).inflate(
                R.layout.pop_searchhistory, null);
        // 使用下面的构造方法
        final PopupWindow popupWindow = new PopupWindow(popupWindow_view,
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xffffff));
        popupWindow.setOutsideTouchable(true);
        popupWindow_view.findViewById(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearDatas();
                ToastUtils.show(activity, "清除成功");
                if (null != popupWindow && popupWindow.isShowing())
                    popupWindow.dismiss();
            }
        });
        LRecyclerView lRecyclerview = (LRecyclerView) popupWindow_view.findViewById(R.id.rv_history);
        lRecyclerview.setLayoutManager(new LinearLayoutManager(activity));
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(activity);
        mDatas = getDatas();
        adapter.setDataList(mDatas);
        LRecyclerViewAdapter ladapter = new LRecyclerViewAdapter(adapter);
        lRecyclerview.setAdapter(ladapter);
        //禁用下拉刷新功能
        lRecyclerview.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        lRecyclerview.setLoadMoreEnabled(false);
        lRecyclerview.setItemAnimator(new DefaultItemAnimator());
        ladapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (onHistoryChooseListener != null) {
                    onHistoryChooseListener.onHistory(mDatas.get(position));
                }
                if (null != popupWindow && popupWindow.isShowing())
                    popupWindow.dismiss();
            }

        });
        popupWindow.showAsDropDown(root);
    }

    public void clearDatas() {
        SharedPreferences mySharedPreferences = activity.getSharedPreferences(type,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.clear().commit();
    }

    public List<String> getDatas() {
        SharedPreferences mySharedPreferences = activity.getSharedPreferences(type,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        String keywords = mySharedPreferences.getString("keyword", "");
        List<String> mdatas = new ArrayList<>();
        String[] array = keywords.split("&keyword&");
        if (array.length > 0) {
            mdatas = Arrays.asList(array);
        }
        return mdatas;
    }

    public void setKeyword(String keyword) {

        SharedPreferences mySharedPreferences = activity.getSharedPreferences(type,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        String keywords = mySharedPreferences.getString("keyword", "");
        if (keywords.contains(keyword)) {
            return;
        }
        if (StringUtils.checkStr(keywords)) {
            editor.putString("keyword", keyword + "&keyword&" + keywords);
        } else {
            editor.putString("keyword", keyword);
        }
        editor.commit();
    }


    OnHistoryChooseListener onHistoryChooseListener;

    public interface OnHistoryChooseListener {
        void onHistory(String keyword);
    }

    public void setOnHistoryChooseListener(OnHistoryChooseListener onHistoryChooseListener) {
        this.onHistoryChooseListener = onHistoryChooseListener;
    }
}

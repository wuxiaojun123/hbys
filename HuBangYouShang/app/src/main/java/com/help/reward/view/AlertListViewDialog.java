
package com.help.reward.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.help.reward.R;

import java.util.List;

public class AlertListViewDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView img_line;
    private ListView listview;
    private Display display;

    public AlertListViewDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AlertListViewDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_listview_alertdialog, null);

        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        listview = (ListView) view.findViewById(R.id.listview);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        txt_title.setText("计费详情");
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

        return this;
    }
    
    /*public AlertListViewDialog setListView(List<BallingDetailsBeanResp> list){
        if(list != null && !list.isEmpty()){
            CommonAdapter<BallingDetailsBeanResp> mAdapter = new CommonAdapter<BallingDetailsBeanResp>(context, list, R.layout.item_balling_details_dialog) {

                @Override
                public void convert(ViewHolder holder, BallingDetailsBeanResp t) {
                    holder.setText(R.id.tv_description, t.description)
                            .setText(R.id.tv_price, t.price + "停车币/小时");
                }
            };
            listview.setAdapter(mAdapter);
        }
        return this;
    }*/

    public AlertListViewDialog setTitle(String title) {
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public AlertListViewDialog setPositiveButton(String text,
            final OnClickListener listener) {
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertListViewDialog setNegativeButton(String text,
            final OnClickListener listener) {
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
        btn_pos.setVisibility(View.VISIBLE);
        btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
        btn_neg.setVisibility(View.VISIBLE);
        btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
        img_line.setVisibility(View.VISIBLE);
    }

    public void show() {
        setLayout();
        dialog.show();
    }
}

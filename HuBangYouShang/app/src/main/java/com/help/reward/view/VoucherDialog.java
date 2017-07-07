package com.help.reward.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.help.reward.R;
import com.help.reward.adapter.VoucherAdapter;
import com.help.reward.bean.VoucherBean;
import com.help.reward.utils.DisplayUtil;
import com.idotools.utils.DeviceUtil;
import com.idotools.utils.MetricsUtils;
import com.idotools.utils.MobileScreenUtils;

import java.util.List;

/**
 * Created by wuxiaojun on 17-7-7.
 */

public class VoucherDialog {

    private Context mContext;
    private Dialog mDialog;
    private Button btn_close;
    private CheckBox id_cb_use_voucher;
    private LRecyclerView mLRecyclerView;

    private List<VoucherBean> mList;
    private VoucherAdapter mAdapter;

    public VoucherDialog(Context context, List<VoucherBean> list) {
        this.mContext = context;
        this.mList = list;
        build();
    }


    public void build() {
        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        View view = getView();
        setDialog(view);
    }

    private void setDialog(View view) {
        mDialog.setContentView(view);
        Window wm = mDialog.getWindow();
        wm.setGravity(Gravity.BOTTOM | Gravity.LEFT);
        WindowManager.LayoutParams attributes = wm.getAttributes();
        attributes.width = MobileScreenUtils.getScreenWidth(mContext);
        attributes.height = DisplayUtil.dip2px(mContext, 350);
        attributes.x = 0;
        attributes.y = 0;
        wm.setAttributes(attributes);
    }

    @NonNull
    private View getView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_voucher, null);
        id_cb_use_voucher = (CheckBox) view.findViewById(R.id.id_cb_use_voucher);
        mLRecyclerView = (LRecyclerView) view.findViewById(R.id.id_recyclerview);
        btn_close = (Button) view.findViewById(R.id.btn_close);
        initListener();
        return view;
    }

    private void initListener() {
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        mAdapter = new VoucherAdapter(mContext);
        LRecyclerViewAdapter lRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mLRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mLRecyclerView.setAdapter(lRecyclerViewAdapter);
        mLRecyclerView.setLoadMoreEnabled(false);
        mLRecyclerView.setPullRefreshEnabled(false);
        mAdapter.setDataList(mList);
    }

    public void showDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }


}

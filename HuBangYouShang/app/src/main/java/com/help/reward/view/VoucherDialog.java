package com.help.reward.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.recyclerview.LRecyclerView;
import com.base.recyclerview.LRecyclerViewAdapter;
import com.help.reward.R;
import com.help.reward.adapter.VoucherAdapter;
import com.help.reward.bean.VoucherBean;
import com.help.reward.utils.DisplayUtil;
import com.idotools.utils.DeviceUtil;
import com.idotools.utils.LogUtils;
import com.idotools.utils.MetricsUtils;
import com.idotools.utils.MobileScreenUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 可用优惠卷
 * Created by wuxiaojun on 17-7-7.
 */

public class VoucherDialog implements VoucherAdapter.OnVoucherCheckedChangedListener {

    private Context mContext;
    private Dialog mDialog;
    private Button btn_close;
    private CheckBox id_cb_use_voucher;
    private RecyclerView mLRecyclerView;

    private List<VoucherBean> mList;
    private VoucherAdapter mAdapter;
    private HashMap<String, String> mVoucherMap;

    public VoucherDialog(Context context, List<VoucherBean> list, HashMap<String, String> mVoucherMap) {
        this.mContext = context;
        this.mList = list;
        this.mVoucherMap = mVoucherMap;
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
        mLRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview);
        btn_close = (Button) view.findViewById(R.id.btn_close);
        tv_type = (TextView) view.findViewById(R.id.tv_type);
        initListener();
        return view;
    }

    private LinearLayoutManager mLinearLayoutManager;
    private int mCurrentPosition = 0;
    private TextView tv_type;
    private int mTypeHeight;

    private void initListener() {
        mAdapter = new VoucherAdapter(mContext, mList);
        mAdapter.setOnVoucherCheckedChangedListener(this);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLRecyclerView.setLayoutManager(mLinearLayoutManager);
        mLRecyclerView.setAdapter(mAdapter);
        initDismissListener();
        initCheckListener();
        initScrollListener();
    }

    @Override
    public void onCheckedChangedListener(String voucherId) {
        if (!TextUtils.isEmpty(voucherId)) {
            if (mVoucherMap.containsKey(voucherId)) {
                mVoucherMap.remove(voucherId);
            }
        }
    }

    private void initDismissListener() {
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_cb_use_voucher.isChecked()) {
                    // 不使用优惠卷,则需要把hashmap集合中可能包含的key删除掉
                    Iterator<HashMap.Entry<String, String>> iterator = mVoucherMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        HashMap.Entry<String, String> map = iterator.next();
                        String key = map.getKey();
                        boolean removeFlag = false;
                        for (VoucherBean voucherBean : mList) {
                            if (voucherBean.voucher_id.equals(key)) {
                                removeFlag = true;
                                break;
                            }
                        }
                        if (removeFlag) {
                            iterator.remove();
                        }
                    }
                    LogUtils.e("不使用优惠卷,map集合长度是" + mVoucherMap.size());

                } else {
                    for (VoucherBean voucherBean : mList) {
                        if (voucherBean.isChecked) {
//                            LogUtils.e("选中的优惠卷id是：" + voucherBean.voucher_id + "--" + voucherBean.voucher_store_id + "--" + voucherBean.voucher_price);
                            if (mVoucherMap.containsKey(voucherBean.voucher_id)) {
                                LogUtils.e("选中的优惠劵id是:" + voucherBean.voucher_id);
                                mVoucherMap.remove(voucherBean.voucher_id);
                            }
                            mVoucherMap.put(voucherBean.voucher_id, voucherBean.voucher_t_id + "|" + voucherBean.voucher_store_id + "|" + voucherBean.voucher_price + ",");
                            break;
                        }
                    }
                }
                dismissDialog();
            }
        });
    }

    private void initCheckListener() {
        id_cb_use_voucher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 不使用优惠卷
                    if (mList != null) {
                        for (VoucherBean voucherBean : mList) {
                            voucherBean.isChecked = false;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /****
     * 监听recyclerview的滑动
     * 有两种type ,type1显示一种ui type2显示另外一种ui
     * 初始化进来的时候根据每一个bean的不同type显示不同ui，如果currentPosition跟currentPosition-1的type相同那么使用相同的ui,如果不同，则显示不一样的ui
     * 当他们滑动的时候时刻判断currentPosition和上一个的type是否一样，如果不一样，则需要将ui进行改变
     */
    private void initScrollListener() {
        mLRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mTypeHeight = tv_type.getMeasuredHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int itemTypeCurrent = mAdapter.getItemViewType(mCurrentPosition);
                if (mAdapter.getItemCount() > 1) {
                    int itemTypeNext = mAdapter.getItemViewType(mCurrentPosition + 1);
                    if (itemTypeCurrent != itemTypeNext) {
                        // 跟据mCurrentPosition查找当前显示的View
                        View itemView = mLinearLayoutManager.findViewByPosition(mCurrentPosition + 1);
                        if (itemView != null) {
                            if (itemView.getTop() <= mTypeHeight) {
                                // 需要滑动悬浮view
                                tv_type.setTranslationY((itemView.getTop() - mTypeHeight));
                            } else {
                                tv_type.setY(0);
                            }
                        }
                    }
                }

                if (mCurrentPosition != mLinearLayoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
                    tv_type.setY(0);
                    if (mList.get(mCurrentPosition).useable) {
                        tv_type.setText(R.string.string_available_coupon);
                    } else {
                        tv_type.setText(R.string.string_not_available_coupon);
                    }
                }

            }
        });

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

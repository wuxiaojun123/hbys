package com.help.reward.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.help.reward.R;
import com.help.reward.bean.MessageBean;
import com.help.reward.utils.GlideUtils;
import com.help.reward.utils.ScreenUtils;
import com.help.reward.view.SlidingButtonView;
import com.idotools.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MJJ on 2015/7/25.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> implements SlidingButtonView.IonSlidingButtonListener {

    private Context mContext;
    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private SlidingButtonView mMenu = null;
    private List<MessageBean> mDatas = new ArrayList<>();
    String type;

    public MessageAdapter(Context context, String type) {
        this.type = type;
        mContext = context;
        mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        //设置内容布局的宽为屏幕宽度

        holder.layout_content.getLayoutParams().width = ScreenUtils.getScreenWidth(mContext);

        holder.tv_title.setText(mDatas.get(position).message_title);
        holder.tv_body.setText(mDatas.get(position).message_body);
        holder.tv_time.setText(DateUtil.getDateToString(mDatas.get(position).message_time*1000));
        if (holder.tv_order != null) {
            holder.tv_order.setText(mDatas.get(position).message_orderid);
        }
        if (holder.iv_icon != null) {
            GlideUtils.loadImage(mDatas.get(position).message_image, holder.iv_icon);
        }
        holder.layout_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = holder.getLayoutPosition();
                    mIDeleteBtnClickListener.onItemClick(v, n);
                }
            }
        });
        holder.btn_Delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();
                mIDeleteBtnClickListener.onDeleteBtnCilck(v, n);
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        int layoutId = 0;
        if ("5".equals(type)) {
            layoutId = R.layout.item_message_exchange;
        } else if ("1".equals(type)) {
            layoutId = R.layout.item_message_system;
        } else {
            layoutId = R.layout.item_message_post;
        }
        View view = LayoutInflater.from(mContext).inflate(layoutId, arg0, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_delete)
        TextView btn_Delete;
        @BindView(R.id.layout_content)
        RelativeLayout layout_content;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_body)
        TextView tv_body;
        @BindView(R.id.tv_time)
        TextView tv_time;

        TextView tv_order;
        ImageView iv_icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if("5".equals(type)){
                tv_order= (TextView) itemView.findViewById(R.id.tv_order);
                iv_icon=(ImageView)itemView.findViewById(R.id.iv_icon);
            }
            ((SlidingButtonView) itemView).setSlidingButtonListener(MessageAdapter.this);
        }
    }

    public void addAll(List<MessageBean> list) {
        int lastIndex = this.mDatas.size();
        if (this.mDatas.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void setDatas(List<MessageBean> list) {
        if (list != null)
            this.mDatas = list;
        else
            mDatas = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);

    }

    public List<MessageBean> getDataList() {
        return mDatas;
    }

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

    /**
     * 滑动或者点击了Item监听
     *
     * @param slidingButtonView
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if (menuIsOpen()) {
            if (mMenu != slidingButtonView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }

    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }


    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);

        void onDeleteBtnCilck(View view, int position);
    }
}


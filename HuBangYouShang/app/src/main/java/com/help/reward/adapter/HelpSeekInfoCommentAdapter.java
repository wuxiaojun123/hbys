package com.help.reward.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.activity.HelpSeekCommentDetailActivity;
import com.help.reward.activity.HelpSeekInfoActivity;
import com.help.reward.activity.OrderDetailsActivity;
import com.help.reward.adapter.viewholder.SuperViewHolder;
import com.help.reward.bean.HelpSeekCommentBean;
import com.help.reward.bean.Response.StringResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ActivitySlideAnim;
import com.help.reward.utils.DialogUtil;
import com.help.reward.utils.GlideUtils;
import com.help.reward.utils.IntentUtil;
import com.help.reward.view.HelpSeekInfoCommentMorePop;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.DateUtil;
import com.idotools.utils.ToastUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 *
 */

public class HelpSeekInfoCommentAdapter extends BaseRecyclerAdapter<HelpSeekCommentBean> {
    Activity mActivity;
    boolean isMyPost = false;//我是不是发帖人
    String status;
    boolean hasmore;

    public HelpSeekInfoCommentAdapter(Activity context) {
        super(context);
        this.mActivity = context;
    }

    public void setIsMyPost(boolean isMyPost) {
        this.isMyPost = isMyPost;
    }

    public void setHasmore(boolean hasmore) {
        this.hasmore = hasmore;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int getItemCount() {
        if (mDataList.size() == 0) {
            return 1;
        }
        return mDataList.size();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_helpinfocomment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        if (mDataList.size() == 0) {
            holder.getView(R.id.tv_empty).setVisibility(View.VISIBLE);
            holder.getView(R.id.comment_layout).setVisibility(View.GONE);
            return;
        }
        holder.getView(R.id.tv_empty).setVisibility(View.GONE);
        holder.getView(R.id.comment_layout).setVisibility(View.VISIBLE);
        final HelpSeekCommentBean item = mDataList.get(position);

        LinearLayout title_layout = holder.getView(R.id.title_layout);
        title_layout.setVisibility(View.GONE);
        TextView tv_label = holder.getView(R.id.tv_label);
        if (position == 0) {
            if (!"0".equalsIgnoreCase(item.best_re)) {
                tv_label.setText("最佳跟帖");
            } else {
                tv_label.setText("相关跟帖");
            }
            title_layout.setVisibility(View.VISIBLE);
        }
        if (position == 1 && !"0".equalsIgnoreCase(mDataList.get(0).best_re)) {
            tv_label.setText("其它跟帖");
            title_layout.setVisibility(View.VISIBLE);
        }


        ImageView iv_helpinfo_headimg = holder.getView(R.id.iv_helpinfo_headimg);
        GlideUtils.loadCircleImage(item.member_avatar, iv_helpinfo_headimg);
        IntentUtil.startPersonalHomePage(mActivity,item.u_id,iv_helpinfo_headimg);

        TextView tv_helpinfo_uname = holder.getView(R.id.tv_helpinfo_uname);
        tv_helpinfo_uname.setText(item.u_name);
        TextView tv_helpinfo_date = holder.getView(R.id.tv_helpinfo_date);
        tv_helpinfo_date.setText(DateUtil.getDateToString(item.create_time + ""));

        TextView tv_helpinfo_count = holder.getView(R.id.tv_helpinfo_count);
        tv_helpinfo_count.setText("跟帖" + item.parent);
        tv_helpinfo_count.setVisibility(View.GONE);
        TextView tv_helpinfo_content = holder.getView(R.id.tv_helpinfo_content);
        tv_helpinfo_content.setText(item.content);
        LinearLayout bottom_layout = holder.getView(R.id.bottom_layout);

        ImageView iv_fabulous = holder.getView(R.id.iv_fabulous);
        if ("0".equals(item.useful)) {
            iv_fabulous.setImageResource(R.mipmap.fabulous_normal);
        } else {
            iv_fabulous.setImageResource(R.mipmap.fabulous_selected);
        }

        ImageView iv_awful = holder.getView(R.id.iv_awful);
        if ("0".equals(item.useless)) {
            iv_awful.setImageResource(R.mipmap.awful_normal);
        } else {
            iv_awful.setImageResource(R.mipmap.awful_selected);
        }

        if (!"0".equals(item.useful) || !"0".equals(item.useless)||!isMyPost) {
            iv_fabulous.setEnabled(false);
            iv_awful.setEnabled(false);
        } else {
            iv_fabulous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogUtil.showConfirmCancleDialog(mActivity,"您将增加此用户0.1帮助人数，确定么？",new DialogUtil.OnDialogUtilClickListener(){

                        @Override
                        public void onClick(boolean isLeft) {
                            if(isLeft) {
                                setUseFulOrUseless("set_useful", item.id, position);
                            }
                        }
                    });

                }
            });
            iv_awful.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogUtil.showConfirmCancleDialog(mActivity,"您将减少此用户0.1帮助人数，确定么？",new DialogUtil.OnDialogUtilClickListener(){

                        @Override
                        public void onClick(boolean isLeft) {
                            if(isLeft) {
                                setUseFulOrUseless("set_useless", item.id, position);
                            }
                        }
                    });

                }
            });
        }

      final ImageView iv_reply = holder.getView(R.id.iv_reply);

        if ("true".equalsIgnoreCase(item.new_unread)) {
            iv_reply.setImageResource(R.mipmap.reply_selected);
        } else {
            iv_reply.setImageResource(R.mipmap.reply_normal);
        }
        iv_reply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //继续跟帖
                Intent intentDetail = new Intent(mActivity, HelpSeekCommentDetailActivity.class);
                intentDetail.putExtra("id", item.id);
                intentDetail.putExtra("type", item.type);
                intentDetail.putExtra("post_id", item.post_id);
                intentDetail.putExtra("status", status);
                mActivity.startActivity(intentDetail);
                ActivitySlideAnim.slideInAnim(mActivity);
                if ("true".equalsIgnoreCase(item.new_unread)&&!"结帖".equals(status)) {
                    iv_reply.setImageResource(R.mipmap.reply_normal);
                    item.new_unread="false";
                }

            }
        });
        //点击内容也可以跳转回复
        tv_helpinfo_content.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //继续跟帖
                Intent intentDetail = new Intent(mActivity, HelpSeekCommentDetailActivity.class);
                intentDetail.putExtra("id", item.id);
                intentDetail.putExtra("type", item.type);
                intentDetail.putExtra("post_id", item.post_id);
                intentDetail.putExtra("status", status);
                mActivity.startActivity(intentDetail);
                ActivitySlideAnim.slideInAnim(mActivity);
                if ("true".equalsIgnoreCase(item.new_unread)&&!"结帖".equals(status)) {
                    iv_reply.setImageResource(R.mipmap.reply_normal);
                }

            }
        });
        final ImageView iv_more = holder.getView(R.id.iv_more);
        iv_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new HelpSeekInfoCommentMorePop(status, item.post_id, item.id, item.u_name, item.content,"help").showPopupWindow(mActivity, iv_more);

            }
        });
        ImageView iv_helpinfo_private = holder.getView(R.id.iv_helpinfo_private);
        if ("1".equals(item.type)) {
            iv_helpinfo_private.setVisibility(View.VISIBLE);
        } else {
            iv_helpinfo_private.setVisibility(View.GONE);
        }
        if (isMyPost) {
            iv_more.setVisibility(View.VISIBLE);
        } else {
            iv_more.setVisibility(View.GONE);
        }
        TextView tv_nomore = holder.getView(R.id.tv_nomore);
        if (!hasmore && position == mDataList.size() - 1) {
            tv_nomore.setVisibility(View.VISIBLE);
        } else {
            tv_nomore.setVisibility(View.GONE);
        }

        if ("结帖".equals(status)) {
            iv_reply.setImageResource(R.mipmap.review_normal);
            iv_fabulous.setEnabled(false);
            iv_awful.setEnabled(false);
           // if ("0".equals(item.complained)) {
//                iv_fabulous.setVisibility(View.GONE);
            //   }
            //    if ("0".equals(item.given_points)) {
//                iv_awful.setVisibility(View.GONE);
            //    }
        }
        if(!isMyPost){
            iv_fabulous.setVisibility(View.GONE);
            iv_awful.setVisibility(View.GONE);
        }
    }

    private void setUseFulOrUseless(final String op, String parent_id, final int position) {
        MyProcessDialog.showDialog(mContext);
        HelpNetwork
                .getHelpApi()
                .getSetUseFulOrUselessBean(App.APP_CLIENT_KEY, op, parent_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<StringResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(StringResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            HelpSeekCommentBean item = mDataList.get(position);
                            if ("set_useful".equalsIgnoreCase(op)) {
                                item.useful = "1";
                            } else {
                                item.useless = "1";
                            }
                            notifyDataSetChanged();
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }
}
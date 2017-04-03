package com.reward.help.merchant.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.EMCallBack;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseBaseFragment;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.idotools.utils.DataCleanManager;
import com.idotools.utils.ToastUtils;
import com.reward.help.merchant.App;
import com.reward.help.merchant.R;
import com.reward.help.merchant.activity.AboutUsActivity;
import com.reward.help.merchant.activity.CheckPhoneNumberActivity;
import com.reward.help.merchant.activity.FeedbackActivity;
import com.reward.help.merchant.activity.LoginActivity;
import com.reward.help.merchant.activity.MainActivity;
import com.reward.help.merchant.activity.ProfileActivity;
import com.reward.help.merchant.activity.StoreInfoActivity;
import com.reward.help.merchant.bean.LoginBean;
import com.reward.help.merchant.chat.DemoHelper;
import com.reward.help.merchant.utils.Constant;
import com.reward.help.merchant.utils.GlideUtils;
import com.reward.help.merchant.utils.SpUtils;
import com.reward.help.merchant.view.AlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MineFragment extends EaseBaseFragment implements View.OnClickListener{

    @BindView(R.id.iv_title_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.iv_my_photo)
    ImageView mIvMyPhoto;
    @BindView(R.id.tv_usrname)
    TextView mTvName;

    @BindView(R.id.iv_store_photo)
    ImageView mIvMyStorePhoto;
    @BindView(R.id.tv_store_name)
    TextView mTvMyStoreName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    protected void initView() {
        mIvBack.setVisibility(View.GONE);
        mTvRight.setVisibility(View.GONE);
        mTvTitle.setText(getText(R.string.mine_title));


        String username = DemoHelper.getInstance().getCurrentUsernName();
        GlideUtils.setUserAvatar(getContext(), username, mIvMyPhoto);
        //EaseUserUtils.setUserNick(username, mTvName);
        LoginBean userInfo = SpUtils.getUserInfo();

        if (userInfo != null && !TextUtils.isEmpty(userInfo.seller_name)) {
            mTvName.setText(userInfo.seller_name);
        } else {
            EaseUserUtils.setUserNick(username, mTvName);
        }

        if (userInfo != null && !TextUtils.isEmpty(userInfo.store_name)) {
            mTvMyStoreName.setText(String.format(getString(R.string.mine_store_info_tip), userInfo.store_name));
        }

        GlideUtils.loadBoundImage("",mIvMyStorePhoto);
    }




    @Override
    protected void setUpView() {

    }

    @OnClick({R.id.rl_my_info,R.id.rl_my_store_info,R.id.tv_mine_modify_password,R.id.tv_mine_clear_cache
            ,R.id.tv_mine_customer_center,R.id.tv_mine_feek_back,R.id.tv_mine_about_us,R.id.rl_mine_logout})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_my_info:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;
            case R.id.rl_my_store_info:
                startActivity(new Intent(getActivity(), StoreInfoActivity.class));
                break;
            case R.id.tv_mine_modify_password:
                //TODO modify password
                startActivity(new Intent(getActivity(),CheckPhoneNumberActivity.class));
                break;
            case R.id.tv_mine_clear_cache:
                //TODO clear cache
                cleanCache();
                break;
            case R.id.tv_mine_customer_center:
                //TODO call somebody
                call();
                break;
            case R.id.tv_mine_feek_back:
                //TODO feekback
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.tv_mine_about_us:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;

            case R.id.rl_mine_logout:
                logout();
                break;
        }

    }

    private void call() {
        new AlertDialog(getActivity())
                .builder()
                .setTitle("系统提示")
                .setMsg("025-58840881")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:025-58840881"));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }



    private void cleanCache() {
        new AlertDialog(getActivity())
                .builder()
                .setTitle("系统提示")
                .setMsg("是否清除所有图片缓存")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataCleanManager.cleanInternalCache(getActivity());
                        DataCleanManager.cleanCustomCache(Constant.ROOT);
                        DataCleanManager.cleanFiles(getActivity());
                        DataCleanManager.cleanExternalCache(getActivity());

                        ToastUtils.show(getActivity(),"清除成功");
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }


    /**
     * 退出登录
     */
 private void logout() {
     final ProgressDialog pd = new ProgressDialog(getActivity());
     String st = getResources().getString(R.string.Are_logged_out);
     pd.setMessage(st);
     pd.setCanceledOnTouchOutside(false);
     pd.show();
     DemoHelper.getInstance().logout(false,new EMCallBack() {

         @Override
         public void onSuccess() {
             getActivity().runOnUiThread(new Runnable() {
                 public void run() {
                     pd.dismiss();
                     // show login screen
                     ((MainActivity) getActivity()).finish();
                     startActivity(new Intent(getActivity(), LoginActivity.class));
                     App.setAppClientCookie("");
                     App.setAppClientKey("");

                 }
             });
         }

         @Override
         public void onProgress(int progress, String status) {

         }

         @Override
         public void onError(int code, String message) {
             getActivity().runOnUiThread(new Runnable() {

                 @Override
                 public void run() {
                     // TODO Auto-generated method stub
                     pd.dismiss();
                     Toast.makeText(getActivity(), "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                 }
             });
         }
     });
    }
}

package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.adapter.PropertyAdapter;
import com.help.reward.bean.PropertyBean;
import com.help.reward.bean.Response.BaseResponse;
import com.help.reward.network.ShopcartNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.GlideUtils;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ADBrian on 15/04/2017.
 */

public class GoodPropertyActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.iv_good_pic)
    ImageView mIvPic;

    @BindView(R.id.tv_good_price)
    TextView mTvPrice;

    @BindView(R.id.tv_good_num)
    TextView mTvNum;

    @BindView(R.id.tv_good_pro)
    TextView mTvProtip;

    @BindView(R.id.lv_property)
    ListView mList;

    @BindView(R.id.ib_dialog_number_add)
    ImageButton mNumAdd;

    @BindView(R.id.ib_dialog_number_des)
    ImageButton mNumDes;

    @BindView(R.id.et_dialog_number_show)
    EditText mNumShow;


    private  PropertyBean propertyBean;
    private int numShow = 1;
    private  int Max_num = 99;
    private String goodsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //窗口对齐屏幕宽度
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
        setFinishOnTouchOutside(true);
        setContentView(R.layout.activity_good_property);
        ButterKnife.bind(this);

        propertyBean = (PropertyBean) getIntent().getSerializableExtra("goods_property");
        goodsId  = propertyBean.getGoods_id();
        initView();
    }

    private void initView() {
        PropertyAdapter propertyAdapter = new PropertyAdapter(GoodPropertyActivity.this);
        mList.setAdapter(propertyAdapter);
        if (propertyBean != null) {
            GlideUtils.loadImage(propertyBean.getGoods_pic(),mIvPic);
            mTvPrice.setText("¥ "+ propertyBean.getGoods_price());
            mTvProtip.setText(propertyBean.getTip());
            mTvNum.setText("库存" + propertyBean.getGoods_num());
            propertyAdapter.setList(propertyBean.getPropertyList());
        }


        mNumAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newNum = numShow + 1;
                setButtonState(newNum,mNumAdd,mNumDes);
                mNumShow.setText(numShow + "");
                mNumShow.setSelection(Integer.toString(numShow).length());
            }
        });

        mNumDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newNum = numShow - 1;
                setButtonState(newNum,mNumAdd,mNumDes);
                mNumShow.setText(numShow + "");
                mNumShow.setSelection(Integer.toString(numShow).length());

            }
        });

        mNumShow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int newNum = -1;
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    newNum = Integer.parseInt(s.toString().trim());
                    if (newNum > Max_num) {
                        mNumShow.setText(Max_num +"");
                    }
                } else {
                    newNum = 1;
                }
                setButtonState(newNum,mNumAdd,mNumDes);
            }
        });
        setButtonState(numShow,mNumAdd,mNumDes);
        mNumShow.setText(numShow +"");
    }


    private void setButtonState(int num, ImageButton mIbNumAdd, ImageButton mIbNumDes) {
        if (num <= 1) {
            numShow = 1;
            mIbNumAdd.setEnabled(true);
            mIbNumDes.setEnabled(false);
        } else {
            if (num >= Max_num) {
                numShow = Max_num;
                mIbNumAdd.setEnabled(false);
                mIbNumDes.setEnabled(true);
            } else {
                numShow = num;
                mIbNumAdd.setEnabled(true);
                mIbNumDes.setEnabled(true);
            }
        }
    }

    @OnClick({R.id.btn_close,R.id.tv_goodinfo_shopcart_add,R.id.tv_goodinfo_buy})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                this.finish();
                break;
            case R.id.tv_goodinfo_shopcart_add:
                addToShopcart();
                break;
            case R.id.tv_goodinfo_buy:
                Intent intent = new Intent(GoodPropertyActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("cart_id",goodsId+"|"+numShow);
                intent.putExtra("if_cart","0");
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        propertyBean.setSelectNum(numShow +"");
        Intent intent = new Intent();
        intent.putExtra("selectInfo",propertyBean);
        setResult(RESULT_OK,intent);

    }

    private void addToShopcart() {
        if (!TextUtils.isEmpty(goodsId)) {
            MyProcessDialog.showDialog(mContext);
            ShopcartNetwork.getShopcartCookieApi().getShopcartAdd(App.APP_CLIENT_KEY,goodsId,numShow +"")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse>() {

                        @Override
                        public void onError(Throwable e) {
                            MyProcessDialog.closeDialog();
                            e.printStackTrace();
                            if (e instanceof UnknownHostException) {
                                ToastUtils.show(mContext, "请求到错误服务器");
                            } else if (e instanceof SocketTimeoutException) {
                                ToastUtils.show(mContext, "请求超时");
                            }
                        }

                        @Override
                        public void onNext(BaseResponse baseResponse) {
                            MyProcessDialog.closeDialog();
                            if (baseResponse.code == 200) {
                                ToastUtils.show(mContext, "加入购物车成功");
                            } else {
                                ToastUtils.show(mContext, baseResponse.msg);
                            }
                        }
                    });
        }
    }
}

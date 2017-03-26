package com.help.reward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.AreaBean;
import com.help.reward.bean.Response.AreaResponse;
import com.help.reward.bean.HelpBoardBean;
import com.help.reward.bean.Response.HelpBoardResponse;
import com.help.reward.bean.Response.HelpSubResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.utils.ChooseCameraPopuUtils;
import com.help.reward.utils.DealChoosePicUtils;
import com.help.reward.utils.PickerUtils;
import com.help.reward.utils.StringUtils;
import com.help.reward.utils.UploadImageUtils;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.ToastUtils;
import com.lvfq.pickerview.TimePickerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.help.reward.R.id.tv_release_help_data;

/**
 * 发布求助帖
 * Created by MXY on 2017/2/19.
 */

public class ReleaseHelpActivity extends BaseActivity implements DealChoosePicUtils.DealChoosePicListener, UploadImageUtils.OnUploadImageListener {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_release_help_title)
    EditText etReleaseHelpTitle;
    @BindView(R.id.tv_release_help_address)
    TextView tvReleaseHelpAddress;
    @BindView(R.id.tv_release_help_type)
    TextView tvReleaseHelpType;
    @BindView(tv_release_help_data)
    TextView tvReleaseHelpData;
    @BindView(R.id.et_release_help_content)
    EditText etReleaseHelpContent;

    @BindView(R.id.tv_release_help_score)
    EditText tv_release_help_score;

    @BindView(R.id.iv_release_addphoto)
    ImageView ivReleaseAddphoto;
    DealChoosePicUtils dealChoosePicUtils;
    ArrayList<AreaBean> cityList = new ArrayList<>();
    ArrayList<HelpBoardBean> boardList = new ArrayList<>();
    String area_id;
    String board_id;
    UploadImageUtils uploadImagUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_help);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        tvTitle.setText("发布求助帖");
        tvTitleRight.setText("发布");
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_right, R.id.tv_release_help_address, R.id.tv_release_help_type,
            tv_release_help_data, R.id.iv_release_addphoto})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                subHelp();
                break;
            case R.id.tv_release_help_address:
                getAreaData();
                break;
            case R.id.tv_release_help_type:
                getBoardData();
                // 单项选择
//                for (int i = 0; i <= 10; i++) {
//                    HelpBoardBean h = new HelpBoardBean();
//                    h.board_id = i + "";
//                    h.board_name = "fenlei" + i;
//                    boardList.add(h);
//                }
//                PickerUtils.alertBottomWheelOption(this, boardList, new PickerUtils.OnWheelViewClick() {
//                    @Override
//                    public void onClick(View view, int postion) {
//                        tvReleaseHelpType.setText(boardList.get(postion).board_name);
//                        board_id = boardList.get(postion).board_id;
//                    }
//                });
                break;
            case tv_release_help_data:
                PickerUtils.alertTimerPicker(this, TimePickerView.Type.YEAR_MONTH_DAY, "yyyy-MM-dd", new PickerUtils.TimerPickerCallBack() {
                    @Override
                    public void onTimeSelect(String date) {
                        tvReleaseHelpData.setText(date);
                    }
                });
                break;
            case R.id.iv_release_addphoto:
                ChooseCameraPopuUtils.showPopupWindow(this, v);
                if (dealChoosePicUtils == null) {
                    dealChoosePicUtils = new DealChoosePicUtils(this);
                    dealChoosePicUtils.setDealChoosePicListener(this);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (dealChoosePicUtils != null)
            dealChoosePicUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finishDeal(String path, int type) {
        // TODO Auto-generated method stub
        ToastUtils.show(this, path);
        if(uploadImagUtils==null){
            uploadImagUtils = new UploadImageUtils(mContext);
            uploadImagUtils.setOnUploadImageListener(this);
        }
//        uploadImagUtils.upImage(path,"seek_help");
    }

    private void subHelp() {
        String title = etReleaseHelpTitle.getText().toString().trim();
        String end_time = tvReleaseHelpData.getText().toString().trim();
        String content = etReleaseHelpContent.getText().toString().trim();
        String score = tv_release_help_score.getText().toString().trim();
        if (!StringUtils.checkStr(title)) {
            ToastUtils.show(mContext, "请输入标题");
            return;
        }
        if (!StringUtils.checkStr(content)) {
            ToastUtils.show(mContext, "请输入内容");
            return;
        }
        if (!StringUtils.checkStr(score) || Integer.parseInt(score) < 10) {
            ToastUtils.show(mContext, "请输入悬赏分，最少10分");
            return;
        }
        if (!StringUtils.checkStr(board_id)) {
            ToastUtils.show(mContext, "请选择分类");
            return;
        }
        subHelpData(title, content, score, end_time);
    }

    protected Subscription subscribe;

    private void subHelpData(String title, String content, String score, String end_time) {
        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
                .subHelpSeekBean(App.APP_CLIENT_KEY,"release_post", board_id, title, content, area_id, end_time, score, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpSubResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(HelpSubResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            ToastUtils.show(mContext, response.data.id + "[[[[[[[");
                            Intent intent = new Intent(mContext, HelpInfoActivity.class);
                            intent.putExtra("id", response.data.id);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });

    }


    /**
     * 获取地址
     */
    private void getAreaData() {
        if (cityList.size() != 0) {
            PickerUtils.alertBottomWheelOption(this, cityList, new PickerUtils.OnWheelViewClick() {
                @Override
                public void onClick(View view, int postion) {
                    tvReleaseHelpAddress.setText(cityList.get(postion).area_name);
                    area_id = cityList.get(postion).area_id;
                }
            });
            return;
        }

        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpNoCookieApi()
                .getAreaBean(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AreaResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(AreaResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            cityList.addAll(response.data.area_list);
                            PickerUtils.alertBottomWheelOption(mContext, cityList, new PickerUtils.OnWheelViewClick() {
                                @Override
                                public void onClick(View view, int postion) {
                                    tvReleaseHelpAddress.setText(cityList.get(postion).area_name);
                                    area_id = cityList.get(postion).area_id;
                                }
                            });
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }

    /**
     * 获取分类
     */
    private void getBoardData() {
        if (boardList.size() != 0) {
            PickerUtils.alertBottomWheelOption(this, boardList, new PickerUtils.OnWheelViewClick() {
                @Override
                public void onClick(View view, int postion) {
                    tvReleaseHelpType.setText(boardList.get(postion).board_name);
                    board_id = boardList.get(postion).board_id;
                }
            });
            return;
        }

        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpNoCookieApi()
                .getBoardBean(App.APP_CLIENT_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<HelpBoardResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyProcessDialog.closeDialog();
                        ToastUtils.show(mContext, R.string.string_error);
                    }

                    @Override
                    public void onNext(HelpBoardResponse response) {
                        MyProcessDialog.closeDialog();
                        if (response.code == 200) {
                            boardList.addAll(response.data.board_list);
                            PickerUtils.alertBottomWheelOption(mContext, cityList, new PickerUtils.OnWheelViewClick() {
                                @Override
                                public void onClick(View view, int postion) {
                                    tvReleaseHelpAddress.setText(cityList.get(postion).area_name);
                                    area_id = cityList.get(postion).area_id;
                                }
                            });
                        } else {
                            ToastUtils.show(mContext, response.msg);
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onLoadError() {
        //图片上传失败
    }

    @Override
    public void onLoadSucced(String default_dir, String file_name) {
        //图片上传成功
        Log.e("onLoadSucced",default_dir+"==="+file_name);
    }
}

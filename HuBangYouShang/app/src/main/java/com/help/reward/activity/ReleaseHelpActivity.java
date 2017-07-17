package com.help.reward.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.AddDayBean;
import com.help.reward.bean.AreaBean;
import com.help.reward.bean.HelpBoardBean;
import com.help.reward.bean.Response.AreaResponse;
import com.help.reward.bean.Response.HelpBoardResponse;
import com.help.reward.bean.Response.HelpSubResponse;
import com.help.reward.network.HelpNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.help.reward.rxbus.RxBus;
import com.help.reward.rxbus.event.type.HelpCommitRxbusType;
import com.help.reward.rxbus.event.type.UpdateLoginDataRxbusType;
import com.help.reward.utils.ChooseCameraPopuUtils;
import com.help.reward.utils.DialogUtil;
import com.help.reward.utils.GlideUtils;
import com.help.reward.utils.PickerUtils;
import com.help.reward.utils.StringUtils;
import com.help.reward.view.MyProcessDialog;
import com.idotools.utils.DateUtil;
import com.idotools.utils.LogUtils;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

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

public class ReleaseHelpActivity extends BaseActivity {

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
    ArrayList<AreaBean> cityList = new ArrayList<>();
    ArrayList<HelpBoardBean> boardList = new ArrayList<>();
    //    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<AddDayBean> dateDayList = new ArrayList<>();
    String end_time; // 有效时间 格式是yyyy/MM/dd HH/mm/ss
    String area_id;
    String board_id;
    ChooseCameraPopuUtils chooseCameraPopuUtils;


    @BindView(R.id.iv_photo1)
    ImageView iv_photo1;
    @BindView(R.id.iv_delete1)
    ImageView iv_delete1;
    @BindView(R.id.iv_photo2)
    ImageView iv_photo2;
    @BindView(R.id.iv_delete2)
    ImageView iv_delete2;
    @BindView(R.id.iv_photo3)
    ImageView iv_photo3;
    @BindView(R.id.iv_delete3)
    ImageView iv_delete3;
    @BindView(R.id.iv_photo4)
    ImageView iv_photo4;
    @BindView(R.id.iv_delete4)
    ImageView iv_delete4;

    @BindView(R.id.tv_photonum)
    TextView tv_photonum;

    List<String> photoUrl = new ArrayList<>();
    List<String> file_names = new ArrayList<>();

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
        chooseCameraPopuUtils = new ChooseCameraPopuUtils(this, "seek_help");
        chooseCameraPopuUtils.setOnUploadImageListener(new ChooseCameraPopuUtils.OnUploadImageListener() {
            @Override
            public void onLoadError() {

            }

            @Override
            public void onLoadSucced(String file_name, String url) {
                photoUrl.add(url);
                file_names.add(file_name);
                showPhoto();
            }
        });
    }

    void showPhoto() {
        iv_photo1.setVisibility(View.GONE);
        iv_delete1.setVisibility(View.GONE);
        iv_photo2.setVisibility(View.GONE);
        iv_delete2.setVisibility(View.GONE);
        iv_photo3.setVisibility(View.GONE);
        iv_delete3.setVisibility(View.GONE);
        iv_photo4.setVisibility(View.GONE);
        iv_delete4.setVisibility(View.GONE);
        ivReleaseAddphoto.setVisibility(View.VISIBLE);
        switch (photoUrl.size()) {
            case 4:
                ivReleaseAddphoto.setVisibility(View.GONE);
                iv_photo4.setVisibility(View.VISIBLE);
                iv_delete4.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(photoUrl.get(3), iv_photo4);
            case 3:
                iv_photo3.setVisibility(View.VISIBLE);
                iv_delete3.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(photoUrl.get(2), iv_photo3);
            case 2:
                iv_photo2.setVisibility(View.VISIBLE);
                iv_delete2.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(photoUrl.get(1), iv_photo2);
            case 1:
                iv_photo1.setVisibility(View.VISIBLE);
                iv_delete1.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(photoUrl.get(0), iv_photo1);
                break;
        }
        tv_photonum.setText("还可上传（" + (4 - photoUrl.size()) + "）张");
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_right, R.id.tv_release_help_address, R.id.tv_release_help_type,
            tv_release_help_data, R.id.iv_release_addphoto, R.id.iv_delete1, R.id.iv_delete2, R.id.iv_delete3, R.id.iv_delete4})
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
                break;
            case tv_release_help_data:
                /*if(dateList.size()<=0){
                    for (int i = 1; i <=15 ; i++) {
                        dateList.add(i+"天");
                    }
                }

                PickerUtils.alertBottomWheelOption(this, dateList, new PickerUtils.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        tvReleaseHelpData.setText(dateList.get(postion));
                    }
                });*/
                if (dateDayList.size() <= 0) {
                    for (int i = 1; i <= 15; i++) {
                        dateDayList.add(new AddDayBean(i + "天", DateUtil.addDayToStringTime(i)));
                    }
                }

                PickerUtils.alertBottomWheelOption(this, dateDayList, new PickerUtils.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        end_time = dateDayList.get(postion).time;
                        tvReleaseHelpData.setText(dateDayList.get(postion).day);
                    }
                });

                break;
            case R.id.iv_release_addphoto:
                chooseCameraPopuUtils.showPopupWindow();
                break;
            case R.id.iv_delete1:
                photoUrl.remove(0);
                file_names.remove(0);
                showPhoto();
                break;
            case R.id.iv_delete2:
                photoUrl.remove(1);
                file_names.remove(1);
                showPhoto();
                break;
            case R.id.iv_delete3:
                photoUrl.remove(2);
                file_names.remove(2);
                showPhoto();
                break;
            case R.id.iv_delete4:
                photoUrl.remove(3);
                file_names.remove(3);
                showPhoto();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (chooseCameraPopuUtils != null)
            chooseCameraPopuUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void subHelp() {
       final String title = etReleaseHelpTitle.getText().toString().trim();
//        String endTimeStr = tvReleaseHelpData.getText().toString().trim();
        final  String content = etReleaseHelpContent.getText().toString().trim();
        final   String score = tv_release_help_score.getText().toString().trim();
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
        if (!StringUtils.checkStr(area_id)) {
            ToastUtils.show(mContext, "请选择地区");
            return;
        }
        if (!StringUtils.checkStr(end_time)) {
            ToastUtils.show(mContext, "请选择有效时间");
            return;
        }
        DialogUtil.showConfirmCancleDialog(this, "您的求助帖即将发布，需扣除"+score+"帮赏分冻结，确认发布？", new DialogUtil.OnDialogUtilClickListener() {
            @Override
            public void onClick(boolean isLeft) {
                if (isLeft) {
                    subHelpData(title, content, score, end_time);
                }
            }
        });

    }

    protected Subscription subscribe;

    private void subHelpData(String title, String content, String score, String end_time) {
        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
                .subHelpSeekBean(App.APP_CLIENT_KEY, "release_post", board_id, title, content, area_id, end_time, score, (String[]) file_names.toArray(new String[file_names.size()]))
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
                            Intent intent = new Intent(mContext, HelpSeekInfoActivity.class);
                            intent.putExtra("id", response.data.id);
                            intent.putExtra("from", "ReleaseHelpActivity");
                            startActivity(intent);
                            finish();
                            RxBus.getDefault().post(new UpdateLoginDataRxbusType(true));
                            RxBus.getDefault().post(new HelpCommitRxbusType());
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
                .getHelpApi()
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
                    board_id = boardList.get(postion).id;
                }
            });
            return;
        }

        MyProcessDialog.showDialog(mContext);
        subscribe = HelpNetwork
                .getHelpApi()
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
                            PickerUtils.alertBottomWheelOption(ReleaseHelpActivity.this, boardList, new PickerUtils.OnWheelViewClick() {
                                @Override
                                public void onClick(View view, int postion) {
                                    tvReleaseHelpType.setText(boardList.get(postion).board_name);
                                    board_id = boardList.get(postion).id;
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

}

package com.help.reward.manager;

import com.help.reward.App;
import com.help.reward.R;
import com.help.reward.bean.AreaBean;
import com.help.reward.bean.BusinessBean;
import com.help.reward.bean.Response.AeraResponse;
import com.help.reward.bean.Response.BusinessResponse;
import com.help.reward.network.PersonalNetwork;
import com.help.reward.network.base.BaseSubscriber;
import com.idotools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chihane.jdaddressselector.AddressProvider;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 2017/4/13.
 */

public class AddressManager implements AddressProvider {

    private AddressProvider.AddressReceiver<Province> addressReceiver;
    private List<AreaBean> provinceList;

    private AddressProvider.AddressReceiver<City> addressCityReceiver;
    private List<AreaBean> mCityList;

    private AddressProvider.AddressReceiver<County> addressCountryReceiver;
    private List<AreaBean> mCountryList;

    @Override
    public void provideProvinces(AddressProvider.AddressReceiver<Province> addressReceiver) {
        this.addressReceiver = addressReceiver;
        if(provinceList == null){
            getShengfen("",0);
        }
    }

    @Override
    public void provideCitiesWith(int provinceId, AddressProvider.AddressReceiver<City> addressReceiver) {
        /*City city = new City();
        city.province_id = provinceId;
        city.id = 2;
        city.name = "测试用城市";*/

        this.addressCityReceiver = addressReceiver;
        getShengfen(provinceId+"",1);
    }

    @Override
    public void provideCountiesWith(int cityId, AddressProvider.AddressReceiver<County> addressReceiver) {
        /*County county = new County();
        county.city_id = cityId;
        county.id = 3;
        county.name = "测试用乡镇";*/
        this.addressCountryReceiver = addressReceiver;
        getShengfen(cityId+"",2);
    }

    @Override
    public void provideStreetsWith(int countyId, AddressProvider.AddressReceiver<Street> addressReceiver) {
        addressReceiver.send(null);
    }

    public void getShengfen(final String area_id, final int flag){
        PersonalNetwork
                .getResponseApi()
                .getAeraResponse("area",area_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AeraResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.show(App.getApplication(), R.string.string_error);
                    }

                    @Override
                    public void onNext(AeraResponse response) {
                        if (response.code == 200) {
                            if (response.data != null) {
                                // 设置用户属性
                                if(flag == 0){
                                    provinceList = response.data.area_list;
                                    List<Province> mList = new ArrayList<Province>();
                                    for (AreaBean bean:provinceList) {
                                        Province province = new Province();
                                        province.id = Integer.parseInt(bean.area_id);
                                        province.name = bean.area_name;
                                        mList.add(province);
                                    }
                                    addressReceiver.send(mList);
                                }else if(flag == 1){
                                    mCityList = response.data.area_list;
                                    List<City> mList = new ArrayList<City>();
                                    for (AreaBean bean:mCityList) {
                                        City city = new City();
                                        city.id = Integer.parseInt(bean.area_id);
                                        city.province_id = Integer.parseInt(area_id);
                                        city.name = bean.area_name;
                                        mList.add(city);
                                    }
                                    addressCityReceiver.send(mList);
                                }else if(flag == 2){
                                    mCountryList = response.data.area_list;
                                    List<County> mList = new ArrayList<County>();
                                    for (AreaBean bean:mCountryList) {
                                        County country = new County();
                                        country.id = Integer.parseInt(bean.area_id);
                                        country.city_id = Integer.parseInt(area_id);
                                        country.name = bean.area_name;
                                        mList.add(country);
                                    }
                                    addressCountryReceiver.send(mList);
                                }
                            }
                        } else {
                            ToastUtils.show(App.getApplication(), response.msg);
                        }
                    }
                });
    }

}

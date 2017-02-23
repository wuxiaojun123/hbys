package com.wxj.hbys.network;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wuxiaojun on 2017/2/23.
 */

public class BaseNetwork {

    public static OkHttpClient mOkHttpClient = new OkHttpClient();

    public static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();

    public static CallAdapter.Factory rxjavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
}

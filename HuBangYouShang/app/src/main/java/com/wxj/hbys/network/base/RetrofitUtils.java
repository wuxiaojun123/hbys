package com.wxj.hbys.network.base;

import com.idotools.utils.LogUtils;
import com.wxj.hbys.App;
import com.wxj.hbys.utils.Constant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wuxiaojun on 2017/2/28.
 */

public class RetrofitUtils {

//    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();

    private static CallAdapter.Factory rxjavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    private static Retrofit mRetrofit;
    private static Retrofit mCookieRetrofit;

    public static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            OkHttpClient mOkHttpClient = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(new GetCookiesInterceptor())
                    .build();

            mRetrofit = new Retrofit.Builder()
                    .client(mOkHttpClient)
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxjavaCallAdapterFactory)
                    .build();
        }
        return mRetrofit;
    }

    public static Retrofit getRetrofitCookie() {
        if (mCookieRetrofit == null) {
            OkHttpClient mOkHttpClient = new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            if(App.APP_CLIENT_COOKIE != null){
                                Request request = chain.request().newBuilder().addHeader("Cookie", App.APP_CLIENT_COOKIE).build();
                                return chain.proceed(request);
                            }else{
                                return null;
                            }
                        }
                    })
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .build();

            mCookieRetrofit = new Retrofit.Builder()
                    .client(mOkHttpClient)
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxjavaCallAdapterFactory)
                    .build();
        }
        return mCookieRetrofit;
    }

}

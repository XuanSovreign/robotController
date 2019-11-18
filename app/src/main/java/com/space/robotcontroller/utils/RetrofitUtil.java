package com.space.robotcontroller.utils;

import com.space.robotcontroller.ui.HomeActivity;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by licht on 2019/10/29.
 */

public class RetrofitUtil {
    private static Retrofit sRetrofit;

    public static Retrofit getInstance() {
        if (sRetrofit == null) {
            synchronized (RetrofitUtil.class) {
                if (sRetrofit == null) {

                    sRetrofit=new Retrofit.Builder()
                                .baseUrl("http://"+ HomeActivity.mIpAddress+":2016/")
                                .addConverterFactory(SimpleXmlConverterFactory.create())
                                .build();
                }
            }
        }
        return sRetrofit;
    }
}

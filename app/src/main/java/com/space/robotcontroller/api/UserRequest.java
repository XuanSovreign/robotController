package com.space.robotcontroller.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by licht on 2019/10/29.
 */

public interface UserRequest {
    @GET("hardware")
    Call<ResponseBody> getHardwareStatus(@Query("func") String func, @Query("args") String args);

    @GET("configuration")
    Call<ResponseBody> getConfiguration(@Query("func") String func, @Query("args") String args);
}

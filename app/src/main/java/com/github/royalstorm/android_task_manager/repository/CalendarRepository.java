package com.github.royalstorm.android_task_manager.repository;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface CalendarRepository {

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/export")
    Call<ResponseBody> export();
}

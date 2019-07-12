package com.github.royalstorm.android_task_manager.repository;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CalendarRepository {

    @GET("/api/v1/export")
    Call<ResponseBody> exportFromServer(@Header("X-Firebase-Auth") String userToken);
}

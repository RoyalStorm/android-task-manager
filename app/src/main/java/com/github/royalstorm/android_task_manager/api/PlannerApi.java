package com.github.royalstorm.android_task_manager.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PlannerApi {
    @GET("api/v1/tasks")
    Call<List<Object>> getData();
}

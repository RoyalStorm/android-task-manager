package com.github.royalstorm.android_task_manager.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlannerApi {
    @GET("api/v1/tasks")
    Call<List<Object>> get();

    @POST("api/v1/tasks")
    Call<Object> post(@Query("body") Object body);

    @PUT("api/v1/tasks/{id}")
    Call<Object> put(@Path("id") int taskId, @Query("body") Object body);

    @DELETE("api/v1/tasks/{id}")
    Call<Object> delete(@Path("id") int taskId);
}

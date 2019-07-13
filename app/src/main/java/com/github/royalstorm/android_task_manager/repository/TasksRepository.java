package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.dto.TaskResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TasksRepository {

    @GET("/api/v1/tasks")
    Call<TaskResponse> getTasks(@Header("X-Firebase-Auth") String userToken);

    @POST("/api/v1/tasks")
    Call<TaskResponse> save(
            @Query("event_id") Long eventId,
            @Body Task task,
            @Header("X-Firebase-Auth") String userToken
    );

    @GET("/api/v1/tasks/{id}")
    Call<TaskResponse> getTaskById(@Path("id") Long id, @Header("X-Firebase-Auth") String userToken);

    @DELETE("/api/v1/tasks/{id}")
    Call<TaskResponse> delete(@Path("id") Long id, @Header("X-Firebase-Auth") String userToken);

    @PATCH("/api/v1/tasks/{id}")
    Call<TaskResponse> update(
            @Path("id") Long id,
            @Body Task task,
            @Header("X-Firebase-Auth") String userToken
    );
}

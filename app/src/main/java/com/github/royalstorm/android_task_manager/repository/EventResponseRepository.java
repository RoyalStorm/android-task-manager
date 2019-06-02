package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dao.EventResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface EventResponseRepository {
    @Headers({
            "X-Firebase-Auth: serega_mem",
    })
    @GET("/api/v1/events")
    Call<EventResponse> findAll();

    @Headers({
            "X-Firebase-Auth: serega_mem",
    })
    @POST("/api/v1/events")
    Call<EventResponse> save(@Body Event event);
}

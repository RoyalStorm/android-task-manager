package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.EventResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface EventResponseRepository {
    @Headers({
            "X-Firebase-Auth: serega_mem",
    })
    @GET("/api/v1/events")
    Call<EventResponse> findAll();
}

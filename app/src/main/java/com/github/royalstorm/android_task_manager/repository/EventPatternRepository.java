package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dto.EventPatternResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface EventPatternRepository {
    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/patterns")
    Call<EventPatternResponse> getAll(@QueryMap Map<String, String> params);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "X-Firebase-Auth: serega_mem"
    })
    @POST("/api/v1/patterns")
    Call<EventPatternResponse> save(@Query("event_id") int eventId, @Body Event event);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "X-Firebase-Auth: serega_mem"
    })
    @PATCH("/api/v1/patterns/{id}")
    Call<EventPatternResponse> update(@Path("id") int id, @Body Event event);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
            "X-Firebase-Auth: serega_mem"
    })
    @DELETE("/api/v1/patterns/{id}")
    Call<EventPatternResponse> delete(@Path("id") int id);
}

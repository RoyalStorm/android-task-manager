package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.EventPattern;
import com.github.royalstorm.android_task_manager.dto.EventPatternResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventPatternRepository {
    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/patterns/{id}")
    Call<EventPatternResponse> getPatternsById(@Path("id") Long id);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @POST("/api/v1/patterns")
    Call<EventPatternResponse> save(@Query("event_id") Long eventId, @Body EventPattern eventPattern);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @PATCH("/api/v1/patterns/{id}")
    Call<EventPatternResponse> update(@Path("id") Long id, @Body EventPattern eventPattern);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @DELETE("/api/v1/patterns/{id}")
    Call<Void> delete(@Path("id") Long id);
}

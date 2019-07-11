package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.EventPattern;
import com.github.royalstorm.android_task_manager.dto.EventPatternResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventPatternRepository {

    @GET("/api/v1/patterns/{id}")
    Call<EventPatternResponse> getPatternsById(@Path("id") Long id, @Header("X-Firebase-Auth") String userToken);

    @POST("/api/v1/patterns")
    Call<EventPatternResponse> save(@Query("event_id") Long eventId, @Body EventPattern eventPattern, @Header("X-Firebase-Auth") String userToken);

    @PATCH("/api/v1/patterns/{id}")
    Call<EventPatternResponse> update(@Path("id") Long id, @Body EventPattern eventPattern, @Header("X-Firebase-Auth") String userToken);

    @DELETE("/api/v1/patterns/{id}")
    Call<Void> delete(@Path("id") Long id, @Header("X-Firebase-Auth") String userToken);
}

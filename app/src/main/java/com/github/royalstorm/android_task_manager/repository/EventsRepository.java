package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dto.EventInstanceResponse;
import com.github.royalstorm.android_task_manager.dto.EventResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventsRepository {

    @POST("/api/v1/events")
    Call<EventResponse> save(@Body Event event, @Header("X-Firebase-Auth") String userToken);

    @GET("/api/v1/events")
    Call<EventResponse> getEventsById(
            @Query("id") Long[] id,
            @Header("X-Firebase-Auth") String userToken
    );

    @DELETE("/api/v1/events/{id}")
    Call<Void> delete(@Path("id") Long id, @Header("X-Firebase-Auth") String userToken);

    @PATCH("/api/v1/events/{id}")
    Call<EventResponse> update(
            @Path("id") Long id,
            @Body Event event,
            @Header("X-Firebase-Auth") String userToken
    );

    @GET("/api/v1/events/instances")
    Call<EventInstanceResponse> getEventInstancesByInterval(
            @Query("from") Long from,
            @Query("to") Long to,
            @Header("X-Firebase-Auth") String userToken
    );
}

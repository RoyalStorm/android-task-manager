package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dto.EventInstanceResponse;
import com.github.royalstorm.android_task_manager.dto.EventResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventRepository {

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/events")
    Call<EventResponse> getAllEvents();

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/events")
    Call<EventResponse> getEventsByInterval(@Query("from") Long from, @Query("to") Long to);

    /*@Headers({
            "X-Firebase-Auth: serega_mem"
    })*/
    @POST("/api/v1/events")
    Call<EventResponse> save(@Body Event event, @Header("X-Firebase-Auth") String userToken);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/events")
    Call<EventResponse> getEventsById(@Query("id") Long[] id);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @DELETE("/api/v1/events/{id}")
    Call<Void> delete(@Path("id") Long id);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @PATCH("/api/v1/events/{id}")
    Call<EventResponse> update(@Path("id") Long id, @Body Event event);

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/events/instances")
    Call<EventInstanceResponse> getAllEventInstances();

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/events/instances")
    Call<EventInstanceResponse> getEventInstancesByInterval(@Query("from") Long from, @Query("to") Long to);
}

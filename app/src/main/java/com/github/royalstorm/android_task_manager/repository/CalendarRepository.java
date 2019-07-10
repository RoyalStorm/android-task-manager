package com.github.royalstorm.android_task_manager.repository;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CalendarRepository {

    @Headers({
            "X-Firebase-Auth: serega_mem"
    })
    @GET("/api/v1/exportFromServer")
    Call<ResponseBody> exportFromServer();

    @Headers({
            "X-Firebase-Auth: serega_mem",
            "Application/json: multipart/form-data"
    })
    @POST("/api/v1/import")
    @Multipart
    Call<Void> importToServer(@Part MultipartBody.Part file, @Header("X-Firebase-Auth") String token);
}

package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.PermissionRequest;
import com.github.royalstorm.android_task_manager.dto.PermissionResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PermissionRepository {

    @GET("/api/v1/grant")
    Call<Void> getGrant(
            @Query("user_id") String userId,
            @Query("entity_id") Long entityId,
            @Query("entity_type") String entityType,
            @Query("action") String action,
            @Header("X-Firebase-Auth") String userToken
    );

    @GET("/api/v1/permissions")
    Call<PermissionResponse> getPermissions(
            @Query("entity_type") String entityType,
            @Query("mine") boolean mine,
            @Header("X-Firebase-Auth") String userToken
    );

    @DELETE("/api/v1/permissions/{id}")
    Call<Void> delete(@Path("id") Long id, @Header("X-Firebase-Auth") String userToken);

    @GET("/api/v1/share")
    Call<String> getSharingPermission(
            @Query("entity_type") String entityType,
            @Header("X-Firebase-Auth") String userToken
    );

    @POST("/api/v1/share")
    Call<ResponseBody> generateSharingLink(
            @Body PermissionRequest[] permissionRequests,
            @Header("X-Firebase-Auth") String userToken
    );

    @GET("/api/v1/share/{token}")
    Call<Void> activateShareLink(
            @Path("token") String token,
            @Header("X-Firebase-Auth") String userToken
    );
}

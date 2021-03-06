package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.PermissionRequest;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermissionService {

    private RetrofitClient retrofitClient;

    private RequestPermissionCallback requestPermissionCallback;

    private static final int TOKEN_BEGIN = 44;

    public PermissionService(RequestPermissionCallback requestPermissionCallback) {
        retrofitClient = RetrofitClient.getInstance();
        this.requestPermissionCallback = requestPermissionCallback;
    }

    public void delete(Long id, String userToken) {
        retrofitClient.getPermissionRepository().delete(id, userToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    requestPermissionCallback.requestPermissionSuccess(true, null);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void generateSharingLink(PermissionRequest[] permissionRequests, String userToken) {
        retrofitClient.getPermissionRepository().generateSharingLink(permissionRequests, userToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                    try {
                        requestPermissionCallback.requestPermissionSuccess(true, response.body().string().substring(TOKEN_BEGIN));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    public interface RequestPermissionCallback {
        void requestPermissionSuccess(boolean success, String sharingToken);
    }
}

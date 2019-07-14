package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.PermissionRequest;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermissionService {

    private RetrofitClient retrofitClient;

    private RequestPermissionCallback requestPermissionCallback;

    private static final int TOKEN_BEGIN = 23;
    private static final int TOKEN_END = 30;

    public PermissionService(RequestPermissionCallback requestPermissionCallback) {
        retrofitClient = RetrofitClient.getInstance();
        this.requestPermissionCallback = requestPermissionCallback;
    }

    public void generateSharingLink(PermissionRequest[] permissionRequests, String userToken) {
        retrofitClient.getPermissionRepository().generateSharingLink(permissionRequests, userToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                    requestPermissionCallback.requestPermissionSuccess(
                            response.isSuccessful(),
                            response.body().toString().substring(TOKEN_BEGIN, TOKEN_END)
                    );
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    public interface RequestPermissionCallback {
        void requestPermissionSuccess(boolean success, String sharingLing);
    }
}

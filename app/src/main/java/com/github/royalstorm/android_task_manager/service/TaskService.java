package com.github.royalstorm.android_task_manager.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.royalstorm.android_task_manager.shared.Calendar;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskService {

    public void get() {
        Calendar.getApi().get().enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(@NonNull Call<List<Object>> call, @NonNull Response<List<Object>> response) {
                if (response.isSuccessful()) {
                    Log.d("________", "done");
                } else {

                    switch (response.code()) {
                        case 404:
                            Log.d("____", "404");
                            break;
                        case 500:
                            Log.d("_____", "500");
                            break;
                    }

                    // или
                    // Также можете использовать ResponseBody для получения текста ошибки
                    ResponseBody errorBody = response.errorBody();
                    try {
                        Log.d("_______________", errorBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Log.d("_____________", t + "");
            }
        });
    }

    public Call<Object> post(String path, Object body) {
        return null;
    }

    public Call<Object> put(String path, Object body) {
        return null;
    }

    public Call<Object> delete(String path, int id) {
        return null;
    }
}

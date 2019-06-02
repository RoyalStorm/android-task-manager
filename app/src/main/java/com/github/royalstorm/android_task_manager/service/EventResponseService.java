package com.github.royalstorm.android_task_manager.service;

import android.util.Log;

import com.github.royalstorm.android_task_manager.dao.EventResponse;
import com.github.royalstorm.android_task_manager.shared.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventResponseService {
    private RetrofitInstance retrofitInstance = new RetrofitInstance();

    public void getAll() {
        retrofitInstance.getEventResponseRepository().findAll().enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.d("Status: ", response.code() + "");
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable throwable) {
                Log.d("Error: ", throwable.getMessage().toString());
            }
        });
    }
}

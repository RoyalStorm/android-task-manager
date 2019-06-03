package com.github.royalstorm.android_task_manager.service;

import android.util.Log;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dao.EventResponse;
import com.github.royalstorm.android_task_manager.shared.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventService {
    private RetrofitInstance retrofitInstance = new RetrofitInstance();

    public void getAll() {
        retrofitInstance.getEventRepository().getAll().enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.d("Status: ", response.code() + "");
                Log.d("Body: ", response.body().toString());
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable throwable) {
                Log.d("Error: ", throwable.getMessage().toString());
            }
        });
    }

    public void save(Event event) {
        retrofitInstance.getEventRepository().save(event).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.d("_________Status", response.code() + "");
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable throwable) {
                Log.d("_________Error: ", throwable.getMessage().toString());
            }
        });
    }
}

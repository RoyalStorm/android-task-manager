package com.github.royalstorm.android_task_manager.service;

import android.util.Log;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dto.EventResponse;
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
                Log.d("___GET Status", response.code() + "");
                Log.d("___GET Body", response.body().toString());
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable throwable) {
                Log.d("___GET Error", throwable.getMessage());
            }
        });
    }

    public void save(Event event) {
        retrofitInstance.getEventRepository().save(event).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.d("___POST Response code", response.code() + "");
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable throwable) {
                Log.d("___POST Error", throwable.getMessage());
            }
        });
    }

    public void update(int id, Event event) {
        retrofitInstance.getEventRepository().update(id, event).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.d("___PUT Response code", response.code() + "");
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable throwable) {
                Log.d("___PUT Error", throwable.getMessage());
            }
        });
    }

    public void delete(int id) {
        retrofitInstance.getEventRepository().delete(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("___DELETE Response code", response.code() + "");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d("___DELETE Error", throwable.getMessage());
            }
        });
    }
}

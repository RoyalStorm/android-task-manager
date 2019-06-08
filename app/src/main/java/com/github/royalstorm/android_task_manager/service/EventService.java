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

    private EventResponse eventResponse;

    public EventResponse getAll() {
        retrofitInstance.getEventRepository().getAll().enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful())
                    eventResponse = response.body();
                else eventResponse = null;
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable throwable) {
            }
        });

        return eventResponse;
    }

    public EventResponse save(Event event) {
        retrofitInstance.getEventRepository().save(event).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.d("___POST Response", response.code() + "");

                if (response.isSuccessful())
                    eventResponse = response.body();
                else eventResponse = null;
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable throwable) {
            }
        });

        return eventResponse;
    }

    public EventResponse update(int id, Event event) {
        retrofitInstance.getEventRepository().update(id, event).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful())
                    eventResponse = response.body();
                else eventResponse = null;
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable throwable) {
            }
        });

        return eventResponse;
    }

    public void delete(int id) {
        retrofitInstance.getEventRepository().delete(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
            }
        });
    }
}

package com.github.royalstorm.android_task_manager.service;

import android.util.Log;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dto.EventResponse;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventService {
    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    private EventResponse eventResponse;

    /*public EventResponse getAll() {
        retrofitClient.getEventRepository().getAll().enqueue(new Callback<EventResponse>() {
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
    }*/

    public EventResponse save(Event event) {
        retrofitClient.getEventRepository().save(event).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.d("___POST Response", response.code() + "");

                Log.d("________id", response.body().getData()[0].getId() + "");

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

    public EventResponse update(Long id, Event event) {
        retrofitClient.getEventRepository().update(id, event).enqueue(new Callback<EventResponse>() {
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

    public void delete(Long id) {
        retrofitClient.getEventRepository().delete(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
            }
        });
    }

    public EventResponse getEventResponse() {
        return eventResponse;
    }
}

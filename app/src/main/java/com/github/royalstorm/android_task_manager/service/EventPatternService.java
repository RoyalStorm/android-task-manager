package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.EventPattern;
import com.github.royalstorm.android_task_manager.dto.EventPatternResponse;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPatternService {
    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    public void save(Long eventId, EventPattern eventPattern, String userToken) {
        retrofitClient.getEventPatternRepository().save(eventId, eventPattern, userToken).enqueue(new Callback<EventPatternResponse>() {
            @Override
            public void onResponse(Call<EventPatternResponse> call, Response<EventPatternResponse> response) {
            }

            @Override
            public void onFailure(Call<EventPatternResponse> call, Throwable throwable) {
            }
        });
    }

    public void update(Long id, EventPattern eventPattern) {
        retrofitClient.getEventPatternRepository().update(id, eventPattern).enqueue(new Callback<EventPatternResponse>() {
            @Override
            public void onResponse(Call<EventPatternResponse> call, Response<EventPatternResponse> response) {
            }

            @Override
            public void onFailure(Call<EventPatternResponse> call, Throwable throwable) {
            }
        });
    }

    public void delete(Long id) {
        retrofitClient.getEventPatternRepository().delete(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
            }
        });
    }
}

package com.github.royalstorm.android_task_manager.service;

import android.util.Log;

import com.github.royalstorm.android_task_manager.dao.EventPattern;
import com.github.royalstorm.android_task_manager.dto.EventPatternResponse;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPatternService {
    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    private EventPatternResponse eventPatternResponse = new EventPatternResponse();

    /*public EventPatternResponse getAll(Long from, Long to) {
        retrofitClient.getEventPatternRepository().findFromTo(params).enqueue(new Callback<EventPatternResponse>() {
            @Override
            public void onResponse(Call<EventPatternResponse> call, Response<EventPatternResponse> response) {
                if (response.isSuccessful())
                    eventPatternResponse = response.body();
                else eventPatternResponse = null;
            }

            @Override
            public void onFailure(Call<EventPatternResponse> call, Throwable throwable) {
            }
        });

        return eventPatternResponse;
    }*/

    public EventPatternResponse save(Long eventId, EventPattern eventPattern) {
        retrofitClient.getEventPatternRepository().save(eventId, eventPattern).enqueue(new Callback<EventPatternResponse>() {
            @Override
            public void onResponse(Call<EventPatternResponse> call, Response<EventPatternResponse> response) {
                Log.d("POST", response.code() + "");

                if (response.isSuccessful())
                    eventPatternResponse = response.body();
                else eventPatternResponse = null;
            }

            @Override
            public void onFailure(Call<EventPatternResponse> call, Throwable throwable) {
            }
        });

        return eventPatternResponse;
    }

    public EventPatternResponse update(Long id, EventPattern eventPattern) {
        retrofitClient.getEventPatternRepository().update(id, eventPattern).enqueue(new Callback<EventPatternResponse>() {
            @Override
            public void onResponse(Call<EventPatternResponse> call, Response<EventPatternResponse> response) {
                if (response.isSuccessful())
                    eventPatternResponse = response.body();
                else eventPatternResponse = null;
            }

            @Override
            public void onFailure(Call<EventPatternResponse> call, Throwable throwable) {
            }
        });

        return eventPatternResponse;
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

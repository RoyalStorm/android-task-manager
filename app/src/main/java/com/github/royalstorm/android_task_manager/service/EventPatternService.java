package com.github.royalstorm.android_task_manager.service;

import android.util.Log;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dto.EventPatternResponse;
import com.github.royalstorm.android_task_manager.shared.RetrofitInstance;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPatternService {
    private RetrofitInstance retrofitInstance = new RetrofitInstance();

    private EventPatternResponse eventPatternResponse = new EventPatternResponse();

    public EventPatternResponse getAll(Map<String, String> params) {
        retrofitInstance.getEventPatternRepository().getAll(params).enqueue(new Callback<EventPatternResponse>() {
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

    public EventPatternResponse save(int eventId, Event event) {
        retrofitInstance.getEventPatternRepository().save(eventId, event).enqueue(new Callback<EventPatternResponse>() {
            @Override
            public void onResponse(Call<EventPatternResponse> call, Response<EventPatternResponse> response) {
                Log.d("___POST Response", response.code() + "");

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

    public EventPatternResponse update(int id, Event event) {
        retrofitInstance.getEventPatternRepository().update(id, event).enqueue(new Callback<EventPatternResponse>() {
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

    public EventPatternResponse delete(int id) {
        retrofitInstance.getEventPatternRepository().delete(id).enqueue(new Callback<EventPatternResponse>() {
            @Override
            public void onResponse(Call<EventPatternResponse> call, Response<EventPatternResponse> response) {
            }

            @Override
            public void onFailure(Call<EventPatternResponse> call, Throwable throwable) {
            }
        });

        return eventPatternResponse;
    }
}

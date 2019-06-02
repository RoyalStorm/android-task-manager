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

    public void save(Event event) {
        retrofitInstance.getEventRepository().save(event).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("_________Response body", response.body().toString());
                }
                Log.d("_________Status", response.code() + "");
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable throwable) {
                Log.d("_________Error: ", throwable.getMessage().toString());
            }
        });
    }
}

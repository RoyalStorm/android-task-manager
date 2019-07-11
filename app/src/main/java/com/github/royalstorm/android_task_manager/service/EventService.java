package com.github.royalstorm.android_task_manager.service;

import android.util.Log;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dto.EventInstanceResponse;
import com.github.royalstorm.android_task_manager.dto.EventResponse;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventService {

    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    /*public void save(Event event) {
        retrofitClient.getEventRepository().save(event).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable throwable) {
            }
        });
    }*/

    public void update(Long id, Event event) {
        retrofitClient.getEventRepository().update(id, event).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable throwable) {
            }
        });
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

    public void getEventInstancesByInterval(Long from, Long to, String userToken) {
        retrofitClient.getEventRepository().getEventInstancesByInterval(from, to, userToken).enqueue(new Callback<EventInstanceResponse>() {
            @Override
            public void onResponse(Call<EventInstanceResponse> call, Response<EventInstanceResponse> response) {
                Log.d("______________", response.body().getCount() + "");
                if (response.isSuccessful())
                    EventBus.getDefault().post(Arrays.asList(response.body().getData()));
            }

            @Override
            public void onFailure(Call<EventInstanceResponse> call, Throwable t) {

            }
        });
    }
}

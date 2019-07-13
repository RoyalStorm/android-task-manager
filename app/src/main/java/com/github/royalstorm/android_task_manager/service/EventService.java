package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dto.EventInstanceResponse;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventService {

    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    public void getEventInstancesByInterval(Long from, Long to, String userToken) {
        retrofitClient.getEventsRepository().getEventInstancesByInterval(from, to, userToken).enqueue(new Callback<EventInstanceResponse>() {
            @Override
            public void onResponse(Call<EventInstanceResponse> call, Response<EventInstanceResponse> response) {
                if (response.isSuccessful())
                    EventBus.getDefault().post(Arrays.asList(response.body().getData()));
            }

            @Override
            public void onFailure(Call<EventInstanceResponse> call, Throwable t) {

            }
        });
    }
}

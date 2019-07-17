package com.github.royalstorm.android_task_manager.service;

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

    private RequestEventCallback requestEventCallback;

    public EventService(EventService.RequestEventCallback requestEventCallback) {
        retrofitClient = RetrofitClient.getInstance();
        this.requestEventCallback = requestEventCallback;
    }

    public void delete(Long id, String userToken) {
        retrofitClient.getEventsRepository().delete(id, userToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    requestEventCallback.requestEventSuccess(true, null);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

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

    public void getEventsById(Long[] ids, String userToken) {
        retrofitClient.getEventsRepository().getEventsById(ids, userToken).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                requestEventCallback.requestEventSuccess(response.isSuccessful(), response.body());
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });
    }

    public interface RequestEventCallback {
        void requestEventSuccess(boolean success, EventResponse eventResponse);
    }
}

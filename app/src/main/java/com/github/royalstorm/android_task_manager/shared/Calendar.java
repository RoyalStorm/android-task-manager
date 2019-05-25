package com.github.royalstorm.android_task_manager.shared;

import android.app.Application;

import com.github.royalstorm.android_task_manager.api.PlannerApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Calendar extends Application {
    private static PlannerApi plannerApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.99.100/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        plannerApi = retrofit.create(PlannerApi.class);
    }

    public static PlannerApi getApi() {
        return plannerApi;
    }
}

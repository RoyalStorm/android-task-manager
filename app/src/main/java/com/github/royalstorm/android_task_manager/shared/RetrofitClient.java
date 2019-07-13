package com.github.royalstorm.android_task_manager.shared;

import com.github.royalstorm.android_task_manager.repository.CalendarRepository;
import com.github.royalstorm.android_task_manager.repository.EventsRepository;
import com.github.royalstorm.android_task_manager.repository.PatternsRepository;
import com.github.royalstorm.android_task_manager.repository.PermissionRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BASE_URL = "http://planner.skillmasters.ga/";

    private static RetrofitClient instance;

    private Retrofit retrofit;

    private static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setLenient()
            .create();

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static RetrofitClient getInstance() {
        if (instance == null)
            instance = new RetrofitClient();

        return instance;
    }

    public CalendarRepository getCalendarRepository() {
        return retrofit.create(CalendarRepository.class);
    }

    public EventsRepository getEventsRepository() {
        return retrofit.create(EventsRepository.class);
    }

    public PatternsRepository getPatternsRepository() {
        return retrofit.create(PatternsRepository.class);
    }

    public PermissionRepository getPermissionRepository() {
        return retrofit.create(PermissionRepository.class);
    }
}

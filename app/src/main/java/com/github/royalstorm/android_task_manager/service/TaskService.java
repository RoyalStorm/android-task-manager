package com.github.royalstorm.android_task_manager.service;

import android.util.Log;
import android.widget.Toast;

import com.github.royalstorm.android_task_manager.activity.MainActivity;
import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.repository.TaskRepository;
import com.github.royalstorm.android_task_manager.shared.Calendar;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskService implements TaskRepository {

    @Override
    public List<Task> findAll() {
        return null;
    }

    @Override
    public Task findById(int id) {
        return null;
    }

    @Override
    public List<Task> findByDate(int year, int month, int day) {
        return null;
    }

    @Override
    public List<Task> findByDateAndTime(int year, int month, int day, int hour, int minute) {
        return null;
    }

    @Override
    public void create(Task event) {

    }

    @Override
    public void update(int id, Task event) {

    }

    @Override
    public void delete(int id) {

    }

    public void retrofitTest() {
        Calendar.getApi().getData().enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                Log.d("_____________", response.body() + "");
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Log.d("_____________", t + "");
            }
        });
    }
}

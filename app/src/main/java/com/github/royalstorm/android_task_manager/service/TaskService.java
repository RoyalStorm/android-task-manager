package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.repository.TaskRepository;

import java.util.List;

public class TaskService implements TaskRepository {

    @Override
    public List<Task> findAll() {
        return null;
    }

    @Override
    public List<Task> findByDate(String date) {
        return null;
    }

    @Override
    public List<Task> findByDateAndTime(String date, String time) {
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
}
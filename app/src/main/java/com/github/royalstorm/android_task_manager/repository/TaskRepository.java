package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.Task;

import java.util.List;

public interface TaskRepository {

    List<Task> findAll();

    Task findById(int id);

    List<Task> findByDate(int year, int month, int day);

    List<Task> findByDateAndTime(int year, int month, int day, int hour, int minute);

    void create(Task event);

    void update(int id, Task event);

    void delete(int id);
}

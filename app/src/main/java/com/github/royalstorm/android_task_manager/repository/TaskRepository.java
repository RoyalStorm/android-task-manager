package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.Task;

import java.util.List;

public interface TaskRepository {

    List<Task> findAll();

    List<Task> findByDate(String date);

    List<Task> findByDateAndTime(String date, String time);

    void create(Task event);

    void update(int id, Task event);

    void delete(int id);
}

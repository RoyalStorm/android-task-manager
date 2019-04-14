package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class MockUpTaskService implements TaskRepository {

    private static List<Task> tasks = new ArrayList<>();
    private List<Task> foundTasks;

    private static int counter = 0;

    private Task getTask(int id) {
        for (Task task : tasks)
            if (task.getId() == id)
                return task;

        return null;
    }

    @Override
    public List<Task> findAll() {
        return tasks;
    }

    @Override
    public Task findById(int id) {
        return getTask(id);
    }

    @Override
    public List<Task> findByDate(int year, int month, int day) {
        foundTasks = new ArrayList<>();

        for (Task task : tasks)
            if (task.getBeginYear() == year &&
                task.getBeginMonth() == month &&
                task.getBeginDay() == day)
                foundTasks.add(task);

        return foundTasks;
    }

    @Override
    public List<Task> findByDateAndTime(int year, int month, int day, int hours, int minutes) {
        foundTasks = new ArrayList<>();

        for (Task task : tasks)
            if (task.getBeginYear() == year &&
                    task.getBeginMonth() == month &&
                    task.getBeginDay() == day &&
                    task.getBeginHour() == hours &&
                    task.getBeginMinute() == minutes)
                foundTasks.add(task);

        return foundTasks;
    }

    @Override
    public void create(Task task) {
        tasks.add(task);

        ++counter;
    }

    @Override
    public void update(int id, Task task) {
        Task taskFromDB = getTask(id);

        taskFromDB = task;
        taskFromDB.setId(id);

        tasks.set(id, taskFromDB);
    }

    @Override
    public void delete(int id) {
        tasks.remove(getTask(id));

        --counter;
    }

    public static int getCounter() {
        return counter;
    }
}

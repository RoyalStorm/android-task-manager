package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.repository.TaskRepository;

import java.util.ArrayList;
import java.util.GregorianCalendar;
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
            if (year >= task.getBeginYear() && year <= task.getEndYear() &&
                month >= task.getBeginMonth() && month <= task.getEndMonth() &&
                day >= task.getBeginDay() && day <= task.getEndDay())
                foundTasks.add(task);

        return foundTasks;
    }

    @Override
    public List<Task> findByDateAndTime(int year, int month, int day, int hour, int minutes) {
        foundTasks = new ArrayList<>();

        GregorianCalendar current = new GregorianCalendar(year, month, day, hour, minutes);

        for (Task task : tasks) {
            GregorianCalendar begin = new GregorianCalendar(task.getBeginYear(), task.getBeginMonth(), task.getBeginDay(), task.getBeginHour(), task.getBeginMinute());
            GregorianCalendar end = new GregorianCalendar(task.getEndYear(), task.getEndMonth(), task.getEndDay(), task.getEndHour(), task.getEndMinute());

            if (isWithinRange(begin, current, end))
                foundTasks.add(task);

        }

        /*for (Task task : tasks)
            if (year >= task.getBeginYear() && year <= task.getEndYear() &&
                month >= task.getBeginMonth() && month <= task.getEndMonth() &&
                day >= task.getBeginDay() && day <= task.getEndDay() &&
                hour >= task.getBeginHour() && hour <= task.getEndHour() &&
                minutes >= task.getBeginMinute() && minutes <= task.getEndMinute())
                foundTasks.add(task);*/

        return foundTasks;
    }

    @Override
    public List<Task> findByDateAndHours(int year, int month, int day, int hour) {
        foundTasks = new ArrayList<>();

        for (Task task : tasks)
            if (year >= task.getBeginYear() && year <= task.getEndYear() &&
                month >= task.getBeginMonth() && month <= task.getEndMonth() &&
                day >= task.getBeginDay() && day <= task.getEndDay() &&
                hour >= task.getBeginHour() && hour <= task.getEndHour())
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
    }

    public static int getCounter() {
        return counter;
    }

    boolean isWithinRange(GregorianCalendar begin, GregorianCalendar current, GregorianCalendar end) {
        return !(current.before(begin) || current.after(end));
    }
}

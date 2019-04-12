package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class MockUpTaskService implements TaskRepository {

    private static List<Task> events = new ArrayList<>();
    private List<Task> foundEvents;

    private static int counter = 0;

    private Task getEvent(int id) {
        for (Task event : events)
            if (event.getId() == id)
                return event;

        return null;
    }

    @Override
    public List<Task> findAll() {
        return events;
    }

    @Override
    public List<Task> findByDate(int year, int month, int day) {
        foundEvents = new ArrayList<>();

        for (Task event : events)
            if (event.getBeginYear() == year &&
                event.getBeginMonth() == month &&
                event.getBeginDay() == day)
                foundEvents.add(event);

        return foundEvents;
    }

    @Override
    public List<Task> findByDateAndTime(int year, int month, int day, int hour, int minute) {
        foundEvents = new ArrayList<>();

        for (Task event : events)
            if (event.getBeginYear() == year &&
                event.getBeginMonth() == month &&
                event.getBeginDay() == day &&
                event.getBeginHour() == hour &&
                event.getBeginMinute() == minute)
                foundEvents.add(event);

        return foundEvents;
    }

    @Override
    public void create(Task event) {
        events.add(event);

        ++counter;
    }

    @Override
    public void update(int id, Task event) {
        Task eventFromDB = getEvent(id);

        eventFromDB = event;
        eventFromDB.setId(id);

        events.set(id, eventFromDB);
    }

    @Override
    public void delete(int id) {
        events.remove(getEvent(id));

        --counter;
    }

    public static int getCounter() {
        return counter;
    }
}

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
    public List<Task> findByDate(String date) {
        foundEvents = new ArrayList<>();

        //TODO: rework this method
        for (Task event : events)
            if (event.getBeginDate().equals(date))
                foundEvents.add(event);

        return foundEvents;
    }

    @Override
    public List<Task> findByDateAndTime(String date, String time) {
        foundEvents = new ArrayList<>();

        //TODO: rework this method
        for (Task event : events)
            if (event.getBeginDate().equals(date) && event.getBeginTime().equals(time))
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

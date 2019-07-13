package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.MockUpEvent;
import com.github.royalstorm.android_task_manager.repository.MockUpEventRepository;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MockUpEventService implements MockUpEventRepository {

    private static List<MockUpEvent> mockUpEvents = new ArrayList<>();
    private List<MockUpEvent> foundMockUpEvents;

    private static int counter = 0;

    private MockUpEvent getTask(int id) {
        for (MockUpEvent mockUpEvent : mockUpEvents)
            if (mockUpEvent.getId() == id)
                return mockUpEvent;

        return null;
    }

    @Override
    public List<MockUpEvent> findAll() {
        return mockUpEvents;
    }

    @Override
    public MockUpEvent findById(int id) {
        return getTask(id);
    }

    @Override
    public List<MockUpEvent> findByDate(int year, int month, int day) {
        foundMockUpEvents = new ArrayList<>();

        GregorianCalendar current = new GregorianCalendar(year, month, day);

        for (MockUpEvent mockUpEvent : mockUpEvents) {
            GregorianCalendar begin = new GregorianCalendar(mockUpEvent.getBeginYear(), mockUpEvent.getBeginMonth(), mockUpEvent.getBeginDay());
            GregorianCalendar end = new GregorianCalendar(mockUpEvent.getEndYear(), mockUpEvent.getEndMonth(), mockUpEvent.getEndDay());

            if (isWithinRange(begin, current, end))
                foundMockUpEvents.add(mockUpEvent);
        }

        return foundMockUpEvents;
    }

    @Override
    public List<MockUpEvent> findByDateAndTime(int year, int month, int day, int hour, int minutes) {
        foundMockUpEvents = new ArrayList<>();

        GregorianCalendar current = new GregorianCalendar(year, month, day, hour, minutes);

        for (MockUpEvent mockUpEvent : mockUpEvents) {
            GregorianCalendar begin = new GregorianCalendar(mockUpEvent.getBeginYear(), mockUpEvent.getBeginMonth(), mockUpEvent.getBeginDay(), mockUpEvent.getBeginHour(), mockUpEvent.getBeginMinute());
            GregorianCalendar end = new GregorianCalendar(mockUpEvent.getEndYear(), mockUpEvent.getEndMonth(), mockUpEvent.getEndDay(), mockUpEvent.getEndHour(), mockUpEvent.getEndMinute());

            if (isWithinRange(begin, current, end))
                foundMockUpEvents.add(mockUpEvent);
        }

        return foundMockUpEvents;
    }

    @Override
    public void create(MockUpEvent mockUpEvent) {
        mockUpEvents.add(mockUpEvent);

        ++counter;
    }

    @Override
    public void update(int id, MockUpEvent mockUpEvent) {
        MockUpEvent mockUpEventFromDB = getTask(id);

        mockUpEventFromDB = mockUpEvent;
        mockUpEventFromDB.setId(id);

        mockUpEvents.set(id, mockUpEventFromDB);
    }

    @Override
    public void delete(int id) {
        mockUpEvents.remove(getTask(id));

        --counter;
    }

    public static int getCounter() {
        return counter;
    }

    private boolean isWithinRange(GregorianCalendar begin, GregorianCalendar current, GregorianCalendar end) {
        return !(current.before(begin) || current.after(end));
    }
}

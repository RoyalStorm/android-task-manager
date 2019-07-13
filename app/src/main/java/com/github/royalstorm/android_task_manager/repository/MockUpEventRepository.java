package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.MockUpEvent;

import java.util.List;

public interface MockUpEventRepository {

    List<MockUpEvent> findAll();

    MockUpEvent findById(int id);

    List<MockUpEvent> findByDate(int year, int month, int day);

    List<MockUpEvent> findByDateAndTime(int year, int month, int day, int hour, int minute);

    void create(MockUpEvent event);

    void update(int id, MockUpEvent event);

    void delete(int id);
}

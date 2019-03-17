package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.Event;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EventServiceTest {

    private static final Event event = new Event(0, "День рождения сестры");

    private MockUpEventService mockUpEventService = new MockUpEventService();

    @Before
    public void setUp() {
        mockUpEventService.create(event);
    }

    @Test
    public void shouldReturnEventTitle() {
        Assert.assertEquals("День рождения сестры", mockUpEventService.findById(0).getEventTitle());
    }

    @Test
    public void shouldReturnUpdatedEventTitle() {
        mockUpEventService.update(0, new Event(1, "Поход в кино"));

        Assert.assertEquals("Поход в кино", mockUpEventService.findById(0).getEventTitle());
    }
}

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
        mockUpEventService.add(event);
    }

    @Test
    public void shouldReturnEventTitle() {
        Assert.assertEquals(mockUpEventService.getOne(0).getEventTitle(), "День рождения сестры");
    }

    @Test
    public void shouldReturnUpdatedEventTitle() {
        mockUpEventService.update(0, new Event(1, "Поход в кино"));

        Assert.assertEquals("Поход в кино", mockUpEventService.getOne(0).getEventTitle());
    }
}

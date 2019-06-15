package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;

public class EventInstance {
    /*Readonly fields*/
    @Expose
    private Long startedAt;
    @Expose
    private Long endedAt;
    @Expose
    private Long eventId;
    @Expose
    private Long patternId;

    public Long getStartedAt() {
        return startedAt;
    }

    public Long getEndedAt() {
        return endedAt;
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getPatternId() {
        return patternId;
    }
}

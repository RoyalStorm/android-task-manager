package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventInstance {
    /*Readonly fields*/
    @Expose
    @SerializedName("started_at")
    private Long startedAt;
    @Expose
    @SerializedName("ended_at")
    private Long endedAt;
    @Expose
    @SerializedName("event_id")
    private Long eventId;
    @Expose
    @SerializedName("pattern_id")
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

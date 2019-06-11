package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;

public class EventPattern {
    /*Readonly fields*/
    private int id;
    private String createdAt;
    private String updatedAt;

    @Expose
    private Long duration;
    @Expose
    private Long endedAt;
    @Expose
    private String exrule;
    @Expose
    private String rrule;
    @Expose
    private Long startedAt;
    @Expose
    private String timezone;

    /*Default constructor*/
    public EventPattern() {
    }

    public EventPattern(Long duration, Long endedAt, String exrule, String rrule, Long startedAt, String timezone) {
        this.duration = duration;
        this.endedAt = endedAt;
        this.exrule = exrule;
        this.rrule = rrule;
        this.startedAt = startedAt;
        this.timezone = timezone;
    }

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Long endedAt) {
        this.endedAt = endedAt;
    }

    public String getExrule() {
        return exrule;
    }

    public void setExrule(String exrule) {
        this.exrule = exrule;
    }

    public String getRrule() {
        return rrule;
    }

    public void setRrule(String rrule) {
        this.rrule = rrule;
    }

    public Long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}

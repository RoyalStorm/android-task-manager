package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;

public class EventPattern {
    /*Readonly fields*/
    private int id;
    private String createdAt;
    private String updatedAt;

    @Expose
    private int duration;
    @Expose
    private String endedAt;
    @Expose
    private String exrule;
    @Expose
    private String rrule;
    @Expose
    private String startedAt;
    @Expose
    private int type;

    /*Default constructor*/
    public EventPattern() {
    }

    public EventPattern(int duration, String endedAt, String exrule, String rrule, String startedAt, int type) {
        this.duration = duration;
        this.endedAt = endedAt;
        this.exrule = exrule;
        this.rrule = rrule;
        this.startedAt = startedAt;
        this.type = type;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(String endedAt) {
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

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

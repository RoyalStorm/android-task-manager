package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;

public class EventPattern {
    @Expose
    private String createdAt;
    @Expose
    private String day;

    @Expose
    private int duration;

    @Expose
    private String endedAt;
    @Expose
    private String hour;

    @Expose
    private int id;

    @Expose
    private String minute;
    @Expose
    private String month;
    @Expose
    private String startedAt;

    @Expose
    private int type;

    @Expose
    private String updatedAt;
    @Expose
    private String weekday;
    @Expose
    private String year;

    public EventPattern() {
    }

    public EventPattern(String day, int duration, String endedAt, String hour, String minute, String month, String startedAt, int type, String weekday, String year) {
        this.day = day;
        this.duration = duration;
        this.endedAt = endedAt;
        this.hour = hour;
        this.minute = minute;
        this.month = month;
        this.startedAt = startedAt;
        this.type = type;
        this.weekday = weekday;
        this.year = year;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}

package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventPattern {
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("day")
    @Expose
    private String day;

    @SerializedName("duration")
    @Expose
    private int duration;

    @SerializedName("ended_at")
    @Expose
    private String endedAt;
    @SerializedName("hour")
    @Expose
    private String hour;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("minute")
    @Expose
    private String minute;
    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("started_at")
    @Expose
    private String startedAt;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("weekday")
    @Expose
    private String weekday;
    @SerializedName("year")
    @Expose
    private String year;

    public EventPattern(String createdAt, String day, int duration, String endedAt, String hour, String minute, String month, String startedAt, int type, String updatedAt, String weekday, String year) {
        this.createdAt = createdAt;
        this.day = day;
        this.duration = duration;
        this.endedAt = endedAt;
        this.hour = hour;
        this.minute = minute;
        this.month = month;
        this.startedAt = startedAt;
        this.type = type;
        this.updatedAt = updatedAt;
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

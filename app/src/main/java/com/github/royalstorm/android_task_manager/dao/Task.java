package com.github.royalstorm.android_task_manager.dao;

import java.io.Serializable;

public class Task implements Serializable {
    private int id;

    private String owner;
    private String name;
    private String details;

    private int beginMinute;
    private int beginHour;
    private int beginDay;
    private int beginMonth;
    private int beginYear;

    private int endMinute;
    private int endHour;
    private int endDay;
    private int endMonth;
    private int endYear;

    public Task(int id, String owner, String name, String details, int beginMinute, int beginHour, int beginDay, int beginMonth, int beginYear, int endMinute, int endHour, int endDay, int endMonth, int endYear) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.details = details;
        this.beginMinute = beginMinute;
        this.beginHour = beginHour;
        this.beginDay = beginDay;
        this.beginMonth = beginMonth;
        this.beginYear = beginYear;
        this.endMinute = endMinute;
        this.endHour = endHour;
        this.endDay = endDay;
        this.endMonth = endMonth;
        this.endYear = endYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getBeginMinute() {
        return beginMinute;
    }

    public void setBeginMinute(int beginMinute) {
        this.beginMinute = beginMinute;
    }

    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public int getBeginDay() {
        return beginDay;
    }

    public void setBeginDay(int beginDay) {
        this.beginDay = beginDay;
    }

    public int getBeginMonth() {
        return beginMonth;
    }

    public void setBeginMonth(int beginMonth) {
        this.beginMonth = beginMonth;
    }

    public int getBeginYear() {
        return beginYear;
    }

    public void setBeginYear(int beginYear) {
        this.beginYear = beginYear;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
}

package com.github.royalstorm.android_task_manager.dao;

import java.io.Serializable;

public class Task implements Serializable {
    private int id;

    private String owner;
    private String name;
    private String details;
    private String beginDate;
    private String endDate;
    private String beginTime;
    private String endTime;

    public Task(int id, String owner, String name, String details, String beginDate, String endDate, String beginTime, String endTime) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.details = details;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.beginTime = beginTime;
        this.endTime = endTime;
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

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

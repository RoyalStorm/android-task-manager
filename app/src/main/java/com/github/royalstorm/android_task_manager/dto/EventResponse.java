package com.github.royalstorm.android_task_manager.dto;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class EventResponse {
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("data")
    @Expose
    private Event[] data;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("offset")
    @Expose
    private int offset;

    @SerializedName("success")
    @Expose
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "EventResponse{" +
                "code=" + code +
                ", count=" + count +
                ", data=" + Arrays.toString(data) +
                ", message='" + message + '\'' +
                ", offset=" + offset +
                ", success=" + success +
                '}';
    }
}

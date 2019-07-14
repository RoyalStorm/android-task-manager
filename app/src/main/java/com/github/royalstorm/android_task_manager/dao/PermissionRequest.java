package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PermissionRequest {
    @Expose
    private String action;
    @Expose
    @SerializedName("entity_id")
    private Long entityId;
    @Expose
    @SerializedName("entity_type")
    private String entityType;

    /*Default constructor*/
    public PermissionRequest() {
    }

    public PermissionRequest(String action, Long entityId, String entityType) {
        this.action = action;
        this.entityId = entityId;
        this.entityType = entityType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}

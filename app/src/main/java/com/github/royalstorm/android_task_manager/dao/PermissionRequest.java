package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PermissionRequest {
    @Expose
    private Action action;
    @Expose
    @SerializedName("entity_id")
    private Long entityId;
    @Expose
    @SerializedName("entity_type")
    private EntityType entityType;

    /*Default constructor*/
    public PermissionRequest() {
    }

    public PermissionRequest(Action action, Long entityId, EntityType entityType) {
        this.action = action;
        this.entityId = entityId;
        this.entityType = entityType;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }
}

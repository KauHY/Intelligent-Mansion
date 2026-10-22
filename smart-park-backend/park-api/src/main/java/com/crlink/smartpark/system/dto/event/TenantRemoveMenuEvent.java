package com.crlink.smartpark.system.dto.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class TenantRemoveMenuEvent extends ApplicationEvent {
    @Getter
    private String tenantId;

    public TenantRemoveMenuEvent(Object source, String tenantId) {
        super(source);
        this.tenantId = tenantId;
    }
}

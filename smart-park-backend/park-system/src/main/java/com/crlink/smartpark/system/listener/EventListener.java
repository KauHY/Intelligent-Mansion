package com.crlink.smartpark.system.listener;

import com.crlink.smartpark.system.dto.event.CopyMenuEvent;
import com.crlink.smartpark.system.dto.event.TenantRemoveMenuEvent;
import com.crlink.smartpark.system.service.platform.PlatformTenantMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventListener {
    @Autowired
    private PlatformTenantMenuService platformTenantMenuService;

    @org.springframework.context.event.EventListener
    public void copyMenu(CopyMenuEvent event) {
        log.info("【copy menu for orgId:{},线程名：{} 】", event.getTenantId(), Thread.currentThread().getName());
        platformTenantMenuService.copeMenusForTenant(event.getTenantId());
    }


    @org.springframework.context.event.EventListener
    public void removeMenu(TenantRemoveMenuEvent event) {
        log.info("【remove menu for tenantId:{},线程名：{} 】", event.getTenantId(), Thread.currentThread().getName());
        platformTenantMenuService.removeMenusForTenant(event.getTenantId());
    }
}

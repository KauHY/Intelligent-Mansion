package com.crlink.smartpark.device.common;

import com.crlink.smartpark.device.model.LiquidPosition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 液位配置
 *
 */
@Component
@ConfigurationProperties("device.liquid")
public class LiquidConfig {

    List<LiquidPosition> devices;

    public List<LiquidPosition> getDevices() {
        return devices;
    }

    public void setDevices(List<LiquidPosition> devices) {
        this.devices = devices;
    }


}

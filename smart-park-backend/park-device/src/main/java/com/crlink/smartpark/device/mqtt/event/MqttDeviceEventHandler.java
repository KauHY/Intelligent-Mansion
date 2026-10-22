package com.crlink.smartpark.device.mqtt.event;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.crlink.smartpark.core.utils.RedisUtil;
import com.crlink.smartpark.device.common.MqttConstants;
import com.crlink.smartpark.device.entity.ClientDevice;
import com.crlink.smartpark.device.mqtt.core.MqttAnalyzerEventHandler;
import com.crlink.smartpark.device.service.ClientDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: MQTT 设备状态事件
 * @Version 1.0
 */
@Slf4j
@Component
public class MqttDeviceEventHandler implements MqttAnalyzerEventHandler {

    @Autowired
    private ClientDeviceService iClientDeviceService;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public void handle(String topic, String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String method = jsonObject.getString("method");
        Integer methodCode = MqttConstants.DeviceMqMethod.getCodeByValue(method);
        if (methodCode != null) {
            JSONObject deviceObject = jsonObject.getJSONObject("params");
            JSONObject statusMap = deviceObject.getJSONObject("status");
            String deviceSn = deviceObject.getString("deviceId");
            String gatewaySn = deviceObject.getString("gatewayId");
            List<ClientDevice> clientDeviceList = iClientDeviceService.lambdaQuery().eq(ClientDevice::getDeviceSn, deviceSn)
                    .eq(ClientDevice::getGatewaySn, gatewaySn).list();
            if (CollectionUtil.isNotEmpty(clientDeviceList) && statusMap != null) {
                ClientDevice clientDevice = clientDeviceList.get(0);
                //室内照明
                if (clientDevice.getDeviceType().intValue() == MqttConstants.DeviceType.ONE.getCode().intValue()) {
                    clientDevice.setStatus(statusMap.getInteger("power_switch"));
                }
                //开合帘
                else if (clientDevice.getDeviceType().intValue() == MqttConstants.DeviceType.THREE.getCode().intValue()) {
                    if (statusMap.getInteger("position") == 0) {
                        clientDevice.setStatus(1);
                    } else {
                        clientDevice.setStatus(0);
                    }
                }
                //空调
                else if (clientDevice.getDeviceType().intValue() == MqttConstants.DeviceType.SEVEN.getCode().intValue()) {
                    clientDevice.setStatus(statusMap.getInteger("power_switch"));
                }
                //南北侧门
                else if (clientDevice.getDeviceType().intValue() == MqttConstants.DeviceType.SIX_DOOR.getCode().intValue()) {
                    clientDevice.setStatus(statusMap.getInteger("contact_state"));
                }
                //室内空调
                else if (clientDevice.getDeviceType().intValue() == MqttConstants.DeviceType.FOUR.getCode().intValue()) {
                    clientDevice.setStatus(statusMap.getInteger("air_power_switch"));
                }
                if (clientDevice.getDeviceType().intValue() == MqttConstants.DeviceType.SIX_TEM.getCode().intValue()) {
                    String temp = statusMap.getString("temperature");
                    String humidity = statusMap.getString("humidity");
                    clientDevice.setRemark(temp + "-" + humidity);
                }
                if (clientDevice.getDeviceType().intValue() == MqttConstants.DeviceType.SIX_POWER.getCode().intValue()) {
                    Integer lumen_current = statusMap.getInteger("lumen_current");
                    clientDevice.setRemark(String.valueOf(lumen_current));
                }

                if (deviceSn.equals("2953590202538C")) {
                    clientDevice.setStatus(statusMap.getInteger("tv_power_switch"));
                }
                clientDevice.setUpdateTime(LocalDateTime.now());
                iClientDeviceService.updateById(clientDevice);
            }
            // 将设备状态存入redis中
            redisUtil.setObject(MqttConstants.DEVICE_STATUS_METHOD + "-" + deviceSn + "_" + gatewaySn, statusMap);
        }

    }

    @Override
    public String code() {
        return MqttConstants.MqttTopic.DEVICE.getCode();
    }

}

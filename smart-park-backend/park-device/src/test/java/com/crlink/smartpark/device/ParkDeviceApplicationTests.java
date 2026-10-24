package com.crlink.smartpark.device;

import com.crlink.smartpark.core.utils.RocketmqUtil;
import com.crlink.smartpark.device.dto.MeterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class ParkDeviceApplicationTests {
    enum MyTopic {
        A
    }

    enum MyTag {
        tag2
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testRocketmq() {
        MeterDTO meterDTO = new MeterDTO();
        meterDTO.setDeviceId("deviceID");
        meterDTO.setDeviceName("设备");
        meterDTO.setEffectProperty(1);
        meterDTO.setInitialTime(LocalDateTime.now());
        RocketmqUtil.sendSyncMessage(MyTopic.A, MyTag.tag2, meterDTO);
    }

}

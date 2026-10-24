package com.crlink.smartpark.gateway;

import com.crlink.smartpark.system.dto.house.BuildingSaveDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ParkGatewayApplication.class)
@EnableAutoConfiguration
public class GatewayApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        BuildingSaveDTO entity = new BuildingSaveDTO();
        entity.setCode("111111111111111111111111111");
        redisTemplate.opsForValue().set("123123123123", entity);
    }

}

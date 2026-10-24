package com.crlink.smartpark.core.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * @Description 应用启动后执行
 * @Created by ZHOUXINJIAN3
 */
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    @Resource
    private TaskExecutorUtils taskExecutorUtils;

    @Override
    public void run(String... args) throws Exception {
        CompletableFuture.runAsync(() -> taskExecutorUtils.execute()).join();
    }

}

package com.crlink.smartpark.gateway;

import com.crlink.smartpark.gateway.filter.GatewayExcludeLoadClassFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@EnableAutoConfiguration
@EnableFeignClients(basePackages = "com.crlink.smartpark.**.feign")
@ComponentScan(value = "com.crlink.smartpark.*.*", excludeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM, classes = GatewayExcludeLoadClassFilter.class)})
@SpringBootConfiguration
public class ParkGatewayApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ParkGatewayApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        Environment environment = application.run(args).getEnvironment();
        logApplicationStartup(environment);
    }

    /**
     * 日志打印启动信息
     *
     * @param environment 当前环境
     */
    private static void logApplicationStartup(Environment environment) {
        String protocol = "http";
        if (environment.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = environment.getProperty("server.port");
        String contextPath = environment.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t"
                        + "Local: \t\t{}://localhost:{}{}\n\t"
                        + "External: \t{}://{}:{}{}\n\t"
                        + "Profile(s): \t{}"
                        + "\n----------------------------------------------------------",
                environment.getProperty("spring.application.name"), protocol, serverPort, contextPath, protocol, hostAddress,
                serverPort, contextPath, environment.getActiveProfiles());
    }
}

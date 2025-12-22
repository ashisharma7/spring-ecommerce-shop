package com.shop.order.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimezoneGuardConfig {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        LoggerFactory.getLogger(TimezoneGuardConfig.class)
                .info("JVM timezone forcibly set to UTC");
    }

    protected TimezoneGuardConfig() { }
}

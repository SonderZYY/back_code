package com.xuecheng.feign.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p></p>
 *
 * @Description:
 */
@Configuration
@ComponentScan(basePackages = {"com.xuecheng.feign"})
@EnableFeignClients(basePackages = {"com.xuecheng.feign"})
public class FeignConfig {
}
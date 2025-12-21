package com.hfuu.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 系统模块启动类
 * 
 * @author hfuu
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SystemApplication
{
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
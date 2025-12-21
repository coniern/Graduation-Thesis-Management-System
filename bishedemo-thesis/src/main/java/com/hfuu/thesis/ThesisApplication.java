package com.hfuu.thesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 论文模块启动类
 * 
 * @author hfuu
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ThesisApplication
{
    public static void main(String[] args) {
        SpringApplication.run(ThesisApplication.class, args);
    }
}
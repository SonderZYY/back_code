package com.xuecheng.media;


import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableSwagger2Doc
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.xuecheng.media","com.xuecheng.common.exception"})
public class MediaService {

	public static void main(String[] args) {
		SpringApplication.run(MediaService.class, args);
	}
}
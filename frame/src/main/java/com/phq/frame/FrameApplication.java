package com.phq.frame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * 
* @ClassName: FrameApplication
* @Description: 
*     frame 启动类
* @author panhuaqing
* @date 2018年10月18日
*
 */
@SpringBootApplication
@EnableScheduling //声明使用定时任务
public class FrameApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrameApplication.class, args);
	}
}

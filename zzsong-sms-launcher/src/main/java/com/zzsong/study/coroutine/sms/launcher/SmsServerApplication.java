package com.zzsong.study.coroutine.sms.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 宋志宗 on 2022/1/27
 */
@SpringBootApplication
public class SmsServerApplication {

  public static void main(String[] args) {
    int ioWorkerCount = Runtime.getRuntime().availableProcessors() << 1;
    System.setProperty("reactor.netty.ioWorkerCount", String.valueOf(ioWorkerCount));
    SpringApplication.run(SmsServerApplication.class, args);
  }
}

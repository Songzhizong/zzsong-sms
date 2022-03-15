package com.zzsong.study.coroutine.sms.client;

import cn.idealframework.lang.Lists;
import cn.idealframework.lang.Maps;
import cn.idealframework.transmission.exception.ResultException;
import com.zzsong.study.coroutine.sms.client.impl.ReactiveSmsClientImpl;
import com.zzsong.study.coroutine.sms.common.dto.args.SendSmsArgs;
import org.junit.Ignore;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * @author 宋志宗 on 2022/2/1
 */
@Ignore
public class ReactiveSmsClientTest {
  private static final ReactiveSmsClient client = new ReactiveSmsClientImpl("http://127.0.0.1:23301");

  @org.junit.Test
  public void send() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    SendSmsArgs args = new SendSmsArgs();
    args.setTemplateCode("SMS_CODE");
    args.setMobiles(Lists.of("18256928780"));
    args.setParams(Maps.of("code", "1234"));
    client.send(args)
      .onErrorResume(throwable -> {
        assertTrue(throwable instanceof ResultException);
        return Mono.just(-1L);
      })
      .doFinally(t -> countDownLatch.countDown())
      .subscribe(l -> System.out.println("taskId = " + l));
    //noinspection ResultOfMethodCallIgnored
    countDownLatch.await(5_000, TimeUnit.MILLISECONDS);
  }

  @org.junit.Test
  public void testSend() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    SendSmsArgs args = new SendSmsArgs();
    args.setTemplateCode("SMS_CODE");
    args.setMobiles(Lists.of("18256928780"));
    args.setParams(Maps.of("code", "1234"));
    client.send(Lists.of(args))
      .onErrorResume(throwable -> {
        assertTrue(throwable instanceof ResultException);
        return Mono.just(-1L);
      })
      .doFinally(t -> countDownLatch.countDown())
      .subscribe(l -> System.out.println("taskId = " + l));
    //noinspection ResultOfMethodCallIgnored
    countDownLatch.await(5_000, TimeUnit.MILLISECONDS);
  }
}

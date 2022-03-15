package com.zzsong.study.coroutine.sms.client;

import cn.idealframework.lang.Lists;
import cn.idealframework.lang.Maps;
import cn.idealframework.transmission.exception.ResultException;
import com.zzsong.study.coroutine.sms.client.impl.SmsClientImpl;
import com.zzsong.study.coroutine.sms.common.dto.args.SendSmsArgs;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author 宋志宗 on 2022/2/1
 */
@Ignore
public class SmsClientTest {
  private static final SmsClient client = new SmsClientImpl("http://127.0.0.1:23301");

  @Test
  public void send() {
    SendSmsArgs args = new SendSmsArgs();
    args.setTemplateCode("SMS_CODE");
    args.setMobiles(Lists.of("18256928780"));
    args.setParams(Maps.of("code", "1234"));
    try {
      long send = client.send(args);
      System.out.println("taskId = " + send);
    } catch (Exception e) {
      assertTrue(e instanceof ResultException);
    }
  }

  @Test
  public void testSend() {
    SendSmsArgs args = new SendSmsArgs();
    args.setTemplateCode("SMS_CODE");
    args.setMobiles(Lists.of("18256928780"));
    args.setParams(Maps.of("code", "1234"));
    try {
      long send = client.send(Lists.of(args));
      System.out.println("taskId = " + send);
    } catch (Exception e) {
      assertTrue(e instanceof ResultException);
    }
  }
}

package com.zzsong.study.coroutine.sms.client.impl;

import cn.idealframework.transmission.exception.ResultException;
import com.zzsong.study.coroutine.sms.client.ReactiveSmsClient;
import com.zzsong.study.coroutine.sms.client.SmsClient;
import com.zzsong.study.coroutine.sms.common.dto.args.SendSmsArgs;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @author 宋志宗 on 2022/2/1
 */
public class SmsClientImpl implements SmsClient {
  private final ReactiveSmsClient reactiveSmsClient;


  public SmsClientImpl(String baseUrl) {
    this(new ReactiveSmsClientImpl(baseUrl));
  }

  public SmsClientImpl(String baseUrl, WebClient webClient) {
    this(new ReactiveSmsClientImpl(baseUrl, webClient));
  }

  public SmsClientImpl(ReactiveSmsClient reactiveSmsClient) {
    this.reactiveSmsClient = reactiveSmsClient;
  }

  @Override
  public long send(@Nonnull SendSmsArgs args) throws ResultException {
    Long taskId = reactiveSmsClient.send(args).block();
    assert taskId != null;
    return taskId;
  }

  @Override
  public long send(@Nonnull Collection<SendSmsArgs> argsList) throws ResultException {
    Long taskId = reactiveSmsClient.send(argsList).block();
    assert taskId != null;
    return taskId;
  }
}

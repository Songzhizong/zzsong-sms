package com.zzsong.study.coroutine.sms.client.impl;

import com.zzsong.study.coroutine.sms.client.AsyncSmsClient;
import com.zzsong.study.coroutine.sms.client.ReactiveSmsClient;
import com.zzsong.study.coroutine.sms.common.dto.args.SendSmsArgs;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * @author 宋志宗 on 2022/2/1
 */
public class AsyncSmsClientImpl implements AsyncSmsClient {
  private final ReactiveSmsClient reactiveSmsClient;


  public AsyncSmsClientImpl(String baseUrl) {
    this(new ReactiveSmsClientImpl(baseUrl));
  }

  public AsyncSmsClientImpl(String baseUrl, WebClient webClient) {
    this(new ReactiveSmsClientImpl(baseUrl, webClient));
  }

  public AsyncSmsClientImpl(ReactiveSmsClient reactiveSmsClient) {
    this.reactiveSmsClient = reactiveSmsClient;
  }
  @Nonnull
  @Override
  public CompletableFuture<Long> send(@Nonnull SendSmsArgs args) {
    return reactiveSmsClient.send(args).toFuture();
  }

  @Nonnull
  @Override
  public CompletableFuture<Long> send(@Nonnull Collection<SendSmsArgs> argsList) {
    return reactiveSmsClient.send(argsList).toFuture();
  }
}

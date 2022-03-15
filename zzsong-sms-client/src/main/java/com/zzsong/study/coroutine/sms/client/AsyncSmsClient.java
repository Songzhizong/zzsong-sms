package com.zzsong.study.coroutine.sms.client;

import com.zzsong.study.coroutine.sms.common.dto.args.SendSmsArgs;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * @author 宋志宗 on 2022/2/1
 */
public interface AsyncSmsClient {

  @Nonnull
  CompletableFuture<Long> send(@Nonnull SendSmsArgs args);

  @Nonnull
  CompletableFuture<Long> send(@Nonnull Collection<SendSmsArgs> argsList);
}

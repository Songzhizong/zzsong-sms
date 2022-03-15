package com.zzsong.study.coroutine.sms.client;

import com.zzsong.study.coroutine.sms.common.dto.args.SendSmsArgs;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @author 宋志宗 on 2022/2/1
 */
public interface ReactiveSmsClient {

  @Nonnull
  Mono<Long> send(@Nonnull SendSmsArgs args);

  @Nonnull
  Mono<Long> send(@Nonnull Collection<SendSmsArgs> argsList);
}

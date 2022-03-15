package com.zzsong.study.coroutine.sms.client.impl;

import cn.idealframework.extensions.reactor.ReactorResults;
import cn.idealframework.extensions.reactor.Reactors;
import cn.idealframework.transmission.Result;
import com.zzsong.study.coroutine.sms.client.ReactiveSmsClient;
import com.zzsong.study.coroutine.sms.common.dto.args.SendSmsArgs;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Collection;

/**
 * @author 宋志宗 on 2022/2/1
 */
public class ReactiveSmsClientImpl implements ReactiveSmsClient {
  private final String baseUrl;
  private final WebClient webClient;


  public ReactiveSmsClientImpl(String baseUrl) {
    this(baseUrl, Reactors.webClient(ops ->
        ops.setKeepAlive(true)
          .setResponseTimeout(Duration.ofSeconds(2))
      )
    );
  }

  public ReactiveSmsClientImpl(String baseUrl, WebClient webClient) {
    this.baseUrl = baseUrl;
    this.webClient = webClient;
  }

  @Nonnull
  @Override
  public Mono<Long> send(@Nonnull SendSmsArgs args) {
    String url = baseUrl + "/sms/send";
    return webClient.post().uri(url)
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(args))
      .exchangeToMono(ReactorResults.result(Long.class))
      .onErrorResume(throwable -> Mono.just(Result.exception(throwable)))
      .map(Result::getOrThrow);
  }

  @Nonnull
  @Override
  public Mono<Long> send(@Nonnull Collection<SendSmsArgs> argsList) {
    String url = baseUrl + "/sms/send/batch";
    return webClient.post().uri(url)
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(argsList))
      .exchangeToMono(ReactorResults.result(Long.class))
      .onErrorResume(throwable -> Mono.just(Result.exception(throwable)))
      .map(Result::getOrThrow);
  }
}

package com.zzsong.study.coroutine.sms.server.provider.aliyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2021/7/29
 */
@Getter
@Setter
public class AliYunResponse {

  @Nullable
  @JsonProperty("RequestId")
  private String requestId;

  @Nullable
  @JsonProperty("Message")
  private String message;

  @Nullable
  @JsonProperty("BizId")
  private String bizId;

  @Nullable
  @JsonProperty("Code")
  private String code;
}

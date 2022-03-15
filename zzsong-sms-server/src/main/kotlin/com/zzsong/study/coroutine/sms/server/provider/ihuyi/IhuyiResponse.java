package com.zzsong.study.coroutine.sms.server.provider.ihuyi;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/1/28
 */
@Getter
@Setter
public class IhuyiResponse {

  /** 状态码 */
  @Nullable
  private String code;

  /** 描述信息 */
  @Nullable
  private String msg;

  /** 流水号 */
  @Nullable
  private String smsid;
}

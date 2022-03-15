package com.zzsong.study.coroutine.sms.server.configure;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 宋志宗 on 2022/1/29
 */
@Getter
@Setter
public class SmsServerMockProperties {

  /** 是否启用mock */
  private boolean enabled = false;

  /** 最小延迟时间 */
  private long minDelay = 100;

  /** 最大延迟时间 */
  private long maxDelay = 500;
}

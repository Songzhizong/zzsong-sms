package com.zzsong.study.coroutine.sms.server.provider;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 宋志宗 on 2022/1/27
 */
@Getter
@Setter
public class SendResult {
  /** 是否发生成功 */
  private boolean success;

  /** 手机号码 */
  private String mobile;

  /** 模板id */
  private long templateId;

  /** 短信内容 */
  private String content;

  /** 描述信息 */
  private String description;

  /** 提供商编码 */
  private String providerCode;

  /** 提供商流水号 */
  private String providerSerialNumber;
}

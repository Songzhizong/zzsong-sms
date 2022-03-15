package com.zzsong.study.coroutine.sms.server.application.args;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/1/28
 */
@Getter
@Setter
public class CreateProviderTemplateArgs {

  /**
   * 短信模板id
   *
   * @required
   */
  @Nullable
  private Long templateId;

  /**
   * 供应商编码
   *
   * @required
   */
  @Nullable
  private String providerCode;

  /** 供应商模板编码 */
  @Nullable
  private String providerTemplate;

  /** 短信内容 */
  @Nullable
  private String content;
}

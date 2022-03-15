package com.zzsong.study.coroutine.sms.server.domain.model.template;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 提供商模板信息
 *
 * @author 宋志宗 on 2022/1/28
 */
@Getter
@Setter
public class ProviderTemplate {
  /** 唯一id */
  private long id;

  /** 所属短信模板 */
  private long templateId;

  private String templateCode;

  /** 是否为所属短信模板的默认实现 */
  private boolean defaultImpl;

  /** 短信服务提供商唯一编码 */
  private String providerCode;

  /** 短信服务提示商的模板编码 */
  private String providerTemplate;

  /** 短信内容 */
  private String content;

  /** 创建时间 */
  private LocalDateTime createdTime;

  /** 更新时间 */
  private LocalDateTime updatedTime;
}

package com.zzsong.study.coroutine.sms.server.configure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author 宋志宗 on 2022/1/29
 */
@Getter
@Setter
@Component("smsServerProperties")
@ConfigurationProperties("sms.server")
public class SmsServerProperties {

  /** 忽略数据库中配置的默认模板实现, 强制使用指定的供应商实现 */
  @Nullable
  private String specifiedProvider = null;

  /** mock配置 */
  @Nonnull
  @NestedConfigurationProperty
  private SmsServerMockProperties mock = new SmsServerMockProperties();

}

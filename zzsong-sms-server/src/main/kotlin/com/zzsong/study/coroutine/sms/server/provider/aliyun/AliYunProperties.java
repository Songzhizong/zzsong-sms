package com.zzsong.study.coroutine.sms.server.provider.aliyun;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 宋志宗 on 2021/7/29
 */
@Getter
@Setter
@Component("smsAliYunProperties")
@ConfigurationProperties("sms.server.provider.ali-yun")
public class AliYunProperties {
  private String baseUrl = "https://dysmsapi.aliyuncs.com";
  /**
   * 短信签名名称
   */
  private String signName;

  /**
   * 地域ID
   */
  private String regionId = "cn-hangzhou";

  /**
   * RAM账号的AccessKey ID
   */
  private String accessKeyId = "";

  /**
   * RAM账号AccessKey Secret
   */
  private String accessSecret = "";
}

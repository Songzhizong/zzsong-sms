package com.zzsong.study.coroutine.sms.server.provider.ihuyi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 宋志宗 on 2021/7/29
 */
@Getter
@Setter
@Component("smsIhuyiProperties")
@ConfigurationProperties("sms.server.provider.ihuyi")
public class IhuyiProperties {
  @SuppressWarnings("HttpUrlsUsage")
  private String baseUrl = "http://106.ihuyi.com/webservice/sms.php";

  private String appId = "";

  private String appKey = "";
}

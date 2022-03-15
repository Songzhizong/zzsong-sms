package com.zzsong.study.coroutine.sms.server.provider.aliyun;

import cn.idealframework.json.JsonUtils;
import cn.idealframework.lang.Joiner;
import cn.idealframework.util.Asserts;

import javax.annotation.Nonnull;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 宋志宗 on 2022/1/28
 */
public class AliyunSmsUtils {
  @SuppressWarnings("SpellCheckingInspection")
  private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL = ThreadLocal
    .withInitial(() -> {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      format.setTimeZone(new SimpleTimeZone(0, "GMT"));
      return format;
    });


  public static String createUrl(@Nonnull String baseUrl,
                                 @Nonnull String regionId,
                                 @Nonnull String signName,
                                 @Nonnull String accessKeyId,
                                 @Nonnull String accessSecret,
                                 @Nonnull String templateCode,
                                 @Nonnull Collection<String> mobiles,
                                 @Nonnull Map<String, String> args) throws Exception {
    Asserts.notBlank(baseUrl, "baseUrl must be not blank");
    Asserts.notBlank(accessKeyId, "accessKeyId must be not blank");
    Asserts.notBlank(accessSecret, "accessSecret must be not blank");

    Map<String, String> paras = new HashMap<>();
    // 系统参数
    paras.put("SignatureMethod", "HMAC-SHA1");
    paras.put("SignatureNonce", UUID.randomUUID().toString().replace("-", ""));
    paras.put("AccessKeyId", accessKeyId);
    paras.put("SignatureVersion", "1.0");
    paras.put("Timestamp", THREAD_LOCAL.get().format(new Date()));
    paras.put("Format", "json");
    // 业务API参数
    paras.put("Action", "SendSms");
    paras.put("Version", "2017-05-25");
    paras.put("RegionId", regionId);

    paras.put("PhoneNumbers", Joiner.joinSkipNull(mobiles, ","));
    paras.put("SignName", signName);
    paras.put("TemplateParam", JsonUtils.toJsonString(args));
    paras.put("TemplateCode", templateCode);
    // 参数KEY排序
    TreeMap<String, String> sortParas = new TreeMap<>(paras);
    // 构造待签名的字符串
    Iterator<String> it = sortParas.keySet().iterator();
    StringBuilder sortQueryStringTmp = new StringBuilder();
    while (it.hasNext()) {
      String key = it.next();
      sortQueryStringTmp.append("&")
        .append(specialUrlEncode(key))
        .append("=")
        .append(specialUrlEncode(paras.get(key)));
    }
    String sortedQueryString = sortQueryStringTmp.substring(1);// 去除第一个多余的&符号
    String stringToSign = "GET&" + specialUrlEncode("/") + "&" + specialUrlEncode(sortedQueryString);
    String sign = sign(accessSecret + "&", stringToSign);
    // 签名最后也要做特殊URL编码
    String signature = specialUrlEncode(sign);
    return baseUrl + "?Signature=" + signature + sortQueryStringTmp;
  }

  @Nonnull
  private static String specialUrlEncode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8)
      .replace("+", "%20")
      .replace("*", "%2A")
      .replace("%7E", "~");
  }

  private static String sign(@Nonnull String accessSecret,
                             @Nonnull String stringToSign) throws Exception {
    Mac mac = Mac.getInstance("HmacSHA1");
    mac.init(new SecretKeySpec(accessSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
    byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(signData);
  }
}

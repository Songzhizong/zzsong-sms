package com.zzsong.study.coroutine.sms.common.dto.args;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * 短信发送参数
 *
 * @author 宋志宗 on 2022/1/27
 */
@Getter
@Setter
public class SendSmsArgs {
  /**
   * 短信模板
   *
   * @required
   */
  private String templateCode;

  /**
   * 手机号列表
   *
   * @required
   */
  private List<String> mobiles;

  /** 模板参数列表 */
  @Nullable
  private Map<String, String> params;
}

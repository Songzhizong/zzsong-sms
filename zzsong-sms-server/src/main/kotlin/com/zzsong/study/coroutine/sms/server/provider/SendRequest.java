package com.zzsong.study.coroutine.sms.server.provider;

import com.zzsong.study.coroutine.sms.server.domain.model.template.ProviderTemplateDo;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * 短信发送请求参数
 *
 * @author 宋志宗 on 2022/1/27
 */
@Getter
@Setter
public class SendRequest {

  /** 提供商模板信息 */
  @Nonnull
  private ProviderTemplateDo template;

  /** 手机号列表 */
  @Nonnull
  private Collection<String> mobiles;

  /** 参数列表 */
  @Nullable
  private Map<String, String> params;
}

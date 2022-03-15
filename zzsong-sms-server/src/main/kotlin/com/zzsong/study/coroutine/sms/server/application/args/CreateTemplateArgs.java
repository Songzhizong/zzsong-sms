package com.zzsong.study.coroutine.sms.server.application.args;

import com.zzsong.study.coroutine.sms.server.domain.model.template.TemplateParam;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author 宋志宗 on 2021/7/30
 */
@Getter
@Setter
public class CreateTemplateArgs {
  /** 模板编码,为空自动生成 */
  @Nullable
  private String code;
  /**
   * 模板名称
   *
   * @required
   */
  @Nullable
  private String name;
  /** 模板描述 */
  @Nullable
  private String description;

  /** 模板参数 */
  @Nullable
  private Set<TemplateParam> params;
}

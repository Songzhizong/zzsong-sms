package com.zzsong.study.coroutine.sms.server.domain.model.template;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author 宋志宗 on 2021/7/23
 */
@Getter
@Setter
public class TemplateParam {
  /**
   * 参数名称
   *
   * @required
   */
  private String param;
  /**
   * 参数描述
   */
  @Nullable
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TemplateParam that = (TemplateParam) o;
    return Objects.equals(param, that.param);
  }

  @Override
  public int hashCode() {
    return Objects.hash(param);
  }
}

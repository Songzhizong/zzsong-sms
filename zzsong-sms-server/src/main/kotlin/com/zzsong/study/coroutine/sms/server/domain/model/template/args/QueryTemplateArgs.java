package com.zzsong.study.coroutine.sms.server.domain.model.template.args;

import cn.idealframework.transmission.Paging;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

/**
 * 查询短信模板参数
 *
 * @author 宋志宗 on 2022/1/28
 */
@Getter
@Setter
public class QueryTemplateArgs {
  private Paging paging = Paging.of(1, 10);

  /** 短信模板编码 */
  @Nullable
  private String code;
}
